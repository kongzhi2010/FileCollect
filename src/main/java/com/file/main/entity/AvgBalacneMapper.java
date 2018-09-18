package com.file.main.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


/**
 * Copyright 2012 int-yt
 *
 * All right reserved.
 *
 * @author ww
 * @date Created on 2018年5月9日
 */
public interface AvgBalacneMapper {

    @Select("SELECT * FROM SLY_AVG_BALANCE_INFO WHERE TRANDT = #{TRANDT} and COSTMID = #{COSTMID} ")
    HashMap<String,String> getOne(HashMap<String,String> map);


    @Insert("INSERT INTO SLY_AVG_BALANCE_INFO VALUES(#{COSTMID}, #{TRANDT}, #{BRACHID}, #{BRACHNM}, #{COSTMNM}, "
    	+ "#{HOURCURERENT}, #{HOURTHREEMONTH}, #{HOURSIXMONTH},#{HOURONEYEAR}, #{HOURTWOYEAR},#{HOURTHREEYEAR}, #{HOURFIVEYEAR}, #{HOURNOTICE},#{HOUR369}, #{HOUR368}, #{HOURLARGE},#{HOUROHTER},"
    	+ "#{DAYCURERENT}, #{DAYTHREEMONTH}, #{DAYSIXMONTH},#{DAYONEYEAR}, #{DAYTWOYEAR},#{DAYTHREEYEAR}, #{DAYFIVEYEAR}, #{DAYNOTICE},#{DAY369}, #{DAY368}, #{DAYLARGE},#{DAYOHTER}")
    void insert(HashMap<String,String> map);

    @Update("UPDATE SLY_AVG_BALANCE_INFO SET BRACHID=#{BRACHID}, BRACHNM=#{BRACHNM}, COSTMNM=#{COSTMNM}, "
    		+ "HOURCURERENT = #{HOURCURERENT}, HOURTHREEMONTH = #{HOURTHREEMONTH}, HOURSIXMONTH = #{HOURSIXMONTH},HOURONEYEAR = #{HOURONEYEAR}, HOURTWOYEAR = #{HOURTWOYEAR},HOURTHREEYEAR = #{HOURTHREEYEAR}, HOURFIVEYEAR = #{HOURFIVEYEAR}, HOURNOTICE = #{HOURNOTICE},HOUR369 = #{HOUR369}, HOUR368 = #{HOUR368}, HOURLARGE = #{HOURLARGE},HOUROHTER = #{HOUROHTER}, " 
    		+ "DAYCURERENT = #{DAYCURERENT}, DAYTHREEMONTH = #{DAYTHREEMONTH}, DAYSIXMONTH = #{DAYSIXMONTH},DAYONEYEAR = #{DAYONEYEAR}, DAYTWOYEAR = #{DAYTWOYEAR},DAYTHREEYEAR = #{DAYTHREEYEAR}, DAYFIVEYEAR = #{DAYFIVEYEAR}, DAYNOTICE = #{DAYNOTICE},DAY369 = #{DAY369}, DAY368 = #{DAY368}, DAYLARGE = #{DAYLARGE},DAYOHTER = #{DAYOHTER} " 
    		+ "WHERE COSTMID =#{COSTMID} and TRANDT = #{TRANDT}")
    void update(HashMap<String,String> map);

    @Delete("DELETE FROM SLY_AVG_BALANCE_INFO WHERE id =#{id}")
    void delete(Long id);

}