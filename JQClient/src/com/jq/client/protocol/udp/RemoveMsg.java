package com.jq.client.protocol.udp;

import javax.swing.JOptionPane;

/**ɾ�����Ѿ���ʵ����*/
public class RemoveMsg extends AbstractMessage{

	/** ɾ�����ѵĹ��캯�� */
	public RemoveMsg(String msg) {
		super(msg);
	}

	/** ��дupdate���� */
	@Override
	public void update(UDPServer UDP) {
		//���º����б�,����ɾ��.
		UDP.getMyList().removeItem(UDP.friendsManager.getAllFriends().get(ID));
		UDP.friendsManager.getAllFriends().remove(ID);
		
		JOptionPane.showMessageDialog(null, "[" + ID+"]�����Ƴ�����!",
				"��ʾ", JOptionPane.INFORMATION_MESSAGE);
	}

}
