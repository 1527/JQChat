package com.jq.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Statement;

import com.jq.server.sql.DBSource;
import com.jq.util.Param;

/** ������Ϣ������� */
public class LeftInfoService implements Service {

	protected Socket socket = null;
	protected ObjectOutputStream clientOutputStream = null;
	protected ObjectInputStream clientInputStream = null;

	/**�û���ID*/
	private String ID;
	/**�û����͵���Ϣ*/
	private String msg;

	private static final String SQL_LEFTINFO = "INSERT INTO LEFTINFO VALUES('?','?');";

	/** ������Ϣ�� 
	 * @param msg ��Ϣ��*/
	public LeftInfoService(String msg) {
		//int index = msg.indexOf(Param.SPACE);
		//ID = msg.substring(0, index).trim();
		//this.msg = msg.substring(index + 1).trim();
		String[] temp = msg.split(Param.SPACE);
		ID = temp[0].trim();
		this.msg = temp[1].trim();
	}

	/** ������Ϣ��service���� */
	public void service(DBSource db, ObjectInputStream clientInputStream,
			ObjectOutputStream clientOutputStream) {
		this.clientInputStream = clientInputStream;
		this.clientOutputStream = clientOutputStream;

		Statement stat = null;

		try {
			stat = db.getStatement();
			db.setAutoCommit(false);
			//��������Ϣд�����ݿ�
			update(stat);
			db.commit();
		} catch (Exception e) {
			e.printStackTrace();
			//��������⣬���ݿ�ع�
			db.rollback();
		} finally {
			// �ͷ����ݿ�������Դ--->�����ͷ�!
			db.releaseConnection();
			// �ر�socket��Դ
			close();
		}
	}

	/** �������ݿ⣬��������Ϣд�����ݿ� */
	private void update(Statement stat) throws SQLException {
		String sql = SQL_LEFTINFO.replaceFirst("\\?", ID);
		sql = sql.replaceFirst("\\?", msg);
		stat.executeUpdate(sql);
	}

	/** �ر����� */
	protected void close() {
		try {
			if (socket != null)
				socket.close();
			if (clientOutputStream != null)
				clientOutputStream.close();
			if (clientInputStream != null)
				clientInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
