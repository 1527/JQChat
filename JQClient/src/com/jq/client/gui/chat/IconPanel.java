package com.jq.client.gui.chat;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.jq.util.MyResources;
import com.jq.util.Param;

/**
 * 	��ʾicon���.
 * 	Ŀ��:
 * 		ʹiconPanelΪstatic,�������촰�ڹ���ͬһ��iconPanel,����ÿһ�����촰�ڶ�ʵ
 * 		����һ��iconPanel,����������Դ����.
 * */
public class IconPanel extends JPanel {

	private static final long serialVersionUID = 2887772324964311057L;

	private Icon[] icons = null;
	private JLabel[] iconLabel = null;
	// ��¼��һ�ε�����¼�.
	private MouseAdapter lastEvent = null;

	public IconPanel(String iconPath, int ROW, int COM) {
		setBorder(new LineBorder(new Color(153,
				204, 255), 1, true));
		setBackground(new Color(220, 239, 255));
		setBounds(10, 10, 345, 215);

		setLayout(new GridLayout(ROW, COM, 0, 0));
		initIcon(iconPath);
	}

	/** ��ʼ��icon��Դ */
	private void initIcon(String iconPath) {
		//����Ǳ���ͼ��
		if (iconPath.contains("action"))
			icons = actionIcons(iconPath);
		else
			icons = faceIcons(iconPath);

		iconLabel = new JLabel[icons.length];

		int i = 0;
		for (Icon f : icons) {
			// ʵ��icon�����label
			iconLabel[i] = new JLabel(f);
			// ��label��ӵ�Panel��
			add(iconLabel[i++]);
		}
	}

	/* ��ȡ���� */
	private Icon[] actionIcons(String iconPath) {
		Icon[] icons = new Icon[90];

		String k;
		for (int i = 0; i < 90; i++) {
			if (i < 10)
				k = "0" + i + Param.GIF;
			else
				k = i + Param.GIF;

			icons[i] = MyResources.getImageIcon(iconPath + k);
		}

		return icons;
	}

	/* ��ȡͷ�� */
	private Icon[] faceIcons(String iconPath) {
		Icon[] icons = new Icon[20];

		for (int i = 0; i < 20; i++)
			icons[i] = MyResources.getImageIcon(iconPath + (i + 1) + Param.GIF);

		return icons;
	}

	/**
	 * Ϊ��icon�������������¼�.
	 * 
	 * @param evt
	 *            Ҫ����������¼�
	 * */
	public void setMouseEvent(MouseAdapter evt) {
		// ������´��ڵ�����¼�����,������ӵ�label�����¼�������.
		if (lastEvent != evt) {
			// �Ƴ���һ���������¼�
			removeMouseEvent(evt);

			lastEvent = evt;
			for (JLabel l : iconLabel)
				if (l != null)
					l.addMouseListener(evt);
		}
	}

	/**
	 * Ϊ��icon�������Ƴ�����¼�.
	 * 
	 * @param evt
	 *            Ҫ����������¼�
	 * */
	private void removeMouseEvent(MouseAdapter evt) {
		// ����˴�����һ�εĴ�����ͬ�򲻶�����¼������Ƴ�����.
		if (lastEvent != null)
			for (JLabel l : iconLabel)
				l.removeMouseListener(lastEvent);
	}

}
