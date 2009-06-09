/*
 * $Id: MediaItem.java,v 1.2 2009/05/18 01:30:28 nishi Exp $
 */

package com.nishimotz.mmm.mediaitem;

// TODO: isLoaded と isNotReady の違いは？
// TODO: notReady の場合の外見を変える？

import java.awt.Graphics;
import java.util.logging.Logger;

import org.w3c.dom.Element;

import com.nishimotz.mmm.CastStudio;
import com.nishimotz.mmm.CastStudio.ITEM_STATUS;
import com.nishimotz.mmm.caster.IMediaItemCaster;
import com.nishimotz.mmm.caster.JavaSoundMediaItemCaster;
import com.nishimotz.mmm.caster.SystemTimeProvider;
import com.nishimotz.util.StringUtil;
import com.nishimotz.util.Util;


public class MediaItem implements IMediaItem {
	
	private Logger logger = CastStudio.logger;

	private MediaItemData data;
	private IMediaItemView stickerItemView;
	private IMediaItemView voiceItemView;
	private IMediaItemCaster caster;

	private String itemRpcUrl;
	
	// time
	private double systemStartTime = 0.0;

	private ITEM_STATUS status;

	private boolean loaded = false;
	private boolean duplicated = false;
	
	private boolean loadFailed = false;

	private boolean newItem;
	
	private TYPE type = TYPE.VOICE;
	
	public MediaItem() {
		data = new MediaItemData();
		
		//caster = new JmfMediaItemCaster();
		caster = new JavaSoundMediaItemCaster();
	}
	
	public void setType(TYPE a) {
		type = a;
	}

	public TYPE getType() {
		return type;
	}

	public IMediaItemView getView() {
		if (isSticker()) {
			if (stickerItemView == null) {
				stickerItemView = new StickerItemView();
			}
			return stickerItemView;
		}
		if (voiceItemView == null) {
			voiceItemView = new MediaItemView();
		}
		return voiceItemView;
	}
	
	public boolean isSticker() {
		return type == TYPE.STICKER;
	}

	public void setSticker(boolean isSticker) {
		if (isSticker) {
			setType(TYPE.STICKER);
		} else {
			setType(TYPE.VOICE);
		}
	}

    public boolean isDuplicated() {
    	return duplicated;
    }
    
	public double getSystemStartTime() {
		return systemStartTime;
	}

	public double getMediaTime() {
		return caster.getMediaTime();
	}

	// success: return true
	public /*synchronized*/ boolean load() {
		if (caster.load(data)) {
			loaded = true;
		} else {
			loaded = false;
			loadFailed = true;
		}
		return loaded;
	}
	
	public void setGainAsDB(float db) {
		data.setGainAsDB(db);
		if (isCasting()) {
			caster.setGainAsDB(db);
		}
	}
	
	public float getGainAsDB() {
		return data.getGainAsDB();
	}
	
	public void incrementGain() {
		setGainAsDB((float)(getGainAsDB() + 1.0));
	}
	
	public void decrementGain() {
		setGainAsDB((float)(getGainAsDB() - 1.0));
	}
	
	public boolean syncStart(double sysTime) {
		// system time
		systemStartTime = sysTime;
		return caster.syncStart(sysTime);
	}

	// for monitoring ending part
	public boolean syncStart(double sysTime, double mstart, double mstop) {
		systemStartTime = sysTime;
		return caster.syncStart(sysTime, mstart, mstop);
	}
	
	public void stop() {
		caster.stop();
	}
	
	public double getMediaMaxTime() {
		return data.getMediaMaxTime();
	}
	
	public double getMediaStartTime() {
		return data.getMediaStartTime();
	}

	public double getMediaStopTime() {
		return data.getMediaStopTime();
	}
	
	public String getTitle() {
		return data.getTitle();
	}

	public void setTitle(String s) {
		data.setTitle(s);
	}
	
	public int getPosX() {
		return getView().getPosX();
	}
	
	public int getPosY() {
		return getView().getPosY();
	}

	public String getAuthor() {
		return data.getAuthor();
	}

	public void setAuthor(String a) {
		data.setAuthor(a);
	}

	public void stopDragging() {
		getView().stopDragging();
	}

	public double getMediaDuration() {
		double startTime = data.getMediaStartTime();
		double stopTime = data.getMediaStopTime();
		return stopTime - startTime;
	}
 
	public void setMediaStartTime(double sec) {
		data.setMediaStartTime(sec);
		getView().setRegionBeginRatio(sec / getMediaMaxTime());
	}

	public void setMediaStopTime(double sec) {
		data.setMediaStopTime(sec);
		double ratio = 1.0;
		double t = getMediaMaxTime();
		if (t != 0) { 
			ratio = sec / t;
		}
		getView().setRegionEndRatio(ratio);
		// getView().setRegionEndRatio(sec / getMediaMaxTime());
	}

	public /*synchronized*/ void draw(Graphics ct) {
		getView().draw(this, ct);
	}

	public int getWidth() {
		return getView().getWidth();
	}

	public int getHeight() {
		return getView().getHeight();
	}

	public void drag(int diffX, int diffY) {
		getView().drag(this, diffX, diffY);
	}

	public boolean isPlayerStarted() {
		return caster.isPlayerStarted();
	}

	public void setPos(int x, int y) {
		getView().setPos(x, y);
	}

	public void checkDraggingMode(int x, int y) {
		getView().checkDraggingMode(x, y);
	}

	public void setHoverMode(int x, int y) {
		if (isCasting())
			return;
		getView().setHoverMode(x, y);
	}

	public void resetHoverMode() {
		getView().resetHoverMode();
	}
	
	public void setAudioURL(String urlstr) {
		data.setLocation(urlstr);
	}

	public String getAudioURL() {
		return data.getLocation();
	}

	public void logInfo() {
		double currentTime = SystemTimeProvider.getSystemTime();
		logger.info("mediaItem " + getTitle() 
				+ " " + caster.getStatusAsString() 
				+ " curr=" + StringUtil.formatTime2(currentTime)
				+ " start=" + StringUtil.formatTime2(systemStartTime)
				+ " dur=" + StringUtil.formatTime2(getMediaDuration()));
	}

	public boolean isNotReady() {
		return caster.isNotReady();
	}

	public boolean isCasting() {
		return caster.isCasting();
	}

	public void setStatus(ITEM_STATUS status) {
		this.status  = status;
	}

	public ITEM_STATUS getStatus() {
		return this.status;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setDescription(String desc) {
		data.setDescription(desc);
	}

	public void setCategory(String category) {
		data.setCategory(category);
	}

	public void incrementItemLabel() {
		data.incrementItemLabel();
	}

	public int getItemLabel() {
		return data.getItemLabel();
	}

	public void setItemLabel(int label) {
		data.setItemLabel(label);
	}

	public boolean isLoadFailed() {
		return loadFailed;
	}

	public boolean isCastDone() {
		return caster.isCastDone();
	}

	public void resetCastDone() {
		caster.resetCastDone();
	}

	public int getShapeDataCount() {
		return data.getShapeDataCount();
	}

	public short getShapeDataMin(int n) {
		return data.getShapeDataMin(n);
	}

	public short getShapeDataMax(int n) {
		return data.getShapeDataMax(n);
	}

	public /*synchronized*/ void loadShapeData() {
		data.loadShapeData();
	}

	public void unload() {
		loaded = false;
		caster.unload();
	}

	public void setNewItem(boolean b) {
		newItem = b;
	}

	public boolean isNewItem() {
		return newItem;
	}

	public void saveItemInfo(String uid, String episode_id) {
		String httpUrl = itemRpcUrl 
			+ "?a=save_info" 
			+ "&guid=" + data.getGuid() 
			+ "&uid=" + uid
			+ "&episode_id=" + episode_id
			+ "&fe=" + (data.isFetched() ? "1" : "0")
			+ "&c=" + getItemLabel()
			+ "&t1=" + getMediaStartTime()
			+ "&t2=" + getMediaStopTime()
			+ "&px=" + getPosX()
			+ "&py=" + getPosY()
			+ "&ga=" + getGainAsDB();
		try {
			//logger.info("saveItemInfo " + httpUrl);
			String line = Util.doHttpGet(httpUrl);
			//logger.info("data: " + line);
		} catch ( Exception e ) {
			logger.severe(e.toString());
		}
		logger.info("save done " + httpUrl);
	}
	
	
	public void setupInfo(String rpcUrl, String guid, String uid, String episode_id) {
		itemRpcUrl = rpcUrl;

		String url_shape = rpcUrl + "?a=get_shape" + "&guid=" + guid;
		data.setShapeURL(url_shape);

		String httpUrl = rpcUrl 
			+ "?a=show_info" 
			+ "&guid=" + guid
			+ "&uid=" + uid
			+ "&episode_id=" + episode_id;
		try {
			logger.info("setupInfo " + httpUrl);
			Element root = Util.getRootElementFromUrl(httpUrl);
			setItemLabel(Util.getIntegerByTagName(root, "color", 0));
			int x = Util.getIntegerByTagName(root, "posX", getPosX());
			int y = Util.getIntegerByTagName(root, "posY", getPosY());
			setPos(x, y);
			if (Util.getIntegerByTagName(root, "fetched", 0) == 1) {
				load();
			}
			setMediaStartTime(Util.getDoubleByTagName(root, "mediaStartTime", 
					getMediaStartTime()));
			setMediaStopTime(Util.getDoubleByTagName(root, "mediaStopTime", 
					getMediaStopTime()));
			setGainAsDB(Util.getFloatByTagName(root, "gain", getGainAsDB()));
		} catch (Exception e) {
			logger.severe("setupInfo failed : " + httpUrl);
			// e.printStackTrace();
		}
		logger.info("setupInfo (done) " + httpUrl);
	}

	public void setGuid(String itemGuid) {
		data.setGuid(itemGuid);
	}

	public boolean isPosUndef() {
		if (this.getView().getPosX() == CastStudio.NOT_A_POS) return true;
		return false;
	}

}
