package com.jq.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/** [MsgPages] 
 * �����¼��ҳ������.
 * ���������¼�Ĺ���.*/
public class MsgPages {
	
	/** ��ҳ�� */
	private int 				pageNum = 0;
	/** ���ڵ�ҳ�� */
	private int 				currentPage = 0;
	private StringBuilder[] 	pages = null;
	private HashMap<String, StringBuilder> pageMap = new HashMap<String, StringBuilder>();
	private ArrayList<String> 	dateList = new ArrayList<String>();
	
	/** �����¼��ҳ������ ���캯��. */
	public MsgPages(FileManager fm) {
		
		pageNum = fm.getLines() / Param.PAGE_LINES;
		if(fm.getLines() % Param.PAGE_LINES > 0)
			pageNum = pageNum + 1;

		pages = new StringBuilder[pageNum];
		for (int i = 0; i < pages.length; i++)
			pages[i] = new StringBuilder();
		
		/** ��ȡ��ÿһ�е�����. */
		String line;	
		int i = 0;
		/** ���������¼�ļ���������.*/
		BufferedReader inputStream = fm.getBufferedReader();
		try {
			while ((line = inputStream.readLine()) != null) {
				i++;
				if (line.startsWith("[20")) {	//�Ƿ���������Ϊ��ͷ������ȱ��
					line = line.substring(1, line.length() - 1);	//��ȡ���ڣ�ȥ��[]����
					pageMap.put(line, pages[i / (Param.PAGE_LINES + 1)]);	//��51�о��ǵ�2ҳ��
					dateList.add(line);	//��ͨ���鿴���ڲ鿴��¼���ʼ���������
				}
				pages[i / (Param.PAGE_LINES + 1)].append(line + Param.NEWLINE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ��һҳ.
	 * @return String ��һҳ����������.
	 * */
	public String getFristPage() {
		if (pageNum == 0)
			return null;
		
		currentPage = 0;
		return pages[0].toString();
	}

	/**
	 * ��ȡ���һҳ.
	 * @return String ��ȡ���һҳ������.
	 * */
	public String getLastPage() {
		if (pageNum == 0)
			return null;

		currentPage = pageNum - 1;
		return pages[pageNum - 1].toString();
	}

	/**
	 * ��ȡ��һҳ. 
	 * @return String ��ǰҳ����һҳ������.
	 * */
	public String getPrePage() {
		if (pageNum == 0)
			return null;
		
		currentPage--;
		if (currentPage <= 0)
			currentPage = 0;
		
		return pages[currentPage].toString();
	}

	/**
	 * ��ȡ��һҳ.
	 * @return String ��ȡ��ǰҳ��һҳ������.
	 * */
	public String getNextPage() {
		if (pageNum == 0)
			return null;

		currentPage++;
		if (currentPage >= pageNum - 1)
			currentPage = pageNum - 1;
		
		return pages[currentPage].toString();
	}

	/**
	 * ��ȡ��ǰҳ. 
	 * @return String ��ȡ��ǰҳ������.
	 * */
	public String getCurrentPage() {
		return pages[currentPage].toString();
	}

	/**
	 * ��ȡ����.
	 * */
	public String[] getDates() {
		String[] p = new String[1];
		return dateList.toArray(p);
	}

	/**
	 * ���ݸ������ڻ�ȡ�����������ݵ�ҳ.
	 * @param date
	 *            ���������ڵ��ַ�����ʾ.
	 * @return �������ڵ�ҳ������.
	 * */
	public String getPageByDate(String date) {
		return pageMap.get(date).toString();
	}

	/**
	 * ����. ͨ��������msg���������ڵ�ҳ.
	 * @param msg
	 *            Ҫ����������.
	 * @return String ���򷵻�����ҳ������,���򷵻�null;
	 * */
	public String search(String msg) {
		for (int i = 0; i < pages.length; i++) {
			if (pages[i].toString().contains(msg)) {
				currentPage = i;
				return pages[i].toString();
			}
		}
		return null;
	}

	/**
	 * ��ȡĳҳ���ڼ�ҳ��������.
	 * @param index
	 *            ҳ�±� 0~length-1
	 * @return String ����ҳ����.
	 * */
	public String getPageAt(int index) {
		if (index < 0 || index > pages.length)
			//throw new IndexOutOfBoundsException();
			return "û����һҳ.Sorry,No this page.";
		
		return pages[index].toString();
	}

	/**
	 * ��ȡ��ǰҳ��.
	 * @return int ҳ��.
	 * */
	public int getCurrentNum() {
		return currentPage + 1;
	}

	/**
	 * ��ȡ�ܹ�ҳ��.
	 * @return int �ܹ���ҳ��.
	 * */
	public int getPageNum() {
		return pageNum;
	}

	/**
	 * ɾ������ҳ������.
	 * */
	public void delete() {
		pageNum = 0;
		currentPage = 0;
		dateList.removeAll(dateList);
		for (StringBuilder i : pages)
			i.delete(0, i.toString().length());
	}

	
}
