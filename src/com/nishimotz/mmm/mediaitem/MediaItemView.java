/*
 * $Id: MediaItemView.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.mmm.mediaitem;

//TODO: 再生位置をプログレス表示
//TODO: 描画をスケーラブル化 -> width を決めると他の値が決まる SizeManager?

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.nishimotz.mmm.CastStudio;
import com.nishimotz.util.DrawUtil;
import com.nishimotz.util.Messages;
import com.nishimotz.util.StringUtil;

public class MediaItemView implements IMediaItemView {
	
	private int width = 140;
	private int height = 50;
	private int posX = CastStudio.NOT_A_POS;
	private int posY = CastStudio.NOT_A_POS;
	
	// MediaItem 左上から range 左上へ
	private int rangeOffsetX = 10;
	private int rangeOffsetY = 6; // 17
	private int durationOffsetY = 45;
	
	private int rangeHeight = 26;
	private int rangeWidth = width - 20;
	
	// range 左上からの offset
	private int rangeLeftOffset = 0;
	private int rangeRightOffset = 0;

	// draw
	private final int itemShadowDepth = 3;
	
	private final Color notReadyRangeFillColor  = new Color(0x888888);
	private final Color focusedShadowColor  = new Color(0xff4444);
	private final Color castingRangeFillColor  = new Color(0xff55ff);

	private final Color [] defaultFillColors = {
			new Color(0xcccccc), // white
			new Color(0xeeee22), // yellow
			new Color(0x009900), // green
			new Color(0x66ffff), // light blue
			new Color(0xff1111)  // red
			};
	private final Color [] notReadyFillColors = {
			new Color(0x888888), // white
			new Color(0xaaaa11), // yellow
			new Color(0x004400), // green
			new Color(0x339999), // right blue
			new Color(0xaa1111)  // red
			};
	
	private final Color newItemNotReadyFillColor = new Color(0xffe4b5);

	private final Color loadFailedFillColor   = new Color(0x111111);
	
	private final Color defaultRangeFillColor = new Color(0x99bffe);
	private final Color defaultRangeFillColor2 = new Color(0x69a5fd);
	private final Color defaultRangeBackColor = new Color(0xceceb4);
	
	private final int fontSize = 12;
	private final int titleFontSize = 10;

	private final Color itemAuthorColor = new Color(0x000000);
	private final Font itemAuthorFont
		= new Font(Messages.getString("ItemFont"), Font.BOLD, fontSize);

	private final Color itemTitleColor = new Color(0x222222);
	private final Font itemTitleFont
		= new Font(Messages.getString("ItemFont"), Font.PLAIN, titleFontSize);
	
	private final Color itemInfoColor = new Color(0x444444);
	private final Font itemInfoFont
		= new Font(Messages.getString("ItemFont"), Font.PLAIN, fontSize);
	
	
	// どこにフォーカスしているか
	// どこをドラッグしているか
	// private enum FOCUS {NONE, INNER, RIGHT, LEFT}
	private FOCUS draggingMode = FOCUS.NONE;
	
	// クリックする前の hover 状態
	private FOCUS hoverMode = FOCUS.NONE;
	
	public MediaItemView() {
		assert defaultFillColors.length  == MediaItemData.itemLabelCount;
		assert notReadyFillColors.length == MediaItemData.itemLabelCount;
		// レンジ指定の初期値
		rangeLeftOffset = 0;
		rangeRightOffset = rangeWidth;
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

	public int getHeight() {
		return height;
	}

	public synchronized void setPos(int x, int y) {
		this.posX = x;
		this.posY = y;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public synchronized void drag(MediaItem mediaItem, int diffX, int diffY) {
		if (draggingMode == FOCUS.INNER) {
			int newX = getPosX() + diffX;
			int newY = getPosY() + diffY;
			setPos(newX, newY);
		}
	}
	
	private FOCUS getDraggingMode(int x, int y) {
    	// LEFT および RIGHT でないことを判定した上で実行
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
		rangeLeftOffset = (int)((double) rangeWidth * d);
	}

	public synchronized void setRegionEndRatio(double d) {
		rangeRightOffset = (int)((double) rangeWidth * d);
	}


	public synchronized void stopDragging() {
		draggingMode = FOCUS.NONE;
	}


	public synchronized void draw(MediaItem mediaItem, Graphics g) {
		Graphics2D ct = (Graphics2D) g;
		ct.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
		
		// shadow
		if (getHoverMode() == FOCUS.INNER) {
			ct.setStroke(new BasicStroke(2.0f));
			ct.setColor(focusedShadowColor);
			ct.drawRect(posX - itemShadowDepth, 
					posY - itemShadowDepth, 
					width + itemShadowDepth * 2, 
					height + itemShadowDepth * 2);
			ct.setStroke(new BasicStroke());
		}
		
		// rect
		if (mediaItem.isLoadFailed()){
			ct.setColor(loadFailedFillColor);
		} else if (mediaItem.isNotReady()){
			if (mediaItem.isNewItem()){
				ct.setColor(newItemNotReadyFillColor);
				ct.fillRect(posX, posY, width, height);
			} else {
				int label = mediaItem.getItemLabel();
				ct.setColor(notReadyFillColors[label]);
				ct.fillRect(posX, posY, width, height);
			}
		} else {
			int label = mediaItem.getItemLabel();
			// ct.setColor(defaultFillColors[label]);
			Color baseColor = defaultFillColors[label];
			Color hiliteColor = new Color(0xf0f0f0);
			DrawUtil.gradientFillRect1(ct,baseColor,hiliteColor,posX,posY,width,height);
		}
		
		DrawUtil.drawBulge(ct,posX,posY,width,height);
		
		// handle
		
		// 範囲指定ボックス
		int hx1 = getPosX() + rangeOffsetX;
		int hy1 = getPosY() + rangeOffsetY;
		
		// 範囲指定ボックス
		int hx2 = hx1 + rangeLeftOffset;
		int w = rangeRightOffset - rangeLeftOffset;
		if (mediaItem.isNotReady()) {
			ct.setColor(notReadyRangeFillColor);
			ct.drawRect(hx1, hy1, rangeWidth, rangeHeight);
			ct.fillRect(hx2 + 1, hy1 + 1, w - 1, rangeHeight - 1);
		} else if (mediaItem.isCasting()) {
			ct.setColor(castingRangeFillColor);
			ct.drawRect(hx1, hy1, rangeWidth, rangeHeight);
			ct.fillRect(hx2 + 1, hy1 + 1, w - 1, rangeHeight - 1);
		} else {
			ct.setColor(defaultRangeBackColor); // Color.WHITE;
			ct.fillRect(hx1, hy1, rangeWidth, rangeHeight);
//			ct.fillRect(hx2 + 1, hy1 + 1, w - 1, rangeHeight - 1);
			Color upperColor = defaultRangeFillColor;
			Color lowerColor = defaultRangeFillColor2;
			DrawUtil.gradientFillRect2(ct,upperColor,lowerColor,hx2+1,hy1+1,w-1,rangeHeight-2);
		}
		// shadow
		DrawUtil.drawDimple(ct,hx1,hy1,rangeWidth,rangeHeight);
		
		// caption

		String txt3 = mediaItem.getAuthor();
		ct.setFont(itemAuthorFont);
		ct.setColor(itemAuthorColor);
		ct.drawString(txt3, getPosX() + 14, hy1 + 12 ); 
		
		String txt2 = mediaItem.getTitle();	    		
		ct.setFont(itemTitleFont);
		ct.setColor(itemTitleColor);
		ct.drawString(txt2, getPosX() + 14, hy1 + 24 ); 

		double mediaDuration = mediaItem.getMediaDuration();
		String txt = StringUtil.formatTime(mediaDuration)
			+ " [" + mediaItem.getGainAsDB() + "dB]"; 
		ct.setFont(itemInfoFont);
		ct.setColor(itemInfoColor);
		ct.drawString(txt,  getPosX() + 14, getPosY() + durationOffsetY);

		ct.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		
	}

}