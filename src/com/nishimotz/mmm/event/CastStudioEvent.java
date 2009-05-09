/*
 * $Id: CastStudioEvent.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.mmm.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class CastStudioEvent {
	private MouseEvent mouseEvent;
	private MouseWheelEvent mouseWheelEvent;

	public MouseEvent getMouseEvent() {
		return mouseEvent;
	}

	public void setMouseEvent(MouseEvent mouseEvent) {
		this.mouseEvent = mouseEvent;
	}

	public MouseWheelEvent getMouseWheelEvent() {
		return mouseWheelEvent;
	}

	public void setMouseWheelEvent(MouseWheelEvent mouseEvent) {
		this.mouseWheelEvent = mouseEvent;
	
	}
}
