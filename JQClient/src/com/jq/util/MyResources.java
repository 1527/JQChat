package com.jq.util;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

/** ͼ����Դ */
public class MyResources {
	
	public static final String ICON = "resources/icon/";
	public static final String ACTION = "resources/action/";
	public static final String USERFACE = "resources/userFace/";
	
	/**
	 * ����ָ��·����ȡImage.ʹ��class�������,����·������.
	 * 	@param path Ҫ��ȡImage��·��.
	 * */
	public static Image getImage(String path){
		return getImageIcon(path).getImage();
	}
	
	/**
	 * ����ָ��·����ȡImageIcon.ʹ��class�������,����·������.
	 * 	@param path Ҫ��ȡImageIcon��·��.
	 * */
	public static ImageIcon getImageIcon(String path){
		return new ImageIcon(MyResources.class.getClassLoader().getResource(path));
	}
	
	/**
	 * 	��ȡ��Ļ�е�X����
	 * */
	public static int getScreenCenterX(){
        /*�����ʾ���ߴ�*/
		return (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2);
	}
	
	
	/**
	 * 	��ȡ��Ļ�е�Y����
	 * */
	public static int getScreenCenterY(){
        /*�����ʾ���ߴ�*/
		return (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
	}
}
