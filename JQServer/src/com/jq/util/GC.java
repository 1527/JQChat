package com.jq.util;

/**
 * 	����������.
 *  �ػ��߳�.
 * 	ÿ30����ִ��һ��,����JVMִ����������.
 * */
public class GC extends Thread{
	
	public GC(){
		setDaemon(true);	//���ô˽���Ϊ�ػ��߳�
		setPriority(NORM_PRIORITY);	//�������ȼ�
		start();
	}
	
	public void run(){
		while(true){
			try {
				Thread.sleep(1800000);	//60*30=1800s=1800000ms
				System.gc();	//����ϵͳ����������gc
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
