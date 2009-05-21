/*
 * $Id: MouseWheelMovedEvent.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.mmm.event;

import java.awt.event.MouseWheelEvent;


public class MouseWheelMovedEvent extends CastStudioEvent {
	public MouseWheelMovedEvent(MouseWheelEvent arg0) {
		setMouseWheelEvent(arg0);
	}
}
