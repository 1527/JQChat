package com.jq.client.gui.mainframe;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.jq.client.gui.chat.IconPanel;
import com.jq.util.MyResources;

/**�û�ͷ�����ô���*/
public class FacePanel  extends JPanel{

	private static final long serialVersionUID = 4809125106388845206L;
	private static final int COM = 5;
	private static final int ROW = 4;
	
	private MouseAdapter mouseEvent = null;
	private static Icon lastIcon = null;
	/**ȫ�����촰�ڹ���ͬһ���������*/
	private static IconPanel facePanel = new IconPanel(MyResources.USERFACE, ROW,COM);
	private InfoFrame		owner = null;
	protected JLabel closeButton;
	
	public FacePanel(JFrame parent) {
		super();
		owner = (InfoFrame)parent;
		closeButton = new JLabel();
		
		this.setBackground(new Color(220, 239, 255));
		this.setBorder(new LineBorder(new Color(153, 204, 255), 1, true));
		this.setLayout(null);
		
		closeButton.setIcon(MyResources.getImageIcon(MyResources.ICON
				+ "close.gif"));
		closeButton.setToolTipText("�ر�");
		closeButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				close();
			}
		});

		this.add(closeButton);
		closeButton.setBounds(247, 5, 16, 22);
		
		//ʵ������ǰ������ӵ�е��������¼�.
		mouseEvent = new MouseAdapter() {
						public void mouseClicked(java.awt.event.MouseEvent e) {
							Icon checkedFace = ((JLabel) e.getSource()).getIcon();
			
							lastIcon = MyResources.getImageIcon(MyResources.USERFACE + 
											   new File(((ImageIcon) checkedFace).getDescription()).getName());
							
							owner.face.setIcon(lastIcon);
							
							setVisible(false);
						}
					};
					
		facePanel.setMouseEvent(mouseEvent);
		add(facePanel);
		
		facePanel.setSize(233, 175);
		setSize(267, 195);
	}

	/**��ȡѡ���ͷ��*/
	public Icon getFace() {
		return lastIcon;
	}

	/** ��ʾͷ�� */
	public void showFace() {
		if (!isVisible()){			
			//setLocation(owner.getX() + 50, owner.getY() + 50);
			setVisible(true);
		}
	}
	
	/**�ر�*/
	public void close(){
		if(isVisible()){
			setVisible(false);
		}
	}
	
}
