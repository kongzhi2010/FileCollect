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
public interface BalacneMapper {

    @Select("SELECT * FROM SLY_BALANCE_INFO WHERE TRANDT = #{TRANDT} and COSTMID = #{COSTMID} ")
    HashMap<String,String> getOne(HashMap<String,String> map);


    @Insert("INSERT INTO SLY_BALANCE_INFO VALUES(#{COSTMID}, #{TRANDT}, #{BRACHID}, #{BRACHNM}, #{COSTMNM}, #{HOURCURERENT}, #{HOURREGULAR}, #{HOURCOUNT}, "
    	+ "#{MONTHCURERENT}, #{MONTHREGULAR},#{MONTHCOUNT}, #{QUARTERCURERENT}, #{QUARTERREGULAR},#{QUARTERCOUNT}, #{YEARCURERENT}, #{YEARREGULAR},#{YEARCOUNT},"
    	+ "#{PROMONTHCURERENT}, #{PROMONTHREGULAR},#{PROMONTHCOUNT}, #{PROQUARTERCURERENT}, #{PROQUARTERREGULAR},#{PROQUARTERCOUNT}, #{PROYEARCURERENT}, #{PROYEARREGULAR},#{PROYEARCOUNT})")
    void insert(HashMap<String,String> map);

    @Update("UPDATE SLY_BALANCE_INFO SET BRACHID=#{BRACHID}, BRACHNM=#{BRACHNM}, COSTMNM=#{COSTMNM}, "
    		+ "HOURCURERENT = #{HOURCURERENT}, HOURREGULAR = #{HOURREGULAR}, HOURCOUNT = #{HOURCOUNT}," 
    		+ "MONTHCURERENT = #{MONTHCURERENT}, MONTHREGULAR = #{MONTHREGULAR},MONTHCOUNT = #{MONTHCOUNT}, QUARTERCURERENT = #{QUARTERCURERENT}, QUARTERREGULAR = #{QUARTERREGULAR},QUARTERCOUNT = #{QUARTERCOUNT}, YEARCURERENT = #{YEARCURERENT}, YEARREGULAR = #{YEARREGULAR},YEARCOUNT = #{YEARCOUNT}," 
    		+ "PROMONTHCURERENT = #{PROMONTHCURERENT}, PROMONTHREGULAR = #{PROMONTHREGULAR},PROMONTHCOUNT = #{PROMONTHCOUNT}, PROQUARTERCURERENT = #{PROQUARTERCURERENT}, PROQUARTERREGULAR = #{PROQUARTERREGULAR},PROQUARTERCOUNT = #{PROQUARTERCOUNT}, PROYEARCURERENT = #{PROYEARCURERENT}, PROYEARREGULAR = #{PROYEARREGULAR},PROYEARCOUNT = #{PROYEARCOUNT} "
    		+ "WHERE COSTMID =#{COSTMID} and TRANDT = #{TRANDT}")
    void update(HashMap<String,String> map);

    @Delete("DELETE FROM SLY_BALANCE_INFO WHERE id =#{id}")
    void delete(Long id);

}