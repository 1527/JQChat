package com.jq.client.protocol.tcp;

import com.jq.util.FriendInfo;

/** �鿴�������Ͼ������ 
 * �̳���AbstractService
 * ʵ����Service<FriendInfo,String>�ӿ�*/
public class FriendInfoService extends AbstractService implements
		Service<FriendInfo, String> {
	
	/** �鿴���ѷ���Ĺ��캯�� */
	public FriendInfoService(TCPServer TCP) {
		super(TCP);
	}

	/**
	 * �鿴���ѵľ��巽��
	 * @param msg
	 *            ִ�в�������Ϣ. ID-->Ҫ�鿴���ϵĺ���ID
	 * @return FriendInfo ���غ���������FriendInfo
	 * */
	public FriendInfo service(String msg) {
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_GETINFO + msg);
			String f = (String) clientInputStream.readObject();

			if (f.equals(TCPServer.SUCCESS))
				return (FriendInfo) clientInputStream.readObject();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} // ���͵�¼����

		return null;
	}

}
