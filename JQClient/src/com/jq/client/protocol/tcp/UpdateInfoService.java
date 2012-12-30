package com.jq.client.protocol.tcp;

import com.jq.util.FriendInfo;

/**�����û����Ͼ������
 * �̳���AbstractService
 * ʵ����Service<String,String>�ӿ�*/
public class UpdateInfoService  extends AbstractService implements Service<String,FriendInfo>{

	/**�����û����Ͼ�������캯��*/
	public UpdateInfoService(TCPServer TCP) {
		super(TCP);
	}
	/**
	 * �����û����ϵľ��巽��
	 * @param myInfo
	 * 		Nickname  face  sex  age Academy department Provence City Email Homepage Description UserID
	 * @return ��־���ɹ�����ʧ�� 
	 * */
	public String service(FriendInfo myInfo) {
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_UPDATAINFO + "�ޱ�ʶ");
			
			clientOutputStream.writeObject(myInfo);
			return (String)clientInputStream.readObject();
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}

}
