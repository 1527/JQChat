
package com.jq.client.protocol.tcp;

/**
 * 	����ӿ�,�ṩ���ַ���������.
 * @param msg ����������͵���Ϣ.
 * */
public interface Service<T,U> {
	public T service(U msg);
}