package com.jq.server.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.jq.server.sql.DBSource;

/**
 * 	����ӿ�,�ṩ���ַ���������.
 * @param msg ���յ�����Ϣ.
 * @param clientInputStream ����������
 * @param clientOutputStream ���������
 * */
public interface Service {
	public void service(DBSource db,ObjectInputStream clientInputStream,ObjectOutputStream clientOutputStream);
}
