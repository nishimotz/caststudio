/*
 * $Id: IMediaItemView.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.mmm.mediaitem;

import java.awt.Graphics;

import com.nishimotz.mmm.IPointableView;

public interface IMediaItemView extends IPointableView {

	public abstract void setRegionBeginRatio(double d);
	public abstract void setRegionEndRatio(double d);
	public abstract void draw(MediaItem mediaItem, Graphics g);
	public abstract void checkDraggingMode(int x, int y);
	public abstract void drag(MediaItem mediaItem, int diffX, int diffY);
	public abstract void stopDragging();
}