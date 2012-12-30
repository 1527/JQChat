
package com.jq.client.protocol.udp;

import com.jq.util.SoundPlayer;

/**
 * 	������Ϣ����.
 * */
public class NomalMsg extends AbstractMessage{

	/**������Ϣ���캯��*/
	public NomalMsg(String msg) {
		super(msg);
	}

	/**
	 * 	��Ϣ��ʽ--
	 * 		[ID_��Ϣ��]
	 * 	��Ϣ��--
	 * 		[11-27 00:00:00 xxxxxxxxxxxxxxxxxxxxxxxx]
	 * 		����                     �����ʽ        ������Ϣ
	 * */
	@Override
	public void update(UDPServer UDP) {
		SoundPlayer.play(SoundPlayer.MSG);
		UDP.receiveMsg(ID, msgBody);
	}
}
