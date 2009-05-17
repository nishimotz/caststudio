/*
 * $Id: MediaItemData.java,v 1.2 2009/05/11 01:27:35 nishi Exp $
 */
package com.nishimotz.mmm.mediaitem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import com.nishimotz.mmm.CastStudio;
import com.nishimotz.util.Util;

public class MediaItemData {
	private Logger logger = CastStudio.logger;
	
	private String location;
	private String shapeLocation;
	private String title;
	private String author;
	private float gainAsDB = -0;
	
	private String description;
	private String category;

	private double mediaStartTime = 0.0;
	private double mediaStopTime = 0.0;
	private double mediaMaxTime = 0.0;
	
	// 0, 1, 2, 3, 4
	public static final int itemLabelCount = 5;
	private int itemLabel = 0;
	
	private short[] shapeDataMin;
	private short[] shapeDataMax;
	private boolean shapeDataLoaded = false;
	
	private String fetchedFilePath;
	private boolean isFetched = true; // if JavaSound is used, always fetch 

	private String itemGuid = null;
	
	public int getItemLabel() {
		return itemLabel;
	}
	
	public void setItemLabel(int n) {
		itemLabel = n;
	}
	
	public void incrementItemLabel() {
		itemLabel++;
		if (itemLabel == itemLabelCount) {
			itemLabel = 0;
		}
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getLocation() {
		return this.location;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public float getGainAsDB() {
		return gainAsDB;
	}

	public void setGainAsDB(float gainAsDB) {
		this.gainAsDB = gainAsDB;
	}

	public double getMediaMaxTime() {
		return mediaMaxTime;
	}

	public void setMediaMaxTime(double mediaMaxTime) {
		this.mediaMaxTime = mediaMaxTime;
	}

	public double getMediaStartTime() {
		return mediaStartTime;
	}

	public void setMediaStartTime(double mediaStartTime) {
		this.mediaStartTime = mediaStartTime;
	}

	public double getMediaStopTime() {
		return mediaStopTime;
	}

	public void setMediaStopTime(double mediaStopTime) {
		this.mediaStopTime = mediaStopTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String desc) {
		description = desc;
	}

	public String getDescription() {
		return description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void loadShapeData() {
		if (shapeDataLoaded) {
			logger.info("loadShapeData ... already loaded");
			return;
		}
		if (shapeLocation == null || shapeLocation.length() == 0) {
			logger.info("loadShapeData() shapeLocation not found.");
			return;
		}
		if (!shapeLocation.startsWith("http:")) {
			logger.info("loadShapeData() not supported local file.");
			return;
		}
		try {
			//logger.info("loadShapeData() connecting (" + shapeLocation + ")");
			URL url = new URL(shapeLocation);
			HttpURLConnection uc;
			uc = (HttpURLConnection)url.openConnection();
			uc.setRequestMethod("GET");
			InputStreamReader isr = new InputStreamReader(uc.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			
			//logger.info("loadShapeData() connected to " + shapeLocation);
			String line;
			line = Util.removeNewLines(br.readLine());
			assert line.equals("mmmshape 0.1");
			line = Util.removeNewLines(br.readLine());
			assert line.startsWith("nFrame ");
			int nFrame = Integer.valueOf(line.replaceAll("nFrame ", ""));
			shapeDataMin = new short[nFrame];
			shapeDataMax = new short[nFrame];
			for (int i = 0; i < nFrame; i++) {
				line = Util.removeNewLines(br.readLine());
				List v = Util.makeTokenizedList(line, " ");
				assert v.size() == 3;
				int n0 = Integer.valueOf((String)v.get(0));
				short n1 = Short.valueOf((String)v.get(1));
				short n2 = Short.valueOf((String)v.get(2));
				assert n0 == i;
				shapeDataMin[i] = n1;
				shapeDataMax[i] = n2;
				//logger.info("data: " + line + " -> min:" + n1 + " max:" + n2);
			}
			br.close();
			uc.disconnect();
		} catch ( Exception e ) {
			logger.severe(e.toString());
		}
		logger.info("loadShapeData() load done " + shapeLocation);
		shapeDataLoaded = true;
	}
	
	public int getShapeDataCount() {
		if (!shapeDataLoaded) return 0;
		if (shapeDataMin == null) return 0;
		assert shapeDataMin.length == shapeDataMax.length;
		return shapeDataMin.length;
	}

	public short getShapeDataMin(int n) {
		if (!shapeDataLoaded) return 0;
		assert shapeDataMin != null;
		if (n < 0 || shapeDataMin.length <= n) return 0;
		return shapeDataMin[n];
	}
	
	public short getShapeDataMax(int n) {
		if (!shapeDataLoaded) return 0;
		assert shapeDataMax != null;
		if (n < 0 || shapeDataMax.length <= n) return 0;
		return shapeDataMax[n];
	}

	public void setShapeURL(String u) {
		shapeLocation = u;
	}

	public boolean isFetched() {
		return isFetched;
	}

	public void setGuid(String itemGuid) {
		this.itemGuid = itemGuid;
	}

	public String getGuid() {
		return this.itemGuid;
	}
}
