/*
 * $Id: DrawUtil.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.util;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

public class DrawUtil {

	/**
	 * @param ct
	 * @param baseColor
	 * @param hiliteColor
	 * @param px
	 * @param py
	 * @param w
	 * @param h
	 */
	public static void gradientFillRect1(Graphics2D ct,
			Color baseColor, Color hiliteColor, 
			int px, int py, int w, int h) {
		int h2 = h / 4;
		int py2 = py + h2;
		ct.setPaint(new GradientPaint(px, py, baseColor, px, py2-1, hiliteColor));
		ct.fillRect(px, py, w, h2);
		ct.setPaint(new GradientPaint(px, py2, hiliteColor, px, py+h-1, baseColor));
		ct.fillRect(px, py2, w, h-h2);
	}

	/**
	 * @param ct
	 * @param upperColor
	 * @param lowerColor
	 * @param px
	 * @param py
	 * @param w
	 * @param h
	 */
	public static void gradientFillRect2(Graphics2D ct,
			Color upperColor, Color lowerColor, 
			int px, int py, int w, int h) {
		int h2 = h / 4;
		int h3 = h / 4;
		int py2 = py + h2;
		int py3 = py2 + h3;
		ct.setColor(upperColor);
		ct.fillRect(px, py, w, h2);
		ct.setPaint(new GradientPaint(px, py2, upperColor, px, py3-1, lowerColor));
		ct.fillRect(px, py2, w, h3);
		ct.setColor(lowerColor);
		ct.fillRect(px, py3, w, h-h2-h3);
	}

	public static void drawDimple(Graphics2D ct, int x, int y, int w, int h) {
		ct.setColor(new Color(0x333333));
		ct.drawLine(x, y, x+w, y);
		ct.setColor(Color.GRAY);
		ct.drawLine(x, y, x, y+h);
		ct.drawLine(x+w, y, x+w, y+h);
		ct.setColor(new Color(0xcccccc));
		ct.drawLine(x, y+h, x+w, y+h);
	}

	public static void drawBulge(Graphics2D ct, int x, int y, int w, int h) {
		ct.setColor(new Color(0xcccccc));
		ct.drawLine(x, y, x+w, y);
		ct.setColor(Color.GRAY);
		ct.drawLine(x, y, x, y+h);
		ct.drawLine(x+w, y, x+w, y+h);
		ct.setColor(new Color(0x333333));
		ct.drawLine(x, y+h, x+w, y+h);
	}

	
}
