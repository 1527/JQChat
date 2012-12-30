package com.jq.client.protocol.tcp;

/**��ȡ��������������
 * �̳���AbstractService
 * ʵ����Service<String,String>�ӿ�*/
public class PasswordService  extends AbstractService implements Service<String,String>{

	/**��ȡ��������������*/
	public PasswordService(TCPServer TCP) {
		super(TCP);
	}

	/**��������ľ��巽��
	 * @param msg ��ʽ��ID_PW
	 * @return flag ��־�Ƿ�ɹ�*/
	public String service(String msg) {
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_PASSWORD + msg);
			return (String)clientInputStream.readObject();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}

}
