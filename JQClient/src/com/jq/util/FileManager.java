package com.jq.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * [�����ı���������.] 
 * ����ִ�������¼�Ķ�д����. ��ȡʱ��ȡ�����¼������,�Ա��ҳ��ʾ.
 * д��ʱ,�������¼�а�����ǰ����,��д�뵱ǰ����,����д�뵱ǰ����.
 * */
public class FileManager {
	private BufferedReader inputStream = null;
	private BufferedWriter outputStream = null;
	private StringBuilder sb = new StringBuilder();
	private String fileName;
	private int line = 0;

	private static SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd"); // ��ʽ������ʹ��

	/**
	 * ���캯��.
	 * 
	 * @param hostID
	 *            ��ǰ�ͻ����û�ID
	 * @param friendID
	 *            �������ID.
	 * */
	public FileManager(String hostID, String friendID) {

		checkExsit(hostID, friendID);

		start();
	}
	
	/** ����û������¼�ļ���·�����ڷ� */
	private void checkExsit(String hostID, String friendID) {
		
		File f = new File(Param.CURRENTPATH + "/JQchatLog/" + hostID);
		if (!f.exists())	//�����û��JQchatLogĿ¼
			f.mkdirs();

		fileName = f.getAbsolutePath() + "/" + friendID + ".txt";
		f = new File(fileName);
		if (!f.exists())   // ��������¼�ļ����� 
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**��ʼ���ļ����ж�ȡ����*/
	public void start() {

		try {
			inputStream = new BufferedReader(new FileReader(fileName));

			String l;
			while ((l = inputStream.readLine()) != null) {
				line++;	//������1
				sb.append(l + Param.NEWLINE);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * ��ȡ��ǰ����.��ʽΪ [2011-11-11] 
	 * @return String ��ʽ���������.
	 * */
	private static String getDate() {
		Date date = new Date();
		return "[" + s.format(date) + "]";
	}

	/** �����¼. */
	public void removeMsg() {
		line = 0;
		sb.delete(0, sb.toString().length());
	}

	/**	���������¼. */
	public void save(String msg) {
		String newMsg = msg;
		// ���msg��Ϊ�գ���sb�����в�����ʱ�䣬�����ʱ��
		if (msg.trim().length() != 0 && !sb.toString().contains(getDate()))
			newMsg = getDate() + Param.NEWLINE + "---------------------"
					+ Param.NEWLINE + newMsg;

		sb.append(Param.NEWLINE + newMsg);
		try {
			outputStream = new BufferedWriter(new FileWriter(fileName));
			outputStream.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ��ȡ��¼����.
	 * @return String �����¼String��ʽ.
	 * */
	public String getString() {
		return sb.toString();
	}

	/**
	 * ��ȡ������.
	 * @return BufferedRead ���������¼��������.
	 * */
	public BufferedReader getBufferedReader() {
		return new BufferedReader(new StringReader(getString()));
	}

	/**
	 * ��ȡ����.
	 * @return int �����¼��������.
	 * */
	public int getLines() {
		return line;
	}

}