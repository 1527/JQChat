package com.jq.server.sql;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.jq.server.sql.DBSource;
import com.jq.server.sql.SQLConnection;
import com.jq.util.PropertyFile;

/** [���ݿ����ӳ���] 
 * ����ʵ��һ�����ݿ����ӳ� ��ǿ�����ݿ��ʹ��Ч��.
 * �����̳߳�. */
public class SQLPoolServer {

	private PropertyFile prop = null; // ��ȡ�������ļ�
	private BlockingQueue<DBSource> SQLPool = null; // ���ݿ����ӳ�
	private int maxPool = 0; // ���ӳ������Ŀ

	/** ���ݽ������ݿ������ļ�����PropertyFile	����ȡ������̳߳ص������Ŀ
	 * ����maxPool�����ӣ�DBSource��������ŵ�SQLPool���ӳ��У�Queue�� */
	public SQLPoolServer(PropertyFile properFile) {
		prop = properFile;
		SQLPool = new LinkedBlockingQueue<DBSource>();
		maxPool = prop.getMax();

		System.out.println("�������ݿ��");
		// �������ӳ�
		for (int i=1; i<=maxPool; i++) {
			DBSource db = new SQLConnection(prop, this );
			try {
				SQLPool.put(db);
				System.out.println("������# "+i+"�����ӣ����ŵ����ӳ���");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
        System.out.println("�������ݿ����ɣ���"+maxPool+"��");	
	}

	/** ʹ���������� 
	 * ���SQLConnection��������ִ���û������ݿ�Ĳ���. 
	 * ������������û��Ԫ��,�������ȴ�. */
	public synchronized DBSource getSQLServer() {
		DBSource db = null;
		try {
			db = SQLPool.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			db = null;
		}
		return db;
	}

	/** ʹ����������ݿ����ӳع黹SQLServer */
	public void returnSQLServer(DBSource db) {
		try {
			SQLPool.put(db);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** �ر����ӳ�	 */
	public void closePoolServer() throws SQLException {
		DBSource db = null;
		for (int i = 0; i < SQLPool.size(); i++) {
			try {
				db = SQLPool.take();
				db.closeConnection();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// SQLPoolServer s = new SQLPoolServer(new
	// PropertyFile("src/resources/properties/jdbc�����ļ�.properties"));

}
