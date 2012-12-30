
package com.jq.client.protocol.tcp;

import java.io.IOException;

/**
 *  �û����ߺ������������������Ϣ.
 *  �Զ�����,�Զ��ر���.
 *  �̳���AbstractService
 *  ʵ����Service<String,String>�ӿ�
 * */
public class LogoutService extends AbstractService implements Service<String, String>{
	
	/** ���ߵĹ��캯�� */
	public LogoutService(TCPServer TCP) {
		super(TCP);
	}
	
	/**
	 * ���ߵľ��巽��
	 * @param msg �û�ID
	 * */
	public String service(String msg) {
		//ע����ʽ ID
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_LOGOUT + msg);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally{
			TCP.close();
		}
		
		return null;
	}
}
