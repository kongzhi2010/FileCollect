package com.file.main.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.file.main.config.SFTP;
import com.file.main.utils.Constants;
import com.file.main.utils.G4Utils;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${sftp.remote}")
    private String remoteDir;
    @Value("${sftp.local}")
    private String local;
    @Value("${sftp.listFiles}")
    private String[] files;
    @Value("${file.dates}")
    private String[] dates;
    @Autowired
    private SFTP sftp;
    //1.@Scheduled(fixedRate = 5000) //通过@Scheduled声明该方法是计划任务，使用fixedRate属性每隔固定时间5秒执行一次 
    //2.@Scheduled(cron = “0 0/10 * * * ?”) //使用cron属性可按照指定时间执行，本例指的是每10分钟执行一次；
    @Scheduled(cron = "${sftp.downTimer}") // 每天2点执行
    public void getToken() {
        log.info("getsftp定时任务启动");
//		String trandate = "20160327";
    	if(dates != null && dates.length>0) {
    		for(int i=0;i<dates.length;i++) {
    			if(dates[i]!=null && dates[i].length()>0)
    				scheduled(dates[i]);
    		}
    	}else {
    		String trandate = G4Utils.getOneDate(Constants.NOMAL_Date, -1);// 0:当天  -1:上一天  1:下一天
    		scheduled(trandate);
    	}
		// 文件解压缩
/*		LinuxZip zip = new LinuxZip();
		ret = zip.unTar(localfile, local);
		if(!ret){
			log.error("解压缩文件[{}]失败，[{}]将不予导入IBPS文件。",localfile,trandate);
			return;
		}*/
		
    	log.info("getsftp定时任务结束");
	}
    private void scheduled(String trandate) {
		if(files==null && files.length<=0 )return;
		if(files.length<=0)return;
		StringBuffer sbpatch = new StringBuffer();
		sbpatch.append(local);
		sbpatch.append("/");
		sbpatch.append(trandate);
		sbpatch.append("/816/");
		File src =  new File(sbpatch.toString());
		if(!src.exists()){
			src.mkdirs();
		}
		String localPath = sbpatch.toString();
		
		StringBuffer remoteDirpatch = new StringBuffer();
		remoteDirpatch.append("/");
		remoteDirpatch.append(trandate);
		remoteDirpatch.append("/816/");
		String remoteDirPath = remoteDir + remoteDirpatch.toString();
		// 下载文件 
		boolean ret = false;
		ret = sftp.connect();
		
		if(!ret){
			log.error("无法连接SFTP服务器，[{}]将不予导入IBPS文件。",trandate);
			sftp.disconnect();
			return;
		}
		
		for(int i=0;i<files.length;i++) {
			String filename = files[i]; // 远程文件名称
			ret = sftp.downloadFile(remoteDirPath, filename, localPath);
			if(!ret){
				log.error("下载文件失败，[{}]将不予导入。",trandate);
				continue;
			}
			log.info("[{}]文件[{}]完成。",trandate,filename);
		}
		sftp.disconnect();
    }
}