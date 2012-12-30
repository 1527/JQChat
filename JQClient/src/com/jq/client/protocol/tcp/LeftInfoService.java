
package com.jq.client.protocol.tcp;

/** ������Ϣ�������
 * �̳���AbstractService
 * ʵ����Service<String,String>�ӿ�*/
public class LeftInfoService extends AbstractService implements Service<String,String>{

	/** ������Ϣ�Ĺ��캯�� */
	public LeftInfoService(TCPServer TCP) {
		super(TCP);
	}

	/**
	 * ������Ϣ���巽��
	 * @param msg �����ѷ��͵���Ϣ.
	 * */
	public String service(String msg) {
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_LEFTINFO + msg);//���͵�¼����
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
		
		return null;
	}

}
