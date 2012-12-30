package com.jq.client.gui.common;

import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;

import com.jq.client.gui.chat.ChatFrame;
import com.jq.util.FileManager;
import com.jq.util.MsgPages;
import com.jq.util.MyResources;
import com.jq.util.Param;
/**
 * ������ʾ����ѵ������¼. 
 * ���Բ�����������.
 * �鿴ָ�����ڵ������¼. 
 * ��ʾ���÷�ҳ��ʾ. 
 * Ҳ��ɾ�������¼.
 * 
 * */
public class MsgManagerPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 649094468583966794L;

	private JComboBox dateComboBox;
	private JLabel deleteLabel, firstPage, prePage, nextPage, lastPage;
	private JTextArea showTextPane;
	private JTextField searchTxt;
	private JScrollPane jsp;
	private JLabel pageNumLabel,title;
	private JButton closeButton,searchButton;

	private ChatFrame jf = null;
	private MsgPages pages = null;
	private FileManager fm = null;


	public MsgManagerPanel(ChatFrame jf, FileManager fm) {
		this.jf = jf;
		this.fm = fm;
		this.pages = new MsgPages(fm);

		initComponents();
	}

	/** ��ʼ������ */
	private void initComponents() {
		searchButton = new JButton("����");
		searchTxt = new JTextField();
		jsp = new JScrollPane();
		showTextPane = new JTextArea();
		dateComboBox = new JComboBox();
		prePage = new JLabel();
		firstPage = new JLabel();
		lastPage = new JLabel();
		nextPage = new JLabel();
		pageNumLabel = new JLabel();
		deleteLabel = new JLabel();
		closeButton = new JButton("�ر�");
		title = new JLabel("���������¼");

		setPreferredSize(new Dimension(380, 390));
		setLayout(null);
		
		title.setBounds(10 , 10, 150, 30);
		//����
		searchTxt.setBounds(205, 10, 100, 30);	
		searchTxt.addActionListener(this);
		searchTxt.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent evt) {
				searchTxtKey(evt);
			}
		});
		searchButton.setBounds(310, 10, 80, 30);
		add(title);
		add(searchButton);
		add(searchTxt);
		
		//�����¼����
		showTextPane.setEditable(false);
		showLog(pages.getFristPage());
		showTextPane.setLineWrap(true);
		jsp.setViewportView(showTextPane);
		//jtp.addTab("���������¼", jsp);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);// ͬʱ�Ѻ���Ĺ�������ȥ
		jsp.setBounds(10, 50, 385, 370);
		add(jsp);
		
		// �������� 
		dateComboBox.setModel(new DefaultComboBoxModel(pages.getDates()));
		dateComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				dateComboBoxItemStateChanged(evt);
			}
		});

		add(dateComboBox);
		dateComboBox.setBounds(10, 430, 110, 24);
		
		//ɾ����ť
		deleteLabel.setIcon(MyResources
				.getImageIcon((MyResources.ICON + "delete.png"))); // NOI18N
		deleteLabel.setToolTipText("ɾ�������¼");
		deleteLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				deleteLabelMouseClicked(evt);
			}
		});
		add(deleteLabel);
		deleteLabel.setBounds(126, 425, 24, 24);

		//��һҳ
		firstPage.setHorizontalAlignment(SwingConstants.RIGHT);
		firstPage.setIcon(MyResources
				.getImageIcon((MyResources.ICON + "firstPage.png"))); // NOI18N
		firstPage.setToolTipText("��һҳ");
		firstPage.setIconTextGap(0);
		firstPage.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				firstPageMouseClicked(evt);
			}
		});
		add(firstPage);
		firstPage.setBounds(176, 430, 24, 24);
		
		//ǰһҳ
		prePage.setIcon(MyResources
				.getImageIcon((MyResources.ICON + "prePage.png"))); 
		prePage.setToolTipText("��һҳ");
		prePage.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				prePageMouseClicked(evt);
			}
		});
		add(prePage);
		prePage.setBounds(206, 430, 24, 24);
		
		//ҳ��
		pageNumLabel.setText(pages.getCurrentNum() + "/" + pages.getPageNum());
		add(pageNumLabel);
		pageNumLabel.setBounds(230, 430, 24, 24);

		//��һҳ
		nextPage.setIcon(MyResources
				.getImageIcon((MyResources.ICON + "nextPage.png"))); // NOI18N
		nextPage.setToolTipText("��һҳ");
		nextPage.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				nextPageMouseClicked(evt);
			}
		});
		add(nextPage);
		nextPage.setBounds(255, 430, 24, 24);

		//���һҳ
		lastPage.setIcon(MyResources
				.getImageIcon((MyResources.ICON + "lastPage.png"))); // NOI18N
		lastPage.setToolTipText("���һҳ");
		lastPage.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				lastPageMouseClicked(evt);
			}
		});
		add(lastPage);
		lastPage.setBounds(280, 430, 24, 24);

		//�رհ�ť
		closeButton.addActionListener(this);
		add(closeButton);
		closeButton.setBounds(330, 425, 60, 30);

		if (jf == null)
			closeButton.setVisible(false);

		setLayout(null);
		setBounds(0, 10, 400, 390);
	}

	/**
	 * ��ѯ����ʵ��.ͨ��Enter������
	 * ֻ�ܲ�ѯ��������ؼ��ֵ���һҳ�����ܸ�����
	 * */
	private void searchTxtKey(KeyEvent evt) {
		if (evt.getKeyChar() == KeyEvent.VK_ENTER
				&& searchTxt.getText().trim().length() != 0) {

			String searchString = searchTxt.getText().trim();
			String result = pages.search(searchString);

			if (result != null) {
				showLog(result);
			} else
				JOptionPane.showMessageDialog(this, "û������ƥ��!", "��ʾ",
						JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * ���ڸ�ʽ����ʾ
	 * */
	private void showLog(String msg) {
		showTextPane.setText("");
		if (msg == null)
			return;

		// ���зָ�
		String[] m = msg.split(Param.NEWLINE);
		for (String line : m) {
			// �Ƿ�Ϊ���ѷ�����Ϣʱ����ʾ. ��ʽ:xxx 11-22 09:47:41
			
			showTextPane.append(line+Param.NEWLINE);
		}

		m = null;
		pageNumLabel.setText(pages.getCurrentNum() + "/" + pages.getPageNum());
	}

	private void firstPageMouseClicked(MouseEvent evt) {
		showLog(pages.getFristPage());
	}

	private void prePageMouseClicked(MouseEvent evt) {
		showLog(pages.getPrePage());
	}

	private void nextPageMouseClicked(MouseEvent evt) {
		showLog(pages.getNextPage());
	}

	private void lastPageMouseClicked(MouseEvent evt) {
		showLog(pages.getLastPage());
	}

	private void deleteLabelMouseClicked(MouseEvent evt) {
		int i = JOptionPane.showConfirmDialog(this, "�Ƿ�ȷ��ɾ�������¼?", "����",
				JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (i == JOptionPane.NO_OPTION)
			return;

		dateComboBox.setModel(new DefaultComboBoxModel(new String[] { "" }));
		showTextPane.setText("");
		pages.delete();
		pageNumLabel.setText("0/1");
		// //////////////////////////
		fm.removeMsg();
	}

	private void dateComboBoxItemStateChanged(ItemEvent evt) {
		String date = (String) dateComboBox.getSelectedItem();
		showLog(pages.getPageByDate(date));
	}

	/**
	 * ���·�ҳ��ʾ
	 * 
	 * @param newFm
	 *            ���µ�FileManager����
	 * */
	public void setMsgPages(FileManager newFm) {
		fm = newFm;
		pages = new MsgPages(newFm);
		dateComboBox.setModel(new DefaultComboBoxModel(pages.getDates()));
		showLog(pages.getFristPage());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == closeButton) {
			if (jf != null)
				jf.normalStatus();
		}else if(e.getSource() == searchButton){
			if (searchTxt.getText().trim().length() != 0) {

				String searchString = searchTxt.getText().trim();
				String result = pages.search(searchString);

				if (result != null) {
					showLog(result);
				} else {
					JOptionPane.showMessageDialog(this, "û������ƥ��!", "��ʾ",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}

	}

}
