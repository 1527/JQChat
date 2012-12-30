package com.jq.client.gui.mainframe;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;

import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.client.protocol.udp.MsgFactory;
import com.jq.client.protocol.udp.UDPServer;
import com.jq.util.Friend;
import com.jq.util.MyResources;
import com.jq.util.Param;

/**
 * �Զ���List�ؼ�. 
 * ʵ�ֹ���: ����ʾ����ͷ��,�ǳƺͺ��и���ǩ��. 
 * 	1.�����. 
 * 	2.ɾ����. 
 * 	3.������(�������߻����ߺ�����б�ʹ��).
 * 	4.������.
 * */
public class MyList extends JList {

	private static final long serialVersionUID = -1716222153753784579L;

	private DefaultListModel friends = null;
	private UDPServer UDP = null;
	/** ��ʾ���ѵķ�ʽ */
	private boolean showA = true;
	/** ά��һ����ʾ������Ϣ��Map */
	private Map<String, InfoFrame> infoMaps = new HashMap<String, InfoFrame>();

	/**
	 * ���캯��.
	 * 
	 * @param friends
	 *            �����б������.
	 * @param UDP
	 *            UDPServer.
	 * */
	public MyList(DefaultListModel friends, final UDPServer UDP) {
		super(friends);

		this.friends = friends;
		this.UDP = UDP;
		UDP.setMyList(this);

		// ���õ����˵�.
		setComponentPopupMenu(new MyPopupMenu());

		setOpaque(true); /* ����� */
		setCellRenderer(new ListRender1()); // ������Ⱦ��

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					Friend f = (Friend) getSelectedValue();
					showChatFrame(f);
				}
			}
		});
	}

	/**
	 * ��ʾ�����Friend�����촰��
	 * 
	 * @param friend
	 *            ����
	 */
	private void showChatFrame(Friend friend) {
		if (!friend.getID().equals(UDP.hostUser.getID())){
			UDP.addChatFrame(friend.getID());
		}else {
			JOptionPane.showMessageDialog(this, "�������Լ����Լ�����.","��ʾ", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * �ڴ��б��ָ��λ�ô�����ָ��Ԫ�ء�
	 * �������������Χ��index < 0 || index > size()���� ���׳�
	 * ArrayIndexOutOfBoundsException
	 *
	 * ��Ӻ���ʱ�õ���0λ�����Լ���1�Ժ���Ǻ��ѡ��������ǲ����ߵġ�
	 * @param index
	 *            ָ����λ��.
	 * @param item
	 *            Ҫ�����Ԫ��.
	 * */
	public void add(int index, Friend item) {
		friends.add(index, item);
	}

	/**
	 * ��ָ��Ԫ����ӵ�������ĩβ.
	 * 
	 * @param item
	 *            Ҫ�����Ԫ��.
	 * */
	public void addItem(Friend item) {
		friends.addElement(item);
	}

	/**
	 * �Ӵ��б����Ƴ������ĵ�һ����������С�ģ�ƥ����.
	 * 
	 * @param item
	 *            Ҫ�Ƴ���Ԫ��.
	 * */
	public void removeItem(Friend item) {
		friends.removeElement(item);
	}

	//-------------��дDefaultListModel�ķ���----------------
	/**
	 * �Ƴ����б���ָ��λ�ô���Ԫ�ء�
	 * ���ش��б����Ƴ���Ԫ�ء� 
	 * �������������Χ��index < 0 || index >= size()���� ���׳�
	 * ArrayIndexOutOfBoundsException��
	 * 
	 * @param index
	 *            ָ��Ҫ�Ƴ�Ԫ�ص�λ��.
	 *
	public void removeItemAt(int index) {
		friends.remove(index);
	} */

	/**
	 * �Ӵ��б����Ƴ�����Ԫ�ء�
	 * �˵��÷��غ��б��ǿյģ����Ǹõ����׳��쳣���� 
	 * 
	public void clear() {
		friends.clear();
	}*/

	/**
	 * ����ָ�������Ƿ�Ϊ������е����.
	 * 
	 * @return boolean ���ָ�������Ǵ��б��е�������򷵻� true
	 * 
	public boolean contains(Friend item) {
		return friends.contains(item);
	}*/

	/**
	 * �����б���ָ��λ�ô���Ԫ�ء� 
	 * �������������Χ��index < 0 || index >= size()���� ���׳�
	 * ArrayIndexOutOfBoundsException��
	 * 
	 * @param index
	 *            Ҫ���ص�Ԫ�ص�����
	 * 
	public Friend get(int index) {
		return (Friend) friends.get(index);
	}*/

	/**
	 * ���� item �ĵ�һ�γ��� 
	 * ���б��иò�����һ�γ���ʱ����λ���ϵ�������
	 * 	    ���û���ҵ��ö����򷵻� -1
	 * 
	 * @param item
	 *            һ������
	 * 
	public int indexOf(Friend item) {
		return friends.indexOf(item);
	}*/

	/**
	 * ���Դ��б����Ƿ������
	 * 
	 * @return boolean ���ҽ������б���û�������Ҳ����˵���СΪ�㣩ʱ���� true�����򷵻� false
	 * 
	public boolean isEmpty() {
		return friends.isEmpty();
	}*/

	/**
	 * ���ش��б��е������.
	 * ��������
	 * @return int ����Ԫ�ظ���.
	 * 
	public int Size() {
		return friends.size();
	}*/

	/**
	 * ����ȷ˳�򷵻ذ������б�������Ԫ�ص�����
	 * 
	 * @return Object[]
	 * 
	public Object[] toArray() {
		return friends.toArray();
	}*/
	

	/** ��ʾfriend�������� */
	public void showInfoFrame(Friend friend) {
		InfoFrame infoFrame = infoMaps.get(friend.getID());

		if (infoFrame == null) {
			if (friend.getID().equals(UDP.hostUser.getID()))	//����鿴�����Լ�
				infoFrame = new InfoFrame(null, friend, true);
			else
				infoFrame = new InfoFrame(null, friend, false);	//�鿴���Ǻ���
			infoMaps.put(friend.getID(), infoFrame);
		}

		infoFrame.setLocation(MyResources.getScreenCenterX() - 200,
				MyResources.getScreenCenterY() - 200);
		infoFrame.setVisible(true);
	}

	/**
	 * [�ڲ���] list�ĵ����˵�. 
	 * ����: 
	 * 		1.���������
	 * 		2.�޸���ʾ 
	 * 		3.�鿴�������� 
	 * 		4.ɾ������
	 * */
	class MyPopupMenu extends JPopupMenu {

		private static final long serialVersionUID = 7059674442129911004L;

		private JMenuItem chatMenuItem, remarkMenuItem, getInfoMenuItem,
				deleteMenuItem;

		public MyPopupMenu() {
			chatMenuItem = new JMenuItem("���������");
			remarkMenuItem = new JMenuItem("�޸���ʾ");
			getInfoMenuItem = new JMenuItem("�鿴����");
			deleteMenuItem = new JMenuItem("ɾ������");

			chatMenuItem.setIcon(MyResources.getImageIcon(MyResources.ICON
					+ "chat.gif"));
			chatMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					Friend friend = (Friend) getSelectedValue();
					if (friend != null)
						showChatFrame(friend);
				}
			});
			add(chatMenuItem);

			remarkMenuItem.setIcon(MyResources.getImageIcon(MyResources.ICON
					+ "tray.gif"));
			remarkMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (showA) {
						setCellRenderer(new ListRender2()); // ������Ⱦ��
						showA = false;
					} else {
						setCellRenderer(new ListRender1()); // ������Ⱦ��
						showA = true;
					}
				}
			});
			add(remarkMenuItem);

			getInfoMenuItem.setIcon(MyResources.getImageIcon(MyResources.ICON
					+ "getInfo.gif"));
			getInfoMenuItem.setText("�鿴����");
			getInfoMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					Friend friend = (Friend) getSelectedValue();
					if (friend != null)
						showInfoFrame(friend);
				}
			});
			add(getInfoMenuItem);

			add(new JSeparator());

			deleteMenuItem.setIcon(MyResources.getImageIcon(MyResources.ICON
					+ "close.gif"));
			deleteMenuItem.setText("ɾ������");
			deleteMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					Friend friend = (Friend) getSelectedValue();
					if (friend.getID().equals(UDP.hostUser.getID())){
						JOptionPane.showConfirmDialog(MyList.this, "����ɾ���Լ���",
								"����", JOptionPane.OK_OPTION,
								JOptionPane.WARNING_MESSAGE);
						return;
					}

					int i = JOptionPane.showConfirmDialog(MyList.this,
							"ȷ��ɾ������:" + friend.getNickName() + "["
									+ friend.getID() + "]��?", "��ʾ",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (i == JOptionPane.NO_OPTION){
							return;
					}else if(i == JOptionPane.YES_OPTION){
						removeFriend(friend);
					}
				}
			});
			add(deleteMenuItem);
		}

		/** ɾ�����ѷ��� */
		@SuppressWarnings("unchecked")
		private void removeFriend(Friend friend) {
			Service<String, String> service = (Service<String, String>) ServiceFactory
					.getService(ServiceFactory.TASK_REMOVE,
							TCPServer.SERVER_IP, TCPServer.PORT);

			String flag = service.service(UDP.hostUser.getID() + Param.SPACE
					+ friend.getID());
			if (flag.equals(TCPServer.SUCCESS)) {
				// ���º����б�����º�������,����ɾ��.
				UDP.friendsManager.getAllFriends().remove(friend.getID());
				UDP.getMyList().removeItem(friend);

				// �����������������ͨ��ɾ����Ϣ
				if (friend.getStatus() != 0){
					UDP.sendMessage(
							MsgFactory.REMOVEMSG + UDP.hostUser.getID(),
							friend.getIP(), friend.getPort());
				}
				JOptionPane.showMessageDialog(this, "ɾ������:" + "["
						+ friend.getID() + "]�ɹ�!", "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "ɾ������:" + "["
						+ friend.getID() + "]ʧ��!", "��ʾ",
						JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	/**
	 * [�ڲ���] List�б���б�����Ⱦ���A.
	 * */
	class ListRender1 extends JPanel implements ListCellRenderer {
		private static final long serialVersionUID = -7707554732894912210L;

		private JLabel name,face,signedString;
		
		public ListRender1() {
			setOpaque(true); /* ����� */

			initComponents();
		}

		/**
		 * ��ʼ������.
		 * */
		private void initComponents() {
			signedString = new JLabel();
			name = new JLabel();
			face = new JLabel();

			setLayout(new AbsoluteLayout());

			add(signedString,new AbsoluteConstraints(50, 20, 140, -1));
			add(name, new AbsoluteConstraints(50, 0, 40, -1));
			add(face, new AbsoluteConstraints(2, 1, 44, 44));
		}

		/**
		 * ��д�ӿں���,ʵ���Զ���list��.
		 * */
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Friend selectedFriend = (Friend) value;

			/* ���ú���ͷ�� */
			face.setIcon(selectedFriend.getFaceIcon());

			/* ����״̬ */
			if ((selectedFriend.getStatus() == 0 || selectedFriend.getStatus() == 1))
				name.setIcon(null);	//��������ʵ״̬
			else
				name.setIcon(selectedFriend.getStatusIcon());
			name.setText(selectedFriend.getNickName());

			//setToolTipText(selectedFriend.getSignedString());
			signedString.setText(selectedFriend.getSignedString());

			setToolTipText("<html><font color=red>������������Ϣ</font><br>�ǳƣ�"
					+ selectedFriend.getNickName()
					+ "["
					+ selectedFriend.getID()
					+ "]<br>״̬��"
					+ (selectedFriend.getStatus() == 0
							|| selectedFriend.getStatus() == 1 ? "����" : "����")
					+ "<br>�û�IP��" + selectedFriend.getIP() + "<br>����ǩ����"
					+ selectedFriend.getSignedString() + "<br><br></html>");

			/* ѡ�к���ػ� */
			if (cellHasFocus) {
				this.setBackground(new Color(232, 236, 251));
				name.setForeground(Color.RED);
				signedString.setForeground(Color.RED);
				setBorder(new javax.swing.border.LineBorder(new Color(
						200, 218, 254), 2, true));
			} else {
				this.setBackground(new Color(241, 247, 254));

				if (selectedFriend.getStatus() != 0
						&& selectedFriend.getStatus() != 1) { // ��������
					name.setForeground(new Color(0, 51, 102));
					signedString.setForeground(new Color(0, 51, 102));
				} else {
					name.setForeground(new Color(150, 150, 150)); // δ����
					signedString.setForeground(new Color(150, 150, 150));
				}
			}
			return this;
		}

	}

	/**
	 * [�ڲ���] List�б���б�����Ⱦ���B.
	 * */
	class ListRender2 extends JPanel implements ListCellRenderer {

		private static final long serialVersionUID = 3313503743992044426L;

		private JLabel name, signedString;

		public ListRender2() {
			//setOpaque(true);
			initComponents();
		}

		private void initComponents() {

			signedString = new JLabel();
			name = new JLabel();

			setLayout(new AbsoluteLayout());
			add(signedString,new AbsoluteConstraints(110, 0, 110, 25));
			add(name, new AbsoluteConstraints(10, 0, 120, 25));
		}

		/**
		 * ��д�ӿں���,ʵ���Զ���list��.
		 * */
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Friend selectedFriend = (Friend) value;

			/* ���ú���ͷ�� */
			name.setIcon(MyResources.getImageIcon(MyResources.ICON + "tray.gif"));
			/* ����״̬ */
			if ((selectedFriend.getStatus() == 0 || selectedFriend.getStatus() == 1))
				name.setText(selectedFriend.getNickName() + "[����]");
			else
				name.setText(selectedFriend.getNickName() + "[����]");

			//setToolTipText(selectedFriend.getSignedString());
			signedString.setText(selectedFriend.getSignedString());

			setToolTipText("<html><font color=red>������������Ϣ</font><br>�ǳƣ�"
					+ selectedFriend.getNickName()
					+ "["
					+ selectedFriend.getID()
					+ "]<br>״̬��"
					+ (selectedFriend.getStatus() == 0
							|| selectedFriend.getStatus() == 1 ? "����" : "����")
					+ "<br>�û�IP��" + selectedFriend.getIP() + "<br>����ǩ����"
					+ selectedFriend.getSignedString() + "<br><br></html>");

			/* ѡ�к���ػ� */
			if (cellHasFocus) {
				this.setBackground(new Color(232, 236, 251));
				name.setForeground(Color.RED);
				signedString.setForeground(Color.RED);
			} else {
				this.setBackground(new Color(241, 247, 254));

				if (selectedFriend.getStatus() != 0
						&& selectedFriend.getStatus() != 1) { // ��������
					name.setForeground(new Color(0, 51, 102));
					signedString.setForeground(new Color(0, 51, 102));
				} else {
					name.setForeground(new Color(150, 150, 150)); // δ����
					signedString.setForeground(new Color(150, 150, 150));	//��ɫ
				}
			}
			return this;
		}
	}
}