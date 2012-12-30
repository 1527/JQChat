package com.jq.client.protocol.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * [TCP������������]
 * TCPServer 
 * ִ�пͻ�����������˵�TCP��Ϣͨ��. 
 * ����ִ�ж������:
 * 			[1].ע�����û�.
 * 			[2].��½����[������ȡ�����б�,��������״̬���û�IP,�˿���Ϣ��]
 * 			[3].ע������.
 * 			[4].�����û���������.
 * 			[5].���º�����������.
 * 			[6].������������.
 * 			[7].��Ӻ�������.
 * 			[8].ɾ����������.
 * 			[9].����������Ϣ����.
 * 
 * ����������ӳɹ�������֮���ӵ����������.
 * ʧ���򵯳�"���ӷ���������,���Ժ�����...!"��ʾ�򲢹ر�Ӧ�ó���.
 * */
public class TCPServer {
	private Socket socket = null;
	private OutputStream clientOutputStream = null;
	private InputStream clientInputStream = null;
	
	public static JFrame owner = null;
	public static String SERVER_IP = null;
	public static int PORT = 0;

	/** �ɹ���ʾ�� */
	public static final String SUCCESS = "SUCCESS";
	/** ʧ�ܱ�ʾ�� */
	public static final String ERROR = "ERROR";
	/** �û��ѵ�¼��ʾ�� */
	public static final String LOGINED = "9";

	/**
	 * ���캯��.
	 * 
	 * @param IP
	 *            ������IP��ַ.
	 * @param port
	 *            �����������˿�.
	 * */
	public TCPServer(String IP, int port) {
		if (null == IP) {
			JOptionPane.showMessageDialog(owner, "���ӷ���������,�������������÷�������ַ���˿�!",
					"����", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		try {
			socket = new Socket(IP, port);

			clientOutputStream = socket.getOutputStream();
			clientInputStream = socket.getInputStream();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(owner, "���ӷ���������,���Ժ�����...!", "����",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}

	/**
	 * ����InputStream���ڴӷ�������ȡ��.
	 * 
	 * @param InputStream
	 *            ���ӷ�����Socket���ص�InputStream.
	 * 
	 * */
	public InputStream getInputStream() {
		System.out.println("TCP:���ӷ�����Socket���ص�InputStream");
		return clientInputStream;
	}

	/**
	 * ����InputStream�����������д����.
	 * 
	 * @param OutputStream
	 *            ���ӷ�����Socket���ص�OutputStream.
	 * */
	public OutputStream getOutputStream() {
		System.out.println("TCP:���ӷ�����Socket���ص�OutputStream");
		return clientOutputStream;
	}

	/**
	 * �˳�ʱ����Դ�����.
	 * */
	public void close() {
		try {
			if (socket != null)
				socket.close();
			if (clientOutputStream != null)
				clientOutputStream.close();
			if (clientInputStream != null)
				clientInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}