package com.jq.client.gui.chat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

import com.jq.client.gui.chat.ChatFrame;
import com.jq.client.gui.common.MsgManagerPanel;
import com.jq.client.gui.mainframe.InfoFrame;
import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.client.protocol.udp.MsgFactory;
import com.jq.client.protocol.udp.UDPServer;
import com.jq.util.FileManager;
import com.jq.util.Friend;
import com.jq.util.IPInfo;
import com.jq.util.MyResources;
import com.jq.util.Param;
import com.jq.util.ScreenCapture;

/**
 * 	ChatFrame
 * 	���촰�ڽ���.
 * 	����ʵ������ѵ��������.�����ж��ֹ���.
 * 	����:
 * 			1.��������
 * 			2.���ͱ���
 * 			3.����ͼƬ
 * 			4.�����ļ�
 * 			5.����
 * 			6.�鿴����ѵ������¼
 * 			7.�鿴��������
 * 
 * 	ʹ��UDPЭ����շ��ͱ���,������Ϣ.
 * 	ʹ��TCP/IPЭ��ʵ��ͼ��,�ļ��ķ��ͺͽ���.
 * 	
 * */
public class ChatFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = -7267725017357797698L;
	
	private boolean extendFlag = false;	//��չ����Ƿ�����
	private FileManager	fileManager = null;	//�����¼�ļ�������
	private UDPServer UDP = null;
	private Friend myFriend = null;
	
	private MsgManagerPanel extendPanel;
	private ActionPanel actionPanel = null;;
	private FontPanel fontPanel = null;

	private static final SimpleDateFormat s = new SimpleDateFormat(
			"HH:mm:ss"); // ��ʽ������ʹ�ã�8���ַ�
	private InfoFrame infoFrame = null;

	JPanel jp1, jp3; // jp1��ʾ�Է���Ϣ��jp3��ʾ��ť֮���
	JEditPane receiveEditText,sendEditText;	//receiveEditText�����촰�ڣ�sendEditText�Ƿ���Ϣ����
	JScrollPane jsp2,jsp1;		//��ӹ�����
	JToolBar jtb;		//jtb������
	JToggleButton font,brow,chatHistory;
	JButton file,catchScreen;
	JButton jb1,jb2,jb3;	//jb1�����ļ���jb2ȡ����jb3����
	JLabel avator,name,status,sign;
	
	public ChatFrame (UDPServer UDP,Friend friend){
		this.UDP = UDP;
		this.myFriend = friend;
		
		setTitle("��"+myFriend.getNickName()+"������");	
		setIconImage(MyResources.getImage(MyResources.ICON + "chatIcon.png"));
		setLayout(null);
		jp1 = new JPanel();
		jp3 = new JPanel();
		
		avator = new JLabel(myFriend.getFaceIcon());
		name = new JLabel(myFriend.getNickName()+"\t"+"["+myFriend.getID()+"]");	
		//JLabel id = new JLabel(" (100001) ");	//
		status = new JLabel("["+myFriend.getStatusInfo()+"]");	//״̬
		sign = new JLabel(myFriend.getSignedString());
		
		name.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent evt) {
				if (infoFrame == null)
					infoFrame = new InfoFrame(ChatFrame.this,myFriend,false);
				
				infoFrame.setLocationRelativeTo(ChatFrame.this);
				infoFrame.setVisible(true);
			}

			public void mouseEntered(MouseEvent evt) {
				name.setForeground(Color.RED);
			}

			public void mouseExited(MouseEvent evt) {
				name.setForeground(Color.BLACK);
			}
		});
		avator.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent evt) {
				if (infoFrame == null)
					infoFrame = new InfoFrame(ChatFrame.this,myFriend,false);
				
				infoFrame.setLocation(ChatFrame.this.getX() + 10, ChatFrame.this.getY() + 50);
				infoFrame.setVisible(true);
			}

			public void mouseEntered(MouseEvent evt) {
				name.setForeground(Color.RED);
			}

			public void mouseExited(MouseEvent evt) {
				name.setForeground(Color.BLACK);
			}
		});
		
		avator.setBounds(0, 5, 40, 40);	//ͷ����40x40��
		name.setBounds(50,5,180,20);		//��ʾ�ǳ�
		//id.setBounds(140,5, 80, 20);		//��ʾID
		status.setBounds(230,5,80,20);		//��ʾ״̬
		sign.setBounds(50, 25, 300, 20);	//��ʾ����ǩ��
		jp1.setLayout(null);
		jp1.add(avator);
		jp1.add(name);
		//jp1.add(id);
		jp1.add(status);
		jp1.add(sign);
		jp1.setBounds(10, 2, 430, 45);
		add(jp1);
		
		actionPanel = new ActionPanel(this);
		//actionPanel.setBounds(12, 74, 365, 235);
		actionPanel.setVisible(false);
		//add(actionPanel);
		
		fontPanel = new FontPanel(ChatFrame.this);
		//fontPanel.setBounds(12, 282, 426, 30);
		fontPanel.setVisible(false);
		//add(fontPanel);
		
		receiveEditText = new JEditPane(false);
		receiveEditText.setEditable(false);
		jsp1 = new JScrollPane(receiveEditText);
		jsp1.setBounds(10, 50, 430, 260);
		add(jsp1);
		 
		
		
		jtb = new JToolBar();
		font = new JToggleButton(MyResources.getImageIcon(MyResources.ICON + "font.png"));
		font.setToolTipText("����");
		font.addActionListener(this);
		brow = new JToggleButton(MyResources.getImageIcon(MyResources.ICON + "brow.png"));
		brow.setToolTipText("����");
		brow.addActionListener(this);
		file = new JButton(MyResources.getImageIcon(MyResources.ICON + "file.png"));
		file.setToolTipText("�����ļ�");
		file.addActionListener(this);	
		catchScreen = new JButton(MyResources.getImageIcon(MyResources.ICON + "catchScreen.png"));
		catchScreen.setToolTipText("����");
		catchScreen.addActionListener(this);
		chatHistory = new JToggleButton(MyResources.getImageIcon(MyResources.ICON + "chat.png"));
		chatHistory.setToolTipText("�����¼");
		chatHistory.addActionListener(this);
		
		jtb.setFloatable(false);
		jtb.setRollover(true);
		jtb.add(font);
		jtb.add(brow);
		jtb.add(file);
		jtb.add(catchScreen);
		jtb.add(chatHistory);
		jtb.setBounds(10, 312, 430, 30);
		add(jtb);
		
		sendEditText = new JEditPane(true);
		sendEditText.setEditable(true);
		sendEditText.requestFocus();
		//sendEditText.setLineWrap(true);		//�Զ�����
		//jsp2 = new JScrollPane(sendEditText,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jsp2 = new JScrollPane(sendEditText);
		jsp2.setBounds(10, 344, 430, 90);
		add(jsp2);	
		
		jb1 = new JButton("�����ļ�");
		jb2 = new JButton("�ر�");
		jb3 = new JButton("����");
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		jb3.addActionListener(this);
		jb1.setBounds(0, 00, 140, 30);
		jb2.setBounds(230, 00, 100, 30);
		jb3.setBounds(330, 00, 100, 30);
		jp3.setLayout(null);
		jp3.add(jb1);
		jp3.add(jb2);
		jp3.add(jb3);
		jp3.setBounds(10, 436, 430, 30);
		add(jp3);
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//JFrame.HIDE_ON_CLOSE
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				close();
		}});
		setResizable(false);
		this.getRootPane().setDefaultButton(jb3);//Ĭ�ϻس��Ƿ��Ͱ�ť
		setSize(450,500);
		setVisible(true);
		setLocationRelativeTo(null);
		//pack();
	}

	protected void close() {
		/*������������*/
    	if (receiveEditText.getText().trim().length() != 0){
    		if (fileManager == null)
    			fileManager = new FileManager(UDP.hostUser.getID(),myFriend.getID());
    		
    		receiveEditText.setText("");
    		fileManager.save(receiveEditText.sb.toString());
    	}
    	
    	UDP.removeChatFrame(myFriend.getID(),this);
		dispose();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==brow){
			
			if (!actionPanel.isVisible()){
				actionPanel.showBrow();
			}else{
				actionPanel.close();
			}
		}else if(e.getSource()==font){
			if (!fontPanel.isVisible()){
				fontPanel.showFont();
			}else{
				fontPanel.close();
			}
       	   //JOptionPane.showMessageDialog(ChatFrame.this, "��������","����",JOptionPane.INFORMATION_MESSAGE);	//����
		}else if(e.getSource()==file){//�����ļ�
			sendFile();
	
		}else if(e.getSource()==catchScreen){//��Ļ������ֻ�ܱ��棬û������
			try {
				ScreenCapture.getInstance().getImage();
				//sendEditText.insertImage(ScreenCapture.getInstance().getPickedIcon());
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}else if(e.getSource()==chatHistory){//�����¼
			if(!extendFlag){
				if (extendPanel == null){
					//��ʼ������ʾ�����¼���
					if (fileManager == null)
						fileManager = new FileManager(UDP.hostUser.getID(),myFriend.getID());
					extendPanel = new MsgManagerPanel(ChatFrame.this,fileManager);
					
					//extendPanel.setBackground(new java.awt.Color(220, 239, 255));
					extendPanel.setLayout(null);
					add(extendPanel);
					extendPanel.setBounds(438, 5, 400, 465);
					
					//�������»����������,��������ʾ����
					paintAll(getGraphics());
				}
				
				if (fileManager == null){
					fileManager = new FileManager("1000","1001");
					extendPanel.setMsgPages(fileManager);
				}
				
				extendStatus();
			}
			else
				normalStatus();
			
		}else if(e.getSource()==jb1){
			sendFile();
		}else if(e.getSource()==jb2){	//����
			close();
		}else if(e.getSource()==jb3){	//������Ϣ
			sendMsg();
		}
	}

	/**������Ϣ*/
	private void sendMsg() {
		/**
		 * 	��Ϣ��ʽ-------->[ID_��Ϣ��]
		 * 	��Ϣ��---------->[11-25 00:00:00:1223xxxxxxxxxxxxxxxxxxxxxxxx]
		 * 						����        �����ʽ        ������Ϣ
		 * 
		 * 1.��շ��ʹ��ڵ��ı�
		 * 2.�ڽ��ܿ�����ʾ�Լ����͵��ı�
		 * 3.�ٰ���Ϣ���͸�����
		 * */
		String content = sendEditText.getDetailText().trim();
		System.out.println(content+"\n"+content.length());
		if (content.length() != 0){
			sendEditText.setText(null);
			sendEditText.requestFocusInWindow();
			//���͵��û���Ϣ����ʽ��ID ʱ��+�����ʽ
			String msg = UDP.hostUser.getID() + " " + getDate()
					+ fontPanel.getType();

			// ���Լ����͵���Ϣ����ʾ���Լ������촰����
			// ���������ʽ��
			receiveEditText.insertUserString(UDP.hostUser.getNickName() + " "
					+ getDate(), new Color(0, 127, 64));
			receiveEditText.setFontStyle(fontPanel.getFontName(),
					fontPanel.getFontColor(), fontPanel.getFontType(),
					fontPanel.getFontSize());
			receiveEditText.insertToTextPanel(content);

			// ����Ϣ���͸�����
			if (myFriend.getStatus() != 0) // ��������
				UDP.sendMessage(MsgFactory.NOMALMSG + msg + content,
						myFriend.getIP(), myFriend.getPort());
			else { // ���Ѳ�����
				@SuppressWarnings("unchecked")
				Service<String, String> service = (Service<String, String>) ServiceFactory
						.getService(ServiceFactory.TASK_LEFTINFO,
								TCPServer.SERVER_IP, TCPServer.PORT);
				service.service(myFriend.getID() + Param.SPACE + msg
						+ content);
			}
		}else{
			JOptionPane.showMessageDialog(ChatFrame.this, "���ܷ��Ϳ���Ϣ", "����", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**�����ļ�*/
	private void sendFile() {
		String receiveID = myFriend.getID();
		if(UDP.hostUser.getID() == receiveID){
			JOptionPane.showMessageDialog(this, "�����Լ����Լ������ļ���","����" , JOptionPane.WARNING_MESSAGE);
		}else{
			JFileChooser jfc = new JFileChooser();
			if(jfc.showOpenDialog(this)== JFileChooser.APPROVE_OPTION){
				File file = jfc.getSelectedFile();
				
				UDP.sendMessage(
						MsgFactory.SENDFILEMSG + UDP.hostUser.getID()
								+ Param.SPACE + file.getName()
								+ Param.SPACE + UDP.hostUser.getIP()
								+ Param.SPACE + UDP.hostUser.getPort(),
						myFriend.getIP(), myFriend.getPort());
				try {
					//Socket s = new Socket(TCPServer.SERVER_IP,IPInfo.getFilePort());
					Socket s = new Socket(myFriend.getIP(),IPInfo.getFilePort());
					FileInputStream fis = new FileInputStream(file);
					byte[] buffer  = new byte[Param.DATA_SIZE];
					int cnt = 0;
					OutputStream os = s.getOutputStream();
					while((cnt=fis.read(buffer))>=0) {
					   os.write(buffer,0,cnt);
					}
					os.close();
					fis.close();
					s.close();
				} catch( Exception e1) {
					e1.printStackTrace();
					JOptionPane.showConfirmDialog(this, "7000�˿ڱ�ռ�ã�", "����", JOptionPane.ERROR_MESSAGE);
				}
				//UDP.sendFile(MsgFactory.SENDFILEMSG + UDP.hostUser.getID(),file,
					//	myFriend.getIP(), myFriend.getPort());
			}
		}
		
	}

	/**
     * 	��ȡ��ǰ����.��ʽΪ 11-26 12:05:09
     * 	@return String ��ʽ���������.
     * */
    public static String getDate(){
    	Date date = new Date();
		return s.format(date);
    }

	/**
	 * 	��չ״̬
	 * */
	public void extendStatus(){
		setSize(850, 500);
		extendFlag = true;
	}
	
	/**
	 * 	����״̬.
	 * */
	public void normalStatus(){
		setSize(450, 500);
		extendFlag = false;
	}
	
	/**
	 * 	��Ϣ��ʽ--
	 * 		[ID_��Ϣ��]
	 * 	��Ϣ��--
	 * 		[11-27 00:00:00 xxxxxxxxxxxxxxxxxxxxxxxx]
	 * 						����        �����ʽ        ������Ϣ
	 * */
	public void setReceiveMsg(String receiveMsg){
		// ��ȡ��������
		String date = receiveMsg.substring(0, 8);
		// ��ȡ�����ʽ
		String fontType = receiveMsg.substring(8, 12);
		// ������Ϣ����
		receiveMsg = receiveMsg.substring(12);

		receiveEditText.insertUserString(myFriend.getNickName() +" "+ date,
				new Color(0,0,255));
		// ��������
		receiveEditText
				.setFontStyle(fontPanel.getFontName(Integer.parseInt(fontType
						.substring(0, 1))), // ��������
						fontPanel.getFontColor(Integer.parseInt(fontType
								.substring(1, 2))),// ������ɫ
						Integer.parseInt(fontType.substring(2, 3)), // �����ʽ
						fontPanel.getFontSize(Integer.parseInt(fontType
								.substring(3, 4)))); // �����С
		// ��ʾ��Ϣ.
		receiveEditText.insertToTextPanel(receiveMsg);

	}
}
