package com.jq.client.protocol.tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** ��������� */
public abstract class AbstractService {
	protected ObjectInputStream clientInputStream = null;
	protected ObjectOutputStream clientOutputStream = null;
	protected TCPServer TCP = null;

	/** ����TCPServer�๹����������
	 * ��ȡInputStream��OutputStream
	 * @param TCP ��ȡ���ӵ�socket */
	public AbstractService(TCPServer TCP) {
		this.TCP = TCP;
		try {
			clientInputStream = new ObjectInputStream(TCP.getInputStream());
			clientOutputStream = new ObjectOutputStream(TCP.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ObjectInputStream getObjectInputStream() {
		return clientInputStream;
	}

	public ObjectOutputStream getObjectOutputStream() {
		return clientOutputStream;
	}

}
