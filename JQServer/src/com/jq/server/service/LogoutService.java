package com.jq.server.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.sql.Statement;

import com.jq.server.sql.DBSource;

/**
 * �û�ע����¼�������.
 * */
public class LogoutService extends AbstractService implements Service {

	private static final String SQL_LOGOUT = "UPDATE UserIPInfo SET Status = 0 WHERE UserID = '?';";

	/** ע����¼�Ĺ��캯��*/
	public LogoutService(String msg) {
		super(msg);
		System.out.println("�ر�");
	}

	/** ע����¼��service ���� */
	public void service(DBSource db, ObjectInputStream clientInputStream,
			ObjectOutputStream clientOutputStream) {
		Statement stat = null;
		try {
			stat = db.getStatement();
			String SQL = SQL_LOGOUT.replaceFirst("\\?", msg[0]);

			try {
				db.setAutoCommit(false);
				stat.executeUpdate(SQL);
				db.commit();
			} catch (SQLException e) {
				db.rollback();
				e.printStackTrace();
			} finally {
				// �ͷ����ݿ�������Դ
				db.releaseConnection();
				// �ر�socket��Դ
				close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
