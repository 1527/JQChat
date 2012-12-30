package com.jq.client.protocol.tcp;

import java.util.ArrayList;

import com.jq.util.Friend;

/**�������Ѿ���ʵ��
 * �̳���AbstractService
 * ʵ����Service<Friend[],String>�ӿ�*/
public class SearchService extends AbstractService implements Service<Friend[], String>{

	/**�������ѵĹ��췽��*/
	public SearchService(TCPServer TCP) {
		super(TCP);
	}

	/** �������ѵľ��巽��
	 * @param msg ��ѯ���SQL */
	@SuppressWarnings("unchecked")
	public Friend[] service(String msg) {
		String flag;
		ArrayList<Friend> list = null;
		
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_SEARCH + msg);
			
			flag = (String)clientInputStream.readObject();
		
			if (flag.equals(TCPServer.SUCCESS)){
				list = (ArrayList<Friend>) clientInputStream.readObject();
				return list.toArray(new Friend[1]);
			}
			return null;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
