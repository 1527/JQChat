package com.jq.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;

import com.jq.server.sql.DBSource;
import com.jq.util.Friend;

/** ��������Ӻ��Ѿ������ */
public class AddtionService extends AbstractService {

	private static final String SQL_UPDATA = "INSERT INTO USERFRIENDS VALUES ('?','?');";

	private static final String SQL_GETINFO = "SELECT UserInfo.UserID, UserInfo.NickName,UserInfo.Remark, IP, Port, "
			+ "UserInfo.Face, Status, UserInfo.Description FROM UserIPInfo, UserInfo "
			+ "WHERE UserIPInfo.UserID = UserInfo.UserID AND UserInfo.UserID = '?';";

	public AddtionService(String msg) {
		super(msg);
	}

	/** ��Ӻ��ѷ���*/
	public void service(DBSource db, ObjectInputStream clientInputStream,
			ObjectOutputStream clientOutputStream) {
		this.clientInputStream = clientInputStream;
		this.clientOutputStream = clientOutputStream;

		Statement stat = null;

		try {
			if (msg.length == 2)
				update(db);
			else {
				stat = db.getStatement();
				getFriendInfo(stat, msg[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// �ͷ����ݿ�������Դ
			db.releaseConnection();
			// �ر�socket��Դ
			close();
		}
	}

	/** ��ȡ������Ϣ */
	private void getFriendInfo(Statement stat, String id) {
		String sql = SQL_GETINFO.replaceFirst("\\?", id);
		try {
			ResultSet rs = stat.executeQuery(sql);
			rs.next();

			clientOutputStream.writeObject(ServiceFactory.SUCCESS);
			clientOutputStream.writeObject(new Friend(rs.getString(1),
					rs.getString(2), rs.getString(3), rs
							.getString(4), rs.getInt(5), rs.getInt(6),
					rs.getInt(7), rs.getString(8)));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				clientOutputStream.writeObject(ServiceFactory.ERROR);
			} catch (IOException e1) {
			}
		}
	}

	/** ���º����б��������ID��*/
	private void update(DBSource db) {
		//��SQL_UPDATA���ʺŻ���(msg[0],msg[1])����ʽ
		String sql1 = SQL_UPDATA.replaceFirst("\\?", msg[0]);
		sql1 = sql1.replaceFirst("\\?", msg[1]);

		//��SQL_UPDATA���ʺŻ���(msg[1],msg[0])����ʽ
		String sql2 = SQL_UPDATA.replaceFirst("\\?", msg[1]);
		sql2 = sql2.replaceFirst("\\?", msg[0]);

		try {
			Statement stat = db.getStatement();
			db.setAutoCommit(false);
			// ִ�����ݿ����
			stat.executeUpdate(sql1);
			stat.executeUpdate(sql2);
			db.commit();
			// ˵��:����û�����Ҫ����Friend,�ʷ���Error
			getFriendInfo(stat, msg[1]);
		} catch (Exception e) {
			//��������ع����ݿ�
			db.rollback();
			e.printStackTrace();
			try {
				clientOutputStream.writeObject(ServiceFactory.ERROR);
			} catch (IOException e1) {
			}
		}
	}

}
