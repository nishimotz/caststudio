/*
 * O'ra-be CastStudio
 * (c) 2006-2009 Takuya Nishimoto
 */
package com.nishimotz.mmm;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.nishimotz.mmm.cuesheet.CueSheet;
import com.nishimotz.mmm.event.CastStudioEvent;
import com.nishimotz.mmm.event.MouseDraggedEvent;
import com.nishimotz.mmm.event.MouseMovedEvent;
import com.nishimotz.mmm.event.MousePressedEvent;
import com.nishimotz.mmm.event.MouseReleasedEvent;
import com.nishimotz.mmm.event.MouseWheelMovedEvent;
import com.nishimotz.mmm.inspector.Inspector;
import com.nishimotz.mmm.mediaitem.MediaItem;
import com.nishimotz.mmm.porter.Porter;
import com.nishimotz.util.Getopt;
//import com.nishimotz.util.JnlpUtil;
import com.nishimotz.util.MediaUtil;
import com.nishimotz.util.Messages;
import com.nishimotz.util.TempFileManager;

public class CastStudio {

	public enum ITEM_STATUS {
		INSPECTOR, PORTER, CUESHEET0, CUESHEET1, WORKSPACE
	}

	/**
	 * @param args
     * [session] -u 201 -e 123456 -r holdStationRSS
	 * u:userid e:episode_id
     * [loggingMode] -g 1 
	 */
	public static void main(String[] args) {
		// JnlpUtil.init();
		new CastStudio(args);
	}
	
	public static Logger logger = Logger.getLogger("mmm");
	
	private static String appTitle;
	private static final long serialVersionUID = 1L;
	public static final int NOT_A_POS = -1;

	private final String defaultRSS = "http://radiofly.to/mmm/irusu/rss.php";

	// instance values
	private CastStudioData data;
	private CastStudioView view;

	private enum SOURCE_MODE {LocalDirectory, LocalRSS, HoldStationRSS, Unknown}
	private SOURCE_MODE sourceMode = SOURCE_MODE.Unknown;
	
	private String localDirectory = "c:/recordings/2005-11/xxx051113";
	private String localRSS = "files.xml";
	private String holdStationRSS = "http://server/caststudio/index.rss";
	// private String station = "11111";
	private String uid = "101";
	private String episode_id = "996332877";
	// private int rssmode = 0;
	
	private int loggingMode = 0;
	
	// showed with GUI
	private String sessionInfo = "";
	
	// mouse position of previous dragged event
	private int prevDragX = NOT_A_POS;
	private int prevDragY = NOT_A_POS;
	
	// item dragging currently
	private MediaItem draggingMediaItem = null;
	// initial position of draggingMediaItem (for undo)
	private int originalPosX = NOT_A_POS;
	private int originalPosY = NOT_A_POS;
	// state at initial position of draggingMediaItem
	public ITEM_STATUS originalItemStatus;

	private Queue<CastStudioEvent> eventQueue = new LinkedList<CastStudioEvent>();

	private Thread eventDispatcherThread;
	private Thread timerTaskThread;

	public static String getAppTitle() {
		return appTitle;
	}

	public CastStudio(String[] args) {
		// delete previous temp files
		try {
			Class.forName(TempFileManager.class.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Messages.setup("com.nishimotz.mmm.messages");
		appTitle = Messages.getString("AppTitle");

		data = new CastStudioData();
		view = new CastStudioView(this);
		
		view.initFrame();
		parseProperties();
		parseArgs(args);
		setupLogger();
		startApplication();
	}

	public void parseProperties() {
		loggingMode = Messages.getAsInteger("LoggingMode", 0);
		
		String str = Messages.getString("SourceMode");
		if (str.equals("HoldStationRSS")) {
			sourceMode = SOURCE_MODE.HoldStationRSS;
		} else if (str.equals("LocalRSS")) {
			sourceMode = SOURCE_MODE.LocalRSS;
		} else if (str.equals("LocalDirectory")) {
			sourceMode = SOURCE_MODE.LocalDirectory;
		} else {
			sourceMode = SOURCE_MODE.Unknown;
		}
		localDirectory    = Messages.getString("LocalDirectory");
		localRSS          = Messages.getString("LocalRSS");
		holdStationRSS    = Messages.getString("HoldStationRSS", defaultRSS);
		// station = Messages.getString("station");
		uid     = Messages.getString("uid");
		episode_id     = Messages.getString("episode_id");
	}
	
	
	/*
	 * override property values by args
	 */
	public void parseArgs(String[] args) {
		Getopt g = new Getopt("", args, "g:r:u:e:");
		int c;
		while ((c = g.getopt()) != -1){
			switch (c)	{
			case 'g':
				loggingMode = Integer.parseInt(g.getOptarg());
				break;
			case 'r':
				holdStationRSS = g.getOptarg();
				break;
			//case 's':
			//	station = g.getOptarg();
			//	break;
			case 'u':
				uid = g.getOptarg();
				break;
			case 'e':
				episode_id = g.getOptarg();
				break;
			}
		}
	}
	
	private void setupLogger() {
		if (loggingMode == 1) {
			logger.setLevel(Level.ALL);
		} else {
			logger.setLevel(Level.OFF);
		}
	}
	
	private String getHoldStationLocation() {
		return holdStationRSS;
			/* + "?station=" + station */ 
			/* + "?episode_id=" + episode_id + "&uid=" + uid; */
	}
	
	private String getHoldStationInfo() {
		return /* "station=" + station + */ 
			 " episode_id=" + episode_id
		    + " uid=" + uid;
	}
	
	private void initSessionInfo() {
		String info = "";
		String src;
		switch (sourceMode) {
		case LocalDirectory:
			src = localDirectory;
			info = "LocalDirectory=" + src;
			break;
		case LocalRSS:
			src = localRSS;
			info = "LocalRSS=" + src;
			break;
		case HoldStationRSS:
			src = getHoldStationLocation();
			info = getHoldStationInfo();
			break;
		}
		setSessionInfo(info);
		view.setTitle(getFrameTitle());
	}

	
	private void loadMediaItems() {
		String src;
		List<MediaItem> mediaItems = data.getMediaItems();
		switch (sourceMode) {
		case LocalDirectory:
			src = localDirectory;
			MediaUtil.makeLocalFileList(src, mediaItems);
			break;
		case LocalRSS:
			src = localRSS;
			MediaUtil.prepareFromRSS(src, mediaItems, uid, episode_id);
			break;
		case HoldStationRSS:
			src = getHoldStationLocation();
			MediaUtil.prepareFromRSS(src, mediaItems, uid, episode_id);
			break;
		}
		view.repaintFrame();
		data.doLayoutPorterAtFirst();
		view.repaintFrame();
		
		for (MediaItem mi : mediaItems) {
			if (mi.isSticker()) {
				if(!mi.load()) {
					logger.info("load failed " + mi.toString());
				}
			}
			if (getInspector().isInside(mi)) {
				getInspector().setMediaItem(mi);
			}
			view.repaintFrame();
		}
		for (int i = 0; i < data.sizeCueSheet(); i++) {
			getCueSheet(i).updateTotalTime(mediaItems);
		}
		view.repaintFrame();
	}

	private void updateMediaItems() {
		if (sourceMode == SOURCE_MODE.HoldStationRSS) {
			String src;
			src = holdStationRSS;
			src = getHoldStationLocation();
			List<MediaItem> mediaItems = data.getMediaItems();
			List<MediaItem> newItems = new ArrayList<MediaItem>();
			List<MediaItem> deletedItems = new ArrayList<MediaItem>();
			MediaUtil.updateFromRSS(src, mediaItems, newItems, deletedItems, uid, episode_id);
			if (newItems.size() > 0) {
				logger.info("new items:" + newItems.size());
				data.doLayoutPorterAtFirst(newItems);
			} else {
				logger.info("no new items");
			}
			newItems = null;
//			getRecycler().doLayoutMediaItems(deletedItems);
//			deletedItems = null;
		}
	}
	
	public void startApplication() {
		view.setCueSheetRegion();
		view.setPorterRegion();
		view.setInspectorRegion();
		view.setRecyclerRegion();
		view.setStickerHolderRegion();
		
		timerTaskThread = new Thread(new TimerTaskRunner());
		timerTaskThread.start();
		initSessionInfo();
		
		// mouse available from here
		view.setupEventListener();
		eventDispatcherThread = new Thread(new EventDispatcher());
		eventDispatcherThread.start();

		logger.info("loading..");
		view.setWaitCursor();
		loadMediaItems();
		view.setDefaultCursor();
		view.repaintFrame();
	}
	
	
	private String getFrameTitle() {
		return getAppTitle() + " " + getSessionInfo();
	}
	
	public void setSessionInfo(String sessionInfo) {
		this.sessionInfo = sessionInfo;
	}

	public String getSessionInfo() {
		return sessionInfo;
	}

	public void setDraggingMediaItem(MediaItem draggingMediaItem) {
		this.draggingMediaItem = draggingMediaItem;
	}

	public MediaItem getDraggingMediaItem() {
		return draggingMediaItem;
	}

	public List<MediaItem> getMediaItems() {
		return data.getMediaItems();
	}

	public CueSheet getCueSheet(int n) {
		return data.getCueSheet(n);
	}

	public Porter getPorter() {
		return data.getPorter();
	}

	public Inspector getInspector() {
		return data.getInspector();
	}

	public void offerEvent(CastStudioEvent ev) {
		if (eventQueue != null && eventDispatcherThread != null) {
			eventQueue.offer(ev);
			eventDispatcherThread.interrupt();
		}
	}

	private void doTimerTask() {
		boolean repaint = false;
		for (int i = 0; i < data.sizeCueSheet(); i++) {
			getCueSheet(i).doTimerTask();
			if (getCueSheet(i).needRepaint()) {
				repaint = true;
			}
		}
		getInspector().doTimerTask();
		if (getInspector().needRepaint()) {
			repaint = true;
		}
		if (repaint) {
			view.repaintFrame();
		}
	}
	
	/**
	 * interrupted by timerTaskThread.interrupt()
	 */
	class TimerTaskRunner implements Runnable {
		public void run() {
			while(true) {
				doTimerTask();
				try { 
					Thread.sleep(200);
				} catch (Exception e) { 
					// logger.info(e.toString());
				}
			}
		}
	}
	
	
	/**
	 * offerEvent(CastStudioEvent ev) によって
	 * eventDispatcherThread.interrupt();
	 * が実行されて割り込まれる
	 */
	class EventDispatcher implements Runnable {
		
		EventHandler handler;
		
		public EventDispatcher() {
			handler = new EventHandler();
		}
		
		public void run() {
			while(iteration()) {
				try { 
					Thread.sleep(1000);
				} catch (Exception e) { 
					// logger.info(e.toString());
				}
			}
		}

		private boolean iteration() {
			CastStudioEvent ev = null;
			while ( (ev = (CastStudioEvent)eventQueue.poll()) != null ) {
				if (ev instanceof MouseDraggedEvent) {
					handler.mouseDragged(ev.getMouseEvent());
				} else if (ev instanceof MouseMovedEvent) {
					handler.mouseMoved(ev.getMouseEvent());
				} else if (ev instanceof MousePressedEvent) {
					handler.mousePressed(ev.getMouseEvent());
				} else if (ev instanceof MouseReleasedEvent) {
					handler.mouseReleased(ev.getMouseEvent());
				} else if (ev instanceof MouseWheelMovedEvent) {
					handler.mouseWheelMoved(ev.getMouseWheelEvent());
				}
				view.repaintFrame();
				ev = null;
			}
			return true;
		}
	}
	
	
	/**
	 *
	 */
	class EventHandler {
		
//		private CastStudio castStudio;
		
//		public EventHandler(CastStudio c) {
//			castStudio = c;
//		}
		
		public EventHandler() {
		}
		
		public void mouseWheelMoved(MouseWheelEvent e) {
			logger.info("MouseWheelMoved " + e.toString());
			if (getMediaItems() != null) {
				int x = e.getX();
				int y = e.getY();
				MediaItem mi = data.findItemFromPosition(x, y);
				if (mi != null) {
					if (e.getWheelRotation() < 0) {
						// up
						mi.incrementGain();
						view.repaintFrame();
					} else {
						// down
						mi.decrementGain();
						view.repaintFrame();
					}
				}
				
			}
		}

		// 上から順にイベントを受理するオブジェクトを探す
		public void mousePressed(MouseEvent e) { 
			if (getMediaItems() == null) return;
			int x = e.getX();
			int y = e.getY();
			prevDragX = x;
			prevDragY = y;
			int button = e.getButton();
			if (isInsideInspectorProperty(x, y)) {
				getInspector().onClickPropertyView(x, y, button);
				logger.info("InspectorProperty pressed");
			} else {
				MediaItem mi = data.findItemFromPosition(x, y);
				if (mi != null) {
					if (e.isShiftDown()) {
						if (button == MouseEvent.BUTTON1) {
							mi.decrementGain();
						} else if (button == MouseEvent.BUTTON3) {
							mi.incrementGain();
						}
					} else {
						if (button == MouseEvent.BUTTON1) {
							// mi dragging start
							setDraggingMediaItem(mi);
							mi.checkDraggingMode(x, y);
							originalPosX = mi.getPosX();
							originalPosY = mi.getPosY();
							originalItemStatus = checkState(mi);
						} else if (button == MouseEvent.BUTTON3) {
							mi.incrementItemLabel();
						}
					}
					mi.logInfo();
				} else {
					if (getCueSheet(0).isInside(x, y)) {
						if (button == MouseEvent.BUTTON1) {
							logger.info("cueSheet[0] pressed");
							getCueSheet(0).onClick();
						} else if (button == MouseEvent.BUTTON3) {
							logger.info("cueSheet[0] pressed (right)");
							getCueSheet(0).onClickByRightButton();
						}
					} else if (getCueSheet(1).isInside(x, y)) {
						if (button == MouseEvent.BUTTON1) {
							logger.info("cueSheet[1] pressed"); 
							getCueSheet(1).onClick();
						} else if (button == MouseEvent.BUTTON3) {
							logger.info("cueSheet[1] pressed (right)");
							getCueSheet(1).onClickByRightButton();
						}
					} else if (getPorter().isInside(x, y)) {
						if (button == MouseEvent.BUTTON1) {
							logger.info("porter pressed button1");
							view.setWaitCursor();
							updateMediaItems();
							view.setDefaultCursor();
						} else if (button == MouseEvent.BUTTON3) {
							logger.info("porter pressed button3");
							getPorter().onClick();
						}
					} else if (getInspector().isInside(x, y)) {
						if (button == MouseEvent.BUTTON1) {
							logger.info("inspector pressed (left)");
							getInspector().onClick();
						} else if (button == MouseEvent.BUTTON3) {
							logger.info("inspector pressed (right)");
							getInspector().onClickByRightButton();
						}
					} else {
						logger.info("workspace pressed (" + x + "," + y + ")");
						if (button == MouseEvent.BUTTON3) {
							view.showPopupMenu(x, y);
						}
					}
				}
			}
//			if (timerTaskThread != null) {
//				timerTaskThread.interrupt(); // for repaint GUI
//			}
			view.repaintFrame();
		}
		
		
		private boolean isInsideInspectorProperty(int x, int y) {
			return getInspector().isInsideProperty(x, y);
		}

		// どれかのシートに中途半端に引っかかっている
		private boolean isHalfMast(MediaItem mi) {
			if (getCueSheet(0).isHalfMast(mi)
					|| getCueSheet(1).isHalfMast(mi)
					|| getInspector().isHalfMast(mi)) {
				return true;
			}
			return false;
		}
		
		// mi がどの Sheet に乗っかっているか
		private ITEM_STATUS checkState(MediaItem mi) {
			if (getCueSheet(0).isInside(mi)) {
				return ITEM_STATUS.CUESHEET0;
			} else if (getCueSheet(1).isInside(mi)) {
				return ITEM_STATUS.CUESHEET1;
			} else if (getPorter().isInside(mi)) {
				return ITEM_STATUS.PORTER;
			} else if (getInspector().isInside(mi)) {
				return ITEM_STATUS.INSPECTOR;
			} 
			return ITEM_STATUS.WORKSPACE;
		}
		
		// MediaItem が Frame の外にある
		// TODO: frame の resize に対応すること
		private boolean isOutsideCastStudioFrame(MediaItem mi) {
			int newX = mi.getPosX();
			int newY = mi.getPosY();
			int margin = 30;
			if (newX < 0 || CastStudioView.getFrameWidth() - margin < newX) {
				return true;
			}
			if (newY < 0 || CastStudioView.getFrameHeight() - margin < newY) {
				return true;
			}
			return false;
		}
		
		public void mouseReleased(MouseEvent e) { 
			if (getMediaItems() == null) return;
			prevDragX = NOT_A_POS;
			prevDragY = NOT_A_POS;
			MediaItem mi = getDraggingMediaItem();
			if (mi != null) {
				mi.stopDragging();
				setDraggingMediaItem(null);
				ITEM_STATUS state = checkState(mi);

				// 判定
				boolean succeed = true;
				if (isHalfMast(mi) || isOutsideCastStudioFrame(mi)) {
					// 中途半端な位置だと移動失敗
					succeed = false;
				} else {
					// inspector に入るのは１つだけ
					if (originalItemStatus != ITEM_STATUS.INSPECTOR 
							&& state == ITEM_STATUS.INSPECTOR
							&& !getInspector().isEmpty()) {
						succeed = false;
					} 
//					// ポーターからsticker を取り出すときには複製
//					if (originalItemStatus == ITEM_STATUS.PORTER
//						&& state != ITEM_STATUS.PORTER
//						&& mi.isSticker()) {
//						if (mi.isLoaded() && isLoadingItems == false) { 
//							duplicate = true;
//							logger.info("duplicate " + mi.toString());
//						} else {
//							succeed = false;
//						}
//					} 
					// CueSheet に入るのは loaded だけ
					if (state == ITEM_STATUS.CUESHEET0 && !mi.isLoaded()) {
						succeed = false;
					}
					if (state == ITEM_STATUS.CUESHEET1 && !mi.isLoaded()) {
						succeed = false;
					}
					// 準備中・再生中の Inspector 
					if (originalItemStatus == ITEM_STATUS.INSPECTOR) {
						if (getInspector().isPreparing() 
								|| getInspector().isPlaying()) {
							if (state != ITEM_STATUS.INSPECTOR) {
								mi.stop();
								getInspector().unsetMediaItem(mi);
							}
						}
					} 

					// 再生中のアイテムを取り出すとキューシートも停止する
					if (getCueSheet(0).isCueSheetPlaying() 
							&& originalItemStatus == ITEM_STATUS.CUESHEET0
							&& state == ITEM_STATUS.WORKSPACE
							&& mi.isCasting()) {
						mi.stop();
						getCueSheet(0).setCueSheetPlaying(false);
						succeed = true;
					} 
					if (getCueSheet(1).isCueSheetPlaying() 
							&& originalItemStatus == ITEM_STATUS.CUESHEET1
							&& state == ITEM_STATUS.WORKSPACE
							&& mi.isCasting()) {
						mi.stop();
						getCueSheet(1).setCueSheetPlaying(false);
						succeed = true;
					} 

/*
   					// 再生中のキューシートの中で素材を移動することは禁止
					if (getCueSheet(0).isCueSheetPlaying() 
							&& originalItemStatus == ITEM_STATUS.CUESHEET0 
							&& state == ITEM_STATUS.CUESHEET0) {
						succeed = false;
					} 
					if (getCueSheet(1).isCueSheetPlaying() 
							&& originalItemStatus == ITEM_STATUS.CUESHEET1 
							&& state == ITEM_STATUS.CUESHEET1) {
						succeed = false;
					} 
					
					// add CueSheet
					if (originalItemStatus != ITEM_STATUS.CUESHEET0
							&& state == ITEM_STATUS.CUESHEET0
							&& !getCueSheet(0).isAddMediaItemAllowed(mi)) {
						succeed = false;
					}
					if (originalItemStatus != ITEM_STATUS.CUESHEET1
							&& state == ITEM_STATUS.CUESHEET1
							&& !getCueSheet(1).isAddMediaItemAllowed(mi)) {
						succeed = false;
					}
*/
				}

				// 後処理
				if (succeed) {
//					if (duplicate) {
//						// newItem は isDuplicated になる
//						int x = mi.getPosX();
//						int y = mi.getPosY();
//						
//						MediaItem newItem = (MediaItem)(mi.clone());
//						newItem.setPos(x, y);
//						data.getMediaItems().add(newItem);
//
//						// 元の item は元の位置に戻す
//						mi.setPos(originalPosX, originalPosY);
//					}
					
					// 移動成功の処理
					mi.setStatus(state);
					// 一番後ろに移動する
					data.changeOrderToLast(mi);

					// inspector から出す
					if (originalItemStatus == ITEM_STATUS.INSPECTOR 
							&& state != ITEM_STATUS.INSPECTOR) {
						// Inspector の登録解除
						getInspector().unsetMediaItem(mi);
					} 
					// inspector に入れる
					if (originalItemStatus != ITEM_STATUS.INSPECTOR 
							&& state == ITEM_STATUS.INSPECTOR) {
						getInspector().setMediaItem(mi);
						// view.setRepaintByRedraw(true);
					} 
					
					// add/remove CueSheet(0)
					if (originalItemStatus != ITEM_STATUS.CUESHEET0
							&& state == ITEM_STATUS.CUESHEET0) {
						getCueSheet(0).addMediaItem(mi);
					}
					if (originalItemStatus == ITEM_STATUS.CUESHEET0
							&& state != ITEM_STATUS.CUESHEET0) {
						getCueSheet(0).removeMediaItem(mi);
					}
					
					// add/remove CueSheet(1)
					if (originalItemStatus != ITEM_STATUS.CUESHEET1
							&& state == ITEM_STATUS.CUESHEET1) {
						getCueSheet(1).addMediaItem(mi);
					}
					if (originalItemStatus == ITEM_STATUS.CUESHEET1
							&& state != ITEM_STATUS.CUESHEET1) {
						getCueSheet(1).removeMediaItem(mi);
					}
				} else {
					// 移動をアンドゥする
					mi.setPos(originalPosX, originalPosY);
					// cueSheet に戻っていく場合
					getCueSheet(0).updateTotalTime(getMediaItems());
					getCueSheet(1).updateTotalTime(getMediaItems());
				}
			}
//			if (timerTaskThread != null) {
//				timerTaskThread.interrupt(); // for repaint GUI
//			}
			view.repaintFrame();
		}
		
		public void mouseDragged(MouseEvent e) {
			if (getMediaItems() == null) return;
			int x = e.getX();
			int y = e.getY();
			int button = e.getButton();
			if (isInsideInspectorProperty(x, y)) {
				getInspector().onClickPropertyView(x, y, button);
				logger.info("InspectorProperty dragged");
				//timerTaskThread.interrupt(); // for repaint GUI
			} else {
				MediaItem dragItem = getDraggingMediaItem();
				if (dragItem != null 
						&& prevDragX != NOT_A_POS 
						&& prevDragY != NOT_A_POS) {
					int diffX = x - prevDragX;
					int diffY = y - prevDragY;
					dragItem.drag(diffX, diffY);
					if (getInspector().isInside(dragItem)) {
						getInspector().setItemEntering(true);
					} else {
						getInspector().setItemEntering(false);
					}
					getCueSheet(0).updateTotalTime(getMediaItems());
					getCueSheet(1).updateTotalTime(getMediaItems());
				}
			}
			prevDragX = x;
			prevDragY = y;
			//
//			if (timerTaskThread != null) {
//				timerTaskThread.interrupt(); // for repaint GUI
//			}
			view.repaintFrame();
		}
		
		public void mouseMoved(MouseEvent e) { 
			List<MediaItem> mediaItems = getMediaItems();
			if (mediaItems == null) return;
			int x = e.getX();
			int y = e.getY();
			if (isInsideInspectorProperty(x, y)) {
				getInspector().setHoverPropertyView(x, y);
				for (MediaItem mi : mediaItems) {
			    	mi.resetHoverMode();
				}
			} else {
				getInspector().resetHoverPropertyView();
				MediaItem found = data.findItemFromPosition(x, y);
				for (MediaItem mi : mediaItems) {
					if (mi == found) {
						mi.setHoverMode(x, y);
						getCueSheet(0).resetHoverMode();
						getCueSheet(1).resetHoverMode();
						getInspector().resetHoverMode();
					} else {
				    	mi.resetHoverMode(); 
				    	// found == null の場合はすべて reset される
					}
				} 
				if (found == null) {
					getCueSheet(0).setHoverMode(x,y);
					getCueSheet(1).setHoverMode(x,y);
					getInspector().setHoverMode(x, y);
				}
			}
			if (timerTaskThread != null) {
				timerTaskThread.interrupt(); // for repaint GUI
			}
		}
	}

	public Porter getRecycler() {
		return data.getRecycler();
	}

	public Porter getStickerHolder() {
		return data.getStickerHolder();
	}

	public void saveItemInfo() {
		logger.info("save item colors");
		data.saveItemsInfo(uid, episode_id);
	}

}
