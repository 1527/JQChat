package com.jq.client.protocol.tcp;

import com.jq.util.Friend;

/** ��Ӻ��Ѿ������ 
 * �̳���AbstractService��ʵ����Service<Friend,String>�ӿ�*/
public class AddtionService extends AbstractService implements Service<Friend, String> {

	/** ��Ӻ��ѷ���Ĺ��캯�� */
	public AddtionService(TCPServer TCP) {
		super(TCP); //���ø���Ĺ��캯��AbstractService(TCPService TCP)
	}

	/**
	 * ��Ӻ��ѵľ���ִ�з���
	 * @param msg
	 *            ִ�в�������Ϣ. ��ӷ� ID_ID ���շ���ȡ��Ӻ�����Ϣ ID
	 * @return Friend ������û����ô˺���ʱ����null,������û������򷵻�Friend
	 * */
	public Friend service(String msg) {
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_ADDTION + msg);
			String f = (String) clientInputStream.readObject();

			if (f.equals(TCPServer.SUCCESS))
				return (Friend) clientInputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} // ���͵�¼����

		return null;
	}
}
