package com.jq.client.protocol.udp;

import javax.swing.JOptionPane;

/** �ܾ������ļ���Ϣ */
public class RejectMsg extends AbstractMessage {

	public RejectMsg(String msg) {
		super(msg);
	}

	/** ��дupdate����*/
	@Override
	public void update(UDPServer UDP) {
		JOptionPane.showConfirmDialog(null, "�û�[" + ID + "]�ܾ������ļ�[" + msgBody
				+ "]", "�ܾ������ļ�", JOptionPane.WARNING_MESSAGE);
	}

}
