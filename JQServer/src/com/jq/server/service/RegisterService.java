package com.jq.server.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.jq.server.service.ServiceFactory;
import com.jq.server.sql.DBSource;
import com.jq.util.Param;

/** �û�ע�������� */
public class RegisterService extends AbstractService {

	private String ID = null;

	private static final String SQL_MAXID = "SELECT MAX(USERID) FROM USERLOGIN;";

	private static final String SQL_USERIPINFO = "INSERT INTO USERIPINFO VALUES ('?','0.0.0.0',0,0);";

	private static final String SQL_USERLOGIN_INSERT = "INSERT INTO USERLOGIN VALUES ('?','?');";

	private static final String SQL_USERLOGIN_UPDATE = "UPDATE USERLOGIN SET PASSWORD = '?' WHERE USERID = '?';";

	private static final String SQL_USERLOGIN_REMOVE = "DELETE FROM USERLOGIN WHERE USERID = '?';";

	private static final String SQL_USERINFO = "INSERT INTO USERINFO SET USERID = '?', NICKNAME = '?', RegisterEmail = '?' , FACE = 15, AGE = 20;";

	/** �û�ע�� ���캯�� */
	public RegisterService(String msg) {
		super(msg);
	}

	/** �û�ע�� ������ */
	public void service(DBSource db, ObjectInputStream clientInputStream,
			ObjectOutputStream clientOutputStream) {
		this.clientInputStream = clientInputStream;
		this.clientOutputStream = clientOutputStream;

		try {
			String newID = getNewID(db.getStatement(), db);
			clientOutputStream.writeObject(newID);
			String infos = (String) clientInputStream.readObject();

			update(db, infos);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// �ͷ����ݿ�������Դ
			db.releaseConnection();
			// �ر�socket��Դ
			close();
		}
	}

	// ��ʽ: ID_Password_nickname_RegisterEmail
	/** ���û���Ϣ д�����ݿ� */
	private void update(DBSource db, String info) {
		Statement stat = null;
		String[] infos = null;
		try {
			stat = db.getStatement();

			// �û�ȡ��ע��
			if (info.equals(ServiceFactory.ERROR)) {
				// ʧ�ܺ�ɾ����ID����
				db.setAutoCommit(false);
				stat.executeUpdate(SQL_USERLOGIN_REMOVE.replaceFirst("\\?",
						String.valueOf(ID)));
				db.commit();
				return;
			}

			infos = info.split(Param.SPACE);

			String sql_ip = SQL_USERIPINFO.replaceFirst("\\?", infos[0]);

			String sql_login = SQL_USERLOGIN_UPDATE.replaceFirst("\\?", infos[1]);
			sql_login = sql_login.replaceFirst("\\?", infos[0]);

			String sql_info = SQL_USERINFO.replaceFirst("\\?", infos[0]);
			sql_info = sql_info.replaceFirst("\\?", infos[2]);
			sql_info = sql_info.replaceFirst("\\?", infos[3]);

			db.setAutoCommit(false);
			stat.executeUpdate(sql_ip);
			stat.executeUpdate(sql_login);
			stat.executeUpdate(sql_info);
			db.commit();

			clientOutputStream.writeObject(ServiceFactory.SUCCESS);
		} catch (Exception e) {
			db.rollback();
			try {
				// ʧ�ܺ�ɾ����ID����
				db.setAutoCommit(false);
				stat.executeUpdate(SQL_USERLOGIN_REMOVE.replaceFirst("\\?",
						String.valueOf(infos[0])));
				db.commit();
				clientOutputStream.writeObject(ServiceFactory.ERROR);
			} catch (Exception e1) {
			}
			e.printStackTrace();
		}
	}

	/** ���û�ע�ᴦ��. ���ظ��û������ݿ����Ѵ����û���ID��������+1���º���.
	 * @return ��ID */
	private String getNewID(Statement stat, DBSource db) {
		int newID = 1000;

		/** �������ݿ�����ͬ��,��ֹ����û�ͬʱ�������û����ID���ظ� */
		synchronized (SQLServerProcess.sqlPool) {
			try {
				/* �������� */
				ResultSet rs = stat.executeQuery(SQL_MAXID);
				if (rs.next() && rs.getString(1) != null)
					newID = Integer.parseInt(rs.getString(1)) + 1;

				ID = String.valueOf(newID);
				// ����ID�������ݿ�,��ֹ���̳߳�ͻ
				db.setAutoCommit(false);
				stat.executeUpdate(SQL_USERLOGIN_INSERT.replaceFirst("\\?", ID));
				db.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				return String.valueOf(newID);
			}
		}

		return String.valueOf(newID);
	}

}
