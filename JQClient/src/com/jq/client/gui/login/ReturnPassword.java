package com.jq.client.gui.login;

import java.awt.event.*;
import java.util.regex.Pattern;
import javax.swing.*;

import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.util.MD5;
import com.jq.util.MyResources;
import com.jq.util.Param;

public class ReturnPassword implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	JFrame jf;
	JTextField jtf_id,jtf_email;
	JPasswordField password1,password2;
	JButton find,back;
	
	public ReturnPassword(){
		jf = new JFrame("�һ�����");
		jf.setLayout(null);
		jf.setAlwaysOnTop(true);
		jf.setIconImage(MyResources.getImage(MyResources.ICON + "tray.gif"));
		
		int width = 220;
		int height = 30;
		
		String label1[]={"��ʾ������ϸ��д���Ա��һ��������룡","�ˡ����ţ�","�ʡ����䣺","�� �� �� ��","�ظ����룺"};
		JLabel tag[] = new JLabel[label1.length];
		for(int i=0;i<label1.length;i++){
			tag[i] = new JLabel(label1[i]);
			tag[i].setBounds(10, 5+i*30, width+20, height);
			jf.add(tag[i]);
		}
		String label2[]={"���������˺�","�һ������ƾ��","������������","ȷ������"};
		JLabel explain[] = new JLabel[label2.length];
		for(int i=0;i<label2.length;i++){
			explain[i] = new JLabel(label2[i]);
			explain[i].setBounds(315, 35+i*30, width, height);
			jf.add(explain[i]);
		}
		
		jtf_id = new JTextField();
		jtf_email = new JTextField();
		jtf_id.setBounds(80, 35, width, height);
		jtf_email.setBounds(80, 65, width, height);
		
		password1 = new JPasswordField();
		password2 = new JPasswordField();
		password1.setEchoChar('��');
		password2.setEchoChar('��');
		password1.setBounds(80, 95, width, height);
		password2.setBounds(80, 125, width, height);

		jf.add(jtf_id);
		jf.add(jtf_email);
		jf.add(password1);
		jf.add(password2);
		
		find = new JButton("�޸�����");
		back = new JButton("����");
		find.setBounds( 80, 162, 100, 30);
		back.setBounds(230, 162, 100, 30);
		jf.add(find);
		jf.add(back);
		find.addActionListener(this);
		back.addActionListener(this);
		
		jf.setSize(420,225);
		jf.setLocationRelativeTo( null );
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.setResizable(false);
	}
	
	/*public static void main(String args[]){
		JFrame.setDefaultLookAndFeelDecorated( true ); 
		try {
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("΢���ź�", Font.PLAIN, 12));
		} catch (Exception e) {
			e.printStackTrace();
		}
		new ReturnPassword();
	}*/
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == back) {
			jf.setVisible(false);
			// new Login();
		} else if (e.getSource() == find) {
			if (!check())
				return;
			// MD5.code(String.valueOf(password1.getPassword()));

			TCPServer.owner = jf;
			@SuppressWarnings("unchecked")
			Service<String, String> service = (Service<String, String>) ServiceFactory
					.getService(ServiceFactory.TASK_PASSWORD, Setup.ip
							.getText().trim(), Integer.parseInt(Setup.sPort
							.getText().trim()));

			String flag = service.service(getInfos()); // �˴��ַ�������

			if (flag.equals(TCPServer.SUCCESS)) {
				JOptionPane.showMessageDialog(null, "��ϲ�㣬��������Ѿ����衣", "˵��",
						JOptionPane.INFORMATION_MESSAGE);
				jf.dispose();
			} else {
				JOptionPane.showMessageDialog(null, "�һ�����ʧ�ܣ�Ҳ����������䲻��ȷ��", "����",
						JOptionPane.WARNING_MESSAGE);
			}
			// new Login();
		}
	}

	private boolean check() {
		if (jtf_id.getText().isEmpty() || jtf_email.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "����д������Ϣ��", "����",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (!Pattern.matches("^([a-z0-9A-Z]+[_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", jtf_email.getText())) {
			JOptionPane.showMessageDialog(null, "������д����ȷ��\n ��ʽ����ȷ��", "����",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (!String.valueOf(password1.getPassword()).equals(
				String.valueOf(password2.getPassword()))) {
			JOptionPane.showMessageDialog(null, "������������벻ͬ��", "����",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (String.valueOf(password1.getPassword()).length() < 6) {
			JOptionPane.showMessageDialog(null, "����������̡�\n ����Ҫ��λ��", "����",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	private String getInfos() {
		String ID = jtf_id.getText().trim();
		String PW = MD5.code(String.valueOf(password1.getPassword()));
		String email = jtf_email.getText().trim();
		return ID + Param.SPACE + PW + Param.SPACE + email;
	}

}
