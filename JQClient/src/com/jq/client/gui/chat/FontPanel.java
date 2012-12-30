package com.jq.client.gui.chat;

import java.awt.Color;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.LineBorder;

import com.jq.util.MyResources;

public class FontPanel extends JDialog{

	private static final long serialVersionUID = 4732092019204972340L;
	private JComboBox font,color,size;
	private JToggleButton bold,italic,underline;
	private JPanel background;
	private ChatFrame owner = null;
	private String[] fontNames;
	private static final Color[] FONTCOLOR = { Color.BLACK, Color.BLUE,
		Color.RED, Color.PINK, Color.GREEN };
	
	public FontPanel(JFrame parent) {
		super(parent);
		owner = (ChatFrame) parent;
		background = new JPanel();
		background.setLayout(null);
		//setSize(428, 30);
		background.setBackground(new Color(200, 239, 255));
		background.setBorder(new LineBorder(new Color(153, 204, 255),
				1, true));
		this.getRootPane().setWindowDecorationStyle(0);
		this.setLayout(null);
		
		//GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    //fontNames = ge.getAvailableFontFamilyNames();
		fontNames = new String[]{"Arial","Comic Sans MS","Times New Roman","����","����","΢���ź�","������","����","����"};
		font = new JComboBox();
		font.setModel(new DefaultComboBoxModel(fontNames));
		font.setSelectedIndex(5);//Ĭ����΢���ź�
		background.add(font);
		
		font.setBounds(8, 3, 80, 22);
		
		size = new JComboBox();
		size.setModel(new DefaultComboBoxModel(new String[] { "12", "14",
				"16", "18", "20" }));
		background.add(size);
		size.setBounds(99, 3, 55, 22);
		
		color = new JComboBox();
		color.setModel(new DefaultComboBoxModel(new String[] { "��ɫ", "��ɫ",
				"��ɫ", "��ɫ", "��ɫ" }));
		background.add(color);
		color.setBounds(165, 3, 65, 22);
		
		bold = new JToggleButton();
		bold.setIcon(MyResources.getImageIcon(MyResources.ICON
				+ "bold.gif"));
		background.add(bold);
		bold.setBounds(235, 1, 26, 26);
		
		italic = new JToggleButton();
		italic.setIcon(MyResources.getImageIcon(MyResources.ICON
				+ "italic.gif")); 
		background.add(italic);
		italic.setBounds(265, 1, 26, 26);
		
		underline = new JToggleButton();
		underline.setIcon(MyResources.getImageIcon(MyResources.ICON
				+ "underline.gif"));
		background.add(underline);
		underline.setBounds(295, 1, 26, 26);
		
		//background.setSize(426, 30);
		background.setBounds(0, 0, 426, 30);
		add(background);
		setSize(426,30);

	}

	/**
	 * ��ȡ��ǰ��������.
	 * 
	 * @return String ��ǰ��������.
	 * */
	public String getFontName() {
		return font.getSelectedItem().toString();
	}

	/**
	 * ��ȡ��i�����������.
	 * 
	 * @param i
	 *            Ҫ��ѯ�������±�.
	 * @return String ��i�����������.
	 * */
	public String getFontName(int i) {
		return font.getItemAt(i).toString();
	}


	/**
	 * ��ȡ��ǰ�����С
	 * 
	 * @return int ��ǰ����Ĵ�С.
	 * 
	 * */
	public int getFontSize() {
		int i = Integer.parseInt(size.getSelectedItem().toString().trim());
		return i;
	}


	/**
	 * ��ȡ��i�������С
	 * 
	 * @param i
	 *            Ҫ��ѯ�������С���±�.
	 * @return int ���ص������С.
	 * */
	public int getFontSize(int i) {
		return Integer.parseInt(size.getItemAt(i).toString().trim());
	}
	
	/**
	 * ��ȡ��ǰ������ɫ "��ɫ", "��ɫ", "��ɫ", "��ɫ", "��ɫ"
	 * 
	 * @return Color ��ǰ������ɫ.
	 * */
	public Color getFontColor() {
		return FONTCOLOR[color.getSelectedIndex()];
	}

	

	/**
	 * ��ȡ��ǰ������ɫ "��ɫ", "��ɫ", "��ɫ", "��ɫ", "��ɫ"
	 * 
	 * @param i
	 *            Ҫ��ѯ����ɫ�±�.
	 * @return Color ��i����ɫ.
	 * */
	public Color getFontColor(int i) {
		return FONTCOLOR[i];
	}

	/**
	 * ��ȡ������ϸ��ʽ�ַ���
	 * ��ʽΪ��2123
	 * */
	// String fontName, Color col, int type, int fontSize
	public String getType() {
		return "" + font.getSelectedIndex() + color.getSelectedIndex() + getFontType()
				+ size.getSelectedIndex();
	}

	/**
	 * ��ȡ��ǰ������ʽ. ������ʽ: 0 Ϊ����. 1 Ϊ����. 2 Ϊб��. 3 Ϊ�»���. 4 Ϊ������»��� . 5Ϊ�����б��. 6
	 * Ϊб����»���. 7Ϊ����,б����»���.
	 * 
	 * @return int �������ʽ.
	 * */
	public int getFontType() {
		if (!bold.isSelected() && !italic.isSelected()
				&& !underline.isSelected())
			return 0;
		else if (bold.isSelected() && !italic.isSelected()
				&& !underline.isSelected())
			return 1;
		else if (!bold.isSelected() && italic.isSelected()
				&& !underline.isSelected())
			return 2;
		else if (!bold.isSelected() && !italic.isSelected()
				&& underline.isSelected())
			return 3;
		else if (bold.isSelected() && !italic.isSelected()
				&& underline.isSelected())
			return 4;
		else if (bold.isSelected() && italic.isSelected()
				&& !underline.isSelected())
			return 5;
		else if (!bold.isSelected() && italic.isSelected()
				&& underline.isSelected())
			return 6;
		else if (bold.isSelected() && italic.isSelected()
				&& underline.isSelected())
			return 7;
		return 0;
	}
	
	/**
		 * �ر�������������¼�. �ر�ǰ�������õ��������ô����ı���,���Ĳ���ʾ����.
		 * */
	public void close() {
		this.setVisible(false);
		int fontType = Font.PLAIN;
		if(bold.isSelected()&&italic.isSelected()){
			fontType = Font.BOLD+Font.ITALIC;
		}else if(!bold.isSelected()&&italic.isSelected()){
			fontType = Font.ITALIC;
		}else if(bold.isSelected()&&!italic.isSelected()){
			fontType = Font.BOLD;
		}
		Font f = new Font(getFontName(), fontType, getFontSize());
		owner.sendEditText.setFont(f);
		owner.sendEditText.setForeground(getFontColor());
	}

	public void showFont() {
		this.setLocation(owner.getLocationOnScreen().x+13, owner.getLocationOnScreen().y+302);
		this.setVisible(true);
		
	}

}