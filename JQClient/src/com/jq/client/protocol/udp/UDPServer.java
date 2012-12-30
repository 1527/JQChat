package com.jq.client.protocol.udp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

import javax.swing.JOptionPane;

import com.jq.client.gui.chat.ChatFrame;
import com.jq.client.gui.mainframe.FriendsManager;
import com.jq.client.gui.mainframe.MyList;
import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.util.Friend;
import com.jq.util.Param;

/**
 * [��Ϣ������]
 * UDPServer
 * ��ΪUDPЭ��ķ���������.
 * ���ڴ���UDP�ͻ�����Ϣ���������.
 * �������ÿ���ͻ���Ψһ��.
 * ���ڴ���ȫ����Ϣ�ķ��ͺͽ���. �̳�Thread.
 * 
 * ���պͷ��ͺ��ѷ��͵���Ϣ,����:
 * 		0-----��ͨ������Ϣ.
 *     	1-----����������Ϣ.
 *      2-----����������Ϣ.
 *      3-----���������Ϣ.
 *      4-----����ɾ����Ϣ.
 *      5-----�����ļ���Ϣ.
 *      6-----�����ļ���Ϣ.
 * 
 * ���յ�����Ϣ����:�����ߵ�ID����Ϣ��.
 * 		������ʽΪ :[����_ID_��Ϣ��]
 * 
 * UDPЭ���ռ�ö˿�Ϊ�û����ö˿�.ÿ���ͻ���ֻ��ռ��һ��,
 * 
 * */

public class UDPServer extends Thread {

	private int localPort = 0; // ����ռ�ö˿�.
	private byte[] sendBuff = null; // ������Ϣ���ֽڻ���.
	private byte[] receiveBuff = null; // ������Ϣ���ֽڻ���.
	private DatagramPacket sendPacket = null; // ���͵����ݱ���
	private DatagramPacket receivePacket = null; // ���յ����ݱ���
	private DatagramSocket socket = null; // UDP�׽���
	public FriendsManager friendsManager = null;
	public Friend hostUser = null;
	public ChatFrame temp = null;
	private MyList myList = null;

	/** UDPServer�Ĺ��캯��
	 * @param localPort ����ռ�ö˿�
	 * @param friendsManager ���ѹ���
	 * @param hostUser ����*/
	public UDPServer(int localPort, FriendsManager friendsManager,
			Friend hostUser) {
		this.friendsManager = friendsManager;
		this.friendsManager.setUDP(this);
		this.hostUser = hostUser;
		initServer(localPort);
		/* �����ߺ��ѷ���������Ϣ */
		sendLoginMessage(hostUser);
	}

	/** ����ѷ�������ͨ�� 
	 * msg�ĸ�ʽ����ϢType+ID_IP_Port_Status*/
	private void sendLoginMessage(Friend f) {
		final String msg = MsgFactory.ONLINEMSG + f.getID() + Param.SPACE
				+ f.getIP() + Param.SPACE + f.getPort() + Param.SPACE
				+ f.getStatus();

		runMsg(msg);
	}

	/** ����������Ϣ
	 * msg�ĸ�ʽ��Type[OUTLINEMSG]+�û�ID*/
	public void sendLogoutMessage() {
		final String msg = MsgFactory.OUTLINEMSG + hostUser.getID(); 
		runMsg(msg);
	}

	/**ִ����Ϣ
	 * @msg ��������Ϣ*/
	private void runMsg(String msg) {
		Map<String, Friend> map = getAllFriends();
		Friend friend = null;

		for (String id : map.keySet()) {
			friend = map.get(id);

			// �������߷�������ͨ��,�����Լ���
			if (friend.getStatus() != 0
					&& !friend.getID().equals(hostUser.getID()))
				sendMessage(msg, friend.getIP(), friend.getPort());
		}
	}

	/**ͨ��FriendsManager��ȡ����
	 * Map<ID,Friend>*/
	public Map<String, Friend> getAllFriends() {
		return friendsManager.getAllFriends();
	}

	/**
	 * ������ط����׼��.
	 * 
	 * @param localPoat
	 *            Ҫ���õı��ض˿�.
	 * */
	@SuppressWarnings("unchecked")
	private void initServer(int localPort) {
		try {
			socket = new DatagramSocket(localPort);
		} catch (SocketException e) {
			/* ���˿ڱ�ռ��ʱ���쳣���� */
			while (true) {
				try {
					/* �������ö˿ڵ���һ���˿� */
					socket = new DatagramSocket(++localPort);
					break;
				} catch (SocketException e1) {
					JOptionPane.showMessageDialog(null, "�ͻ��˶˿ڱ�ռ�ã���鿴.", "����", JOptionPane.ERROR_MESSAGE);
					//�˿ڱ�ռ���˳���Ҫ����ע������
					Service<String, String> service = (Service<String, String>) ServiceFactory
					.getService(ServiceFactory.TASK_LOGOUT, TCPServer.SERVER_IP, TCPServer.PORT);

					service.service(hostUser.getID());
					System.exit(1);
				}
			}
		}

		this.localPort = localPort;
		this.hostUser.setIP(getLocalAddress());
		this.hostUser.setPort(localPort);
		/* ���ͽ�����Ϣ��buff,�����������ֽ���1024֮��. */
		sendBuff = new byte[Param.DATA_SIZE];
		receiveBuff = new byte[Param.DATA_SIZE];

		/* ���������߳� */
		this.start();
	}

	@Override
	public void run() {
		receivePacket = new DatagramPacket(receiveBuff, receiveBuff.length);

		while (!socket.isClosed()) {
			try {
				socket.receive(receivePacket);
				String message = new String(receivePacket.getData(), 0,
						receivePacket.getLength());
				/* ������Ϣ */
				MsgFactory.getMsg(message).update(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ��Ϣ���ͺ���
	 * 
	 * @param msg
	 *            ���͵���Ϣ.
	 * @param IP
	 *            ���ն˵�IP��ַ
	 * @param port
	 *            ���ն˵ļ����˿�.
	 * */
	public void sendMessage(String msg, String IP, int port) {
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(IP);
			sendBuff = msg.getBytes();

			sendPacket = new DatagramPacket(sendBuff, sendBuff.length, addr,
					port);
			/* ������Ϣ */
			socket.send(sendPacket);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���յ���Ϣ����ʾ.
	 * 
	 * @param id
	 *            ������Ϣ�ĺ���id
	 * @param msg
	 *            ���յ�����Ϣ
	 * */
	public void receiveMsg(String id, String msg) {
		friendsManager.addClientFrame(id).setReceiveMsg(msg);
	}

	/**�����ļ�*/
	public void sendFile(String msg, File file, String IP, int port) {
		try {
			InputStream is = new FileInputStream(file);
			InetAddress addr = InetAddress.getByName(IP);
			/* ���ͽ�����Ϣ��buff,�����������ֽ���1024֮��. */
			//sendBuff = new byte[Param.DATA_SIZE];
			sendBuff = msg.getBytes();
			sendPacket = new DatagramPacket(sendBuff,is.read(sendBuff),addr,port);
			socket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ������촰��.
	 * 
	 * @param id
	 *            Ҫ��ӵĺ��ѵ�id
	 * */
	public void addChatFrame(String id) {
		friendsManager.addClientFrame(id);
	}

	/**
	 * �ر����촰�ں��Ƴ�������Դ.
	 * */
	public void removeChatFrame(String id, ChatFrame frame) {
		friendsManager.removeClientFrame(id, frame);
	}

	/**
	 * ��̬����.��ȡ����IP��ַ.
	 * 
	 * @return String ����IP��ַ���ַ���.
	 * */
	public static String getLocalAddress() {
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
		return ip;
	}

	/**
	 * ���ص�ǰ�ͻ��˵�ǰռ�õĶ˿�.
	 * 
	 * @return int ���ص�ǰ�ͻ���ռ�õĶ˿ں�.
	 * */
	public int getLocalPort() {
		return localPort;
	}

	/**
	 * �ر�UDP��socket.
	 * */
	public void close() {
		if (socket != null)
			socket.close();
	}

	/** ���ú����б� */
	public void setMyList(MyList mylist) {
		this.myList = mylist;
	}

	/** ��ú����б� */
	public MyList getMyList() {
		return myList;
	}

}