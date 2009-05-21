/*
 * $Id: PorterView.java,v 1.2 2009/05/18 01:30:28 nishi Exp $
 */
package com.nishimotz.mmm.porter;

//TODO: ColorManager の導入？

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.nishimotz.mmm.sheet.AbstractSheet;
import com.nishimotz.mmm.sheet.AbstractSheetView;
import com.nishimotz.util.DrawUtil;

public class PorterView extends AbstractSheetView {
	
	private Color porterColor = new Color(0x868e99);
	
	public void drawSheet(AbstractSheet sheet, Graphics ct) {
    	int px = posX;
    	int py = posY;
    	int w = width;
    	int h = height;
		ct.setColor(porterColor);
		ct.fillRect(px, py, w, h);
		DrawUtil.drawBulge((Graphics2D)ct,px,py,w,h);
    }
}
