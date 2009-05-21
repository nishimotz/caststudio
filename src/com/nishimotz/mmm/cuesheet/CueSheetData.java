/*
 * $Id: CueSheetData.java,v 1.2 2009/05/18 01:30:28 nishi Exp $
 */
package com.nishimotz.mmm.cuesheet;

// CueSheetData は CastStudioData と同じ MediaItem の参照を保持する

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
