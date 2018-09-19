package com.file.main.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.HashMap;
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
import com.file.main.entity.AvgBalacneMapper;
import com.file.main.entity.BalacneMapper;
import com.file.main.entity.ProductMapper;
import com.file.main.utils.Constants;
import com.file.main.utils.G4Utils;

@Configuration
@EnableScheduling
public class ReadAvgBalanceFileConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private AvgBalacneMapper avgbalacneMapper;
    
    @Value("${sftp.local}")
    private String local;
    @Value("${avgbalance.isopen}")
    private Integer isopen;
    @Value("${file.dates}")
    private String[] dates;
    @Scheduled(cron = "${avgbalance.avgbalanceTimer}") // 每天2点执行
    public void getToken() {
    	if(isopen==0) {
    		return;
    	}
    	log.info("avg_balance定时任务启动");
    	if(dates != null && dates.length>0) {
    		for(int i=0;i<dates.length;i++) {
    			if(dates[i]!=null && dates[i].length()>0)
    				scheduled(dates[i]);
    		}
    	}else {
    		String trandate = G4Utils.getOneDate(Constants.NOMAL_Date, -1);// 0:当天  -1:上一天  1:下一天
    		scheduled(trandate);
    	}
		
		
	}
    private void scheduled(String trandate) {
		StringBuffer sbpatch = new StringBuffer();
		sbpatch.append(local);
		sbpatch.append("/");
		sbpatch.append(trandate);
		sbpatch.append("/");
		File src =  new File(sbpatch.toString());
		if(!src.exists()){
			src.mkdir();
		}
		String localPath = sbpatch.toString();

		String ibpsfile = localPath+"AVG_BALANCE.del"; // 本地绝对路径IBPS文件 PRODUCT
		// 文件入库
		try {
			String encoding = "UTF-8"; // 以GBK编码读取文件
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ibpsfile), Charset.forName(encoding)));
			String line=null;
			
			line=br.readLine();
			
			while(line!=null){
				try {
				String[] dits = line.split("\\|"); // 逗号分隔
				if(dits.length<11){
					line=br.readLine();
	        		continue;
				}
				String TRANDT = TrimColon(dits[0]);
				String BRACHID = TrimColon(dits[1]);
				String BRACHNM = TrimColon(dits[2]);
				String COSTMID = TrimColon(dits[3]);
				String COSTMNM = TrimColon(dits[4]);
				

				String HOURCURERENT = TrimNumColon(dits[5]);
				String HOURTHREEMONTH = TrimNumColon(dits[6]);
				String HOURSIXMONTH = TrimNumColon(dits[7]);
				String HOURONEYEAR = TrimNumColon(dits[8]);
				String HOURTWOYEAR = TrimNumColon(dits[9]);
				String HOURTHREEYEAR = TrimNumColon(dits[10]);
				String HOURFIVEYEAR = TrimNumColon(dits[11]);
				String HOURNOTICE = TrimNumColon(dits[12]);
				String HOUR369 = TrimNumColon(dits[13]);
				String HOUR368 = TrimNumColon(dits[14]);
				String HOURLARGE = TrimNumColon(dits[15]);
				String HOUROHTER = TrimNumColon(dits[16]);
				
				
				String DAYCURERENT = TrimNumColon(dits[17]);
				String DAYTHREEMONTH = TrimNumColon(dits[18]);
				String DAYSIXMONTH = TrimNumColon(dits[19]);
				String DAYONEYEAR = TrimNumColon(dits[20]);
				String DAYTWOYEAR = TrimNumColon(dits[21]);
				String DAYTHREEYEAR = TrimNumColon(dits[22]);
				String DAYFIVEYEAR = TrimNumColon(dits[23]);
				String DAYNOTICE = TrimNumColon(dits[24]);
				String DAY369 = TrimNumColon(dits[25]);
				String DAY368 = TrimNumColon(dits[26]);
				String DAYLARGE = TrimNumColon(dits[27]);
				String DAYOHTER = TrimNumColon(dits[28]);
				
				
				//log.debug("TRANDT[{}] BRACHID[{}] BRACHNM[{}] COSTMID[{}] COSTMNM[{}]",bankid,bank_type,phy_name,short_name,bankid2,bksts,active_date);
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("TRANDT", TRANDT);
				map.put("BRACHID", BRACHID);
				map.put("BRACHNM", BRACHNM);
				map.put("COSTMID", COSTMID);
				map.put("COSTMNM", COSTMNM);

				map.put("HOURCURERENT", HOURCURERENT);
				map.put("HOURTHREEMONTH", HOURTHREEMONTH);
				map.put("HOURSIXMONTH", HOURSIXMONTH);
				map.put("HOURONEYEAR", HOURONEYEAR);
				map.put("HOURTWOYEAR", HOURTWOYEAR);
				map.put("HOURTHREEYEAR", HOURTHREEYEAR);	
				map.put("HOURFIVEYEAR", HOURFIVEYEAR);
				map.put("HOURNOTICE", HOURNOTICE);
				map.put("HOUR369", HOUR369);
				map.put("HOUR368", HOUR368);
				map.put("HOURLARGE", HOURLARGE);
				map.put("HOUROHTER", HOUROHTER);
				
				map.put("DAYCURERENT", DAYCURERENT);
				map.put("DAYTHREEMONTH", DAYTHREEMONTH);
				map.put("DAYSIXMONTH", DAYSIXMONTH);
				map.put("DAYONEYEAR", DAYONEYEAR);
				map.put("DAYTWOYEAR", DAYTWOYEAR);
				map.put("DAYTHREEYEAR", DAYTHREEYEAR);	
				map.put("DAYFIVEYEAR", DAYFIVEYEAR);
				map.put("DAYNOTICE", DAYNOTICE);
				map.put("DAY369", DAY369);
				map.put("DAY368", DAY368);
				map.put("DAYLARGE", DAYLARGE);
				map.put("DAYOHTER", DAYOHTER);
				
				HashMap<String,String> out = avgbalacneMapper.getOne(map);
				if(out == null){
					avgbalacneMapper.insert(map);
				}else {
					avgbalacneMapper.update(map);
				}
				}catch (Exception e) {
					// TODO: handle exception
					continue;
				}finally {
					line=br.readLine();
				}
				// 循环下一行
			}
			log.info("[{}]更新文件[{}]完成。",trandate,"avgbalacne");
			br.close();	
		} catch (FileNotFoundException e) {
			log.error("本地文件不存在[{}]，[{}]将不予导入IBPS文件。","avgbalance",trandate,e);
		} catch (IOException e) {
			log.error("读取本地文件失败[{}]，[{}]将不予导入IBPS文件。","avgbalance",trandate,e);
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
	private String TrimNumColon(String param){
		String val = param;
		if(val==null){
			return "0.00";
		}
		if(val.startsWith("\"")){
			val = val.substring(1);
		}
		if(val.endsWith("\"")){
			val = val.substring(0,val.length()-1);
		}
		val = val.trim();
		if(val.length()<=0) {
			return "0.00";
		}
		return val;
	}
}