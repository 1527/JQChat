
package com.jq.util;

/** ϵͳ�ĸ��ֲ��� */
public class Param {
	/**�ո�*/
	public static String SPACE = " ";
	/**ƽ̨�޹�--����*/
	public static final String NEWLINE = System.getProperty("line.separator");
	/**IP������ʽ*/
	public static final String IP_REGEX = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])(\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])){3}$";
	/**����ַ�������#dd.gif��ʽ�����������ʽ*/
	public static final String GIF_REGEX = ".*\\d\\d\\.gif.*";
	
	/**��ҳÿҳ��¼����*/
	public static final int PAGE_LINES = 50;
	
	/**��ʾ���壬Ĭ������*/
	public static final String FONT = "΢���ź�";
	/***/
	//public static final String DEFAULT_FONT = "΢���ź�"; 
	/**.GIF*/
	public static final String GIF = ".gif";
	/**Ĭ�Ͽͻ��˶˿�*/
	public static final String CLIENT_PORT = String.valueOf(IPInfo.getClientPort());//"8000";
	/**�������˿�*/
	public static final String SERVER_PORT = String.valueOf(IPInfo.getServerPort());//"6000";
	/**������IP��ַ*/
	public static final String SERVER_IP = IPInfo.getIP();// "127.0.0.1";

	/**���ݰ��ֽڴ�С*/
	public static final int DATA_SIZE = 1024;
	/**��ǰ·��*/
	public static final String CURRENTPATH = System.getProperties().getProperty(("user.dir"));
	/**б��*/
	public static final String ITALIC = "/";
}
