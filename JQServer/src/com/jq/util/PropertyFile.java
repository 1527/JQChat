package com.jq.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.jq.util.Param;

/**
 * [�����ļ���ȡ��] 
 * Properties�����ȡ���ݿ������ļ�, ͨ��PropertyFile������Ի����ϸ����.
 * ����mySQL���ݿ��û�����,����.JDBC��������,URL�� 
 * ʵ�������Ӧ���˺�,�����������Ϣ���м���****
 * 
 */
public class PropertyFile {
	
	private Properties prop = null;
	
	private String url; // url
	private String user; // mySQL�û���user
	private String password; // mySQL�û�����
	private String driver; // mySQL��������
	private int max; // ���ӳ������Connection��Ŀ

	/**
	 *@param filePath Properties�ļ���·��
	 */
	public PropertyFile(String filePath) {	
		prop = new Properties();

		try {
			prop.load(new FileInputStream(new File(filePath)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ��ȡProperties��ϸ��Ϣ
		driver = prop.getProperty(Param.DRIVER);
		url = prop.getProperty(Param.URL);
		user = prop.getProperty(Param.USER);
		password = prop.getProperty(Param.PASSWORD);
		max = Integer.parseInt(prop.getProperty(Param.POOLMAX));
	}

	/** ����URL */
	public String getURL() {
		return url;
	}

	/** �����˺�User */
	public String getUser() {
		return user;
	}

	/** ��������Password	 */
	public String getPassword() {
		return password;
	}
	
	/** ����mySQL���� driver */
	public String getDriver() {
		return driver;
	}

	/** �������ӳ����������Ŀ	 */
	public int getMax() {
		return max;
	}
}