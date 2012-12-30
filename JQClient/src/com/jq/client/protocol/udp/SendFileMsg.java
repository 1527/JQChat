package com.jq.client.protocol.udp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.jq.util.IPInfo;
import com.jq.util.Param;

/**�����ļ���Ϣ��*/
public class SendFileMsg extends AbstractMessage{

	/*
	String fileName = "";
	String fileContent = "";
	*/
	private String fileName = "";
	private String fileSendID;
	private String fileSendIP;
	private int fileSendPort;
	
	/**�����ļ��Ĺ��캯��
	 * @param msg*/
	public SendFileMsg(String msg) {
		super(msg);
		/*
		int i = msgBody.indexOf(Param.SPACE);
		if(i == -1){
			fileName = msgBody;
			fileContent = "";
		}else{
			fileName = msgBody.substring(0, i);
			fileContent = msgBody.substring(i+1);
		}*/
		String[] mess = msgBody.split(Param.SPACE);
		//System.out.println(mess);
		fileSendID = ID;
		fileName = mess[0];
		fileSendIP = mess[1];
		fileSendPort = Integer.parseInt(mess[2]);
		
	}

	/** ��дupdate���� */
	@Override
	public void update(UDPServer UDP) {
		ServerSocket ss;
		Socket s;
		try {
			ss = new ServerSocket(IPInfo.getFilePort());
			OutputStream os = null;
			InputStream is = null;
			byte[] buffer = new byte[Param.DATA_SIZE];
			int cnt = 0;
			int i = JOptionPane.showConfirmDialog(null, "�û�[" + fileSendID
					+ "]�����ļ�\"" + fileName + "\",\n �Ƿ����?", "�����ļ�",
					JOptionPane.YES_NO_OPTION);
			if (i == JOptionPane.YES_OPTION) {
				
				s = ss.accept();
				is = s.getInputStream();
				s.close();
				try {
					File f = new File(Param.CURRENTPATH + "/JQFiles/"
							+ UDP.hostUser.getID());
					if (!f.exists()) // �����û��JQFilesĿ¼
						f.mkdirs();

					fileName = f.getAbsolutePath() + "/" + fileName;
					os = new FileOutputStream(fileName);
					f = new File(fileName);
					if (!f.exists()) // ����ļ�����
						try {
							f.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					else { // ����Ѵ��ڸ��ļ��򸲸�

					}
					// OutputStream out=new FileOutputStream(f);
					// out.write(fileContent.getBytes("UTF-8"));
					// out.close();

					while ((cnt = is.read(buffer)) >= 0) {
						os.write(buffer, 0, cnt);
					}
					os.flush();
					JOptionPane.showConfirmDialog(null,
							"�ļ����ͳɹ���������" + fileName, "�ļ����ͳɹ�",
							JOptionPane.INFORMATION_MESSAGE,JOptionPane.OK_CANCEL_OPTION);

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {// �ܾ�����
				UDP.sendMessage(MsgFactory.REJECTMSG + UDP.hostUser.getID()
						+ Param.SPACE + fileName, fileSendIP, fileSendPort);
			}
			
			os.close();
			is.close();
			ss.close();

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
