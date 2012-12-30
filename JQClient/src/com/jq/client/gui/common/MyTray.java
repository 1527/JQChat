package com.jq.client.gui.common;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.JFrame;

import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.client.protocol.udp.UDPServer;
import com.jq.util.MyResources;
import com.jq.util.Param;

/**
 * 	ʵ��ϵͳ���̹���.
 * 	@author ����
 * */
public class MyTray{
	private Image image = null;
    private PopupMenu 	menu;							// Ϊ������̼�һ�������˵�
    private TrayIcon  icon;
    private static boolean isFrist = true;
	public SystemTray tray;
	
	public MyTray(final JFrame jf, final UDPServer UDP){
		//��ȡϵͳ����
		tray = SystemTray.getSystemTray();

		//���̵��Ҽ��˵�
		menu = new PopupMenu();
		//�˳��˵�sss
		MenuItem exitItem = new MenuItem("�˳�JQ");
		exitItem.addActionListener(new ActionListener(){
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				//System.exit(0);
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
		});
		
		//��ʾ�����ڲ˵�
		MenuItem showItem = new MenuItem("��ʾ������");
		showItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				showFrame(jf);
				tray.remove(icon);
			}
		});
		
		menu.add(showItem);
		menu.add(exitItem);
		
		
		
		//���̵ı���
		String title = "JQ 2011";
		//����ͼ��
		image = MyResources.getImage(MyResources.ICON + "tray.gif");		/*װ������ͼ��*/
		//ʵ����TrayIcon����
		icon = new TrayIcon(image, title, menu);/*Ϊ������̼�һ����ʾ��Ϣ*/
		//�����������¼�
		icon.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					showFrame(jf);
					//tray.remove(icon);
				}
			}
		});
		
        try{
            tray.add(icon);
            if (isFrist){
            	icon.displayMessage("��ʾ", "JQ�ͻ���" + Param.NEWLINE + 
            							"������ϵͳ����!", MessageType.INFO);// ���г����ʱ�����½ǻ���ʾ��Ϣ
            	isFrist = false;
            }
        }
        catch (java.awt.AWTException e) {
            System.err.println("�޷���������������� " + e);
        }
	}
	

	/**��ʾ����*/
	private void showFrame(JFrame frame){
		frame.setVisible(true);		/*��ʾ*/
		frame.setExtendedState(JFrame.NORMAL);	/*����״̬Ϊ������ʾ*/
	}
	
	/**
	 * 	ϵͳ�Ƿ�֧��ϵͳ���̹���.
	 * 	@return boolean ֧��-true.��֧��-false.
	 * */
	public static boolean isSupported(){
		return SystemTray.isSupported();
	}
	
	
}
