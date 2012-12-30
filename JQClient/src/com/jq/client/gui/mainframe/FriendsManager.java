package com.jq.client.gui.mainframe;

import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;

import com.jq.client.gui.chat.ChatFrame;
import com.jq.client.protocol.udp.UDPServer;
import com.jq.util.Friend;

/**
 * ���ڶԺ����б����ͳһ����.
 * */
public class FriendsManager {

	private DefaultListModel list = new DefaultListModel();
	private UDPServer UDP = null;
	/* ά��һ�����ر��˵ĺ������촰�ڵ�map. */
	private Map<String, ChatFrame> closedChatFrame = new HashMap<String, ChatFrame>();
	/* ά��һ����ǰ�Ѵ򿪺������촰�ڵ�map. */
	private Map<String, ChatFrame> activeChatFrame = new HashMap<String, ChatFrame>();
	/* ά��һ�����к��ѵ�map */
	private Map<String, Friend> friendsInfo = new HashMap<String, Friend>();

	/** FriendManager ���캯�� */
	public FriendsManager(Friend[] friends) {
		/* ��ȫ��������䵽map�� */
		for (Friend friend : friends) {
			friendsInfo.put(friend.getID(), friend); // <ID,friend(��Ϣ)>ȫ������
			list.addElement(friend); // ��ʾ�б�
		}
	}

	/** ����UDPЭ��.*/
	public void setUDP(UDPServer UDP) {
		this.UDP = UDP;
	}

	/** ���¶Ի�����,��ӵ�activeChatFrame��,����ʾ����. */
	public ChatFrame addClientFrame(String id) {
		Friend f = friendsInfo.get(id);

		if (f == null)
			return null;
		ChatFrame frame = activeChatFrame.get(id);
		// ������촰��Ϊ����ʾ״̬
		if (frame == null) {
			//���û���Ѵ򿪵���ú�������Ĵ���
			// ����ѹر�map�������Ƿ�����˴���,������ʾ,�����½�һ��
			frame = closedChatFrame.get(f.getID());

			if (frame == null) {
				frame = new ChatFrame(UDP, f);
				activeChatFrame.put(f.getID(), frame);
			} else {
				frame.normalStatus();
				frame.setVisible(true);
			}
		}

		return frame;
	}

	/**
	 * ���رնԻ���ʱ��,��activeChatFrame���Ƴ�frame
	 * ���뵽closedChatFrame��Map��
	 * @param id
	 *            Ҫ�Ƴ��ĺ���ID.
	 * @param frame
	 *            �Ƴ��Ĵ���
	 * */
	public void removeClientFrame(String id, ChatFrame frame) {
		if (activeChatFrame.containsKey(id)) {
			activeChatFrame.remove(id);

			if (!closedChatFrame.containsKey(id))
				closedChatFrame.put(id, frame);
		}
	}

	/** ��ȡ��ʾ�б�DefaultListModel */
	public DefaultListModel getDefaultListModel() {
		return list;
	}

	/** ��ȡȫ������map */
	public Map<String, Friend> getAllFriends() {
		return friendsInfo;
	}

	/** ��ȡ��ǰ�����map */
	public Map<String, ChatFrame> getActiveFriends() {
		return activeChatFrame;
	}

	/** ��ȡ�ѹرմ��ں���map */
	public Map<String, ChatFrame> getClosedFriends() {
		return closedChatFrame;
	}

}
