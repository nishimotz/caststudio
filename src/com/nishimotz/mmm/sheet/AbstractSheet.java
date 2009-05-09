/*
 * $Id: AbstractSheet.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.mmm.sheet;

import java.awt.Graphics;
import java.util.logging.Logger;

import com.nishimotz.mmm.CastStudio;
import com.nishimotz.mmm.mediaitem.MediaItem;

public abstract class AbstractSheet {
	
	protected AbstractSheetView view;
	protected Logger logger = CastStudio.logger;

	protected AbstractSheet() {
		
	}
	
	public void setRegion(int x, int y, int w, int h) {
		view.setRegion(x, y, w, h);
	}
	
	public void draw(Graphics ct) {
		view.drawSheet(this, ct);
	}
	
	public boolean isInside(int x, int y) {
		return view.isInside(x, y);
	}

	public boolean isOverlapped(MediaItem mi) {
		return view.isOverlapped(mi);
	}

	public boolean isInside(MediaItem mi) {
		return view.isInside(mi);
	}

	// 中途半端にシートに引っかかっている
	public boolean isHalfMast(MediaItem mi) {
		return isOverlapped(mi) && !isInside(mi);
	}

	public void onClick() {
		// to override
	}
}
