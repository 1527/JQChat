package com.jq.util;

import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.jq.util.Friend;
import com.jq.util.MyResources;

/**
 * 	[�û�����������.]
 * 	Friend
 * 	���ڹ�����ѵĻ������ݶ���.
 *  �������ݰ����û�ID,����״̬,��½IP,�˿�,����ǩ����
 *  ��ʱ������.
 *  
 * 	ʵ�����л�,���������紫�����.
 * 
 * */
public class Friend implements Serializable {

	private String	ID = null; 			// ����JQ����
	private String 	nickName = null; 	// �����ǳ�
	private	String	remark;				// ���ѱ�ע
	private String 	IP = null; 	// ���ѵ�ǰIP��ַ
	private int 	port = 0; 			// ���ѵ�ǰ�ͻ���ռ�ö˿�
	private int 	face = 1; 			// ���ѵ�ǰͷ��
	private int 	status = 0; 		// ���ѵ�ǰ״̬,0:������ 1:����.....
	private String 	signedString = null;// ���Ѹ���ǩ��
	
	private ImageIcon	faceImage = null;	
	
	private static final long serialVersionUID = -5250532791652145335L;

	/**
	 * ���캯��.
	 * @param ID
	 *            �û�JQ����
	 * @param nickName
	 *            �û��ǳ�
	 * @param remark
	 * 			  ���ѱ�ע
	 * @param IP
	 *            �û���ǰIP��ַ
	 * @param port
	 *            �û���ǰ�ͻ���ռ�ö˿�
	 * @param face
	 *            �û�ͷ��
	 * @param status
	 *            �û���ǰ״̬
	 * @param signedString
	 *            �û�����ǩ��
	 * */
	public Friend(String ID, String nickName, String remark,String IP, int port, int face,
												int status, String signedString) {
		this.ID = ID;
		this.nickName = nickName;
		this.remark = remark;
		this.IP = IP;
		this.port = port;
		this.face = face;
		this.status = status;
		this.signedString = signedString;
		
	}

	/**
	 * 	���غ���ID����.
	 * 	@return String ���غ���ID����.
	 * */
	public String getID() {
		return ID;
	}
	
	/**
	 * 	���غ����ǳ�.
	 * 	@return String ���غ����ǳ�.
	 * */
	public String getNickName() {
		return nickName;
	}
	
	/**
	 * 	��ȡ���ѱ�ע
	 * 	@return String ���ѱ�ע
	 * */
	public String getRemark() {
		return remark;
	}

	/**
	 * 	���غ��ѵ�ǰIP��ַ.
	 * 	@return String ���غ��ѵ�ǰIP��ַ.
	 * */
	public String getIP() {
		return IP;
	}
	
	/**
	 * 	���غ��ѵ�ǰ�ͻ��˶˿�.
	 * 	@return int ���ص�ǰ���ѿͻ��˶˿�.
	 * */
	public int getPort() {
		return port;
	}
	
	/**
	 * 	��ȡ��ͷ����.
	 *  @return int ����ͷ��ı��
	 * */
	public int getFace(){
		return face;
	}
	
	/**
	 * 	���غ��ѵ�ǰ״̬.
	 *  0:������;1:����
	 * 	@return int ���غ��ѵ�ǰ״̬.
	 * */
	public int getStutas() {
		return status;
	}
	
	/**
	 * 	���غ��ѵĸ���ǩ��.
	 * 	@return String ���غ��Ѹ���ǩ��.
	 * */
	public String getSignedString() {
		return signedString;
	}
	
	/**
	 * 	�˷���Ӧ�ڿͻ��˵���,���ڷ��ش��û���ͷ��ImageIcon.
	 * 	@return ImageIcon ���ص�ǰ�û�ͷ��.
	 * */
	public ImageIcon getFaceIcon() {
		if (status == 0 || status == 1)		/*δ���߻�����*/
			faceImage = MyResources.getImageIcon(MyResources.USERFACE + face + "-1.gif");
		else
			faceImage = MyResources.getImageIcon(MyResources.USERFACE + face + ".gif");
		return faceImage;
	}
	
	/**
	 * 	״̬ͼ��
	 * 	״̬˵��:
	 * 			0-δ��¼(������ʹ��)
	 * 			1-����.
	 * 			2-�뿪.
	 * 			3-æµ.
	 * 			4-����.
	 * */
	public ImageIcon getStutasIcon(){
		if (status == 1)
			return MyResources.getImageIcon(MyResources.ICON + "hide.gif");
		else if (status == 2)
			return MyResources.getImageIcon(MyResources.ICON + "leave.gif");
		else if (status == 3)
			return MyResources.getImageIcon(MyResources.ICON + "nobother.gif");
		else if (status == 4)
			return MyResources.getImageIcon(MyResources.ICON + "online.gif");
		return null;
	}

	/**
	 * 	���ú����ǳ�.
	 * 	@param name �����ǳ�.
	 * */
	public void setNickName(String name) {
		nickName = name;
	}
	
	/**
	 * 	���ú��ѱ�ע
	 * 	@param remark ���ú��ѱ�ע
	 * */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
	 * 	����IP��ַ
	 * */
	public void setIP(String IP){
		this.IP = IP;
	}
	
	/**
	 * 	���ö˿�
	 * */
	public void setPort(int port){
		this.port = port;
	}

	/**
	 * 	���ú���ͷ��.
	 * 	@param i ����ͷ��.
	 * */
	public void setFace(Icon i) {
		faceImage = (ImageIcon) i;
	}
	
	/**
	 * 	����ͷ����.
	 * */
	public void setFace(int i){
		face = i;
	}
	
	/**
	 * 	���ú�������״̬.
	 * */
	public void setStutas(int i){
		status = i;
	}
	
	/**
	 * 	���ú��Ѹ���ǩ��.
	 * 	@param s ���Ѹ���ǩ��.
	 * */
	public void setSignedString(String s) {
		signedString = s;
	}
	
	public String toString() {
		return ID + "[" + nickName + "]";
	}
	
	
	/**
	 * 	��дequals.����Ҫ.
	 * 	��MyList�в�����ʱʹ��.
	 * */
	@Override
	public boolean equals(Object arg0) {
		Friend f = (Friend)arg0;
		return ID == f.ID;
	}

	@Override
	public int hashCode() {
		return ID.hashCode();
	}

}
