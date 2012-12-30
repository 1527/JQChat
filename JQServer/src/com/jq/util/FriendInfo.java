package com.jq.util;

import java.io.Serializable;

/** �洢�������� */
public class FriendInfo implements Serializable {

	private static final long serialVersionUID = 7524772647377526557L;

	public String id = null;
	public String nickname = null;
	public String signedString = null;
	public String face = "2";

	public String sex = "��";
	public String province = null;
	public String city = null;
	public String mail = null;
	public String homePage = null;
	public int age = 0;
	public String academy = null;
	public String department = null;
	
	/**
	 * ���캯��.
	 * 
	 * @param sex
	 *            �Ա�.
	 * @param province
	 *            ʡ��.
	 * @param city
	 *            ����.
	 * @param mail
	 *            ����.
	 * @param homePage
	 *            ��ҳ.
	 * @param age
	 *            ����.
	 * @param Academy
	 *            Ժϵ.
	 * @param department
	 *            רҵ.
	 * */
	public FriendInfo(String sex, String province, String city, String mail,
			String homePage, String age, String academy, String department) {
		this.sex = sex;
		this.province = province;
		this.city = city;
		this.mail = mail;
		this.homePage = homePage;
		if (age.length() != 0)
			this.age = Integer.parseInt(age);
		else
			this.age = 18; // Ĭ��18

		this.academy = academy;
		this.department = academy;
	}
}
