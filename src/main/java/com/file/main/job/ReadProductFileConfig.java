package com.file.main.job;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
import com.file.main.entity.ProductMapper;
import com.file.main.utils.Constants;
import com.file.main.utils.G4Utils;

@Configuration
@EnableScheduling
public class ReadProductFileConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private ProductMapper productMapper;
    
    @Value("${sftp.local}")
    private String local;
    @Value("${product.isopen}")
    private Integer isopen;
    @Scheduled(cron = "${product.productTimer}") // 每天2点执行
    public void getToken() {
    	if(isopen==0) {
    		return;
    	}
        log.info("product定时任务启动");
		String trandate = G4Utils.getOneDate(Constants.NOMAL_Date, -1);// 0:当天  -1:上一天  1:下一天
		StringBuffer sbpatch = new StringBuffer();
		sbpatch.append(local);
		sbpatch.append("/");
		sbpatch.append(trandate);
		sbpatch.append("/816/");
		File src =  new File(sbpatch.toString());
		if(!src.exists()){
			src.mkdir();
		}
		local = sbpatch.toString();

		String ibpsfile = local+"PRODUCT.del"; // 本地绝对路径IBPS文件 PRODUCT
		// 文件入库
		try {
			String encoding = "UTF-8"; // 以GBK编码读取文件
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ibpsfile), Charset.forName(encoding)));
			String line=null;
			
			line=br.readLine();
			
			while(line!=null){
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
				String COSTMCT = TrimColon(dits[5]);
				String CARDCT = TrimColon(dits[6]);
				String PASBKCT = TrimColon(dits[7]);
				String DEPOSITCT = TrimColon(dits[8]);
				String MOBILECT = TrimColon(dits[9]);
				String PERSONCT = TrimColon(dits[10]);
//				log.debug("bankid[{}] bank_type[{}] phy_name[{}] short_name[{}] bankid2[{}] status[{}] active_date[{}]",bankid,bank_type,phy_name,short_name,bankid2,bksts,active_date);
				
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("TRANDT", TRANDT);
				map.put("BRACHID", BRACHID);
				map.put("BRACHNM", BRACHNM);
				map.put("COSTMID", COSTMID);
				map.put("COSTMNM", COSTMNM);
				map.put("COSTMCT", COSTMCT);
				map.put("CARDCT", CARDCT);
				map.put("PASBKCT", PASBKCT);
				map.put("DEPOSITCT", DEPOSITCT);
				map.put("MOBILECT", MOBILECT);
				map.put("PERSONCT", PERSONCT);	
				
				HashMap<String,String> out = productMapper.getOne(map);
				if(out == null){
					productMapper.insert(map);
				}
				
				// 循环下一行
	        	line=br.readLine();
			}
			log.debug("[{}]更新文件[{}]完成。",trandate,"product");
			br.close();	
		} catch (FileNotFoundException e) {
			log.error("本地文件不存在[{}]，[{}]将不予导入IBPS文件。","product",trandate,e);
		} catch (IOException e) {
			log.error("读取本地文件失败[{}]，[{}]将不予导入IBPS文件。","product",trandate,e);
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