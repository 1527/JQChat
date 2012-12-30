package com.jq.client.gui.mainframe;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.client.protocol.udp.MsgFactory;
import com.jq.client.protocol.udp.UDPServer;
import com.jq.util.Friend;
import com.jq.util.MyResources;
import com.jq.util.Param;

/**
 * [������������] 
 * SearchDialog 
 * ������Ӻ���ʱ�Ժ��ѵ�����. 
 * ���Һ��ֱ��˫���Ժ��ѽ�����ӻ�ͨ�������ť.
 * 
 * */

public class SearchDialog implements ActionListener {

	private static final long serialVersionUID = -3689562276413856483L;
	private boolean IDSearch = true; 
	private UDPServer UDP = null;
	private MyList friendList = null;
	private FriendsManager friendsManager = null;
	InfoFrame infoFrame;
	Friend[] friends;
	
	JFrame jf;
	JTextField jtf;
	JButton search;
	JTable table;
	JScrollPane jsp;
	JButton back, add;
	

	public SearchDialog(JFrame parent,UDPServer UDP,MyList friendsList,FriendsManager friendsManager) {
		
		//super(parent, true);
		this.UDP = UDP;
		this.friendList = friendsList;
		this.friendsManager = friendsManager;
		
		jf = new JFrame("��Ӻ���");
		jf.setAlwaysOnTop(true);
		jf.setIconImage(MyResources.getImage(MyResources.ICON + "tray.gif"));
		jf.setLayout(null);

		JLabel jl1 = new JLabel("���ң�");
		JPanel jp1 = new JPanel();
		jtf = new JTextField();
		jtf.setEditable(true);
		search = new JButton("����",MyResources.getImageIcon(MyResources.ICON + "search.png"));
		search.setToolTipText("����");
		search.addActionListener(this);
		jp1.setLayout(null);
		jl1.setBounds(0, 0, 50, 30);
		jtf.setBounds(50, 0, 210, 30);
		search.setBounds(280, 0, 100, 30);
		jp1.add(jl1);
		jp1.add(jtf);
		jp1.add(search);
		jp1.setBounds(10, 10, 380, 40);
		jf.add(jp1);

		JPanel jp2 = new JPanel();
		jp2.setLayout(null);
		
		//String[] title = new String[]{" JQ�� "," �ǳ� ","����״̬"};
		table = new JTable();//null,title);
	
		table.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent evt) {
				tableMouseClicked(evt);
			}
		});
		
		jsp = new JScrollPane(table);
		jsp.setBounds(0, 0, 380, 180);
		jp2.add(jsp);
		jp2.setBounds(10, 50, 380, 180);
		jf.add(jp2);

		JPanel jp3 = new JPanel();
		jp3.setLayout(null);
		back = new JButton("����");
		add = new JButton("��Ӻ���");
		back.addActionListener(this);
		add.addActionListener(this);
		back.setBounds(0, 0, 140, 30);
		add.setBounds(240, 0, 140, 30);
		jp3.add(back);
		jp3.add(add);
		jp3.setBounds(10, 235, 380, 30);
		jf.add(jp3);

		jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);// JFrame.HIDE_ON_CLOSE
		jf.setResizable(false);
		jf.setSize(400, 300);
		jf.setVisible(true);
		jf.setLocationRelativeTo(null);

	}

	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == back) {
			jf.dispose();
		} else if (e.getSource() == add) { // ��Ӻ���
			if ( table.getSelectedRow() != -1){
				TableModel t = table.getModel();
				String ID = (String)t.getValueAt(table.getSelectedRow(),0);
				String name = (String)t.getValueAt(table.getSelectedRow(),1);
				
				int i = JOptionPane.showConfirmDialog(null,"ȷ����Ӻ���:"+ name + "[" + ID+"]��?"
																						,"��ʾ",	JOptionPane.YES_OPTION,JOptionPane.QUESTION_MESSAGE);
				if (i == JOptionPane.NO_OPTION)
					return;
				
				if(friendsManager.getAllFriends().containsKey(ID) || ID.equals(UDP.hostUser.getID())){
		    		JOptionPane.showMessageDialog(null, "������Ӻ���:" +  name + "[" + ID + "]",
																						"��ʾ", JOptionPane.INFORMATION_MESSAGE);
		    		return;
				}
				
				updataList(connecting(ID),ID,name);
			}
		} else if (e.getSource() == search) { // ����
			if (!check())
				return;
				

			Friend[] friend = searching();
			if (friend != null)
				setTable(friend);
			else
	    		JOptionPane.showMessageDialog(null, "û��ƥ���JQ�û�!!",
						"��ʾ", JOptionPane.ERROR_MESSAGE);
		}

	}
	
	/** ˫����������ʱ ,��ʾ������Ϣ */
	protected void tableMouseClicked(MouseEvent evt) {
		if (evt.getClickCount() == 2 && table.getSelectedRow() != -1){		
			infoFrame = new InfoFrame(jf,friends[table.getSelectedRow()],false);
			
			infoFrame.setLocationRelativeTo(jf);
			infoFrame.setVisible(true);
		}
		
	}
	
	/** ��ӳɹ�����º������ */
	private void updataList(Friend newFriend, String ID, String name) {
		if (null != newFriend) {
			friendsManager.getAllFriends().put(newFriend.getID(), newFriend);

			// ���º����б�����º�������.
			if (newFriend.getStatus() != 0 && newFriend.getStatus() != 1)
				friendList.add(1, newFriend); // ������û����ߣ��򽫴˼ӵ�ָ��λ�á�
			else
				friendList.addItem(newFriend);

			if (newFriend.getStatus() != 0) {
				// ���������Ϣ.
				UDP.sendMessage(MsgFactory.ADTIONMSG + UDP.hostUser.getID(),
						newFriend.getIP(), newFriend.getPort());
			}
			JOptionPane.showMessageDialog(null, "��Ӻ���:" + name + "[" + ID
					+ "]�ɹ�!", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
		} else
			JOptionPane.showMessageDialog(null, "��Ӻ���:" + name + "[" + ID
					+ "]ʧ��!", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
	}

	/** ���ӷ������������ݿⲢ��ȡ����Friend */
	@SuppressWarnings("unchecked")
	private Friend connecting(String ID) {
		Service<Friend, String> service = (Service<Friend, String>) ServiceFactory
				.getService(ServiceFactory.TASK_ADDTION, TCPServer.SERVER_IP,
						TCPServer.PORT);
		// �������������Ӹú���
		return service.service(UDP.hostUser.getID() + Param.SPACE + ID);
	}


	

	/**
	 * 	����ѯ�����ӵ�����.
	 * */
	private void setTable(Friend[] f){
		Object[][] m = new Object[f.length][3];
		friends = new Friend[f.length];
		for (int i = 0; i < m.length; i++){
			m[i] = new Object[]{f[i].getID(),f[i].getNickName(),
										f[i].getStatus() == 0 ? "����" : 
										(f[i].getStatus() == 4 ? "����" : 
										(f[i].getStatus() == 3 ? "æµ" : "�뿪"))};
			friends[i] = f[i];
		}
		String[] title = new String[]{" JQ�� "," �ǳ� ","����״̬"};
		
		table.setModel(new DefaultTableModel(m,title) {
			private static final long serialVersionUID = -2489641194169198722L;
			boolean[] canEdit = new boolean[] { false, false, false};

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		table.setEnabled(true);
	}


	/** ���ӷ�������ȡ�����Ϣ */
	@SuppressWarnings("unchecked")
	private Friend[] searching() {
		Service<Friend[], String> service = (Service<Friend[], String>) ServiceFactory
				.getService(ServiceFactory.TASK_SEARCH, TCPServer.SERVER_IP,
						TCPServer.PORT);
		return service.service(getSQLstr());
	}
	
	/**
	 * ���������в��Һ�����Ϣ.
	 * ����������͵�����ΪSQL��ѯ������������.
	 * Select * form where UserInfo.NickName = ? OR ID = ?;
	 * */
	private String getSQLstr(){
		if(IDSearch)
			return jtf.getText().trim();
		
		StringBuilder SQL = new StringBuilder("");
		/* ��ϲ�ѯ��� ,δ���*/
	    SQL.append("UserInfo.NickName_=_'" + jtf.getText().trim() + "'_AND");
		String string = SQL.toString();
		//System.out.println(string);
		return string + ";";
	}

	/**
	 * 	����ѯ�����Ƿ����Ҫ��.
	 * */
	private boolean check() {
		if(jtf.getText().length() == 0){
			return false;
		}
		return true;
	}
	
	/*
	public static void main(String args[]) {
		// JFrame.setDefaultLookAndFeelDecorated( true );
		try {
			UIManager
					.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			// UIManager.getLookAndFeelDefaults().put("defaultFont", new
			// Font("΢���ź�", Font.PLAIN, 12));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//new SearchDialog();
	}*/

}
