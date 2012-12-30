package com.jq.client.gui.login;


import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import com.jq.client.protocol.tcp.AbstractService;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.util.MD5;
import com.jq.util.MyResources;
import com.jq.util.Param;

import java.util.regex.*;

/**
 * ���û�ע�ᴰ��
 * 
 * ����ģʽ:
 * 		����������������ȡ��ID����.
 * 		������������ID����
 * 		�û���дע����Ϣ.
 * 		�ɹ�������������ͻ�������,�������ش��ɹ�/ʧ�ܱ�ʾ.
 * 		���û���;ȡ��ע���������������ȡ����ʾ.
 * */

public class Register extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private AbstractService service = null;
	
	JTextField jtf_id, jtf_name, jtf_email;
	JTextArea jta_comment;
	JPasswordField password1, password2;
	JButton register, reset, back;

	public Register(final AbstractService service) {
		this.service = service;
		setTitle("ע��");
		setAlwaysOnTop(true);
		setIconImage(MyResources.getImage(MyResources.ICON + "tray.gif"));
		setLayout(null);
		
		int width = 220;
		int height = 30;

		//������30������һ�У�������10 �� 90��315��ʼ
		String label1[] = { "ע����ʾ���밴Ҫ����ϸ��д��", "�ˡ����ţ�", "�ǡ����ƣ�", "�ʡ����䣺",
				"�ܡ����룺", "�ظ����룺","����˵����" };
		JLabel tag[] = new JLabel[label1.length];		//��JLabelͨ��setBounds����������
		for (int i = 0; i < label1.length; i++) {
			tag[i] = new JLabel(label1[i]);
			tag[i].setBounds(10, 5 + i * 30, width, height);
			add(tag[i]);
		}
		
		String label2[] = { "�˺ű���Ψһ", "�ǳƿ�������д", "�һ������ƾ��", "������6λ����", "���ٴ���������" };
		JLabel explain[] = new JLabel[label2.length];		//��jlabelͨ��setBounds����������
		for (int i = 0; i < label2.length; i++) {
			explain[i] = new JLabel(label2[i]);
			explain[i].setBounds(315, 35 + i * 30, width, height);
			add(explain[i]);
		}

		jtf_id = new JTextField();
		jtf_id.setEditable(false);
		jtf_name = new JTextField();
		jtf_email = new JTextField();
		jtf_id.setBounds(80, 35, width, height);
		jtf_name.setBounds(80, 65, width, height);
		jtf_email.setBounds(80, 95, width, height);

		password1 = new JPasswordField();
		password2 = new JPasswordField();
		password1.setEchoChar('��');
		password2.setEchoChar('��');
		password1.setBounds(80, 125, width, height);
		password2.setBounds(80, 155, width, height);
		
		jta_comment = new JTextArea();
		jta_comment.setBounds(80, 185, width+90, height*2);
		
		add(jtf_id);
		add(jtf_name);
		add(jtf_email);
		add(password1);
		add(password2);
		add(jta_comment);
		
		register = new JButton("ע��");
		reset = new JButton("����");
		back = new JButton("����");
		register.setBounds( 40, 250, 100, 30);
		reset.setBounds(160, 250, 100, 30);
		back.setBounds(280, 250, 100, 30);
		
		add(register);
		add(reset);
		add(back);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				//ȡ��ע��
				try {
					service.getObjectOutputStream().writeObject(TCPServer.ERROR);
				} catch (IOException ep) {
					ep.printStackTrace();
				}
			}});
		register.addActionListener(this);
		reset.addActionListener(this);
		back.addActionListener(this);
		
		setSize(420, 320);
		setLocationRelativeTo(null);		//������ʾ
		//setVisible(true);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}

	/*public static void main(String args[]){
		JFrame.setDefaultLookAndFeelDecorated( true ); 
		try {
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("΢���ź�", Font.PLAIN, 12));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		new Register();
	}*/
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == back) {
			//ȡ��ע��
			try {
				service.getObjectOutputStream().writeObject(TCPServer.ERROR);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			setVisible(false);
		} else if (e.getSource() == register) {
			if(!check())
				return;
			
			try {
				service.getObjectOutputStream().writeObject(getInfos());
				String flag = (String)service.getObjectInputStream().readObject();
				
				if(flag.equals(TCPServer.SUCCESS))
					JOptionPane.showMessageDialog(null, "["+ jtf_id.getText() + "]ע��ɹ�!",
																					"��ʾ", JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, "["+ jtf_id.getText() + "]ע��ʧ��!",
							"��ʾ", JOptionPane.INFORMATION_MESSAGE);
				dispose();
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}else if(e.getSource() == reset ){
			
			jtf_name.setText(null);
			jtf_email.setText(null);
			password1.setText(null);
			password2.setText(null);
			jta_comment.setText(null);
		}
	}
	
	/** ����ID */
	public void setID(String id){
		jtf_id.setText(id);
	}
	
	/** ��ȡ��Ϣ�� 
	 * ID_Password_nickname_Email */
	public String getInfos(){
		String ID = jtf_id.getText().trim();
		String PW = MD5.code(String.valueOf(password1.getPassword()));
		String nickName = jtf_name.getText().trim();
		String email = jtf_email.getText().trim();
		return ID + Param.SPACE + PW + Param.SPACE + nickName + Param.SPACE
				+ email;
	}

	private boolean check() {
		if (jtf_id.getText().isEmpty() || jtf_email.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "����д������Ϣ��", "����", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (!Pattern.matches("^([a-z0-9A-Z]+[_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", jtf_email.getText())) {
			//6~18���ַ���������ĸ�����֡��»��ߣ�����ĸ��ͷ����ĸ�����ֽ�β
			JOptionPane.showMessageDialog(this, "������д����ȷ��\n ��ʽ����ȷ��", "����",
					JOptionPane.WARNING_MESSAGE);	
			return false;
		}
		if (!String.valueOf(password1.getPassword()).equals(
				String.valueOf(password2.getPassword()))) {
			JOptionPane.showMessageDialog(this, "������������벻ͬ��", "����",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (String.valueOf(password1.getPassword()).length() < 6) {
			JOptionPane.showMessageDialog(this, "����������̡�\n ����Ҫ��λ��", "����",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

}
