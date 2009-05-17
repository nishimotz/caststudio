/*
 * $Id: Porter.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.mmm.porter;

//TODO: データ読み込み機能を CastStudio からここ(or PorterData)に移動
//TODO: layoutMediaItem ページ切り替え or 絞り込み機能？タブ切り替え？

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.nishimotz.mmm.CastStudio;
import com.nishimotz.mmm.mediaitem.MediaItem;
import com.nishimotz.mmm.sheet.AbstractSheet;

public class Porter extends AbstractSheet {

	public Porter() {
		super();
		view = new PorterView(); 
	}

	@Override
	public void onClick() {
		
	}

	// TODO: ゴミ箱の場合は配置の step を詰める
	// TODO: はみ出したら??
	// 現状では最終位置にぜんぶ置く
	public void doLayoutMediaItemsAtFirst(List<MediaItem> mediaItems) {
		int startX = view.getPosX();
		int startY = view.getPosY();
		int stepX = 30;
		int stepY = 55;
		int width  = view.getWidth();
		int height = view.getHeight();
		int marginY = 10;
		int marginStepY = 10;
		int x = startX + 10;
		int y = startY + marginY;
		boolean overflow = false;
	   	List<MediaItem> syncMediaItems = Collections.synchronizedList(mediaItems);
	   	synchronized (syncMediaItems) {
			for (MediaItem item : syncMediaItems) {
				if (item.getPosX() == -1 && item.getPosY() == -1) {
					item.setPos(x, y);
					if (!overflow) {
						y += stepY;
						if (y + item.getHeight() > height) {
							x += stepX;
							y = startY + marginY + marginStepY;
							marginStepY += 10;
							if (startX + width < x + item.getWidth()) {
								overflow = true;
							}
						}
					}
				}
			}
	   	}
	}

	public void doLayoutMediaItems(List<MediaItem> mediaItems) {
		int startX = view.getPosX();
		int startY = view.getPosY();
		int stepX = 30;
		int stepY = 55;
		int width  = view.getWidth();
		int height = view.getHeight();
		int marginY = 10;
		int marginStepY = 10;
		int x = startX + 10;
		int y = startY + marginY;
		boolean overflow = false;
	   	List<MediaItem> syncMediaItems = Collections.synchronizedList(mediaItems);
	   	synchronized (syncMediaItems) {
			for (MediaItem item : syncMediaItems) {
				item.setPos(x, y);
				if (!overflow) {
					y += stepY;
					if (y + item.getHeight() > height) {
						x += stepX;
						y = startY + marginY + marginStepY;
						marginStepY += 10;
						if (startX + width < x + item.getWidth()) {
							overflow = true;
						}
					}
				}
			}
	   	}
	}
	
	public void doLayoutStickerItems(List<MediaItem> mediaItems) {
		int startX = view.getPosX();
		int startY = view.getPosY();
		// int width  = view.getWidth();
		int height = view.getHeight();
		int marginY = 0;
		int x = startX + 5;
		int y = startY + marginY;
	   	List<MediaItem> syncMediaItems = Collections.synchronizedList(mediaItems);
	   	synchronized (syncMediaItems) {
			for (MediaItem item : syncMediaItems) {
				if (item.isPosUndef()) {
					item.setPos(x, y);
					y += item.getHeight();
					if (y > height) {
						y = startY + marginY;
						x += item.getWidth();
					}
				}
			}
	   	}
	}

	public void unloadMediaItems(ArrayList<MediaItem> mediaItems) {
	   	List<MediaItem> syncMediaItems = Collections.synchronizedList(mediaItems);
	   	synchronized (syncMediaItems) {
			for (MediaItem item : syncMediaItems) {
				item.unload();
			}
	   	}
	}

}
