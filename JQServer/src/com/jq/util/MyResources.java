package com.jq.util;

import java.awt.Image;
import javax.swing.ImageIcon;

/** ͼ����Դ */
public class MyResources {
	
	public static final String LOADICON = "resources/skin/loading/";
	public static final String ICON = "resources/skin/icon/";
	public static final String CHAT = "resources/skin/chat/";
	public static final String ACTION = "resources/action/";
	public static final String MAINFRAME = "resources/skin/mainframe/";
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

}
