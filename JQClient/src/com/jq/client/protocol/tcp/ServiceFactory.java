
package com.jq.client.protocol.tcp;

import java.lang.reflect.Constructor;

/**��ȡ��Ӧ����ķ��񹤳��Ĵ���.*/
public class ServiceFactory {
	/**��½*/
	public static final String TASK_LOGIN = "0";
	/**ע��*/
	public static final String TASK_LOGOUT = "1";
	/**���û�ע��*/
	public static final String TASK_NEWUSER = "2";
	/**�����û�����*/
	public static final String TASK_UPDATAINFO = "3";
	/**�鿴��������*/
	public static final String TASK_GETINFO = "4";
	/**��������*/
	public static final String TASK_SEARCH = "5";
	/**��Ӻ���*/
	public static final String TASK_ADDTION = "6";
	/**ɾ������*/
	public static final String TASK_REMOVE = "7";
	/**������Ϣ*/
	public static final String TASK_LEFTINFO = "8";
	/**��ȡ��������*/
	public static final String TASK_PASSWORD = "9";
	/**�������£�������*/
	//public static final String TASK_OTHER= "10";
	
	private static final String[] TYPE = {
			"com.jq.client.protocol.tcp.LoginService",
			"com.jq.client.protocol.tcp.LogoutService",
			"com.jq.client.protocol.tcp.RegisterService",
			"com.jq.client.protocol.tcp.UpdateInfoService",
			"com.jq.client.protocol.tcp.FriendInfoService",
			"com.jq.client.protocol.tcp.SearchService",
			"com.jq.client.protocol.tcp.AddtionService",
			"com.jq.client.protocol.tcp.RemoveFriendService",
			"com.jq.client.protocol.tcp.LeftInfoService",
			"com.jq.client.protocol.tcp.PasswordService" };

	/** ���÷���ʹ��ָ�����ι��췽������ָ��������Ķ��� 
	 * @return Service */
	public static Service<?,?> getService(String serviceType,String IP,int port){
		Class<?> c;
		Constructor<?> constructor;
		/* ʹ�÷�����Ƽ�������� */
		String className = TYPE[Integer.parseInt(serviceType)];
		try {
			//����ָ�����Ƶ��࣬��ȡ��Ӧ��Class����
			c = Class.forName(className);
			/*�������Ĺ�����.*/
			//��ȡ����ָ���������͵Ĺ��췽�� 
			constructor = c.getConstructor(TCPServer.class);
			//��ָ���Ĺ��췽���������ֵ��������һ������
			return (Service<?,?>) constructor.newInstance(new TCPServer(IP,port));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
