package com.file.main.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * SFTP工具类
 * 
 * 
 */
@Component
public class SFTP {

    @Value("${sftp.username}")
	private String username;
    @Value("${sftp.password}")
	private String password;
    @Value("${sftp.host}")
	private String host;
    @Value("${sftp.port}")
	private int port;

	private ChannelSftp sftp = null;

	private static final Logger log = LoggerFactory.getLogger(SFTP.class);


	public boolean connect() {
		if (sftp != null) {
			return true;
		}
		JSch jsch = new JSch();

		try {
			Session sshSession = jsch.getSession(username, host, port);
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect(60000); // 60s 超时

			Channel channel = sshSession.openChannel("sftp");
			channel.connect(60000);// 60s 超时

			sftp = (ChannelSftp) channel;
			log.debug("用户[{}]sftp连接服务器[{}:{}]成功.", username, host, port);
		} catch (JSchException e) {
			sftp = null;
			log.error("连接host:[{}] port:[{}]异常：", host, port, e);
			return false;
		}

		return true;

	}

	public void disconnect() {
		if (sftp == null) {
			if (log.isDebugEnabled()) {
				log.debug("用户[{}]已退出sftp连接.", username);
			}
			return;
		}
		try {
			// 如果没有sesstion的disconnect，程序不会退出
			sftp.getSession().disconnect();
		} catch (JSchException e) {
			log.error("用户[{}]退出sftp连接失败，将强制退出.", username);
			sftp = null;
			return;
		}
		sftp.disconnect();
		sftp.exit();
		sftp = null;
		log.debug("用户[{}]退出sftp连接成功.", username);
	}

	public void showRemoteFiles(String remoteDir) {
		if (sftp == null) {
			log.error("服务器未连接。");
			return;
		}
		try {
			Vector<?> vector = sftp.ls(remoteDir);
			for (Object obj : vector) {
				if (obj instanceof LsEntry) {
					String filename = ((LsEntry) obj).getFilename();
					log.debug(filename);
				}
			}
		} catch (SftpException e) {
			log.error("列表目录[{}]异常:", remoteDir, e);
		}
	}

	/**
	 * 下载FTP服务器上的文件
	 * 
	 * @param remoteDir
	 *            FTP文件目录
	 * @param remoteFileName
	 *            FTP文件名
	 * @param localDir
	 *            本地存放目录
	 */
	public boolean downloadFile(String remoteDir, String remoteFileName,
			String localDir) {

		if (sftp == null) {
			log.error("服务器未连接。");
			return false;
		}

		try {
			sftp.cd(remoteDir);
		} catch (SftpException e) {
			log.error("进入服务器目录[{}]异常:", remoteDir, e);
			return false;
		}

		try {
			if (remoteFileName == null) {
				Vector<?> vector = sftp.ls(".");
				for (Object obj : vector) {
					if (obj instanceof LsEntry) {
						String filename = ((LsEntry) obj).getFilename();
						String localfile = "";
						if(".".equals(filename) || "..".equals(filename)){
							continue;
						}
						SftpATTRS dir = ((LsEntry) obj).getAttrs();
						if(dir.isDir()){ // 子目录不予下载
							continue;
						}
						
						if (localDir.endsWith("/")) {
							localfile = localDir + filename;
						} else {
							localfile = localDir + "/" + filename;
						}
						File file = new File(localfile);
						sftp.get(filename, new FileOutputStream(file));
						log.debug("文件[{}]下载成功.", filename);
					}
				}
			} else {
				String localfile = "";
				if (localDir.endsWith("/")) {
					localfile = localDir + remoteFileName;
				} else {
					localfile = localDir + "/" + remoteFileName;
				}
				File file = new File(localfile);
				sftp.get(remoteFileName, new FileOutputStream(file));
				log.debug("文件[{}]下载成功.", remoteFileName);
			}
		} catch (FileNotFoundException e) {
			log.error("服务器不存在该文件[{}]:", remoteFileName, e);
			return false;
		} catch (SftpException e) {
			log.error("下载文件[{}]异常:", remoteFileName, e);
			return false;
		}

		return true;
	}

	/**
	 * 上传文件到FTP服务器
	 * 
	 * @param localFile
	 *            本地文件
	 * @param remoteDir
	 *            FTP文件目录，如不存在则自动创建;相对路径
	 * @param remoteFileName
	 *            FTP文件名
	 */
	public boolean uploadFile(File localFile, String remoteDir,
			String remoteFileName) {

		String folder = null;
		String[] folders = remoteDir.split("/");
		for (int i = 0; i < folders.length; i++) {
			folder = folders[i];
			if (folder.length() > 0) {
				try {
					sftp.cd(folder);
				} catch (SftpException e) {
					try {
						sftp.mkdir(folder);
						sftp.cd(folder);
					} catch (SftpException e1) {
						log.error("创建远程路径[{}]失败：", folder, e);
						return false;
					}
				}
			}
		}

		try {
			sftp.put(new FileInputStream(localFile), remoteFileName);
			log.debug("文件[{}]上传成功.", remoteFileName);
		} catch (FileNotFoundException e) {
			log.error("文件不存在[{}]：", remoteFileName, e);
			return false;
		} catch (SftpException e) {
			log.error("上传文件[{}]失败：", remoteFileName, e);
			return false;
		}
		return true;
	}

	public boolean delete(String remoteDir, String remoteFileName) {

		try {
			sftp.cd(remoteDir);
			sftp.rm(remoteFileName);
			log.debug("远程删除文件[{}]成功.", remoteFileName);
		} catch (SftpException e) {
			log.debug("远程删除文件[{}]失败:", remoteFileName, e);
			return false;
		}
		return true;

	}
	public void setFtpClient(ChannelSftp sftp1){
		this.sftp = sftp1;
	}
	
	public ChannelSftp getFtpClient(){
		return sftp;
	}
}
