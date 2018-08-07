package com.file.main.utils;

/**
 * 常量表
 * 
 */
public interface Constants {
	/**
	 * XML文档风格<br>
	 * 0:节点属性值方式
	 */
	public static final String XML_Attribute = "0";

	/**
	 * XML文档风格<br>
	 * 1:节点元素值方式
	 */
	public static final String XML_Node = "1";

	/**
	 * 字符串组成类型<br>
	 * number:数字字符串
	 */
	public static final String S_STYLE_N = "number";

	/**
	 * 字符串组成类型<br>
	 * letter:字母字符串
	 */
	public static final String S_STYLE_L = "letter";

	/**
	 * 字符串组成类型<br>
	 * numberletter:数字字母混合字符串
	 */
	public static final String S_STYLE_NL = "numberletter";

	/**
	 * 格式化(24小时制)<br>
	 * FORMAT_DateTime: 日期时间
	 */
	public static final String FORMAT_DateTime = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 格式化(12小时制)<br>
	 * FORMAT_DateTime: 日期时间
	 */
	public static final String FORMAT_DateTime_12 = "yyyy-MM-dd hh:mm:ss";

	/**
	 * 格式化<br>
	 * FORMAT_DateTime: 日期
	 */
	public static final String FORMAT_Date = "yyyy-MM-dd";
	public static final String NOMAL_Date = "yyyyMMdd";
	public static final String DATE_PATTERN = "MMdd";
	public static final String TIME_PATTERN = "HHmmss";
	public static final String DATE_TIME_PATTERN ="yyMMddHHmmss";
	public static final String DATE_TIME ="yyyyMMddHHmmss";
	public static final String YEAR_PATTERN = "yyyy";

	/**
	 * 格式化(24小时制)<br>
	 * FORMAT_DateTime: 时间
	 */
	public static final String FORMAT_Time = "HH:mm:ss";
	
	/**
	 * 格式化(12小时制)<br>
	 * FORMAT_DateTime: 时间
	 */
	public static final String FORMAT_Time_12 = "hh:mm:ss";

	/**
	 * 换行符<br>
	 * \n:换行
	 */
	public static final String ENTER = "\n";

	/**
	 * 异常信息统一头信息<br>
	 * 非常遗憾的通知您,程序发生了异常
	 */
	public static final String Exception_Head = "\nOH,MY GOD! SOME ERRORS OCCURED! " + "AS FOLLOWS.\n";


	/**
	 * 分页查询分页参数缺失错误信息
	 */
	public static final String ERR_MSG_QUERYFORPAGE_STRING = "您正在使用分页查询,但是你传递的分页参数缺失!如果不需要分页操作,您可以尝试使用普通查询:queryForList()方法";
	
	
	
	public static final int ATMP_RCIVE_LENGTH = 4;
	
	public static final int ATMP_HEADER_LENGTH = 32;
	
	public static final String ATMP_SEQ_NAME = "atmptransn";
	
	public static final int CUPS_RCIVE_LENGTH = 4;
	// 北京一鸣神州在ATMP的ID标识
	public static final String YMSZ_BANKID = "60000000";
	// 烟台银行在ATMP的ID标识
	public static final String YANTAI_BANKID = "04044560";
	// 绵阳银行在ATMP的ID标识
	public static final String MIANY_BANKID = "04856590";	
	
	
}