package com.jq.util;

/** �����ļ��е�Ĭ�ϲ��� */
public class Param {

	public static String DRIVER = "driver";
	public static String URL = "url";
	public static String USER = "user";
	public static String PASSWORD = "password";
	public static String POOLMAX = "poolmax";

	public static String SPACE = " ";
	/** ƽ̨�޹�--���� */
	public static final String NEWLINE = System.getProperty("line.separator");
	/** ��ǰ·�� */
	public static final String CURRENTPATH = System.getProperties()
			.getProperty(("user.dir")) + "/properties/";	//user.dir �û��ĵ�ǰ����Ŀ¼ 
	/** ���ݿ������ļ����� */
	public static final String FILENAME = "jdbc config.properties";
	/** Ĭ�����ݿ������ļ����� */
	public static final String PROPERTYFILE = "driver=com.mysql.jdbc.Driver"
			+ NEWLINE + "url=jdbc:mysql://localhost:3306/jq_database" 
			+ NEWLINE + "user=root" 
			+ NEWLINE + "password=123456" 
			+ NEWLINE + "poolmax=10";

}
