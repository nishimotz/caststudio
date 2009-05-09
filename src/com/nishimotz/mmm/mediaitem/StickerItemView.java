/*
 * $Id: StickerItemView.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.mmm.mediaitem;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.nishimotz.util.DrawUtil;
import com.nishimotz.util.Messages;

public class StickerItemView implements IMediaItemView {
	
	private int width = 125;
	private int height = 30;
	private int posX = 0;
	private int posY = 0;
	
	// draw
	private int itemShadowDepth = 4;
	private int fontSize = 12;
	
	private final Color focusedShadowColor  = new Color(0xff4444);

	private final Color castingRangeFillColor  = new Color(0xff55ff);
	
	private final Color notReadyStickerFillColor   = new Color(0x444444);
	private final Color originalStickerFillColor1   = new Color(0xccccff);
	private final Color originalStickerFillColor2   = new Color(0x9999cc);
	private final Color duplicatedStickerFillColor = new Color(0xffffff);
	
	private final Color infoFontColor2 = new Color(0x000000);
	private final Font itemFont
		= new Font(Messages.getString("ItemFont"), Font.BOLD, fontSize);
	
	// �ǂ��Ƀt�H�[�J�X���Ă��邩
	// �ǂ����h���b�O���Ă��邩
	private FOCUS draggingMode = FOCUS.NONE;
	
	// �N���b�N����O�� hover ���
	private FOCUS hoverMode = FOCUS.NONE;
	
	public StickerItemView() {
	}
	
    public FOCUS getHoverMode() {
		return hoverMode;
	}


	public synchronized void checkDraggingMode(int x, int y) {
		draggingMode = getDraggingMode(x, y);		
	}

	public synchronized void resetHoverMode() {
    	this.hoverMode = FOCUS.NONE;
	}

	public synchronized void setHoverMode(int x, int y) {
		this.hoverMode = getDraggingMode(x, y);
	}

	public int getWidth() {
		return width;
	}

	public synchronized void setWidth(int newWidth) {
		this.width = newWidth;
	}
	
	public int getHeight() {
		return height;
	}

	public synchronized void setHeight(int height) {
		this.height = height;
	}

	public void setPos(int x, int y) {
		this.posX = x;
		this.posY = y;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	// MediaItemView �Ɠ���
	public synchronized void drag(MediaItem mediaItem, int diffX, int diffY) {
		if (draggingMode == FOCUS.INNER) {
			int newX = getPosX() + diffX;
			int newY = getPosY() + diffY;
			setPos(newX, newY);
		}
	}
	
	private FOCUS getDraggingMode(int x, int y) {
    	// LEFT ����� RIGHT �łȂ����Ƃ𔻒肵����Ŏ��s
    	int x1 = getPosX();
		int x2 = x1 + width;
		int y1 = getPosY();
		int y2 = y1 + height;
		if (x1 <= x && x <= x2 && y1 <= y && y <= y2) {
			return FOCUS.INNER;
		}
		return FOCUS.NONE;
	}

	public synchronized void setRegionBeginRatio(double d) {
	}

	public void setRegionEndRatio(double d) {
	}

	public boolean isDraggingRegion() {
		return false;
	}

	public synchronized void stopDragging() {
		draggingMode = FOCUS.NONE;
	}

	public synchronized void draw(MediaItem mediaItem, Graphics g) {
		Graphics2D ct = (Graphics2D) g;
		ct.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
		
		// shadow
		if (getHoverMode() == FOCUS.INNER) {
			ct.setColor(focusedShadowColor);
			ct.setStroke(new BasicStroke(2.0f));
			ct.drawRect(posX - itemShadowDepth, 
					posY - itemShadowDepth, 
					width + itemShadowDepth * 2, 
					height + itemShadowDepth * 2);
			ct.setStroke(new BasicStroke());
		}
		
		// rect
		if (mediaItem.isNotReady()){
			ct.setColor(notReadyStickerFillColor);
			ct.fillRect(posX, posY, width, height);
		} else if (mediaItem.isDuplicated()){
			ct.setColor(duplicatedStickerFillColor);
			ct.fillRect(posX, posY, width, height);
		} else if (mediaItem.isCasting()) {
			ct.setColor(castingRangeFillColor);
			ct.fillRect(posX, posY, width, height);
		} else {
			Color upperColor = originalStickerFillColor1;
			Color lowerColor = originalStickerFillColor2;
			DrawUtil.gradientFillRect2(ct,upperColor,lowerColor,posX,posY,width,height);
		}

		DrawUtil.drawBulge(ct,posX,posY,width,height);
		
		String txt2 = mediaItem.getTitle();	    		
		ct.setFont(itemFont);
		ct.setColor(infoFontColor2);
		ct.drawString(txt2, getPosX() + 6, getPosY() + fontSize + 6);
		
		ct.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		
	}

}