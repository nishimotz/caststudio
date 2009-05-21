/*
 * $Id: Region.java,v 1.2 2009/05/18 01:30:28 nishi Exp $
 */
package com.nishimotz.util;

// TODO: ちゃんと使う

public class Region {
	protected int posX = 20;
	protected int posY = 60;
	protected int width = 300;
	protected int height = 600;

	public Region(int x, int y, int w, int h) {
		posX = x;
		posY = y;
		width = w;
		height = h;
	} 
	
	public void setRegion(int x, int y, int w, int h) {
		posX = x;
		posY = y;
		width = w;
		height = h;
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
}
