package com.jq.server.service;

import java.lang.reflect.Constructor;

import com.jq.server.service.Service;

/**
 * ��ȡ��Ӧ����ķ��񹤳��Ĵ���.
 * 
 * [������������.] ���ڷ��ش�����ֿͻ���Ҫ��ִ�еĲ���.
 * 
 * */

public class ServiceFactory {

	/** ��½  LoginService */
	public static final String TASK_LOGIN = "0";
	/** ע�� LogoutService */
	public static final String TASK_LOGOUT = "1";
	/** ���û�ע�� RegisterService */
	public static final String TASK_NEWUSER = "2";
	/** �����û����� UpdateInfoService */
	public static final String TASK_UPDATAINFO = "3";
	/** �鿴�������� FriendInfoService */
	public static final String TASK_GETINFO = "4";
	/** �������� SearchService  */
	public static final String TASK_SEARCH = "5";
	/** ��Ӻ��� AddtionService */
	public static final String TASK_ADDTION = "6";
	/** ɾ������ RemoveFriendService */
	public static final String TASK_REMOVE = "7";
	/** ������Ϣ LeftInfoService */
	public static final String TASK_LEFTINFO = "8";
	/** ��ȡ�������� PasswordService */
	public static final String TASK_PASSWORD = "9";
	/** �ɹ���ʾ�� */
	public static final String SUCCESS = "SUCCESS";
	/** ʧ�ܱ�ʾ�� */
	public static final String ERROR = "ERROR";
	/** �û��ѵ�¼��ʾ�� */
	public static final String LOGINED = "9";

	private static final String[] TYPE = { "com.jq.server.service.LoginService",
			"com.jq.server.service.LogoutService",
			"com.jq.server.service.RegisterService",
			"com.jq.server.service.UpdateInfoService",
			"com.jq.server.service.FriendInfoService",
			"com.jq.server.service.SearchService",
			"com.jq.server.service.AddtionService",
			"com.jq.server.service.RemoveFriendService",
			"com.jq.server.service.LeftInfoService",
			"com.jq.server.service.PasswordService" };

	/** ���÷���ʹ��ָ�����ι��췽������ָ��������Ķ��� 
	 * @return Service */
	public static Service getService(String msg) {
		/* ����--->��Ϣ���� */
		int type = Integer.parseInt(msg.substring(0, 1));
		/* ����--->ID_��Ϣ�� */
		msg = msg.substring(1);
		/* ʹ�÷�����Ƽ�������� */
		String className = TYPE[type];
		Class<?> c;
		@SuppressWarnings("rawtypes")
		Constructor constructor;
		try {
			//����ָ�����Ƶ��࣬��ȡ��Ӧ��Class����
			c = Class.forName(className);
			//��ȡ����ָ���������͵Ĺ��췽�� 
			constructor = c.getConstructor(String.class);// String.class ��������
															// Class<String>
			//��ָ���Ĺ��췽���������ֵ��������һ������
			Service s = (Service) constructor.newInstance(msg);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
