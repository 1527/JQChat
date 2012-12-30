package com.jq.client.protocol.udp;

import java.util.Map;

import com.jq.util.Friend;

/**
 * 	����������Ϣ������.
 * */
public class OutlineMsg extends AbstractMessage{

	/** �������ߵĹ��캯�� */
	public OutlineMsg(String msg) {
		super(msg);
	}

	/** ��дupdate ����*/
	@Override
	public void update(UDPServer UDP) {
		updataList(ID,UDP);
		
	}

	/**���º����б�*/
	private void updataList(String ID,UDPServer UDP){
		Map<String,Friend> map = UDP.getAllFriends();
		
		Friend friend = map.get(ID);
		UDP.getMyList().removeItem(friend);
		
		if (null != friend )
			friend.setStutas(0);
	
		UDP.getMyList().addItem(friend);
	}
}
