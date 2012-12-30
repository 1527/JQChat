package com.jq.client.gui.mainframe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jq.client.protocol.tcp.Service;
import com.jq.client.protocol.tcp.ServiceFactory;
import com.jq.client.protocol.tcp.TCPServer;
import com.jq.util.Friend;
import com.jq.util.MD5;
import com.jq.util.MyResources;
import com.jq.util.Param;

/**�޸����봰��*/
public class ChangePassword extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 6188437101191317559L;
	JButton ok,cancel;
	JLabel title_Label,userID_Label,pwd1_Label,pwd2_Label;
	JTextField userID;
	JPasswordField pwd1,pwd2;

	public ChangePassword(Friend host){
		setTitle("�޸�����");
		setAlwaysOnTop(true);
		setIconImage(MyResources.getImage(MyResources.ICON + "tray.gif"));
		setLayout(null);
		
		title_Label = new JLabel("�� �� �� ��");
		userID_Label = new JLabel("�û��ʺţ�");
		pwd1_Label = new JLabel("�� �� �� ��");
		pwd2_Label = new JLabel("ȷ�����룺");
		
		userID = new JTextField();
		userID.setText(host.getID());
		userID.setEditable(false);
		pwd1 = new JPasswordField();
		pwd1.setEchoChar('��');
		pwd2 = new JPasswordField();
		pwd2.setEchoChar('��');
		
		ok = new JButton("ȷ��");
		cancel = new JButton("ȡ��");
		ok.addActionListener(this);
		cancel.addActionListener(this);
		
		title_Label.setBounds(80, 10, 60, 30);
		userID_Label.setBounds(10, 50, 60, 30);
		userID.setBounds(80, 50, 150, 30);
		pwd1_Label.setBounds(10, 90, 60, 30);
		pwd1.setBounds(80, 90, 150, 30);
		pwd2_Label.setBounds(10, 130, 60, 30);
		pwd2.setBounds(80, 130, 150, 30);
		ok.setBounds(20, 170, 80, 30);
		cancel.setBounds(140, 170, 80, 30);
		
		add(title_Label);
		add(userID_Label);
		add(userID);
		add(pwd1_Label);
		add(pwd1);
		add(pwd2_Label);
		add(pwd2);
		add(ok);
		add(cancel);
		
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(240,240);
		setLocationRelativeTo(null);	//������ʾ
	}

	

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()== ok){
			if(check()){
				TCPServer.owner = this;
				Service<String,String> service = (Service<String, String>) ServiceFactory.getService( 
						ServiceFactory.TASK_PASSWORD, TCPServer.SERVER_IP,TCPServer.PORT);

				String flag = service.service(getInfos());	//�˴��ַ�������
				if (flag.equals(TCPServer.SUCCESS))
					JOptionPane.showMessageDialog(null, "��������ɹ�!",
							"��ʾ", JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, "��������ʧ��!",
							"��ʾ", JOptionPane.ERROR_MESSAGE);
				dispose();
			}else{
				return;
			}
		}else if(e.getSource() == cancel){
			dispose();
		}
		
	}
	
	/**��ȡ������Ϣ
	 * ��ʽ��ID_PW*/
	private String getInfos() {
		return userID.getText().trim() + Param.SPACE + MD5.code(String.valueOf(pwd1.getPassword()).trim());
	}

	/**�����ʽ���*/
	private boolean check(){
		if(pwd1.getPassword().length == 0 || pwd2.getPassword().length == 0 ){
			JOptionPane.showMessageDialog(null, "����д�����룡", "����",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (!String.valueOf(pwd1.getPassword()).equals(
				String.valueOf(pwd2.getPassword()))) {
			JOptionPane.showMessageDialog(null, "������������벻ͬ��\n ���������롣", "����",
					JOptionPane.WARNING_MESSAGE);
			clean();
			return false;
		}
		if (String.valueOf(pwd1.getPassword()).length() < 6) {
			JOptionPane.showMessageDialog(null, "����������̡�\n ����Ҫ��λ��", "����",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		return true;
	}


	/** ����ı� */
	public void clean() {
		pwd1.setText(null);
		pwd2.setText(null);
	}
	
	/*
	public static void main(String[] args) {
		try {
		    UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		    UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("΢���ź�", Font.PLAIN, 12));
		}catch (Exception e) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			}catch (Exception e2) {}
			JOptionPane.showMessageDialog(null, "����Ƥ��ʧ�ܣ�ʹ��Ĭ�Ͻ��档","����", JOptionPane.WARNING_MESSAGE);
		}
		new ChangePassword();

	}*/

}
