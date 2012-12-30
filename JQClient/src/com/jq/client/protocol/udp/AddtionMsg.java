package com.jq.client.protocol.udp;

import javax.swing.JOptionPane;

import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.util.Friend;
import com.jq.util.SoundPlayer;

/**
 * 	������Ӵ�����.
 * */
public class AddtionMsg extends AbstractMessage{

	/** ������ӹ��캯�� */
	public AddtionMsg(String msg) {
		super(msg);
	}

	/** ��дupdate���� */
	@Override
	public void update(UDPServer UDP) {
		Friend friend = connecting(ID);
		
		if (friend != null){
			UDP.friendsManager.getAllFriends().put(friend.getID(), friend);
			
			//���º����б�����º�������.
			//���Ҫ��ӵĺ������ߣ�����ӵ��Լ����棬���ߵĺ�������
			if (friend.getStatus() != 0 && friend.getStatus() != 1)
				UDP.getMyList().add(1,friend);
			else	//����ӵ����
				UDP.getMyList().addItem(friend);
			
    		JOptionPane.showMessageDialog(null, "[" + ID+"]�������Ϊ����!",
					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
    		
    		SoundPlayer.play(SoundPlayer.ADDTION);
		}
	}

	/** ���ӷ������������ݿⲢ��ȡ����Friend */
	@SuppressWarnings("unchecked")
	private Friend connecting(String ID) {
		Service<Friend, String> service = (Service<Friend, String>) ServiceFactory
				.getService(ServiceFactory.TASK_ADDTION, TCPServer.SERVER_IP,
						TCPServer.PORT);
		// �������������Ӹú���
		return service.service(ID);
	}
}
