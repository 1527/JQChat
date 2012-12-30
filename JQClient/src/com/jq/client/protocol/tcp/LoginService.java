package com.jq.client.protocol.tcp;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jq.util.Friend;

/**
 * 	[�û���¼������]
 * 	LoginService
 * 	��½������ʹ����.
 * 	����ͨ����������InputStream��OutPutStream��������
 * 	�û��ĵ�½����.
 * 	
 * 	ִ��һϵ�е�½׼���.
 *  �������������Ϣ����:
 * 				�û��˺�,����,IP,�˿�,״̬.
 * 	��½����[������ȡ�����б�,��������״̬���û�IP,�˿���Ϣ��]
 *  ��½�ɹ�:
 * 				���������غ����б��������Ϣ
 * 				Object[]{result,leftInfo}.
 * 	ʧ�ܷ���null.
 *  �̳���AbstractService
 *  ʵ����Service<Object[],String>�ӿ�
 * */
public class LoginService extends AbstractService implements Service<Object[],String>{
	
	/** ������Ϣ,String[0]��result��String[1]����Ϣ*/
	private String[] leftInfo = new String[1];
	/** �������ڵĸ����� */
	public static JFrame owner = null;
	
	/** ��¼����Ĺ��캯�� */
	public LoginService(TCPServer TCP) {
		super(TCP);
	}
	
	/**
	 * ���ӷ���������.
	 * ˵��:
	 * 	    ��½�ɹ��󷵻غ����б�,ͬʱmsg���齫�ı�Ϊ�û����ܵ���������Ϣ.
	 * @param msg[] Ҫ��������Ϣ.
	 * */
	public Object[] service(String msg) {
		//����һ��String.��ʽΪ[�˺�_����_IP_�˿�_״̬]
		Friend[] result = null;
		try {
			clientOutputStream.writeObject(ServiceFactory.TASK_LOGIN + msg);		//���͵�¼����
			
			String f = (String)clientInputStream.readObject();	
			//���շ�����Ϣ.
			if (f.equals(TCPServer.ERROR)){																		//�˺��������
				JOptionPane.showMessageDialog(owner, "�˺Ż��������!", "����",
						JOptionPane.ERROR_MESSAGE);
				return null;
			}else if (f.equals(TCPServer.LOGINED)) { // �û��ѵ�¼.
				JOptionPane.showMessageDialog(owner, "���û��ѵ�¼!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
				return null;
			}else{	/*��½�ɹ�,���յ��Լ��ͺ�����Ϣ.*/
				result = getFriend();	//result���ص��Ǻ�������
			}
			getLeftInfo();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally{
			TCP.close();
		}
		return new Object[]{result,leftInfo};
	}

	@SuppressWarnings("unchecked")
	private void getLeftInfo() {
		/* ��½�ɹ�,���ո��û����յ���������Ϣ */
		try {
			leftInfo[0] = (String) clientInputStream.readObject();
			if (leftInfo[0].equals(TCPServer.SUCCESS)) {
				ArrayList<String> left = (ArrayList<String>) clientInputStream
					.readObject();
				leftInfo = left.toArray(new String[1]);
			}
			else
				leftInfo = null;
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private Friend[] getFriend() {
		ArrayList<Friend> list = null;
		Friend[] friendsInfo = new Friend[1];

		try {
			list = (ArrayList<Friend>) clientInputStream.readObject();
			//friendsInfo = list.toArray(new Friend[1]);
			friendsInfo = list.toArray(friendsInfo);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return friendsInfo;
	}

	
}
