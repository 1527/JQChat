package com.jq.client.gui.mainframe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.jq.client.gui.common.MyTray;
import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.client.protocol.udp.UDPServer;
import com.jq.util.Friend;
import com.jq.util.IPInfo;
import com.jq.util.MyResources;

public class MainFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private MyList friendList;
	private UDPServer UDP = null;
	//private SearchDialog searchDialog = null;
	private FriendsManager friendsManager = null;
	private Friend[] allFriends = null;
	private ChangePassword changePassword = null;
	private MessageManager	messageManager = null;
	
    private Rectangle rect;		//������QQһ������Ļ��Ե�����Զ�����
    private Point point;		// ����ڴ����λ��
    private Timer timer = new Timer(10, this);;
    
    JFrame jf;
    JPanel main,login;	//login�ǵ�¼���棬main��������  
    JToolBar topBar;
    JButton search,message,changePW,about;
    JLabel avator,name,id,sign;
    String[] status = {"����","�뿪","æµ","����"};;
    JComboBox jcb;
    JScrollPane jsp;
    
	public MainFrame(Friend[] friends,String[] leftMsg){
		this.allFriends = friends;
		initComponents(friends);
		/*��������Ϣ--��ʾ*/
		if (leftMsg != null)
			new ShowLeftInfo(leftMsg);
	}
	
	/**
	 * 	[�ڲ���]
	 * 	������Ϣ��ʾ�����ڲ���.
	 * 	��UDP������Ϣ����һ��.
	 *  @param msg 
	 *  		���յ���������Ϣ����.ID+" "+message
	 * */
	private class ShowLeftInfo extends Thread{
		String[] msg = null;
		
		public ShowLeftInfo(String[] leftInfo){
			this.msg = leftInfo;
			start();
		}
		
		public void run(){
			try {	/*�ȴ�10�����ʾ*/
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			for (String message : msg){
				/* ���� */
				int i = message.indexOf(" ");
				String id = message.substring(0, i + 1).trim(); 	/* ������ID */
				message = message.substring(i + 1).trim(); 		/* ��Ϣ�� */
				
				UDP.receiveMsg(id, message);
				
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**��ʼ������*/
	private void initComponents(Friend[] friends) {
		friendsManager = new FriendsManager(friends);		
		UDP = new UDPServer(IPInfo.getClientPort(),friendsManager,friends[0]);//8000
		friendList = new MyList(friendsManager.getDefaultListModel(),UDP);
		
		jf = new JFrame("JQ 2011");
		jf.setVisible(true);	
		jf.setAlwaysOnTop(true);
		//jf.setExtendedState(Frame.ICONIFIED);
		jf.setIconImage(MyResources.getImage(MyResources.ICON + "tray.gif"));
		//------------������ֱ����С��-----
		if (MyTray.isSupported()){
			//jf.dispose();
			new MyTray(jf,UDP);
		}else{
			jf.setExtendedState(JFrame.ICONIFIED);
		}
		//��¼����
		login = new JPanel();
		login.setLayout(null);
		//��������ʾ����Ļ���ұ�
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension size = tk.getScreenSize();
		jf.setLocation((size.width-300), 50);
		
		JLabel logo = new JLabel(MyResources.getImageIcon(MyResources.ICON+"logo.png"));
		JLabel tip = new JLabel("���ڵ�¼...");
		JProgressBar jpb = new JProgressBar(JProgressBar.HORIZONTAL);	//jpb ������
		jpb.setIndeterminate(true);
		//jpb.setValue(100);
		logo.setBounds(64, 165, 100, 100);		//���� logo��tip��jpb��λ��
		tip.setBounds(90, 250, 100, 100);
		jpb.setBounds(40, 350, 160, 20);
		
		login.add(logo);		//jf����������jpanel�����л�������login������main��
		login.add(tip);
		login.add(jpb);
		jf.add(login);
		
		
		//������
		main = new JPanel();
		main.setLayout(null);
		
		JPanel jp = new JPanel();
		jp.setLayout(null);
		avator = new JLabel(MyResources.getImageIcon(MyResources.USERFACE+"19.gif"));
		avator.setToolTipText("������ĸ�������");
		avator.addMouseListener(new MouseAdapter(){		//���ͷ���޸ĸ�������
			public void mouseClicked(MouseEvent evt) {
				friendList.showInfoFrame(UDP.hostUser);
			}
		});
		name = new JLabel();	//UserInfo.user.name
		id = new JLabel();	//UserInfo.user.id
		sign = new JLabel();	
		jcb = new JComboBox(status);
		
		avator.setBounds(5, 10, 40, 40);	//ͷ����40x40��
		name.setBounds(50,3,150,20);		//��ʾ�ǳ�
		id.setBounds(50,23, 150, 20);		//��ʾID
		sign.setBounds(50, 43, 190, 20);	//��ʾ����ǩ��
		jcb.setBounds(165,10,80,25);		//��ʾ״̬
		
		jp.add(avator);
		jp.add(name);
		jp.add(id);
		jp.add(sign);
		jp.add(jcb);
		jp.setBounds(0, 0, 250, 60);
		
		topBar = new JToolBar();
		search = new JButton(MyResources.getImageIcon(MyResources.ICON+"search.png"));
		search.setToolTipText("������Ӻ���");
		search.addActionListener(this);
		message = new JButton(MyResources.getImageIcon(MyResources.ICON+"message.png"));
		message.setToolTipText("��ʷ��Ϣ����");
		message.addActionListener(this);
		changePW = new JButton(MyResources.getImageIcon(MyResources.ICON+"password.png"));
		changePW.setToolTipText("�޸�����");
		changePW.addActionListener(this);
		about = new JButton(MyResources.getImageIcon(MyResources.ICON+"about.png"));
		about.setToolTipText("����");
		about.addActionListener(this);
		topBar.setFloatable(false);
		topBar.setRollover(true);
		topBar.add(search);
		topBar.add(message);
		topBar.add(changePW);
		topBar.add(about);
		topBar.setBounds(5, 60, 250, 25);
				
		main.add(jp);
		main.add(topBar);
		
		jsp = new JScrollPane();
		jsp.setViewportView(friendList);
		
		JTabbedPane choose = new JTabbedPane();
		choose.setBounds(0, 90, 250, 500);

		choose.add(jsp," ���� ");
		JPanel group = new JPanel();
		choose.add(group," Ⱥ�� ");
		main.add(choose);
		
		
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
			public void windowIconified(WindowEvent e){//��С��ʱ
				/*��дͼ�껯����*/		
				jf.dispose();
			}
		});
		
		roll().start();
		timer.start();		//�����õ�
		jf.setResizable(false);
		jf.setSize(250,630);
		
		setInfo(friends[0]);

	}
	
	private void setInfo(Friend f){
		avator.setIcon(f.getFaceIcon());
		name.setText(f.getNickName() );
		id.setText("[" + f.getID() + "]");
		sign.setText(f.getSignedString());
		jcb.setSelectedIndex(f.getStatus()-1);	//δ���״̬����
	}

	@SuppressWarnings("unchecked")
	public void close() {
		jf.dispose();

		/* ���������������֪ͨ */
		Service<String, String> service = (Service<String, String>) ServiceFactory
				.getService(ServiceFactory.TASK_LOGOUT, TCPServer.SERVER_IP, TCPServer.PORT);

		service.service(UDP.hostUser.getID());
		System.out.println("Main����ע��");
		
		//����ѷ���������Ϣ
		UDP.sendLogoutMessage();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		/* �˳� */
		System.exit(1);
		
	}

	public Thread roll()		//����һ����ʱ����������
    {
        return new Thread()
        {
			public void run(){
				try {
					Thread.sleep(1500);
				} catch (Exception e) {}
				//login.setVisible(false);
				jf.remove(login);
				jf.add(main);
			}
        };
    }
	
	 /**
	  * �ж�һ�����Ƿ���һ��������
	  *  rect��Rectangle����
	  *  point ��Point����
	  * ����ھ����ڷ���true�����ڻ��߶���Ϊnull�򷵻�false
	  */
    public boolean isPtInRect(Rectangle rect, Point point) {
        if (rect != null && point != null) {
            int x0 = rect.x;
            int y0 = rect.y;
            int x1 = rect.width;
            int y1 = rect.height;
            int x = point.x;
            int y = point.y;

            return x >= x0 && x < x1 && y >= y0 && y < y1;
        }
        return false;
    }

    /**������*/
	@Override
	public void actionPerformed(ActionEvent e) {
		int left = jf.getLocationOnScreen().x;	 // ��������Ļ��ߵľ���
		int top = jf.getLocationOnScreen().y;		//��������Ļ�����ľ���
		int width = jf.getWidth();				// ����Ŀ�
		int height = jf.getHeight();			 // ����ĸ�
		rect = new Rectangle(0, 0, width, height);		  // ��ȡ���������
        point = jf.getMousePosition();			  // ��ȡ����ڴ����λ��
        
        Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension size = tk.getScreenSize();
		int right = size.width - left - width;	//��������Ļ�ұߵľ���
        if ((top < 0) && isPtInRect(rect, point)) {
        	 // ������ڵ�ǰ�����ڣ����Ҵ����Top����С��0 ���ô����Top����Ϊ0,���ǽ������ϱ��ؽ�������
        	jf.setLocation(left, 0);
        } else if (top > -3 && top < 3 && !(isPtInRect(rect, point))) {
        	// ��������ϱ߿�����Ļ�Ķ��˵ľ���С��5ʱ ��������겻�ٴ����� ��QQ�������ص���Ļ�Ķ���
        	jf.setLocation(left, 3 - height);
        } else if ((left <0)&& isPtInRect(rect, point)){	//���������
        	jf.setLocation(0, top);
        } else if (left > -3 && left < 3 && !(isPtInRect(rect, point))){
        	jf.setLocation(3 - width, top);
        } else if ((right < 0) && isPtInRect(rect, point)) {	//�������ұ�
        	jf.setLocation(size.width - width, top);
        } else if (right > -3 && right < 3 && !(isPtInRect(rect, point))) {
        	jf.setLocation(size.width - 3, top);
        }
        
        if(e.getSource()==about  ){
        	 JOptionPane.showMessageDialog(jf, "JAVA QQ 2011\n www.laycher.com","����",JOptionPane.INFORMATION_MESSAGE);	//����
		}else if( e.getSource() == search){
			//new SearchDialog();	//��Ӻ���
			//System.out.println("��Ӻ���");
			//if (searchDialog == null)
				//searchDialog = new SearchDialog(jf,UDP,friendList,friendsManager);
			new SearchDialog(jf,UDP,friendList,friendsManager);
		}else if(e.getSource() == message){
			//System.out.println("��Ϣ����");
			if (messageManager == null){
				messageManager = new MessageManager(allFriends,UDP);
			}else {
				messageManager.setVisible(true);
			}
		}else if(e.getSource() == changePW){
			//System.out.println("�޸�����");
			if (changePassword == null){
				changePassword = new ChangePassword(UDP.hostUser);
			}else {
				changePassword.clean();
				changePassword.setVisible(true);
			}
		}
		
	}
	
}
