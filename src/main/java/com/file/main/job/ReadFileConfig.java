package com.file.main.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class ReadFileConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${sftp.local}")
    private String local;
    //1.@Scheduled(fixedRate = 5000) //通过@Scheduled声明该方法是计划任务，使用fixedRate属性每隔固定时间5秒执行一次 
    //2.@Scheduled(cron = “0 0/10 * * * ?”) //使用cron属性可按照指定时间执行，本例指的是每10分钟执行一次；
    @Scheduled(cron = "${sftp.productTimer}") // 每天2点执行
    public void getToken() {
        log.info("getToken定时任务启动");
		String trandate = G4Utils.getOneDate(Constants.NOMAL_Date, -1);// 0:当天  -1:上一天  1:下一天
		StringBuffer sbpatch = new StringBuffer();
		sbpatch.append(local);
		sbpatch.append("/");
		sbpatch.append(trandate);
		sbpatch.append("/");
		File src =  new File(sbpatch.toString());
		if(!src.exists()){
			src.mkdir();
		}
		local = sbpatch.toString();

		String ibpsfile = local+"/"+"PRODUCT.del"; // 本地绝对路径IBPS文件 BALANCE
		// 文件入库
		try {
			String encoding = "GBK"; // 以GBK编码读取文件
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ibpsfile), Charset.forName(encoding)));
			String line=null;
			
			line=br.readLine();
			
/*			while(line!=null){
				String[] dits = line.split(","); // 逗号分隔
				if(dits.length<11){
					line=br.readLine();
	        		continue;
				}
				String bankid = TrimColon(dits[0]);
				String phy_name = TrimColon(dits[1]);
				String short_name = TrimColon(dits[2]);
				String bank_type = TrimColon(dits[3]);
				String bank_ptytp = TrimColon(dits[4]);
				String bankid2 = TrimColon(dits[5]);
				String bankid3 = TrimColon(dits[6]);
				String active_date = TrimColon(dits[7]);
				String bank_chgtp = TrimColon(dits[8]);
				String bank_chgcnt = TrimColon(dits[9]);
				String bksts = TrimColon(dits[10]);
//				log.debug("bankid[{}] bank_type[{}] phy_name[{}] short_name[{}] bankid2[{}] status[{}] active_date[{}]",bankid,bank_type,phy_name,short_name,bankid2,bksts,active_date);
				
				Dto inDto = new BaseDto();
				Dto out = null;
				inDto.put("bankid", bankid);
				inDto.put("phy_name", phy_name);
				inDto.put("short_name", short_name);
				inDto.put("bank_type", bank_type);
				inDto.put("bank_ptytp", bank_ptytp);
				inDto.put("bankid2", bankid2);
				inDto.put("bankid3", bankid3);
				inDto.put("active_date", active_date);
				inDto.put("bank_chgtp", bank_chgtp);
				inDto.put("bank_chgcnt", bank_chgcnt);
				inDto.put("bksts", bksts);	
				inDto.put("update_date", trandate);
				
				out = selectOneBank(inDto);
				if(out == null){
					insertOneBank(inDto);
				}else{
					updateOneBank(inDto);
				}
				
				// 循环下一行
	        	line=br.readLine();
			}
			log.debug("[{}]更新文件[{}]完成。",trandate,filename);
			br.close();	*/
		} catch (FileNotFoundException e) {
			//log.error("本地文件不存在[{}]，[{}]将不予导入IBPS文件。",localfile,trandate,e);
		} catch (IOException e) {
			//log.error("读取本地文件失败[{}]，[{}]将不予导入IBPS文件。",localfile,trandate,e);
		}
		
		
	}
    
	private String TrimColon(String param){
		String val = param;
		if(val==null){
			return val;
		}
		if(val.startsWith("\"")){
			val = val.substring(1);
		}
		if(val.endsWith("\"")){
			val = val.substring(0,val.length()-1);
		}
		val = val.trim();
		return val;
	}
}