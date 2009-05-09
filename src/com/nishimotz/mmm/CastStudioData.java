/*
 * $Id: CastStudioData.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.mmm;

//CastStudioData が CueSheet のインスタンスを保持する。
//CueSheet が CueSheetData や CueSheetView のインスタンスを保持する。
//Porter, MediaItem も同様。

//mediaItems の並びはz-orderの下から上の順番である

//TODO: 複数の cueSheet インスタンス
//TODO: porter が独自に mediaItems のインスタンスを持つ？
//TODO: porter に load の機能を持たせる？


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.nishimotz.mmm.cuesheet.CueSheet;
import com.nishimotz.mmm.inspector.Inspector;
import com.nishimotz.mmm.mediaitem.MediaItem;
import com.nishimotz.mmm.porter.Porter;

public class CastStudioData {
	private CueSheet[] cueSheets = new CueSheet[2];
	
	private Inspector inspector;
	private ArrayList<MediaItem> mediaItems;
   	private List<MediaItem> syncMediaItemList;
	private Porter porter;
	private Porter recycler;
	private Porter stickerHolder;
	
	public CastStudioData() {
//		cueSheets[0] = new CueSheet();
//		cueSheets[1] = new CueSheet();
		for (int i = 0; i < cueSheets.length; i++) {
			cueSheets[i] = new CueSheet();
		}
		porter = new Porter();
		recycler = new Porter();
		stickerHolder = new Porter();
		inspector = new Inspector();
		mediaItems = new ArrayList<MediaItem>();
	   	syncMediaItemList = Collections.synchronizedList(mediaItems);
//		cueSheets[0].setRecycler(recycler);
//		cueSheets[1].setRecycler(recycler);
//		cueSheets[0].setStickerHolder(stickerHolder);
//		cueSheets[1].setStickerHolder(stickerHolder);
		for (CueSheet cs : cueSheets) {
			cs.setRecycler(recycler);
			cs.setStickerHolder(stickerHolder);
		}
	}

	public CueSheet getCueSheet(int n) {
		return cueSheets[n];
	}
	
	public int sizeCueSheet() {
		return cueSheets.length;
	}

	public List<MediaItem> getMediaItems() {
		return syncMediaItemList;
	}

	public Porter getPorter() {
		return porter;
	}

	public Porter getRecycler() {
		return recycler;
	}

	public Porter getStickerHolder() {
		return stickerHolder;
	}

	public Inspector getInspector() {
		return inspector;
	}

	public void doLayoutPorterAtFirst(List<MediaItem> items) {
		ArrayList<MediaItem> ms = new ArrayList<MediaItem>();
		ArrayList<MediaItem> st = new ArrayList<MediaItem>();
		synchronized (items) {
			for (MediaItem mi : items) {
				if (mi.isSticker()) {
					st.add(mi);
				} else {
					ms.add(mi);
				}
			}
		}
		porter.doLayoutMediaItemsAtFirst(ms);
		stickerHolder.doLayoutStickerItems(st);
	}

	public void doLayoutPorterAtFirst() {
		doLayoutPorterAtFirst(getMediaItems());
	}

	public void changeOrderToLast(MediaItem mi) {
		mediaItems.remove(mi);
		mediaItems.add(mi);
	}

	// 一番上は一番最後なので最後からチェックする
	public MediaItem findItemFromPosition(int x, int y) {
		List<MediaItem> items = getMediaItems();
		if (items == null) return null;
		int size = items.size();
		for (int i = size -1; i >= 0; i--) {
			MediaItem mi = items.get(i);
	    	int tx1 = mi.getPosX();
	    	int tx2 = tx1 + mi.getWidth();
	    	int ty1 = mi.getPosY();
	    	int ty2 = ty1 + mi.getHeight();
	    	if (tx1<=x && x<=tx2 && ty1<=y && y<=ty2) {
	    		return mi;
	    	}
		}
		return null;
	}

	public void clearMediaItems() {
		List<MediaItem> items = getMediaItems();
		if (items == null) return;
		synchronized (items) {
			for (MediaItem mi : items) {
				mi.unload();
				mi = null;
			}
		}
	}

	public void saveItemsInfo(String uid) {
		List<MediaItem> items = getMediaItems();
		if (items == null) return;
		synchronized (items) {
			for (MediaItem mi : items) {
				mi.saveItemInfo(uid);
			}
		}
		
	}

}
