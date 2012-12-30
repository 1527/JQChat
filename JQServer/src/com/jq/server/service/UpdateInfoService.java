package com.jq.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Statement;

import com.jq.server.sql.DBSource;
import com.jq.util.FriendInfo;

/** �����û����Ͼ������ */
public class UpdateInfoService extends AbstractService {

	private static final String SQL_UPDATA = "Update UserInfo set Nickname = '?' , face = ? , sex = '?', age = ?, Academy = '?', department = '?', "
			+ "Provence = '?', City = '?', Email = '?', Homepage = '?', Description = '?' where UserID = '?';";

	/** �����û����ϵĹ��췽�� */
	public UpdateInfoService(String msg) {
		super(msg);
	}

	/** �����û����ϵľ��巽��  */
	public void service(DBSource db, ObjectInputStream clientInputStream,
			ObjectOutputStream clientOutputStream) {
		this.clientInputStream = clientInputStream;
		this.clientOutputStream = clientOutputStream;

		try {
			FriendInfo myInfo = (FriendInfo) clientInputStream.readObject();
			updateInfo(db, myInfo);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// �ͷ����ݿ�������Դ
			db.releaseConnection();
			// �ر�socket��Դ
			close();
		}
	}

	/** �����û����ϵ����ݿ�������� */
	private void updateInfo(DBSource db, FriendInfo myInfo) {
		String sql = SQL_UPDATA;

		sql = sql.replaceFirst("\\?", myInfo.nickname);
		sql = sql.replaceFirst("\\?", myInfo.face);
		sql = sql.replaceFirst("\\?", myInfo.sex);
		sql = sql.replaceFirst("\\?", "" + myInfo.age);
		sql = sql.replaceFirst("\\?", myInfo.academy);
		sql = sql.replaceFirst("\\?", myInfo.department);
		sql = sql.replaceFirst("\\?", myInfo.province);
		sql = sql.replaceFirst("\\?", myInfo.city);
		sql = sql.replaceFirst("\\?", myInfo.mail);
		sql = sql.replaceFirst("\\?", myInfo.homePage);
		sql = sql.replaceFirst("\\?", myInfo.signedString.toString());
		sql = sql.replaceFirst("\\?", myInfo.id);

		try {
			Statement stat = db.getStatement();
			db.setAutoCommit(false);
			// ִ�����ݿ����
			stat.executeUpdate(sql);
			db.commit();

			clientOutputStream.writeObject(ServiceFactory.SUCCESS);
		} catch (Exception e) {
			db.rollback();

			e.printStackTrace();
			try {
				clientOutputStream.writeObject(ServiceFactory.ERROR);
			} catch (IOException e1) {
			}
		}
	}

}
