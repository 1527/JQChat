
package com.jq.util;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/** ��ʾ���� */
public class SoundPlayer {
	
	public static final String ADDTION  = "resources/Sound/addtion.wav";
	public static final String MSG  = "resources/Sound/msg.wav";
	public static final String ONLINE  = "resources/Sound/Online.wav";
	
	public static void play(String filePath) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(
											//new File(ClassLoader.getSystemResource(filePath).getFile()));// �����Ƶ������
					new File(SoundPlayer.class.getClassLoader().getResource(filePath).getPath()));
			
			
			AudioFormat aif = ais.getFormat();// ָ�����������ض����ݰ���
			DataLine.Info info = new DataLine.Info(SourceDataLine.class,aif);
			SourceDataLine sdl = (SourceDataLine) AudioSystem.getLine(info);
			// �ӻ�Ƶ�����Դ������
			sdl.open(aif);// �򿪾���ָ����ʽ���У�������ʹ�л�����������ϵͳ��Դ����ÿɲ�����
			sdl.start();// ����������ִ������ I/O
			
			int BUFFER_SIZE = 4000 * 4;
			int nByte = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			while (nByte != -1) {
				nByte = ais.read(buffer, 0, BUFFER_SIZE);// ����Ƶ����ȡָ������������������ֽڣ����������������ֽ������С�
				if (nByte >= 0) {
					 sdl.write(buffer, 0, nByte);// ͨ����Դ�����н���Ƶ����д���Ƶ����
					//System.out.println(sdl.write(buffer, 0, nByte));
				}
			}
			sdl.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
