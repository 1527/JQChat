package com.jq.client.protocol.tcp;

import com.jq.client.gui.login.Register;

/**�û�ע��������
 * �̳���AbstractService
 * ʵ����Service<String,String>�ӿ�*/
public class RegisterService extends AbstractService implements Service<String,String>{

	private Register registerFrame = null;
	
	/** ע��Ĺ��캯�� */
	public RegisterService(TCPServer TCP) {
		super(TCP);
		//System.out.println("����ע��");
	}

	/**
	 * ע��ʵ�ֵķ���
	 * ��ʽ:  ID_PW_nickname_Email
	 * @param msg ע�������Ϣ.
	 * @return ����һ���µ�ID����
	 * */
	public String service(String msg) {
		try {
			registerFrame = new Register(this);
			
			clientOutputStream.writeObject(ServiceFactory.TASK_NEWUSER + msg);
			String newID =  (String)clientInputStream.readObject();
			
			registerFrame.setID(newID);
			registerFrame.setVisible(true);
					
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
		return null;
	}	
}
