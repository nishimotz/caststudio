/*
 * $Id: AbstractSheetView.java,v 1.2 2009/05/18 01:30:28 nishi Exp $
 */
package com.nishimotz.mmm.sheet;

import java.awt.Color;
import java.awt.Graphics;

import com.nishimotz.mmm.IPointableView;
import com.nishimotz.mmm.mediaitem.MediaItem;

public abstract class AbstractSheetView implements IPointableView {
	protected int posX = 20;
	protected int posY = 60;
	protected int width = 300;
	protected int height = 600;
	protected final Color focusedShadowColor  = new Color(0xff4444);
	protected final int itemShadowDepth = 3;

	protected FOCUS hoverMode = FOCUS.NONE;

	public int getHeight() {
		return height;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public int getWidth() {
		return width;
	}

	public void setRegion(int x, int y, int w, int h) {
		posX = x;
		posY = y;
		width = w;
		height = h;
	}

	// クリックされたある点が CueSheet の中にあるか？
	public boolean isInside(int x, int y) {
    	int cx1 = posX;
    	int cy1 = posY;
    	int cx2 = cx1 + width;
    	int cy2 = cy1 + height;
    	
    	if (cx1 <= x && x <= cx2 && cy1 <= y && y <= cy2) 
    		return true;
    	return false;
	}
	
	// MediaItem が CueSheet の中にあるか？
	public boolean isInside(MediaItem mi) {
    	int cx1 = posX;
    	int cy1 = posY;
    	int cx2 = cx1 + width;
    	int cy2 = cy1 + height;
    	
    	int mx1 = mi.getPosX();
    	int my1 = mi.getPosY();
    	int mx2 = mx1 + mi.getWidth();
    	int my2 = my1 + mi.getHeight();
    	
    	if (cx1 <= mx1 && mx2 <= cx2 && cy1 <= my1 && my2 <= cy2) 
    		return true;
    	return false;
	}

	// MediaItem が CueSheet と少しでも重なっているか？
	public boolean isOverlapped(MediaItem mi) {
    	int mx1 = mi.getPosX();
    	int my1 = mi.getPosY();
    	int mx2 = mx1 + mi.getWidth();
    	int my2 = my1 + mi.getHeight();
    	boolean topLeft  = isInside(mx1, my1);
    	boolean topRight = isInside(mx2, my1);
    	boolean btmLeft  = isInside(mx1, my2);
    	boolean btmRight = isInside(mx2, my2);
    	if (topLeft || topRight || btmLeft || btmRight) {
    		return true;
    	}
    	return false;
	}

	public void drawSheet(AbstractSheet sheet, Graphics ct) {
		// to override
	}

	public FOCUS getHoverMode() {
		return hoverMode;
	}

	public void resetHoverMode() {
    	this.hoverMode = FOCUS.NONE;
	}

	public void setHoverMode(int x, int y) {
    	int x1 = getPosX();
		int x2 = x1 + width;
		int y1 = getPosY();
		int y2 = y1 + height;
		if (x1 <= x && x <= x2 && y1 <= y && y <= y2) {
			this.hoverMode = FOCUS.INNER;
		} else {
			this.hoverMode = FOCUS.NONE;
		}
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setPos(int x, int y) {
		this.posX = x;
		this.posY = y;
	}

}
