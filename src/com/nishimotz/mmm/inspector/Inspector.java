/*
 * $Id: Inspector.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.mmm.inspector;

// Inspector の状態遷移：
// ユーザ操作とデバイスの関係により、MediaItem よりも詳細に状態を定義

// TODO stateデザインパターンの利用

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.nishimotz.mmm.caster.SystemTimeProvider;
import com.nishimotz.mmm.mediaitem.MediaItem;
import com.nishimotz.mmm.sheet.AbstractSheet;

public class Inspector extends AbstractSheet {

	private InspectorData data;
	private WaveView propertyView;
	
	private enum STATUS {
		EMPTY, PREPARING, PREPARING_TO_PLAY, PREPARED, PLAY_STARTING, PLAYING, STOPPING
	}
	
	private boolean itemEntering = false;
	
	private STATUS status;
	private STATUS targetStatus;
	private boolean needRepaint = false;
	
	public Inspector() {
		super();
		data = new InspectorData(); 
		view = new InspectorView(); 
		propertyView = new WaveView();
		status = STATUS.EMPTY;
		targetStatus = STATUS.PREPARED; // or PLAYING
	}
	
	private void syncStartByThread() {
		new Thread() {
			public void run() {
				MediaItem mi = data.getMediaItem();
				double start = SystemTimeProvider.getSystemTime(); // ***
				boolean ret = mi.syncStart(start);
				if (ret == false) {
					logger.info("inspector [thread] syncStart FAILED MediaItem " + mi.getTitle());
		    		status = STATUS.PREPARED;
				} else {
					logger.info("inspector [thread] syncStart OK MediaItem " + mi.getTitle());
		    		status = STATUS.PLAYING;
				}
			}
		}.start();
	}

	private void syncEndingStartByThread() {
		new Thread() {
			public void run() {
				MediaItem mi = data.getMediaItem();
				double start = SystemTimeProvider.getSystemTime(); // ***
				double mstart = mi.getMediaStopTime() - 5.0;
				if (mstart < 0.0) {
					mstart = 0.0;
				}
				double mstop = mi.getMediaStopTime();
				boolean ret = mi.syncStart(start, mstart, mstop);
				if (ret == false) {
					logger.info("inspector [thread] syncStart FAILED MediaItem " + mi.getTitle());
		    		status = STATUS.PREPARED;
				} else {
					logger.info("inspector [thread] syncStart OK MediaItem " + mi.getTitle());
		    		status = STATUS.PLAYING;
				}
			}
		}.start();
	}

	@Override
	public void onClick() {
		if (status == STATUS.PREPARED) {
    		status = STATUS.PLAY_STARTING;
			syncStartByThread();
		} else if (status == STATUS.PLAYING) {
    		logger.info("inspector stopping");
    		status = STATUS.STOPPING;
			MediaItem mi = data.getMediaItem();
			mi.stop();
			needRepaint = true;
		} else if (status == STATUS.PREPARING) {
    		status = STATUS.PREPARING_TO_PLAY;
		} else if (status == STATUS.PREPARING_TO_PLAY) {
    		status = STATUS.PREPARING;
		}
		needRepaint = true;
	}
	
	public void onClickByRightButton() {
		if (status == STATUS.PREPARED) {
    		status = STATUS.PLAY_STARTING;
			syncEndingStartByThread();
		} else if (status == STATUS.PLAYING) {
    		logger.info("inspector stopping");
    		status = STATUS.STOPPING;
			MediaItem mi = data.getMediaItem();
			mi.stop();
		} else if (status == STATUS.PREPARING) {
    		status = STATUS.PREPARING_TO_PLAY;
		} else if (status == STATUS.PREPARING_TO_PLAY) {
    		status = STATUS.PREPARING;
		}
		needRepaint = true;
	}

	public void onClickPropertyView(int x, int y, int button) {
		double sec = propertyView.getWaveAreaTime(this, x);
		MediaItem mi = data.getMediaItem();
		double startTime = mi.getMediaStartTime();
		double stopTime = mi.getMediaStopTime();
		// start と stop が逆転しないようにする
		if (button == MouseEvent.BUTTON1 && sec < stopTime) {
			mi.setMediaStartTime(sec);
		} else if (button == MouseEvent.BUTTON3 && startTime < sec) {
			mi.setMediaStopTime(sec);
		}
		needRepaint = true;
	}
	
	public void setPropertyViewRegion(int x, int y, int w, int h) {
		propertyView.setRegion(x, y, w, h);
	}

	public void drawPropertyView(Graphics ct) {
		if (status == STATUS.PREPARING
			|| status == STATUS.PREPARED 
			|| status == STATUS.PREPARING_TO_PLAY
			|| status == STATUS.PLAY_STARTING
			|| status == STATUS.STOPPING
			|| status == STATUS.PLAYING) {
			propertyView.drawSheet(this, ct);
		}
	}

	public MediaItem getMediaItem() {
		return data.getMediaItem();
	}

	public void setMediaItem(MediaItem mediaItem) {
		assert mediaItem != null;
		data.setMediaItem(mediaItem);
		status = STATUS.PREPARING;
		new Thread() {
			public void run() {
				MediaItem mediaItem = data.getMediaItem();
				mediaItem.loadShapeData();
			}
		}.start();
		new Thread() {
			public void run() {
				MediaItem mediaItem = data.getMediaItem();
				if (mediaItem.load()) {
					if (status == STATUS.PREPARING_TO_PLAY) {
						targetStatus = STATUS.PLAYING;
					} else {
						targetStatus = STATUS.PREPARED;
					}
					status = STATUS.PREPARED;
				} else {
					logger.info("load failed");
					status = STATUS.EMPTY;
				}
			}
		}.start();
	}

	public void unsetMediaItem(MediaItem mediaItem) {
		assert mediaItem != null;
		status = STATUS.EMPTY;
	}

	public boolean isEmpty() {
		return status == STATUS.EMPTY;
	}

	public boolean isItemEntering() {
		return (status == STATUS.EMPTY) && itemEntering;
	}

	public boolean isPreparing() {
		return status == STATUS.PREPARING;
	}

	public boolean isPreparingToPlay() {
		return status == STATUS.PREPARING_TO_PLAY;
	}

	public boolean isPrepared() {
		return status == STATUS.PREPARED;
	}

	public boolean isPlayStarting() {
		return status == STATUS.PLAY_STARTING;
	}

	public boolean isPlaying() {
		return status == STATUS.PLAYING;
	}

	public boolean isStopping() {
		return status == STATUS.STOPPING;
	}

	public boolean isInsideProperty(int x, int y) {
		if (status == STATUS.PREPARING
				|| status == STATUS.PREPARED 
				|| status == STATUS.PLAYING) {
			return propertyView.isInside(x, y);
		}
		return false;
	}

	public void doTimerTask() {
		if (status == STATUS.PREPARED 
			&& targetStatus == STATUS.PLAYING) {
			logger.info("doTimerTask invokes syncStart()");
    		status = STATUS.PLAY_STARTING;
			targetStatus = STATUS.PREPARED;
			syncStartByThread();
		}
		MediaItem mi = getMediaItem();
		if (status == STATUS.STOPPING && mi != null 
			&& !mi.isPlayerStarted() ) {
			logger.info("doTimerTask stopping -> prepared");
    		status = STATUS.PREPARED;
		}
		// auto stop
		if (status == STATUS.PLAYING && mi != null && mi.isCastDone() ) {
			logger.info("doTimerTask invokes stop()");
    		status = STATUS.PREPARED;
			getMediaItem().resetCastDone();
    		needRepaint = true;
		}
	}
	
	public void setItemEntering(boolean b) {
		itemEntering = b;
	}

	public void setHoverPropertyView(int x, int y) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void resetHoverPropertyView() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	public void setHoverMode(int x, int y) {
		view.setHoverMode(x,y);
	}

	public void resetHoverMode() {
		view.resetHoverMode();
	}

	public boolean needRepaint() {
		if (isPlaying() || this.needRepaint) {
			this.needRepaint = false;
			return true;
		}
		return false;
	}

}
