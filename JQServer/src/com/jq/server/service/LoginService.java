package com.jq.server.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jq.server.sql.DBSource;
import com.jq.util.Friend;

/**
 * �����û���½����. . 
 * ��½�ɹ��󷵻� �û����ϡ��������Ϻ��û����ܵ���������Ϣ.
 * ״̬˵��: 0-δ��¼(������ʹ��) 1-����. 2-�뿪. 3-æµ. 4-����.
 * 
 * */
public class LoginService extends AbstractService {
	/** ִ�в�ѯ���ݿ�õ��Ľ�� */
	private ResultSet rs = null;

	/** ��½�˺�����˶� */
	private static final String SQL_LOGIN = "SELECT * FROM USERLOGIN WHERE USERID = '?' AND PASSWORD = '?';";
	/** ��ȡ�û��Լ���Ϣ */
	private static final String SQL_INFOS_ME = "SELECT UserInfo.UserID, UserInfo.NickName,UserInfo.Remark,"
			+ "UserInfo.Face, UserInfo.Description, status FROM UserInfo, UserIPInfo "
			+ "WHERE UserIPInfo.UserID = UserInfo.UserID AND UserInfo.UserID = '?';";
	/** ��ȡ������Ϣ����״̬���� */
	private static final String SQL_INFOS_FRIENDS = "SELECT UserInfo.UserID, UserInfo.NickName,UserInfo.Remark, IP, Port, "
			+ "UserInfo.Face, Status, UserInfo.Description FROM UserIPInfo, UserInfo "
			+ "WHERE UserIPInfo.UserID = UserInfo.UserID AND UserInfo.UserID in "
			+ "(SELECT FriendID FROM UserFriends WHERE UserID = '?') ORDER BY Status DESC;";
	/** ��ȡ������Ϣ */
	private static final String SQL_LEFTINFO = "SELECT Content From LeftInfo WHERE IDTo = '?';";
	/** ɾ��������Ϣ */
	private static final String SQL_DELETELEFT = "DELETE FROM LEFTINFO WHERE IDTo = '?';";
	/** �����û�IP��Ϣ */
	private static final String SQL_UPDATA = "UPDATE USERIPINFO SET IP = '?', PORT = '?' ,STATUS = '?' WHERE USERID = '?';";

	/** ��¼����(LoginService)�Ĺ��췽��
	 * @param msg �ĸ�ʽ�ǣ��û�ID+����+IP+�˿�+״̬ */
	public LoginService(String msg) {
		//���ø���(AbstractService)�Ĺ��췽��
		super(msg);
	}

	/** ��¼�����service���е���Ҫ���� */
	public void service(DBSource db, ObjectInputStream clientInputStream,
			ObjectOutputStream clientOutputStream) {
		this.clientInputStream = clientInputStream;
		this.clientOutputStream = clientOutputStream;

		Statement stat = null;
		try {
			stat = db.getStatement();
			if (check(stat)) {
				/* �˺�������ȷ,���غ�����ϢResultSet���Լ�����Ϣ(�����Լ�����Ϣ�ڵ�һ). */
				// ����Լ�����Ϣ.
				//stat = db.getStatement();
				rs = getInfos(stat, true);

				// ��ȡ�����б�
				if (null != rs) {
					/* ���͵�½�ɹ���Ϣ */
					clientOutputStream.writeObject(ServiceFactory.SUCCESS);
					stat = db.getStatement();
					/* ��ȡ�����б� */
					getInfos(stat, false);
					/* ���ͺ�����Ϣ */
					clientOutputStream.writeObject(friendslist);
					List<String> leftInfo = getLeftInfo(db);
					if (leftInfo != null) {
						// ����������Ϣ��־.
						clientOutputStream.writeObject(ServiceFactory.SUCCESS);
						clientOutputStream.writeObject(leftInfo);
					} else {
						clientOutputStream.writeObject(ServiceFactory.ERROR);
					}
					// �����û�IP��Ϣ.
					updataIpInfo(db);
				} else
					return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/*try {
				stat.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}*/
			// �ͷ����ݿ�������Դ
			db.releaseConnection();
			// �ر�socket��Դ
			close();
		}
	}

	/** �鿴�˺������Ƿ���ȷ 
	 * @return true ��ȷ  false ���� */
	private boolean check(Statement stat) {
		try {
			String SQL = SQL_LOGIN.replaceFirst("\\?", msg[0]);
			SQL = SQL.replaceFirst("\\?", msg[1]);
			rs = stat.executeQuery(SQL);

			// �˺�������� 
			if (!rs.next()) {
				clientOutputStream.writeObject(ServiceFactory.ERROR);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/** ��ȡ�û��Լ����û���������
	 * @return �����ResultSet */
	private ResultSet getInfos(Statement stat, boolean flag) {
		try {
			String SQL = null;
			if (flag)
				SQL = SQL_INFOS_ME.replaceFirst("\\?", msg[0]);
			else
				SQL = SQL_INFOS_FRIENDS.replaceFirst("\\?", msg[0]);
			
			rs = stat.executeQuery(SQL);

			if (flag) { // ��ȡ�û��Լ�������
				rs.next();
				if (rs.getInt(6) != 0) { // �ѵ�¼.0Ϊδ��¼.
					// ��ͻ��˷��ʹ����ʾ
					clientOutputStream.writeObject(ServiceFactory.LOGINED);
					return null;
				}
				friendslist.add(new Friend(rs.getString(1), rs
						.getString(2), rs.getString(3), null, 0, rs
						.getInt(4), 1, rs.getString(5)));
			} else {
				// ��ȡ���ѵ�����
				while (rs.next()) {
					friendslist.add(new Friend(rs.getString(1), rs
							.getString(2), rs.getString(3), rs
							.getString(4), rs.getInt(5), rs.getInt(6),
							rs.getInt(7), rs.getString(8)));
				}
			}
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ��ȡ��½�û��Ľ��յ���������Ϣ.
	 * 
	 *            ������Ϣ���շ���ID.
	 * */
	private List<String> getLeftInfo(DBSource db) {
		Statement stat = null;
		try {
			stat = db.getStatement();
			String SQL = SQL_LEFTINFO.replaceFirst("\\?", msg[0]);
			rs = stat.executeQuery(SQL);

			List<String> left = null;
			while (rs.next()) {
				if (left == null)
					left = new ArrayList<String>();

				left.add(rs.getString(1));
			}

			return left;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			removeLeftMsg(db, stat);
		}
		return null;
	}

	/** ɾ��������Ϣ */
	private void removeLeftMsg(DBSource db, Statement stat) {
		try {
			/* ����ȡ��������Ϣɾ�� */
			//stat = db.getStatement();
			String SQL = SQL_DELETELEFT.replaceFirst("\\?", msg[0]);
			/* ʹ������ */
			db.setAutoCommit(false);
			// ִ�в���
			stat.executeUpdate(SQL);
			/* ȷ���ύ */
			db.commit();
		} catch (SQLException e) {
			/* ʧ�ܻع� */
			db.rollback();
			e.printStackTrace();
		}
	}

	/** �����û�IP��Ϣ */
	private void updataIpInfo(DBSource db) {
		try {
			Statement stat = db.getStatement();

			String SQL = SQL_UPDATA.replaceFirst("\\?", msg[2]);
			SQL = SQL.replaceFirst("\\?", msg[3]);
			SQL = SQL.replaceFirst("\\?", msg[4]);
			SQL = SQL.replaceFirst("\\?", msg[0]);

			// ִ�в���
			/* ʹ������ */
			db.setAutoCommit(false);
			// ִ�в���
			stat.executeUpdate(SQL);
			/* ȷ���ύ */
			db.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
