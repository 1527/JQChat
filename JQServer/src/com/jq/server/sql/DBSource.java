package com.jq.server.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/** DBSource�ӿ�,���ڹ淶���Connection�ķ���. ���Ի��,�ر�ȡ�õ�Connection����.*/
public interface DBSource {
	/**
	 * �������ӵ��Զ��ύģʽ����Ϊ����״̬��
	 * @param autoCommit : true ��ʾ�����Զ��ύģʽ�� false ��ʾ�����Զ��ύģʽ.
	 * */
	public void setAutoCommit(boolean autoCommit);
	
	/**
	 * 	ʹ������һ���ύ/�ع�����еĸ��ĳ�Ϊ�־ø��ģ�
	 *  ���ͷŴ� Connection ����ǰ���е��������ݿ�����
	 *  �˷���ֻӦ�����ѽ����Զ��ύģʽʱʹ�á�
	 * */
	public void commit();
	
	/**
	 *  ȡ���ڵ�ǰ�����н��е����и��ģ�
	 *  ���ͷŴ� Connection ����ǰ���е��������ݿ�����
	 *  �˷���ֻӦ�����ѽ����Զ��ύģʽʱʹ�á�
	 * */
	public void rollback();
	
	/**
	 * 	��ȡPrepearedStatement.
	 * @param SQLstr Ԥ�����sqlָ��
	 * */
	public PreparedStatement getPreparedStatement(String SQLstr) throws SQLException;
	
	/**
	 * 	��ȡStatement.
	 *  ��̬Statement
	 * */
	public Statement getStatement() throws SQLException;

	/**
	 * �ر�Connection����,ʵ��ͬ������
	 */
	public void closeConnection() throws SQLException;
	
	/**
	 * 	Connectionʹ����Ϻ������ݿ����ӳع黹Connection
	 * */
	public void releaseConnection();
}