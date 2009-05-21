/*
 * $Id: InspectorView.java,v 1.2 2009/05/18 01:30:28 nishi Exp $
 */
package com.nishimotz.mmm.inspector;

//TODO: ColorManager の導入？

import java.awt.Color;
import java.awt.Graphics2D;
//import java.awt.Font;
import java.awt.Graphics;

import com.nishimotz.mmm.sheet.AbstractSheet;
import com.nishimotz.mmm.sheet.AbstractSheetView;
import com.nishimotz.util.DrawUtil;

public class InspectorView extends AbstractSheetView {
	
	private final Color emptyColor           = new Color(0x666699);
	private final Color itemEnteringColor    = new Color(0x9999ff);
	private final Color preparingColor       = new Color(0xff3333);
	private final Color preparingToPlayColor = new Color(0x99cc33);
	private final Color preparedColor        = new Color(0xcccccc);
	private final Color preparedColor2       = new Color(0xeeeeee);
	private final Color playStartingColor    = new Color(0x33aa33);
	private final Color playingColor         = new Color(0x33cc33);
	private final Color stoppingColor        = new Color(0x999999);
	
	public void drawSheet(AbstractSheet sheet, Graphics ct) {
		Inspector inspector = (Inspector)sheet;
    	int px = posX;
    	int py = posY;
    	int w = width;
    	int h = height;
    	if (inspector.isPreparing()) {
    		ct.setColor(preparingColor);
    		ct.fillRect(px, py, w, h);
    	} else if (inspector.isItemEntering()) {
    		ct.setColor(itemEnteringColor);
    		ct.fillRect(px, py, w, h);
    	} else if (inspector.isPreparingToPlay()) {
    		ct.setColor(preparingToPlayColor);
    		ct.fillRect(px, py, w, h);
    	} else if (inspector.isPrepared()) {
//    		ct.setColor(preparedColor);
//    		ct.fillRect(px, py, w, h);
    		if (getHoverMode() == FOCUS.INNER) {
    			ct.setColor(focusedShadowColor);
    			ct.drawRect(posX - itemShadowDepth, 
    					posY - itemShadowDepth, 
    					width + itemShadowDepth * 2, 
    					height + itemShadowDepth * 2);
    		}
    		DrawUtil.gradientFillRect1((Graphics2D)ct,preparedColor,preparedColor2,px,py,w,h);
    	} else if (inspector.isPlaying()) {
    		if (getHoverMode() == FOCUS.INNER) {
    			ct.setColor(focusedShadowColor);
    			ct.drawRect(posX - itemShadowDepth, 
    					posY - itemShadowDepth, 
    					width + itemShadowDepth * 2, 
    					height + itemShadowDepth * 2);
    		}
    		ct.setColor(playingColor);
    		ct.fillRect(px, py, w, h);
    	} else if (inspector.isPlayStarting()) {
    		ct.setColor(playStartingColor);
    		ct.fillRect(px, py, w, h);
    	} else if (inspector.isStopping()) {
    		ct.setColor(stoppingColor);
    		ct.fillRect(px, py, w, h);
    	} else {
    		ct.setColor(emptyColor);
    	}
		DrawUtil.drawBulge((Graphics2D)ct,px,py,w,h);
		
    }
}
