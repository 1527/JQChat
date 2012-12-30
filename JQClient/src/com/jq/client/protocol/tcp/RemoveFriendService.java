package com.jq.client.protocol.tcp;

/**�Ƴ����Ѿ������
 * �̳���AbstractService
 * ʵ����Service<String,String>�ӿ�*/
public class RemoveFriendService  extends AbstractService implements Service<String,String>{

	/** ɾ�����ѵĹ��캯�� */
	public RemoveFriendService(TCPServer TCP) {
		super(TCP);
	}

	/**������������Ƴ�����
	 * @param msg Ҫ�Ƴ��ĺ���ID
	 * @return String �Ƴ��ɹ�����Successʧ�ܷ���Error
	 * */
	public String service(String msg) {
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_REMOVE + msg);
			return (String)clientInputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return TCPServer.ERROR;
		}		
	}
}
