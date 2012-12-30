package com.jq.client.protocol.udp;

import java.lang.reflect.Constructor;

/**
 * 	��Ϣ������.���ڲ����ض����͵���Ϣ������.
 * */
public class MsgFactory {
	
	/** ��Ϣ��������� */
	private static final String[] TYPE = {
			"com.jq.client.protocol.udp.NomalMsg",
			"com.jq.client.protocol.udp.OnlineMsg",
			"com.jq.client.protocol.udp.OutlineMsg",
			"com.jq.client.protocol.udp.AddtionMsg",
			"com.jq.client.protocol.udp.RemoveMsg",
			"com.jq.client.protocol.udp.SendFileMsg",
			"com.jq.client.protocol.udp.RejectMsg"};

	/**������Ϣ����*/
	public static final int NOMALMSG = 0;
	/**������Ϣ����*/
	public static final int ONLINEMSG = 1;
	/**������Ϣ����*/
	public static final int OUTLINEMSG = 2;
	/**���������Ϣ����*/
	public static final int ADTIONMSG = 3;
	/**����ɾ����Ϣ����*/
	public static final int REMOVEMSG = 4;
	/**�����ļ���Ϣ����*/
	public static final int SENDFILEMSG = 5;
	/**�ܾ������ļ���Ϣ����*/
	public static final int REJECTMSG = 6;
	
	/**
	 * ��ȡ�ض���Ϣ����������Ϣ.
	 * 
	 * @param msg
	 *            ���յ�����Ϣ
	 *            ��Ϣ��ʽ[����_ID_��Ϣ��].
	 *            ����:
	 *            			0-----��ͨ������Ϣ.
	 *            			1-----����������Ϣ.
	 *            			2-----����������Ϣ.
	 *            			3-----���������Ϣ.
	 *            			4-----����ɾ����Ϣ.
	 *            			5-----�����ļ���Ϣ.
	 *            
	 * @return AbstractMessage �������ڴ�����Ϣ����Ϣ������,���ʧ�ܷ���null.
	 * @throws ClassNotFoundException
	 * */
	public static AbstractMessage getMsg(String msg){
		/*����--->��Ϣ����*/
		int type = Integer.parseInt(msg.substring(0, 1));
		/*����--->ID_��Ϣ��*/
		msg = msg.substring(1);

		/*ʹ�÷�����Ƽ��������*/
		String className = TYPE[type];
		Class<?> c;
		@SuppressWarnings("rawtypes")
		Constructor constructor;
		try {
			c = Class.forName(className);
			/*�������Ĺ�����.*/
			constructor = c.getConstructor(String.class);
			return (AbstractMessage) constructor.newInstance(msg);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
