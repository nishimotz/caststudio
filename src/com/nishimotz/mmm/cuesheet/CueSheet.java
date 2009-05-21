/*
 * $Id: CueSheet.java,v 1.2 2009/05/18 01:30:28 nishi Exp $
 */

package com.nishimotz.mmm.cuesheet;

//TODO: fade-in/fade-out操作
//TODO: sheet 本体と clock の実装の分離?
//TODO: 複数のキューシートのデータモデルを切り替えできる?
//TODO: キューシートの分割？階層化、入れ子にする？

// CueSheetData 内の MediaItem の描画は CastStudio が行っている

import java.awt.Graphics;
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
//import java.util.logging.Logger;

import com.nishimotz.mmm.caster.SystemTimeProvider;
import com.nishimotz.mmm.mediaitem.MediaItem;
import com.nishimotz.mmm.porter.Porter;
import com.nishimotz.mmm.sheet.AbstractSheet;
import com.nishimotz.util.StringUtil;

public class CueSheet extends AbstractSheet {

	// protected Logger logger = CastStudio.logger;

	private CueSheetData data;
	
	private boolean cueSheetPlaying = false;

	private double totalDuration = 0.0;
	private double currentCueSheetTime = 0.0;
	private double startedTime = 0.0;
	private double finishTime = 0.0;

	private Porter recycler;
	private Porter stickerHolder;

	private Queue<MediaItem> mediaItemQueue = new LinkedList<MediaItem>();
	private MediaItem currMediaItem = null;
	private MediaItem prevMediaItem = null;
	private double currMediaItemFinishTime = 0.0;

	private boolean loopMode;
	private boolean needRepaint = false;
	
	/**
	 */
	public CueSheet() {
		super();
		view = new CueSheetView();
		data = new CueSheetData();
	}
	
	public void setRecycler(Porter r) {
		recycler = r;
	}
	
	public void setStickerHolder(Porter s) {
		stickerHolder = s;
	}

	private boolean isMediaItemsNull() {
		return (data.getMediaItems() == null);
	}

	private void clearMediaItems() {
		data.getMediaItems().clear();
	}

	private void addMediaItems(MediaItem mi) {
		data.getMediaItems().add(mi);
	}
	
	private int sizeMediaItems() {
		return data.getMediaItems().size();
	}
	
	private MediaItem getMediaItem(int n) {
		return data.getMediaItems().get(n);
	}
	
	// Y座標でソートして Play List を作る
	public synchronized void updateTotalTime(List<MediaItem> castStudioMediaItems) {
		if (isMediaItemsNull()) return;
		// Iterator の同期化
		synchronized (castStudioMediaItems) {
			totalDuration = 0.0;
			// mediaItems.clear();
			clearMediaItems();
		    for (MediaItem mi : castStudioMediaItems) {
		    	if (view.isInside(mi)) {
		    		// mediaItems.add(mi);
		    		addMediaItems(mi);
		    		totalDuration += mi.getMediaDuration();
		    	}
		    }
		}
		sortMediaItems();
	}


    public String getClockMessage1() {
    	double remainSec = 0.0;
    	if (cueSheetPlaying) {
    		remainSec = Math.ceil(finishTime) - Math.floor(currentCueSheetTime);
    	} else {
    		remainSec = Math.ceil(totalDuration);
    	}
		if (remainSec < 0.0) {
			remainSec = 0.0;
		}
		String msg = StringUtil.formatTime(remainSec);
		if (loopMode) {
			msg += "L";
		}
		return msg;
    }
    
    public String getClockMessage2() {
		double erapsedSec = Math.floor(currentCueSheetTime) - Math.floor(startedTime);
		if (erapsedSec < 0.0) {
			erapsedSec = 0.0;
		}
		double totalSec = 0.0;
    	if (cueSheetPlaying) {
    		totalSec = Math.ceil(finishTime) - Math.floor(startedTime);
    	} else {
    		totalSec = Math.ceil(totalDuration);
    	}
		if (totalSec < 0.0) {
			totalSec = 0.0;
		}
		String msg = 
			StringUtil.formatTime(erapsedSec)
			+ " / " 
			+ StringUtil.formatTime(totalSec);
		return msg;
    }
    

    // syncStart をかける
	private synchronized void playCueSheet() {
		if (! isCueSheetPlaying()) {
			setCueSheetPlaying(true);
			startedTime = SystemTimeProvider.getSystemTime();
			totalDuration = 0.0;
			double start = startedTime;
			logger.info("syncStart now = " + startedTime);
		    for (int size = sizeMediaItems(), i = 0; i < size; i++) {
		    	MediaItem mi = getMediaItem(i);
	    		double duration = mi.getMediaDuration();
				mediaItemQueue.offer(mi); // 可能な場合、要素をキューに挿入
				start += duration;
				totalDuration += duration;
		    }
			finishTime = startedTime + totalDuration;
		}
	}

	private synchronized void stopCueSheet() {
		if (isMediaItemsNull()) return;
		if (isCueSheetPlaying()) {
			if (currMediaItem != null) {
				currMediaItem.stop();
				currMediaItemFinishTime = 0.0;
				currMediaItem = null;
			}
			mediaItemQueue.clear();
			startedTime = 0.0;
			finishTime = 0.0;
			currentCueSheetTime = 0.0;
			logger.info("stopCueSheet done.");
			setCueSheetPlaying(false);
			needRepaint = true;
		}
	}

	private void sortMediaItems() {
		data.sortMediaItemsByPosY();
	}
	
	public boolean isCueSheetPlaying() {
		return cueSheetPlaying;
	}
	
	public void setCueSheetPlaying(boolean cueSheetPlaying) {
		this.cueSheetPlaying = cueSheetPlaying;
		if (cueSheetPlaying == false) {
			if (currMediaItem != null) {
				currMediaItem.stop();
			}
			currMediaItemFinishTime = 0.0;
			prevMediaItem = currMediaItem;
			currMediaItem = null;
		}
	}

	public double getTotalDuration() {
		return totalDuration;
	}

	public void drawCueSheet(Graphics ct) {
		((CueSheetView) view).drawSheet(this, ct);
	}
	
	@Override
	public void onClick() {
		if (isCueSheetPlaying()) {
			stopCueSheet();
		} else {
			if (sizeMediaItems() > 0) {
				playCueSheet();
			}
		}
	}

	public void onClickByRightButton() {
		if (loopMode) {
			loopMode = false;
		} else { 
			loopMode = true;
		}
	}

	public synchronized void finishCueSheet() {
	    for (int size = sizeMediaItems(), i = 0; i < size; i++) {
	    	MediaItem mi = getMediaItem(i);
			mi.stop();
			removeMediaItem(mi);
		}
		startedTime = 0.0;
		currentCueSheetTime = 0.0;
		totalDuration = 0.0;
		finishTime = 0.0;
		setCueSheetPlaying(false);
		
		ArrayList<MediaItem> stickers = new ArrayList<MediaItem>();
		ArrayList<MediaItem> messages = new ArrayList<MediaItem>();
	    for (int size = sizeMediaItems(), i = 0; i < size; i++) {
	    	MediaItem mi = getMediaItem(i);
			mi.resetCastDone();
			if (mi.isSticker()) {
				stickers.add(mi);
			} else {
				messages.add(mi);
			}
		}
		stickerHolder.doLayoutMediaItems(stickers);
		recycler.doLayoutMediaItems(messages);
		// recycler.unloadMediaItems(messages);
		clearMediaItems();
	}

	private synchronized void restartCueSheet() {
		currentCueSheetTime = 0.0;
		startedTime = SystemTimeProvider.getSystemTime();
		totalDuration = 0.0;
		double start = startedTime;
		logger.info("syncStart now = " + startedTime);
	    for (int size = sizeMediaItems(), i = 0; i < size; i++) {
	    	MediaItem mi = getMediaItem(i);
    		double duration = mi.getMediaDuration();
			mediaItemQueue.offer(mi); // 可能な場合、要素をキューに挿入
			start += duration;
			totalDuration += duration;
	    }
		finishTime = startedTime + totalDuration;
	}
	
	public synchronized void doTimerTask() {
		if (cueSheetPlaying) {
			// update currentCueSheetTime
			currentCueSheetTime = SystemTimeProvider.getSystemTime();
			
			// finish last item
			if (currMediaItem != null) {
				logger.info("doTimerTask : finishTime=" + currMediaItemFinishTime 
						+ " cueSheetTime=" + currentCueSheetTime);
				if (currMediaItemFinishTime < currentCueSheetTime) {
					currMediaItem.stop();
					currMediaItemFinishTime = 0.0;
					prevMediaItem = currMediaItem;
					currMediaItem = null;
				}
			}
			
			// play next item
			if (currMediaItem == null) {
				currMediaItem = mediaItemQueue.poll(); // キューの先頭を取得および削除
				if (currMediaItem != null) {
					currMediaItemFinishTime = 
						currentCueSheetTime + currMediaItem.getMediaDuration();
					currMediaItem.syncStart(currentCueSheetTime);
				}
			}
			
			if (mediaItemQueue.isEmpty() && currMediaItem == null) {
				logger.info("CueSheet mediaItemQueue.isEmpty && currMediaItem == null");
				if (prevMediaItem.isCastDone()) {
					logger.info("CueSheet lastItemDone");
					prevMediaItem.resetCastDone();
					if (loopMode) {
						restartCueSheet();
					} else {
						finishCueSheet();
					}
				}
	    		needRepaint = true;
			}
		} else {
			currentCueSheetTime = 0.0;
		}
	}

//	public synchronized boolean isAddMediaItemAllowed(MediaItem mi) {
//		if (cueSheetPlaying) {
//			if (currMediaItem.getPosY() < mi.getPosY()) {
//				return true;
//			} else {
//				return false;
//			}
//		}
//		return true;
//	}
	
	public synchronized void addMediaItem(MediaItem mi) {
		logger.info("addMediaItem : " + mi.toString());
		if (cueSheetPlaying) {
			if (currMediaItem == null) {
				logger.info("currMediaItem == null");
				return;
			}
			if (currMediaItem.getPosY() < mi.getPosY()) {
				MediaItem mi1;
				while((mi1 = mediaItemQueue.poll()) != null) {
					totalDuration -= mi1.getMediaDuration();
					finishTime -= mi1.getMediaDuration();
				}
				mediaItemQueue.clear();
				sortMediaItems();
			    for (int size = sizeMediaItems(), i = 0; i < size; i++) {
			    	MediaItem mi2 = getMediaItem(i);
					if (currMediaItem.getPosY() < mi2.getPosY()) {
						mediaItemQueue.offer(mi2);
						totalDuration += mi2.getMediaDuration();
						finishTime += mi2.getMediaDuration();
					}
				}
			} else {
				// 挿入失敗にするべき？
			}
		}
	}

	public synchronized void removeMediaItem(MediaItem mi) {
		logger.info("removeMediaItem : " + mi.toString());
		if (cueSheetPlaying && mediaItemQueue.contains(mi)) {
			mediaItemQueue.remove(mi);
			totalDuration -= mi.getMediaDuration();
			finishTime -= mi.getMediaDuration();
		}
	}

	public void setHoverMode(int x, int y) {
		view.setHoverMode(x,y);
	}

	public void resetHoverMode() {
		view.resetHoverMode();
	}

	public boolean needRepaint() {
		if (this.cueSheetPlaying || this.needRepaint) {
			this.needRepaint = false;
			return true;
		}
		return false;
	}

}
