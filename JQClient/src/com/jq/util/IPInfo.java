package com.jq.util;

import java.net.InetSocketAddress;

/** ��ȡIP��Ϣ���࣬������������IP��ַ�Ͷ˿��Լ��ͻ��˵Ķ˿�*/
public class IPInfo {
	private static int cPort = 8000;
	private static String IP = "127.0.0.1";
	private static int sPort = 6000;
	private static int fPort = 7000;
	
	/** ����IP��ַ */
	public static String getIP() {
		return IP;
	}
	public static void setIP(String iP) {
		IP = iP;
	}
	
	/** ���ؿͻ���ռ�ö˿�.*/
	public static int getClientPort() {
		return cPort;
	}
	public static  void setClientPort(int pORT) {
		cPort = pORT;
	}
	
	/** ���ط�������ռ�ö˿�.*/
	public static int getServerPort() {
		return sPort;
	}
	public static void setServerPort(int pORT) {
		sPort = pORT;
	}
	
	/**
	 * 	���ط�������Ϣ.
	 * */
	public static InetSocketAddress getServerInetAddress(){
		return new InetSocketAddress(IP,sPort);
	}
	
	public static void setFilePort(int fPort) {
		IPInfo.fPort = fPort;
	}
	
	public static int getFilePort() {
		return fPort;
	}
}
