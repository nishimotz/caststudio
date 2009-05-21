/*
 * 作成日: 2005/12/24
 */
package com.nishimotz.mmm;

// TODO: offerEvent シグニチャを持つ Offerable Interfaceを定義し、
// CastStudio を Offerable に置き換えれば一般的なメカニズムになる？

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.nishimotz.mmm.event.CastStudioEvent;
import com.nishimotz.mmm.event.MouseDraggedEvent;
import com.nishimotz.mmm.event.MouseMovedEvent;
import com.nishimotz.mmm.event.MousePressedEvent;
import com.nishimotz.mmm.event.MouseReleasedEvent;
import com.nishimotz.mmm.event.MouseWheelMovedEvent;

class CastStudioEventListener implements MouseListener, MouseMotionListener, MouseWheelListener {
		
	private final CastStudio castStudio;

	/**
	 * @param studio
	 */
	CastStudioEventListener(CastStudio studio) {
		castStudio = studio;
	}

	private void offerEvent(CastStudioEvent ev) {
		castStudio.offerEvent(ev);
	}

	public void mouseClicked(MouseEvent arg0) {
		// do nothing
	}

//	@SuppressWarnings("unchecked")
	public void mousePressed(MouseEvent arg0) {
		CastStudioEvent ev = new MousePressedEvent(arg0);
		offerEvent(ev);
	}

//	@SuppressWarnings("unchecked")
	public void mouseReleased(MouseEvent arg0) {
		CastStudioEvent ev = new MouseReleasedEvent(arg0);
		offerEvent(ev);
	}

	public void mouseEntered(MouseEvent arg0) {
		// do nothing
	}

	public void mouseExited(MouseEvent arg0) {
		// do nothing
	}

//	@SuppressWarnings("unchecked")
	public void mouseDragged(MouseEvent arg0) {
		CastStudioEvent ev = new MouseDraggedEvent(arg0);
		offerEvent(ev);
	}

//	@SuppressWarnings("unchecked")
	public void mouseMoved(MouseEvent arg0) {
		CastStudioEvent ev = new MouseMovedEvent(arg0);
		offerEvent(ev);
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		CastStudioEvent ev = new MouseWheelMovedEvent(e);
		offerEvent(ev);
	}
	
}
