/*
 * $Id: WaveView.java,v 1.2 2009/05/18 01:30:28 nishi Exp $
 */
package com.nishimotz.mmm.inspector;

//TODO: ColorManager の導入？
//TODO: 波形描画のメソッド独立
//TODO: 始終端の拡大、トリミング機能

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.util.ArrayList;

import com.nishimotz.mmm.mediaitem.MediaItem;
import com.nishimotz.mmm.sheet.AbstractSheet;
import com.nishimotz.mmm.sheet.AbstractSheetView;
import com.nishimotz.util.DrawUtil;

public class WaveView extends AbstractSheetView {
	
	private final int waveAreaOffsetX = 10;
	private final int waveAreaOffsetY = 20;
	private final int waveAreaWidthDiff = 20;
	private final int waveAreaHeightDiff = 30;
	private final int infoFontSize = 14;

//	private final Color shadowColor    = new Color(0x666666);
	private final Color backColor      = new Color(0xcccccc);
	private final Color waveShapeColor = new Color(0x0000ff);
	private final Color selectionColor = new Color(0xffffff);
	private final Color currPosColor   = new Color(0xff3333);
	private final Color infoFontColor  = new Color(0x000000);

	private final Font infoFont = new Font("Serif", Font.PLAIN, infoFontSize);

	public WaveView() {	
		super();
	}
	
    public void drawSheet(AbstractSheet sheet, Graphics ct) {
		Graphics2D ct2 = (Graphics2D) ct;
		ct2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));

		Inspector inspector = (Inspector)sheet;
    	int px = posX;
    	int py = posY;
    	int w = width;
    	int h = height;
    	
//    	int shadowStep = 3;
//    	ct.setColor(shadowColor);
//		ct.fillRect(px + shadowStep, py + shadowStep, w, h);
		ct.setColor(backColor);
		ct.fillRect(px, py, w, h);
		DrawUtil.drawBulge((Graphics2D)ct,px,py,w,h);
		
		MediaItem mi = inspector.getMediaItem();
		double maxTime = mi.getMediaMaxTime();
		double startTime = mi.getMediaStartTime();
		double stopTime = mi.getMediaStopTime();
		double currTime = mi.getMediaTime();
		
		int shapeDataCount = mi.getShapeDataCount();
        
		String msg = String.format(
				"%3.2fsec / %3.2f-%3.2f %3.2fsec (%d)", 
				maxTime,
				startTime,
				stopTime,
				currTime,
				shapeDataCount
		);
		ct.setFont(infoFont);
		ct.setColor(infoFontColor);
		int cx = 10;
		int cy = infoFontSize + 4;
        ct.drawString(msg, posX + cx , posY + cy);

        // 波形を描く
        int wavePosX = posX + waveAreaOffsetX;
        int wavePosY = posY + waveAreaOffsetY;
        int waveWidth = width - waveAreaWidthDiff;
        int waveHeight = height - waveAreaHeightDiff;
		
		// 秒からピクセル位置への変換 pixelPos = sec * scaleX2
		double scaleX2 = (double) waveWidth / maxTime;

		// 範囲指定位置
		int startPosX = (int)(startTime * scaleX2);
		int selectionWidth = (int)((stopTime - startTime) * scaleX2);
        ct.setColor(selectionColor);
		ct.fillRect(wavePosX + startPosX, wavePosY, selectionWidth, waveHeight);
		DrawUtil.drawDimple((Graphics2D)ct,wavePosX + startPosX, wavePosY, selectionWidth, waveHeight);
		
		// 現在再生中位置のマーカー
		int currPosX = (int)(currTime * scaleX2);
        ct.setColor(currPosColor);
		ct.fillRect(wavePosX + currPosX, wavePosY, 2, waveHeight);

		// Y軸の座標
		int baseY = wavePosY + (waveHeight/2);

        ct.setColor(waveShapeColor);
		ct.drawLine(wavePosX, baseY, wavePosX + waveWidth, baseY);
		
		// 値からピクセル位置への変換
		double scaleY = (double) waveHeight / 65536.0;
	
		// 微妙にピクセルが重なることがあるので透明度１にしないと汚くなる
		ct2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		if (waveWidth <= shapeDataCount) {
			double scaleX = (double) shapeDataCount / waveWidth;
			// 描画ピクセルに合わせてループを回す
			for (int i = 0; i < waveWidth; i++) {
				int samplePos1 = (int)(scaleX * i);
				int samplePos2 = (int)(scaleX * (i+1));
				short sampleMin = 0;
				short sampleMax = 0;
				for (int j = samplePos1; j < samplePos2; j++) {
					short s1 = mi.getShapeDataMin(j);
					short s2 = mi.getShapeDataMax(j);
					if (s1 < sampleMin) {
						sampleMin = s1;
					} 
					if (sampleMax < s2) {
						sampleMax = s2;
					}
				}
				int plotX1 = wavePosX + i;
				int plotY1 = baseY - (int)((double)sampleMin * scaleY);
				int plotY2 = baseY - (int)((double)sampleMax * scaleY);
				ct.drawLine(plotX1, plotY1, plotX1, plotY2);
			}
		} else {
			double scaleX = (double) waveWidth / shapeDataCount;
			// データに合わせてループを回す
			int prevX = wavePosX;
			for (int i = 0; i < shapeDataCount; i++) {
				short sampleMin = mi.getShapeDataMin(i);
				short sampleMax = mi.getShapeDataMax(i);
				int plotX1 = wavePosX + (int)(i * scaleX);
				int pw = plotX1 - prevX + 1;
				int plotY1 = baseY - (int)(scaleY * sampleMax);
				int ph = (int)(scaleY * (sampleMax - sampleMin));
				prevX = plotX1;
				ct.fillRect(plotX1, plotY1, pw, ph);
			}
		}
    }

	public boolean isInsideWaveArea(int x, int y) {
        int wavePosX = posX + waveAreaOffsetX;
        int wavePosY = posY + waveAreaOffsetY;
        int waveWidth = width - waveAreaWidthDiff;
        int waveHeight = height - waveAreaHeightDiff;
        int wavePosX2 = wavePosX + waveWidth;
        int wavePosY2 = wavePosY + waveHeight;
        if (wavePosX <= x && x < wavePosX2 
        		&& wavePosY <= y && y < wavePosY2) {
        	return true;
        }
		return false;
	}

	public double getWaveAreaTime(Inspector inspector, int x) {
        int wavePosX = posX + waveAreaOffsetX;
        int waveWidth = width - waveAreaWidthDiff;
        int pixel = x - wavePosX;
		MediaItem mi = inspector.getMediaItem();
		double maxTime = mi.getMediaMaxTime();
		double time = maxTime * pixel / waveWidth;
		if (time < 0.0) time = 0.0;
		if (time > maxTime) time = maxTime;
        return time;
	}
}
