package com.jq.server.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import com.jq.server.sql.DBSource;
import com.jq.server.sql.SQLPoolServer;
import com.jq.util.PropertyFile;

/**
 * [���ݿ������] 
 * ����ʵ���û�����Ҫ�����ݿ����.ʵ��DBSource�ӿ�. 
 * ����ʵ�����ݿ����ӳ��е���������. ����ȫ��ͬ��. ʵ�ֹ��ܰ���:
 * [1]���ݵĲ�ѯ. 
 * [2]���ݵĸ���. 
 * [3]���ݵĲ���.
 * 
 */
public class SQLConnection implements DBSource {
	private static int num = 0; // �洢���ӹ���Connection��Ŀ
	private PropertyFile props = null; // jdbc config.properties
	private Connection conn = null; // ���ݿ�����Connection
	private Statement stmt = null; // ����ִ�о�̬ SQL ��䲢�����������ɽ���Ķ���
	private SQLPoolServer SQLPool = null; // ���ݿ����ӳ�
	private int n = 0; // ���ӱ��

	/**
	 * @param PropertyFile  ���ݿ������ļ�����.
	 * @param SQLPool ���ݿ����ӳ�
	 */
	public SQLConnection(PropertyFile prop, SQLPoolServer SQLPool) {
		this.props = prop;
		this.SQLPool = SQLPool;

		try {
			//����MySQL���ݿ�����
			//Class.forName(props.getDriver()).newInstance(); �޸ĵ�
			Class.forName(props.getDriver());
			// ��ȡConnection����. 
			//conn = DriverManager.getConnection(props.getURL(), props.getUser(),
			//		props.getPassword());
			conn = DriverManager.getConnection(props.getURL()+"?user="+props.getUser()+"&password="+props.getPassword()+"&useUnicode=true&characterEncoding=GBK" );
			//�����������
			stmt = conn.createStatement();
			//��ȡ������������ھ������ʱ����
			System.out.println("���ݿ�" + (++num) + "����...");
			n = num;
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "<html>���ݿ������ļ�����,����������<br>"
					+ e.getMessage() + "</html>", "����",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	/**
	 * ��дDBSource�ӿڵ�commit()����
	 * ʹ������һ���ύ/�ع�����еĸ��ĳ�Ϊ�־ø��ģ� ���ͷŴ� Connection ����ǰ���е��������ݿ�����
	 * �˷���ֻӦ�����ѽ����Զ��ύģʽʱʹ�á�
	 * */
	public void commit() {
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��дDBSource�ӿڵ�rollback()����
	 * ȡ���ڵ�ǰ�����н��е����и��ģ� ���ͷŴ� Connection ����ǰ���е��������ݿ�����
	 * �˷���ֻӦ�����ѽ����Զ��ύģʽʱʹ�á�
	 * */
	public void rollback() {
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��дDBSource�ӿڵ�setAutoCommit()����
	 * �������ӵ��Զ��ύģʽ����Ϊ����״̬��
	 * ������Ӵ����Զ��ύģʽ�£����������� SQL ��佫��ִ�в���Ϊ���������ύ��
	 * �������� SQL ��佫�ۼ��������У�ֱ������ commit ������ rollback ����Ϊֹ��
	 * Ĭ������£������Ӵ����Զ��ύģʽ��
	 * 
	 * @param autoCommit  true ��ʾ�����Զ��ύģʽ��Ϊ false ��ʾ�����Զ��ύģʽ.
	 * */
	public void setAutoCommit(boolean autoCommit) {
		try {
			conn.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��дDBSource�ӿڵ�colseConnection()����
	 * �ر�Connection����.����ͬ��.
	 */
	public synchronized void closeConnection() {
		try {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("���ݿ�" + (num--) + "�ر�...");
	}

	/**
	 * ��дDBSource�ӿڵ�getPrearedStatement()����
	 * ��ȡPrepearedStatement.
	 * @param SQLstr
	 *            Ԥ�����sqlָ��
	 * */
	public PreparedStatement getPreparedStatement(String SQLstr)
			throws SQLException {
		return conn.prepareStatement(SQLstr);
	}

	/**
	 * �������ӹ���Connection��Ŀ
	 */
	public int getNum() {
		return n;
	}

	public String toString() {
		return "���ݿ����ӵ� #" + n;
	}

	/**
	 * ��дDBSource�ӿڵ�getStatement()����
	 * ��ȡStatement.
	 * @return Statement ��̬Statement
	 * */
	public Statement getStatement() throws SQLException {
		if (stmt == null)
			stmt = conn.createStatement();
		return stmt;
	}

	/**
	 * ��дDBSource�ӿڵ�releaseConnection()����
	 * Connectionʹ����Ϻ������ݿ����ӳع黹Connection
	 * */
	public void releaseConnection() {
		SQLPool.returnSQLServer(this);
		System.out.println("�ͷ�:" + getNum());
	}
}
