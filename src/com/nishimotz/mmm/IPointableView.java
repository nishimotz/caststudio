/*
 * $Id: IPointableView.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.mmm;

public interface IPointableView {

	public enum FOCUS {NONE, INNER}

	public abstract FOCUS getHoverMode();
	public abstract void resetHoverMode();
	public abstract void setHoverMode(int x, int y);
	public abstract int getWidth();
	// public abstract void setWidth(int width);
	public abstract int getHeight();
	// public abstract void setHeight(int height);
	public abstract void setPos(int x, int y);
	public abstract int getPosX();
	public abstract int getPosY();

}