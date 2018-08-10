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
public interface ProductMapper {

    @Select("SELECT * FROM SLY_PRODUCT_INFO WHERE TRANDT = #{TRANDT} and COSTMID = #{COSTMID} ")
    HashMap<String,String> getOne(HashMap<String,String> map);


    @Insert("INSERT INTO SLY_PRODUCT_INFO VALUES(#{COSTMID}, #{TRANDT}, #{BRACHID}, #{BRACHNM}, #{COSTMNM}, #{COSTMCT}, #{CARDCT}, #{PASBKCT}, #{DEPOSITCT}, #{MOBILECT},#{PERSONCT})")
    void insert(HashMap<String,String> map);

    @Update("UPDATE SLY_PRODUCT_INFO SET BRACHID=#{BRACHID}, BRACHNM=#{BRACHNM}, COSTMNM=#{COSTMNM}, COSTMCT=#{COSTMCT}, CARDCT=#{CARDCT}, PASBKCT=#{PASBKCT}, DEPOSITCT=#{DEPOSITCT}, MOBILECT=#{MOBILECT},PERSONCT=#{PERSONCT} WHERE COSTMID =#{COSTMID} and TRANDT = #{TRANDT}")
    void update(HashMap<String,String> map);

    @Delete("DELETE FROM users WHERE id =#{id}")
    void delete(Long id);

}