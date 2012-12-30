package com.jq.client.protocol.udp;

import com.jq.util.Param;

/**
 * ����Message�������еĴ��������. 
 * ����Ϊ�������,��������� 
 * AddtionMsg,
 * NomalMsg,
 * OnlineMsg, 
 * OutlineMsg,
 * RemoveMsg,
 * SendFileMsg.
 * ���ཫ��дupdate������ʵ�ֲ�ͬ�ĸ��¹���.
 * */
public abstract class AbstractMessage {
	
	/** ������ID */
	protected String ID;
	/** ��Ϣ�� */
	protected String msgBody = null;
	
	/**
	 * ���캯��.
	 * 
	 * @param msg
	 *            ���յ�����Ϣ. ��Ϣ��ʽ [ID_��Ϣ��]
	 * */
	public AbstractMessage(String msg) {
		/** �ո�λ�� */
		int i = msg.indexOf(Param.SPACE); 
		
		if(i == -1){
			ID = msg;
			msgBody = "";
		}else{
			ID = msg.substring(0, i);
			msgBody = msg.substring(i + 1);
		} 			
	}

	/**
	 * �����д�ĸ��·���. ��ͬ������Ϣ��д�ɲ�ͬ�ķ���.
	 * */
	public abstract void update(UDPServer UDP);

	
}
