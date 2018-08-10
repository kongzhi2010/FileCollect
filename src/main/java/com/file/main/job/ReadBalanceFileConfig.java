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
import com.file.main.entity.BalacneMapper;
import com.file.main.entity.ProductMapper;
import com.file.main.utils.Constants;
import com.file.main.utils.G4Utils;

@Configuration
@EnableScheduling
public class ReadBalanceFileConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private BalacneMapper balacneMapper;
    
    @Value("${sftp.local}")
    private String local;
    @Value("${balance.isopen}")
    private Integer isopen;
    @Value("${file.dates}")
    private String[] dates;
    @Scheduled(cron = "${balance.balanceTimer}") // 每天2点执行
    public void getToken() {
    	if(isopen==0) {
    		return;
    	}
    	log.info("balance定时任务启动");
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

		String ibpsfile = localPath+"BALANCE.del"; // 本地绝对路径IBPS文件 PRODUCT
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
				String HOURREGULAR = TrimNumColon(dits[6]);
				String HOURCOUNT= "0.00";
				try {
				 HOURCOUNT = (new BigDecimal(String.valueOf(HOURCURERENT)).add(new BigDecimal(String.valueOf(HOURREGULAR)))).toString();
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				String MONTHCURERENT = TrimNumColon(dits[7]);
				String MONTHREGULAR = TrimNumColon(dits[8]);
				String MONTHCOUNT= "0.00";
				try {
					MONTHCOUNT = (new BigDecimal(String.valueOf(MONTHCURERENT)).add(new BigDecimal(String.valueOf(MONTHREGULAR)))).toString();
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				String QUARTERCURERENT = TrimNumColon(dits[9]);
				String QUARTERREGULAR = TrimNumColon(dits[10]);
				String QUARTERCOUNT= "0.00";
				try {
					QUARTERCOUNT = (new BigDecimal(String.valueOf(QUARTERCURERENT)).add(new BigDecimal(String.valueOf(QUARTERREGULAR)))).toString();
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				String YEARCURERENT = TrimNumColon(dits[11]);
				String YEARREGULAR = TrimNumColon(dits[12]);
				String YEARCOUNT= "0.00";
				try {
					YEARCOUNT = (new BigDecimal(String.valueOf(YEARCURERENT)).add(new BigDecimal(String.valueOf(YEARREGULAR)))).toString();
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				String PROMONTHCURERENT = TrimNumColon(dits[13]);
				String PROMONTHREGULAR = TrimNumColon(dits[14]);
				String PROMONTHCOUNT= "0.00";
				try {
					PROMONTHCOUNT = (new BigDecimal(String.valueOf(PROMONTHCURERENT)).add(new BigDecimal(String.valueOf(PROMONTHREGULAR)))).toString();
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				String PROQUARTERCURERENT = TrimNumColon(dits[15]);
				String PROQUARTERREGULAR = TrimNumColon(dits[16]);
				String PROQUARTERCOUNT= "0.00";
				try {
					PROQUARTERCOUNT = (new BigDecimal(String.valueOf(PROQUARTERCURERENT)).add(new BigDecimal(String.valueOf(PROQUARTERREGULAR)))).toString();
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				String PROYEARCURERENT = TrimNumColon(dits[17]);
				String PROYEARREGULAR = TrimNumColon(dits[18]);
				String PROYEARCOUNT= "0.00";
				try {
					PROYEARCOUNT = (new BigDecimal(String.valueOf(PROYEARCURERENT)).add(new BigDecimal(String.valueOf(PROYEARREGULAR)))).toString();
				}catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				//log.debug("TRANDT[{}] BRACHID[{}] BRACHNM[{}] COSTMID[{}] COSTMNM[{}]",bankid,bank_type,phy_name,short_name,bankid2,bksts,active_date);
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("TRANDT", TRANDT);
				map.put("BRACHID", BRACHID);
				map.put("BRACHNM", BRACHNM);
				map.put("COSTMID", COSTMID);
				map.put("COSTMNM", COSTMNM);

				map.put("HOURCURERENT", HOURCURERENT);
				map.put("HOURREGULAR", HOURREGULAR);
				map.put("HOURCOUNT", HOURCOUNT);
				
				map.put("MONTHCURERENT", MONTHCURERENT);
				map.put("MONTHREGULAR", MONTHREGULAR);
				map.put("MONTHCOUNT", MONTHCOUNT);	
				
				map.put("QUARTERCURERENT", QUARTERCURERENT);
				map.put("QUARTERREGULAR", QUARTERREGULAR);
				map.put("QUARTERCOUNT", QUARTERCOUNT);
				
				map.put("YEARCURERENT", YEARCURERENT);
				map.put("YEARREGULAR", YEARREGULAR);
				map.put("YEARCOUNT", YEARCOUNT);
				
				map.put("PROMONTHCURERENT", PROMONTHCURERENT);
				map.put("PROMONTHREGULAR", PROMONTHREGULAR);
				map.put("PROMONTHCOUNT", PROMONTHCOUNT);
				
				map.put("PROQUARTERCURERENT", PROQUARTERCURERENT);
				map.put("PROQUARTERREGULAR", PROQUARTERREGULAR);
				map.put("PROQUARTERCOUNT", PROQUARTERCOUNT);
				
				map.put("PROYEARCURERENT", PROYEARCURERENT);
				map.put("PROYEARREGULAR", PROYEARREGULAR);
				map.put("PROYEARCOUNT", PROYEARCOUNT);
				
				HashMap<String,String> out = balacneMapper.getOne(map);
				if(out == null){
					balacneMapper.insert(map);
				}else {
					balacneMapper.update(map);
				}
				}catch (Exception e) {
					// TODO: handle exception
					continue;
				}finally {
					line=br.readLine();
				}
				// 循环下一行
			}
			log.info("[{}]更新文件[{}]完成。",trandate,"balacne");
			br.close();	
		} catch (FileNotFoundException e) {
			log.error("本地文件不存在[{}]，[{}]将不予导入IBPS文件。","balacn",trandate,e);
		} catch (IOException e) {
			log.error("读取本地文件失败[{}]，[{}]将不予导入IBPS文件。","balacn",trandate,e);
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