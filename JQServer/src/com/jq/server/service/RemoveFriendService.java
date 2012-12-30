
package com.jq.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.sql.Statement;

import com.jq.server.sql.DBSource;

/**ɾ�����Ѿ������*/
public class RemoveFriendService  extends AbstractService{
	
	private static final String SQL_REMOVE = "DELETE FROM USERFRIENDS WHERE USERID = '?' AND FRIENDID = '?';";

	/** ɾ�����ѹ����� */
	public RemoveFriendService(String msg) {
		super(msg);
	}

	/** ɾ�����Ѿ������к��� */
	public void service(DBSource db, ObjectInputStream clientInputStream, ObjectOutputStream clientOutputStream) {
		this.clientInputStream = clientInputStream;
		this.clientOutputStream = clientOutputStream;
		
		try {
			if (updata(db))
				clientOutputStream.writeObject(ServiceFactory.SUCCESS);
			else
				clientOutputStream.writeObject(ServiceFactory.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				clientOutputStream.writeObject(ServiceFactory.ERROR);
			} catch (IOException e1) {}
		} finally {
			//�ͷ����ݿ�������Դ
			db.releaseConnection();
			//�ر�socket��Դ
			close();
		}
	}
	
	/** �������ݿ⣬˫���û��ĺ��ѹ�ϵ��Ҫ��� 
	 * @return ture �ɹ� false ʧ��*/
	private boolean updata(DBSource db){
		String sql1 = SQL_REMOVE.replaceFirst("\\?", msg[0]);
		sql1 = sql1.replaceFirst("\\?", msg[1]);
		
		String sql2 = SQL_REMOVE.replaceFirst("\\?", msg[1]);
		sql2 = sql2.replaceFirst("\\?", msg[0]);
		
		try {
			Statement stat = db.getStatement();
			db.setAutoCommit(false);
			stat.executeUpdate(sql1);
			stat.executeUpdate(sql2);
			db.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			db.rollback();
			return false;
		}
		
		return true;
	}
	

}
