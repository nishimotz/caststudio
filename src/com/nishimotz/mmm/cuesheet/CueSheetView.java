/*
 * $Id: CueSheetView.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.mmm.cuesheet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.nishimotz.mmm.sheet.AbstractSheet;
import com.nishimotz.mmm.sheet.AbstractSheetView;
import com.nishimotz.util.DrawUtil;
import com.nishimotz.util.Messages;

public class CueSheetView extends AbstractSheetView {

	private final int clockFontSize = 32;
	private final int clockFontSize2 = 24;
	private final int clockOffsetX = 10;
	private final int clockOffsetY = clockFontSize + 4;

	private final Color emptyColor      = new Color(0x747883);
	private final Color preparedColor   = new Color(0xb2b8c4);
	private final Color preparedColor2  = new Color(0x9298a4);
	private final Color playingColor    = new Color(0x66ff66);
	private final Color remainFontColor = new Color(0xff1111);
	private final Color clockBackColor  = new Color(0x99ffffff, true);
	private final Color clockBackColor2  = new Color(0x99cccccc, true);
	
	// posX, posY, width, height ‚Í AbstractSheetView ‚ðŒp³
	public CueSheetView() {
		super();
		posX = 20;
		posY = 60;
		width = 300;
		height = 600;
	}

	public int getClockPosX() {
		return this.posX + clockOffsetX;
	}
	
	public int getClockPosY() {
		return this.posY + clockOffsetY;
	}

	public void drawClock1(Graphics ct, String msg) {
		ct.setFont(new Font(Messages.getString("DrawClockFont"), 
				Font.BOLD, clockFontSize));
		ct.setColor(remainFontColor);
		int cx = getClockPosX();
		int cy = getClockPosY();
        ct.drawString(msg, cx , cy);
    }
    
	public void drawClock2(Graphics ct, String msg) {
		ct.setFont(new Font(Messages.getString("DrawClockFont"), 
				Font.PLAIN, clockFontSize2));
		ct.setColor(Color.BLUE);
		int cx = getClockPosX() + 90;
		int cy = getClockPosY();
        ct.drawString(msg, cx , cy);
    }
    
	public void drawSheet(AbstractSheet sheet, Graphics ct) {
		CueSheet cueSheet = (CueSheet)sheet;
    	int px = posX;
    	int py = posY;
    	int w = width;
    	int h = height;
		if (getHoverMode() == FOCUS.INNER && cueSheet.getTotalDuration() > 0.0) {
			ct.setColor(focusedShadowColor);
			ct.drawRect(posX - itemShadowDepth, 
					posY - itemShadowDepth, 
					width + itemShadowDepth * 2, 
					height + itemShadowDepth * 2);
		}
    	if (cueSheet.getTotalDuration() == 0.0) {
    		ct.setColor(emptyColor);
    		ct.fillRect(px, py, w, h);
    	} else if (cueSheet.isCueSheetPlaying()) {
    		ct.setColor(playingColor);
    		ct.fillRect(px, py, w, h);
    	} else {
//    		ct.setColor(preparedColor);
//    		ct.fillRect(px, py, w, h);
    		Color upperColor = preparedColor2;
    		Color lowerColor = preparedColor;
    		DrawUtil.gradientFillRect1((Graphics2D)ct,upperColor,lowerColor,px,py,w,h);
    	}
		DrawUtil.drawBulge((Graphics2D)ct,px,py,w,h);
		
		int x2 = getClockPosX() - 5;
		int y2 = getClockPosY() - 27;
		int w2 = 240;
		int h2 = 30;
//		ct.setColor(clockBackColor);
//		ct.fillRect(x2,y2,w2,h2);
		Color upperColor = clockBackColor;
		Color lowerColor = clockBackColor2;
		DrawUtil.gradientFillRect2((Graphics2D)ct,upperColor,lowerColor,x2,y2,w2,h2);
		DrawUtil.drawDimple((Graphics2D)ct,x2,y2,w2,h2);
		
		drawClock1(ct, cueSheet.getClockMessage1());
		drawClock2(ct, cueSheet.getClockMessage2());
    }

}
