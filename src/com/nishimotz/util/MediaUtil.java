/*
 * $Id: MediaUtil.java,v 1.2 2009/05/11 01:27:35 nishi Exp $
 */
// TODO: loadAll : mediaItems データモデルを独立クラス化して、そのメソッドに？

package com.nishimotz.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.nishimotz.mmm.CastStudio;
import com.nishimotz.mmm.mediaitem.MediaItem;
import com.nishimotz.rss.Channel;
import com.nishimotz.rss.Enclosure;
import com.nishimotz.rss.IItemChoice;
import com.nishimotz.rss.Item;
import com.nishimotz.rss.ItemAuthor;
import com.nishimotz.rss.ItemCategory;
import com.nishimotz.rss.ItemDescription;
import com.nishimotz.rss.ItemLink;
import com.nishimotz.rss.ItemTitle;
import com.nishimotz.rss.Rss;

public class MediaUtil {
	
	private static Logger logger = CastStudio.logger;

	public static void loadAll(ArrayList<MediaItem> mediaItems) {
		for (MediaItem item : mediaItems) {
			try {
				if (item.load() == false) {
					logger.info("item loading error: " + item.toString());
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void prepareFromRSS(String rssloc, 
			List<MediaItem> mediaItems, String uid) {
		List<Item> rssItems = null;
//		String chRpcUrl;
		try {
			logger.info("Rss() " + rssloc);
			Rss rss = new Rss(rssloc);
			logger.info(rss.toString());
			logger.info("channel: " + rss.getChannel_Title()); 
			Channel ch = rss.getChannel();
			rssItems = Arrays.asList(ch.getItem());
			logger.info("sizeItem: " + rssItems.size());
//			chRpcUrl = ch.getLink();
//			logger.info("chRpcUrl: " + chRpcUrl);
		} catch (Exception e) {
			logger.severe("open RSS failed: " + e.toString()); 
			return;
		}
		
		mediaItems.clear();
		
		int replaceMode = Messages.getAsInteger("AudioFilePathReplace", 0);
		String replaceFrom = Messages.getString("AudioFilePathReplaceFrom", "");
		String replaceTo = Messages.getString("AudioFilePathReplaceTo", "");
		
		for (Item item : rssItems) {
			IItemChoice[] contents = item.getContent();
			String url_audio = ""; 
//			String url_shape = ""; 
			String title = ""; 
			String desc = ""; 
			String category = "";
			String author = "";
			String itemLink = "";
//			boolean hasInfo = false; 
			for (IItemChoice c : contents) {
				if (c instanceof Enclosure) {
					Enclosure enc = (Enclosure)c;
					String type = enc.getType();  
					if (type.equals("audio/x-wav")) {
						url_audio = enc.getUrl();
					}
					if (type.equals("audio/mpeg")) {
						url_audio = enc.getUrl();
					}
//					if (type.equals("application/x-mmm-shape")) {
//						url_shape = enc.getUrl();
//					} 
//					if (type.equals("application/x-mmm-info")) {
//						hasInfo = true;
//					} 
				} else if (c instanceof ItemTitle) {
					title = ((ItemTitle)c).getContent();
				} else if (c instanceof ItemCategory) {
					category = ((ItemCategory)c).getContent();
				} else if (c instanceof ItemAuthor) {
					author = ((ItemAuthor)c).getContent();
				} else if (c instanceof ItemDescription) {
					desc = ((ItemDescription)c).getContent();
				} else if (c instanceof ItemLink) {
					itemLink = ((ItemLink)c).getContent();
				}
			}
			
//			// カンマで文字列を分割
//			String[] array = StringUtil.split(category, "-");
//			String cate;
//			int label = 0;
//			if (array.length >= 2) {
//				cate = array[0];
//				label = Integer.parseInt(array[1]);
//			} else {
//				cate = category;
//				label = 0;
//			}
			
			logger.info("item: " + author 
					+ " " + title 
					+ " " + category 
//					+ " " + label
					+ " " + desc
					+ " " + url_audio);
//			logger.info("item shape: " + url_shape);
//			logger.info("item hasInfo: " + hasInfo);
			
			if (replaceMode != 0) {
				url_audio = url_audio.replaceFirst(replaceFrom, replaceTo);
			}
			
			boolean isSticker = false;
			if (category.equals("sticker")) {
				isSticker = true;
			}
			
			if (url_audio.length() > 0) {
				MediaItem t1 = new MediaItem();
				t1.setSticker(isSticker);
				t1.setAudioURL(url_audio);
				t1.setTitle(title);
				t1.setAuthor(author);
				t1.setDescription(desc);
				t1.setCategory(category);
//				t1.setShapeURL(url_shape);
				if (itemLink.length() > 0) {
//					t1.setItemRpcUrl(itemLink);
					t1.setupInfo(itemLink, url_audio, uid);
				}
				mediaItems.add(t1);
			}
		}
	}
	

	/**
	 * 未読の素材を currMediaItems と newItems に追加する
	 * 消滅した素材は currMediaItems から除去せず deletedItems に追加する
	 * TODO: deletedItems の検索を実装する
	 */
	public static void updateFromRSS(
			String rssloc, 
			List<MediaItem> currMediaItems, 
			List<MediaItem> newItems,
			List<MediaItem> deletedItems,
			String uid) {
		
		List<Item> rssItems = null;
		try {
			Rss rss = new Rss(rssloc);
			logger.info("channel: " + rss.getChannel_Title()); 
			Channel ch = rss.getChannel();
			rssItems = Arrays.asList(ch.getItem());
			logger.info("sizeItem: " + rssItems.size());
		} catch (Exception e) {
			logger.severe("open RSS failed: " + e.toString()); 
			return;
		}
		
		int replaceMode = Messages.getAsInteger("AudioFilePathReplace", 0);
		String replaceFrom = Messages.getString("AudioFilePathReplaceFrom", "");
		String replaceTo = Messages.getString("AudioFilePathReplaceTo", "");
		
		for (Item item : rssItems) {
			IItemChoice[] contents = item.getContent();
			String url_audio = ""; 
//			String url_shape = ""; 
			String title = ""; 
			String desc = ""; 
			String category = "";
			String author = "";
			String itemLink = "";
//			boolean hasInfo = false; 
			for (IItemChoice c : contents) {
				if (c instanceof Enclosure) {
					Enclosure enc = (Enclosure)c;
					String type = enc.getType();  
					if (type.equals("audio/x-wav")) {
						url_audio = enc.getUrl();
					}
					if (type.equals("audio/mpeg")) {
						url_audio = enc.getUrl();
					}
//					if (type.equals("application/x-mmm-shape")) {
//						url_shape = enc.getUrl();
//					} 
//					if (type.equals("application/x-mmm-info")) {
//						hasInfo = true;
//					} 
				} else if (c instanceof ItemTitle) {
					title = ((ItemTitle)c).getContent();
				} else if (c instanceof ItemCategory) {
					category = ((ItemCategory)c).getContent();
				} else if (c instanceof ItemAuthor) {
					author = ((ItemAuthor)c).getContent();
				} else if (c instanceof ItemDescription) {
					desc = ((ItemDescription)c).getContent();
				} else if (c instanceof ItemLink) {
					itemLink = ((ItemLink)c).getContent();
				}
			}
			
//			// カンマで文字列を分割
//			String[] array = StringUtil.split(category, "-");
//			String cate;
//			int label = 0;
//			if (array.length >= 2) {
//				cate = array[0];
//				label = Integer.parseInt(array[1]);
//			} else {
//				cate = category;
//				label = 0;
//			}
			
			logger.info("item: " + author 
					+ " " + title 
					+ " " + category 
//					+ " " + label
					+ " " + desc
					+ " " + url_audio);
//			logger.info("item shape: " + url_shape);
//			logger.info("item hasInfo: " + hasInfo);
			
			if (replaceMode != 0) {
				url_audio = url_audio.replaceFirst(replaceFrom, replaceTo);
			}
			
			boolean isSticker = false;
			if (category.equals("sticker")) {
				isSticker = true;
			}
			
			// TODO: 同じURLでもバージョン違いがありうる
			if (url_audio.length() > 0 && !itemContains(currMediaItems, url_audio)) {
				MediaItem t1 = new MediaItem();
				t1.setSticker(isSticker);
				t1.setAudioURL(url_audio);
				t1.setTitle(title);
				t1.setAuthor(author);
				t1.setDescription(desc);
				t1.setCategory(category);
				t1.setNewItem(true);
				if (itemLink.length() > 0) {
					t1.setupInfo(itemLink, url_audio, uid);
				}
				currMediaItems.add(t1);
				newItems.add(t1);
			}
		}
	}
	

	private static boolean itemContains(List<MediaItem> mediaItems, String url_audio) {
		for (MediaItem mi : mediaItems) {
			if (mi.getAudioURL().equals(url_audio)) {
				return true;
			}
		}
		return false;
	}


	/**
	 * TODO: set author
	 * TODO: 階層ディレクトリのトラバースをする
	 */
	public static void makeLocalFileList(String location, List<MediaItem> mediaItems) {
		mediaItems.clear();

		File path = new File(location);
		if (path.isDirectory() && path.exists()) {
			File[] list = path.listFiles();
			if (list != null) {
				for (File f : list) {
					String urlstr = "";
					String comment = "";
					String author = "LocalFile";
					try {
						URL url = f.toURL();
						urlstr = url.toString().toLowerCase();
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					if (urlstr.endsWith(".mp3")) {
						Date d = new Date(f.lastModified());
						comment = StringUtil.getFileBodyWithoutExt(urlstr) 
						+ " " + StringUtil.formatDate(d);
						
						MediaItem mi = new MediaItem();
						mi.setAudioURL(urlstr);
						mi.setTitle(comment);
						mi.setAuthor(author);
						mediaItems.add(mi);

						logger.info(urlstr + " " + comment + " " + author);
					}
				}
			}
		}
	}


}
