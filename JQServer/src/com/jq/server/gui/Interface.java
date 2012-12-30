package com.jq.server.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.ServerSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.jq.server.service.SQLServerProcess;
import com.jq.server.sql.SQLPoolServer;

public class Interface  {
	
	JFrame jf;
	JButton close;
	JTable onlineUser,registedUser;
	JTabbedPane jtp;
	String IP;	//IP��ַ
	int port ;	//���ü����˿�
	
	SQLPoolServer sqlPool;
	ExecutorService threadPool;
	ServerSocket serverSocket;
	
	public Interface(final SQLPoolServer sqlPool, final ExecutorService threadPool,
			final ServerSocket serverSocket) {
		jf = new JFrame("JQ��������");
		jf.setLayout(new BorderLayout(10,10));
		
		JPanel jp1 = new JPanel();
		Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		jp1.setBorder(BorderFactory.createTitledBorder(border, "������������", TitledBorder.LEFT, TitledBorder.TOP));
		IP = SQLServerProcess.getLocalAddress();
		port = SQLServerProcess.PORT;
		JLabel jl1 = new JLabel("��������ǰ�����ڣ�"+IP+"��"+port+"  ");//8888�˿ں���Ҫ��ȡ������
		close = new JButton("�رշ�����");
		jp1.add(jl1);
		jp1.add(close);
		jf.add(jp1, BorderLayout.NORTH);
		
		onlineUser = new JTable();	//�����û�
		registedUser = new JTable();	//��ע���û�
		jtp = new JTabbedPane();
		jtp.addTab("�����û��б�",new JScrollPane(onlineUser));
		jtp.addTab("��ע���û��б�",new JScrollPane(registedUser));
		//jtp.setTabComponentAt(0, new JLabel("�����û��б�"));
		jf.add(jtp,BorderLayout.CENTER);
		
		final JLabel time = new JLabel("",SwingConstants.RIGHT);//ʱ��ŵ��ұ�
		new java.util.Timer().scheduleAtFixedRate( new TimerTask(){
			DateFormat df = new SimpleDateFormat("yyyy��MM��dd�� HH:mm:ss");
			@Override
			public void run() {
				time.setText("��ǰʱ�䣺"+df.format(new Date())+"  ");
			}
		}, 0, 1000);
		jf.add(time,BorderLayout.SOUTH);
		
		jf.setSize(500, 375);
		jf.setResizable(false);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		jf.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){			
				logout();
			}
		});
		close.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				logout();
			}
		});
		
		this.sqlPool = sqlPool;
		this.threadPool = threadPool;
		this.serverSocket = serverSocket;
	}


	protected void logout() {
		int i = JOptionPane.showConfirmDialog(jf, "ȷ��Ҫ�رշ�������\n\n �����Ὣ�ж������пͻ��˵����ӣ�", "�رշ�����", JOptionPane.YES_NO_OPTION);
		if(i == 0){	//ѡ���ǣ��˳�	
			close(sqlPool,threadPool,serverSocket);	//�ر��̺߳Ͷ˿�
			System.exit(0);
		}else{
			jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
		
	}
	
	private void close(SQLPoolServer sqlPool, ExecutorService threadPool, ServerSocket serverSocket){
		threadPool.shutdown();
		try {
			sqlPool.closePoolServer();
			if (!serverSocket.isClosed())
				serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
