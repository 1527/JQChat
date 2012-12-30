package com.jq.client.gui.chat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.jq.util.MyResources;
import com.jq.util.Param;

/**
 * TextPanel �Զ���TextPanel, �������������ı���ʾ��ʽ,��ʽ,��С. Ҳ����ͬʱ��ʾ�Զ����ı���ʽ��ͼƬ.
 * 
 */
public class JEditPane extends JTextPane {
	private static final long serialVersionUID = 1L;

	public StringBuilder sb = new StringBuilder(); // ���ڼ�¼�����ı��������,�����¼ʹ��.
	private MutableAttributeSet attrSet = null; // ���Լ����ַ����ĸ�ʽ,������,��С,�����ʽ��...
	private StyledDocument doc = null; // ��Ҫ!Document���...��ʹ��Document,��
	// ���ı���ʱ�����ͼƬʱ���׳���ָ���쳣
	// ��StyledDocument����
	private String fontName = Param.FONT; // ��ǰ��������,Ĭ��Ϊ"΢���ź�"
	private Color fontColor = Color.BLACK; // ��ǰ������ɫ,Ĭ��ΪBLACK
	private int fontType = 0; // ��ǰ������ʽ,Ĭ��Ϊ0�����壬б�壬�»���
	private int fontSize = 12; // ��ǰ�����С,Ĭ��Ϊ12
	private MyPopupMenu menu = null;

	private static final Pattern pattern = Pattern.compile(Param.GIF_REGEX,
			Pattern.MULTILINE | Pattern.DOTALL); // Pattern.DOTALLʹ.ƥ�������ַ�
	private static Matcher matcher = null;

	public JEditPane(boolean hasPopupMenu) {
		doc = this.getStyledDocument(); // ���JTextPane��Document
		attrSet = new SimpleAttributeSet(); // ����������ʽ
		setFontStyle(fontName, fontColor, fontType, fontSize); // ����Ĭ����ʽ

		if (hasPopupMenu) {
			menu = new MyPopupMenu(this);
			this.setComponentPopupMenu(menu.getPopupMenu());
		}
	}

	/**
	 * ʹ��AttributeSet��ʽ��β��׷���ַ���.
	 * 
	 * @param str
	 *            ׷�ӵ��ַ���.
	 */
	private void insertString(String str) {
		try {
			doc.insertString(doc.getLength(), str, attrSet);
			this.setCaretPosition(doc.getLength()); // �����ı����Զ�������Ļ�����µ�һ��
		} catch (BadLocationException e) {
			System.out.println("JEditPane.insertString()");
			e.printStackTrace();
		}
	}

	/**
	 * ���÷����߻����ߵ���Ϣ:�û�A 00:00:00
	 * 
	 * @param sender
	 *            �����ߵ����� �ͷ���ʱ�䣬���磺�û�A 00:00:00
	 * @param col
	 *            ������ʹ�õ�������ɫ.
	 * */
	public void insertUserString(String sender, Color col) {
		setFontStyle(Param.FONT, col, 0, 12);//������ʽĬ��Ϊ0�������档�����СΪ12
		insertString(sender + Param.NEWLINE);
		sb.append(sender.trim() + Param.NEWLINE);
	}

	/**
	 * ����Ŀר��,���ڲ������
	 * 
	 * @param icon
	 *            Ҫ����ı���.
	 * */
	public void insertActionFaceIcon(Icon icon) {
		setCaretPosition(doc.getLength()); // ���ò���ǰ���λ��
		insertIcon(icon); // ����ͼƬ

		setCaretPosition(doc.getLength()); // ���ò������λ��
	}

	/**
	 * ����ͼƬ,����ͼƬ����.ͼƬ��ʽΪjpg,gif,png......
	 * 
	 * @param image
	 *            Ϊ�����ͼƬ.
	 */
	public void insertImage(Icon image) {
		setCaretPosition(doc.getLength()); // ���ò���ǰ���λ��
		insertIcon(image); // ����ͼƬ
		insertString(Param.NEWLINE); // ���뻻��

		setCaretPosition(doc.getLength()); // ���ò������λ��
	}

	/** ��ȡJEditPane������,�������Ϊ#12.gif�ĸ�ʽ������ */
	private List<Element> getAllElements() {
		Element[] roots = this.getStyledDocument().getRootElements();
		return getAllElements(roots);
	}

	private List<Element> getAllElements(Element[] roots) {
		List<Element> icons = new LinkedList<Element>();
		for (int i = 0; i < roots.length; i++) {
			if (roots[i] != null){
				icons.add(roots[i]);
				for (int j = 0; j < roots[i].getElementCount(); j++) {
					Element element = roots[i].getElement(j);
					icons.addAll(getAllElements(new Element[] { element }));
				}
			}
		}
		return icons;
	}

	/**
	 * ȡ�������(JTextPane) �е���ϸ����. �����ı���ͼƬ,ͼƬת��Ϊ #01.gif �����ĸ�ʽ
	 * */
	public String getDetailText() {
		Map<Integer, String> map = new HashMap<Integer, String>();
		String text = this.getText();
		List<Element> els = getAllElements();
		for (Element el : els) {
			Icon icon = StyleConstants.getIcon(el.getAttributes());
			if (icon != null) {
				String temp = new File(((ImageIcon) icon).getDescription())
						.getName();
				map.put(el.getStartOffset(), temp);
			}
		}
		StringBuffer sb = new StringBuffer("");
		char[] ch = text.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			String s = map.get(new Integer(i));
			if (s == null)
				sb.append(ch[i]);
			else
				sb.append(s);
		}

		return sb.toString().trim();
	}

	/**
	 * �����յ�����Ϣ(�����ı�ͼƬ)����������뵽�ı���****************************
	 * */
	public void insertToTextPanel(String receiveMessage) {
		receiveMessage = receiveMessage.trim();
		// ��¼���յ����ı���Ϣ,���������¼�洢.
		sb.append(receiveMessage + Param.NEWLINE);

		// �鿴��Ϣ�Ƿ��������.
		matcher = pattern.matcher(receiveMessage);
		if (matcher.matches()) { // ��������
			String iconName = ""; // ����
			int index = 0;
			for (int i = 0; i < receiveMessage.length();) {
				index = receiveMessage.indexOf(Param.GIF, i) - 2;

				// �ж�ʣ�µ��ַ����Ƿ��������,���û����ʣ���(�����)�ַ������
				if (index < 0) {
					insertString(receiveMessage.substring(i,
							receiveMessage.length()));
					break;
				}

				// ��������.
				if (i != index) {
					insertString(receiveMessage.substring(i, index));
					// ��i������һ������
					i = index;
				}

				// ���±����6λ(00.gif)
				i = i + 6;
				// ��ȡ��������
				iconName = receiveMessage.substring(index, i);
				insertActionFaceIcon(MyResources
						.getImageIcon(MyResources.ACTION + iconName)); // �������
			}
			insertString(Param.NEWLINE);
		} else
			// ����������
			insertString(receiveMessage + Param.NEWLINE);
	}

	/**
	 * ���ò����ı��ĸ�ʽ����ʽ.
	 * �����������Լ�����attrset
	 * @param fontName
	 *            ��������.
	 * @param col
	 *            ������ɫ.
	 * @param type
	 *            ������ʽ: 0 Ϊ����. 1 Ϊ����. 2 Ϊб��. 3 Ϊ�»���. 4 Ϊ������»��� . 5Ϊ�����б��. 6
	 *            Ϊб����»���. 7Ϊ����,б����»���.
	 * @param fontSize
	 *            �����С.
	 */
	public void setFontStyle(String fontName, Color col, int type, int fontSize) {
		/*
		 * javax.swing.text.StyleConstants : һ����֪�� �򳣼������Լ��ͷ����ļ��ϣ���ͨ��Ӧ��
		 * AttributeSet �� MutableAttributeSet���������Ͱ�ȫ�ķ�ʽ��ȡ/�������ԡ�
		 */
		this.fontName = fontName;
		this.fontColor = col;
		this.fontType = type;
		this.fontSize = fontSize;

		try {
			StyleConstants.setFontFamily(attrSet, fontName); // ������������
		} catch (Exception e) {
			StyleConstants.setFontFamily(attrSet, Param.FONT); // ���޴�����ʱ
		}
		StyleConstants.setForeground(attrSet, col); // ����������ɫ
		StyleConstants.setFontSize(attrSet, fontSize); // ���������С

		switch (type) { // ����������ʽ
		case 0: // ����
			StyleConstants.setBold(attrSet, false);
			StyleConstants.setItalic(attrSet, false);
			StyleConstants.setUnderline(attrSet, false);
			break;
		case 1: // ����
			StyleConstants.setBold(attrSet, true);
			StyleConstants.setItalic(attrSet, false);
			StyleConstants.setUnderline(attrSet, false);
			break;
		case 2: // б��
			StyleConstants.setBold(attrSet, false);
			StyleConstants.setItalic(attrSet, true);
			StyleConstants.setUnderline(attrSet, false);
			break;
		case 3: // �»���
			StyleConstants.setBold(attrSet, false);
			StyleConstants.setItalic(attrSet, false);
			StyleConstants.setUnderline(attrSet, true);
			break;
		case 4: // ������»���
			StyleConstants.setBold(attrSet, true);
			StyleConstants.setItalic(attrSet, false);
			StyleConstants.setUnderline(attrSet, true);
			break;
		case 5: // �����б��
			StyleConstants.setBold(attrSet, true);
			StyleConstants.setItalic(attrSet, true);
			StyleConstants.setUnderline(attrSet, false);
			break;
		case 6: // б����»���
			StyleConstants.setBold(attrSet, false);
			StyleConstants.setItalic(attrSet, true);
			StyleConstants.setUnderline(attrSet, true);
			break;
		case 7: // ����,б����»���
			StyleConstants.setBold(attrSet, true);
			StyleConstants.setItalic(attrSet, true);
			StyleConstants.setUnderline(attrSet, true);
			break;
		}
	}

	/**
	 * ��õ�ǰ��������
	 */
	public String getFontName() {
		return fontName;
	}

	/**
	 * ��õ�ǰ������ɫ
	 */
	public Color getFontColor() {
		return fontColor;
	}

	/**
	 * ��õ�ǰ������ʽ
	 */
	public int getFontType() {
		return fontType;
	}

	/**
	 * ��õ�ǰ�����С
	 */
	public int getFontSize() {
		return fontSize;
	}

	/*** 
	 * ���õ����˵���
	 * ���С����ơ�ճ����ȫѡ������
	 */
	protected class MyPopupMenu {
		private JMenuItem cut, copy, paste, selectAll, clean;
		private JPopupMenu menu;
		private JEditPane textPane;

		public MyPopupMenu(JEditPane text) {
			cut = new JMenuItem();
			copy = new JMenuItem();
			paste = new JMenuItem();
			selectAll = new JMenuItem();
			clean = new JMenuItem();
			menu = new JPopupMenu();
			textPane = text;

			setPopupMenu();
		}

		/** ���õ����˵� */
		private void setPopupMenu() {
			cut.setText("����");
			cut.setAccelerator(KeyStroke
					.getKeyStroke('X', InputEvent.CTRL_MASK));
			cut.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					textPane.cut();
				}
			});
			menu.add(cut);
			
			copy.setText("����");
			copy.setAccelerator(KeyStroke.getKeyStroke('C',
					InputEvent.CTRL_MASK));
			copy.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					textPane.copy();
				}
			});
			menu.add(copy);

			paste.setText("ճ��");
			paste.setAccelerator(KeyStroke.getKeyStroke('V',
					InputEvent.CTRL_MASK));
			paste.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					textPane.paste();
				}
			});
			menu.add(paste);
			
			menu.add(new JSeparator());

			selectAll.setText("ȫѡ");
			selectAll.setAccelerator(KeyStroke.getKeyStroke('A',
					InputEvent.CTRL_MASK));
			selectAll.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					textPane.selectAll();
				}
			});
			menu.add(selectAll);

			clean.setText("����");
			clean.setAccelerator(KeyStroke.getKeyStroke('D',
					InputEvent.CTRL_MASK));
			clean.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					textPane.setText("");
				}
			});
			menu.add(clean);
		}

		public JPopupMenu getPopupMenu() {
			return menu;
		}
	}
}
