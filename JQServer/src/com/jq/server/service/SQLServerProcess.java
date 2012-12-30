package com.jq.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import com.jq.server.gui.Interface;
import com.jq.server.service.ServiceFactory;
import com.jq.server.sql.DBSource;
import com.jq.server.sql.SQLPoolServer;
import com.jq.util.GC;
import com.jq.util.PropertyFile;

/**
 * [������������.] ���ڴ�����ֿͻ���Ҫ��ִ�еĲ���.
 * 
 * ������������,ʹ�����ݿ����ӳغ��̳߳��������������ͷ���Ч��. �̳߳ز��� java.util.concurrent.ExecutorService.
 * java.util.concurrent.Executors. ���ݿ����ȫ��ʹ��������.���Ա���ͻȻ�¹ʵ��µĲ�һ����.
 * 
 * �����������û�����ģʽ: �û����������ʵ,�����������������̲߳�ȷ�ϱ�ʵ��ִ����ز���.
 * �߳�ִ���û�Ҫ��Ĳ���,���ִ�в������,�ȷ��ͳɹ���ʵSuccess ֮���ڷ��͸��û�Ҫ��Ľ��,��ִ��ʧ�ܻ���ִ�н��,����Error��ʵ.
 * ��Ҫ����null֮��Ķ���. [�������ݼ���]���ݵ�ͨ��δ���з�װ����.�������ݼ�Ľ���ͨ��ʹ��
 * ObjectInputStream��ObjectOuputStream,�ɸ����׵Ľ��иþ�,ֻҪ �����ݷ�װ���Զ�����, ���Զ������ж����ݼ��ܽ��ܼ���.
 * 
 * [ע:]�˰汾�ķ������Ƚϼ�ª.��һ�汾�н���ӷ�����GUI����,����Ӷ����ݿ� ִ����ز����Ĺ���.
 * �������Ϳͻ��˲���һֱ��������,���ͻ���Ҫ��ʹ�÷��������ݿ�ʱ�Ž��� ����,Ч�ʲ��Ǻܸ�.
 * 
 * */
public class SQLServerProcess extends Thread {

	/** ���ݿ������ļ� */
	private PropertyFile prop = null;
	/** ���ݿ����ӳ� */
	static SQLPoolServer sqlPool = null; 
	/** �̳߳� */
	private ExecutorService threadPool = null;
	/** ���������� */
	private ServerSocket serverSocket = null; 
	/** �˿ں�*/
	public static final int PORT = 6000;	

	public SQLServerProcess(PropertyFile propertyFile) {
		prop = propertyFile;
		sqlPool = new SQLPoolServer(prop);
		/* ��ȡ��̬�̳߳�. */
		threadPool = Executors.newCachedThreadPool();
		setUserOutline();

		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {	//�˿�����ռ��
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, PORT + "�˿ڱ�ռ��,��ֹͣ�˶˿ڵķ�������������",
					"����", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		/* �������������ػ����� */
		new GC();
		
		/*��������*/
		new Interface(sqlPool, threadPool, serverSocket);
		start();
	}

	/** ��̬����.��ȡ����IP��ַ. */
	public static String getLocalAddress() {
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
		return ip;
	}

	/** ����������ʱ. �������û�����Ϊ������״̬. */
	private void setUserOutline() {
		/* ��ȡ���ݿ�������Դ */
		DBSource db = sqlPool.getSQLServer();
		String SQL = "UPDATE USERIPINFO SET STATUS = 0;";
		try {
			Statement stat = db.getStatement();

			db.setAutoCommit(false);
			stat.executeUpdate(SQL);
			db.commit();
		} catch (SQLException e) {
			db.rollback();
			e.printStackTrace();
		} finally {
			/* �ͷ���Դ */
			db.releaseConnection();
		}
	}

	@Override
	public void run() {
		while (!serverSocket.isClosed()) {
			try { /* �����������˿� */
				threadPool.execute(new ProcessThread(serverSocket.accept()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * [�ڲ���.] ��������. ���ڴ����û��Է�����������. �û��������������:
	 * [1].ע�����û�.
	 * [2].��½����[������ȡ�����б�,��������״̬���û�IP,�˿���Ϣ��] 
	 * [3].ע��. 
	 * [4].�����û�����.
	 * [5].���º�������. 
	 * [6].��������. 
	 * [7].��Ӻ���. 
	 * [8].ɾ������. 
	 * [9].����������Ϣ. 
	 * �û��ͺ��ѵ������Ϣʹ��Friend�ഫ��. ��ѯ������ϸ����ʹ��FriendInfo�ഫ��.
	 * */
	public class ProcessThread implements Runnable {

		private Socket socket = null;
		/**�û�������*/
		private ObjectInputStream clientInputStream = null;
		/**�û������*/
		private ObjectOutputStream clientOutputStream = null;
		
		public ProcessThread(Socket socket) {
			System.out.println("������:��������!");
			this.socket = socket;
			/* ��ȡ���ݿ����� */
			try {
				clientOutputStream = new ObjectOutputStream(
						socket.getOutputStream());
				clientInputStream = new ObjectInputStream(
						socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("������:��ʼ�����!");
		}

		public void run() {
			try {
				/* ��ȡҪ��ִ��������û�ID����Ϣ. */
				String msg = (String) clientInputStream.readObject();
				DBSource db = sqlPool.getSQLServer();
				ServiceFactory.getService(msg).service(db,clientInputStream, clientOutputStream);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
