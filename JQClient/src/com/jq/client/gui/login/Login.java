package com.jq.client.gui.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jq.client.gui.mainframe.MainFrame;
import com.jq.client.protocol.tcp.LoginService;
import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.client.protocol.udp.UDPServer;
import com.jq.util.Friend;
import com.jq.util.IPInfo;
import com.jq.util.MD5;
import com.jq.util.MyResources;
import com.jq.util.Param;

/** ��¼���� */
public class Login implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	JFrame jf;
	JTextField jtf;	//�û���
	JPasswordField jpf;	//����
	JButton jb1,jb2,jb3,jb4;
	JComboBox jcBox;
	static String ID;

	static String PW;
	
	public Login(){
		jf = new JFrame("JQ 2011");
		jf.setAlwaysOnTop(true);
		//System.out.println(MyResources.ICON + "tray.gif");
		jf.setIconImage(MyResources.getImage(MyResources.ICON + "tray.gif"));
		jf.setLayout(null);
		
		JLabel jl1,jl2,jl3;
		JLabel banner = new JLabel(MyResources.getImageIcon(MyResources.ICON+ "banner.png"));
		banner.setBounds(0, 0, 324, 66);
		jf.add(banner);
		jl1 = new JLabel("�˺ţ�");
		jl2 = new JLabel("���룺");
		jl3 = new JLabel("״̬��");
		jtf = new JTextField();
		jpf = new JPasswordField();
		jpf.setEchoChar('��');
		
		jb1 = new JButton("ע���û�");
		jb2 = new JButton("�һ�����");
		jb3 = new JButton("�߼�����");
		jb4 = new JButton("���̵�¼");
		
		jcBox = new JComboBox();
		jcBox.setModel(new DefaultComboBoxModel(new String[] {"����", "æµ", "�뿪", "����" }));
		jcBox.setEditable(false);
		jcBox.setBounds(60, 165, 170, 30);
		jf.add(jcBox);
		
		jl1.setBounds(15,  80, 140, 30);
		jl2.setBounds(15, 120, 140, 30);
		jl3.setBounds(15, 165, 140, 30);
		jtf.setBounds(60,  80, 170, 30);
		jpf.setBounds(60, 120, 170, 30);
		jb1.setBounds(235, 80, 80, 30);
		jb2.setBounds(235,120, 80, 30);
		jb3.setBounds(15, 205, 140, 30);
		jb4.setBounds(175, 205, 140, 30);
		
		jf.add(jl1);
		jf.add(jl2);
		jf.add(jl3);
		jf.add(jtf);
		jf.add(jpf);
		jf.add(jb1);
		jf.add(jb2);
		jf.add(jb3);
		jf.add(jb4);
		
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		jb3.addActionListener(this);
		jb4.addActionListener(this);
		
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setResizable(false);
		jf.setSize(324,270);
		jf.setLocationRelativeTo(null);	//������ʾ
		jf.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==jb1){		//ע���û�
			//jf.setVisible(false);
			TCPServer.owner = jf;
			@SuppressWarnings("unchecked")
			Service<String, String> service = (Service<String, String>) ServiceFactory
					.getService(ServiceFactory.TASK_NEWUSER, IPInfo.getIP(),
							IPInfo.getServerPort());
			service.service("��ʾ");	//�˴��ַ�������
			//new Register();
		}else if(e.getSource()==jb2){	//�һ�����
			//jf.setVisible(false);
			new ReturnPassword();
		}else if(e.getSource()==jb4){	//������¼
			ID = new String(jtf.getText().trim());
			PW = MD5.code(String.valueOf(jpf.getPassword()));
			//MD5.code(String.valueOf(password1.getPassword()));
			if(ID.length()==0 || PW.length()==0){
				JOptionPane.showMessageDialog(null, "�˺Ż����벻��Ϊ�գ�\n �����м�飡", "����",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			Object[] msg = connecting(ID,PW);
			if (msg != null){
				jf.setVisible(false);
				Friend[] friends = (Friend[])msg[0];
				friends[0].setStutas(4-jcBox.getSelectedIndex());
				new MainFrame(friends,(String[])msg[1]);
			}
			//new Main();
		}else if(e.getSource()==jb3){	//�߼�����
			//jf.setVisible(false);
			new Setup();
		}
	}
	
	/** ���ӷ����� */
	private Object[] connecting(String ID, String PW) {
		TCPServer.owner = jf;
		TCPServer.SERVER_IP = IPInfo.getServerInetAddress().getAddress()
				.getHostAddress();
		TCPServer.PORT = IPInfo.getServerInetAddress().getPort();

		@SuppressWarnings("unchecked")
		Service<Object[], String> service = (Service<Object[], String>) ServiceFactory
				.getService(ServiceFactory.TASK_LOGIN, TCPServer.SERVER_IP,
						TCPServer.PORT);
		String msg = ID + Param.SPACE + PW + Param.SPACE
				+ UDPServer.getLocalAddress() + Param.SPACE
				+ IPInfo.getClientPort() + Param.SPACE
				+ (4 - jcBox.getSelectedIndex());

		//System.out.println(ServiceFactory.TASK_LOGIN + " "
			//	+ TCPServer.SERVER_IP + " " + TCPServer.PORT + msg);

		LoginService.owner = jf;
		return service.service(msg);
	}

	public static String getUserID() {
		return ID;
	}

	public static String getPassword() {
		return PW;
	}

	/*
	//static ������ main;
	public static void main(String args[]){
		try {
		    UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		    UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("΢���ź�", Font.PLAIN, 12));
		}catch (Exception e) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			}catch (Exception e2) {}
			JOptionPane.showMessageDialog(null, "����Ƥ��ʧ�ܣ�ʹ��Ĭ�Ͻ��档","����", JOptionPane.WARNING_MESSAGE);
		}
		new Login();
	}*/
}
