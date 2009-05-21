/*
 * $Id: CastStudioView.java,v 1.2 2009/05/18 01:30:28 nishi Exp $
 */
package com.nishimotz.mmm;

// TODO: manage popup menu event using queue

import java.awt.Cursor;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CastStudioView {

	private static final int frameWidth  = 1000;
	private static final int frameHeight = 720;

	private CastStudio castStudio;
	private CastStudioFrame frame;
	private PopupMenu popupMenu;
	
	public static int getFrameWidth() {
		return frameWidth;
	}
	
	public static int getFrameHeight() {
		return frameHeight;
	}
	
	public CastStudioView(CastStudio cs) {
		castStudio = cs;
		frame = new CastStudioFrame(castStudio);
		frame.setSize(frameWidth, frameHeight);
	}

	public void setPorterRegion() {
		int startX = 680;
		int startY =  60;
		int width  = 300;
		int height = 630;
		castStudio.getPorter().setRegion(startX, startY, width, height);
	}
	
	public void setRecyclerRegion() {
		int startX = 350;
		int startY = 610;
		int width  = 300;
		int height = 100;
		castStudio.getRecycler().setRegion(startX, startY, width, height);
	}
	
	public void setStickerHolderRegion() {
		int startX = 350;
		int startY =  50;
		int width  = 300;
		int height = 140;
		castStudio.getStickerHolder().setRegion(startX, startY, width, height);
	}

	public void setInspectorRegion() {
		int startX = 350;
		int startY = 200;
		int width  = 300;
		int height = 150;
		castStudio.getInspector().setRegion(startX, startY, width, height);
		castStudio.getInspector().setPropertyViewRegion(330, 40, 640, 150);
	}
	
	public void setCueSheetRegion() {
		castStudio.getCueSheet(0).setRegion(20,  60, 300, 300);
		castStudio.getCueSheet(1).setRegion(20, 380, 300, 300);
	}

	public int getWidth() {
		return frame.getWidth();
	}

	public int getHeight() {
		return frame.getHeight();
	}

	public void setTitle(String frameTitle) {
		frame.setTitle(frameTitle);
	}

	public void initFrame() {
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
		
		// popup menu
		popupMenu = new PopupMenu("CastStudio"); // this string is not displayed
		
		MenuItem saveColorPopItem = popupMenu.add(new MenuItem("Save Info"));
		saveColorPopItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setWaitCursor();
				castStudio.saveItemInfo();
				setDefaultCursor();
			}
		});

		MenuItem exitPopItem = popupMenu.add(new MenuItem("Exit"));
		exitPopItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(1);
			}
		});

		frame.add(popupMenu);
	}

	public void setupEventListener() {
		CastStudioEventListener eh = new CastStudioEventListener(castStudio);
		frame.addMouseListener(eh);
		frame.addMouseMotionListener(eh);
		frame.addMouseWheelListener(eh);
	}

	public void repaintFrame() {
		frame.repaint();
	}

	public void showPopupMenu(int x, int y) {
		popupMenu.show(frame, x, y);
	}

	public void setWaitCursor() {
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}

	public void setDefaultCursor() {
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
	}

	public void setRepaintByRedraw(boolean b) {
		frame.setRepaintByRedraw(b);
	}

}
