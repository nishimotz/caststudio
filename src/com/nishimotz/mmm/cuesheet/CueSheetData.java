/*
 * $Id: CueSheetData.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.mmm.cuesheet;

// CueSheetData ÇÕ CastStudioData Ç∆ìØÇ∂ MediaItem ÇÃéQè∆Çï€éùÇ∑ÇÈ

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.nishimotz.mmm.mediaitem.MediaItem;

public class CueSheetData {

	private ArrayList<MediaItem> mediaItems;
   	private List<MediaItem> syncMediaItemList;

	public CueSheetData() {
		mediaItems = new ArrayList<MediaItem>();
	   	syncMediaItemList = Collections.synchronizedList(mediaItems);
	}
	
	public synchronized List<MediaItem> getMediaItems() {
		return syncMediaItemList;
	}
	
	public synchronized void sortMediaItemsByPosY() {
		Collections.sort(mediaItems, new Comparator<MediaItem>() {
			public int compare(MediaItem o1, MediaItem o2) {
				if (o1.getPosY() < o2.getPosY()) {
					return -1;
				}
				return 1;
			}
		});
	}
	
}
