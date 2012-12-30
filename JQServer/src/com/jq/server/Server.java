package com.jq.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.UIManager;

import com.jq.server.service.SQLServerProcess;
import com.jq.util.Param;
import com.jq.util.PropertyFile;

public class Server {
	public static void main(String[] args){
		//JFrame.setDefaultLookAndFeelDecorated( true ); 
		try {
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("΢���ź�", Font.PLAIN, 12));
		} catch (Exception e) {
			e.printStackTrace();
		}
		new SQLServerProcess(checkPropertyFile());
	}
	
	/**�鿴���ݿ������ļ��Ƿ����*/
	private static PropertyFile checkPropertyFile(){
		File file = new File(Param.CURRENTPATH + Param.FILENAME);
		File dir = new File(Param.CURRENTPATH);
		//����������򴴽�һ��Ĭ�ϵ�
		if (!file.exists()) {
			dir.mkdirs();	//����Ŀ¼
			try {
				file.createNewFile();	//�����ļ�
				//PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
				FileWriter out = new FileWriter(file);
				out.write(Param.PROPERTYFILE);	//��Ĭ�ϵ�����д��ȥ
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return new PropertyFile(Param.CURRENTPATH + Param.FILENAME);
	}
}
