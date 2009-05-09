///*
// * $Id: JmfMediaItemCaster.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
// */
//package com.nishimotz.mmm.caster;
//
//import java.net.URL;
//import java.util.logging.Logger;
//
//import javax.media.Controller;
//import javax.media.ControllerEvent;
//import javax.media.ControllerListener;
//import javax.media.EndOfMediaEvent;
//import javax.media.GainControl;
//import javax.media.Manager;
//import javax.media.Processor;
//import javax.media.StopAtTimeEvent;
//import javax.media.StopByRequestEvent;
//import javax.media.Time;
//import javax.media.TimeBase;
//
//import com.nishimotz.mmm.CastStudio;
//import com.nishimotz.mmm.mediaitem.MediaItemData;
//import com.nishimotz.util.JmfStateWaiter;
//
////MEMO:
////�Q�C�� dB = 20 log �搔
////���ʂ�2�{�ɂ���ɂ� +6�A�����ɂ���ɂ� -6
////20 log 2.0 �� +6
////20 log 0.5 �� -6
//
//public class JmfMediaItemCaster implements IMediaItemCaster {
//
//	// static
//	public static double getSystemTime() {
//		return Manager.getSystemTimeBase().getTime().getSeconds();
//	}
//	
//	private Logger logger = CastStudio.logger;
//	
//	//	private Player processor;
//	private Processor processor;
//	
//	private GainControl gainControl;
//	private double myStartTime; // PlayerTime
//	private double myEndTime;   // PlayerTime
//	private double myMediaDuration;
//
//	private MediaItemData mediaItemData;
//	private boolean isCastDone = false;
//
//	// player status
//	private enum STATUS {NOT_READY, READY, PLAYING, STOPPING, DEALLOCATING}
//	private STATUS status = STATUS.NOT_READY;
//
//	private boolean loadAgain = false;
//
//	public JmfMediaItemCaster() {
//	}
//
//	/* (non-Javadoc)
//	 * @see com.nishimotz.mmm.mediaitem.IMediaItemCaster#getMediaTime()
//	 */
//	public double getMediaTime() {
//		if (processor == null) {
//			return 0.0;
//		}
//		return processor.getMediaTime().getSeconds();
//	}
//
//	private double getPlayerTime() {
//		if (processor == null) {
//			return 0.0;
//		}
//		return processor.getTimeBase().getTime().getSeconds();
//	}
//	
//	/** 
//	 * success: return true
//	 */
//	/* (non-Javadoc)
//	 * @see com.nishimotz.mmm.mediaitem.IMediaItemCaster#load(com.nishimotz.mmm.mediaitem.MediaItemData)
//	 */
//	public boolean load(MediaItemData data) {
//		mediaItemData = data;
//		String location;
//		if (FETCH_MODE) {
//			if (!data.isFetched()) {
//				data.doFetch();
//			}
//			location = data.getFetchedFilePath();
//		} else {
//			location = mediaItemData.getLocation();
//		}
//        
//		try {
//
//			// processor = Manager.createProcessor(new URL(location));
//			processor = Manager.createProcessor(new URL(location));
//			processor.addControllerListener(new CasterListener());
//			
//			JmfStateWaiter sw = new JmfStateWaiter(processor);
//			if (!sw.blockingConfigure()) {
//				logger.info("blockingConfigure error : " + location);
//				return false;
//			}
//
//			// null = use Processor as Player 
//			processor.setContentDescriptor(null); 
//			
//			if (!sw.blockingPrefetch()) {
//				logger.info("blockingPrefetch error : " + location);
//				return false;
//			}
//			
//			logger.info("prefetch done : " + location);
//			gainControl = processor.getGainControl();
//			status = STATUS.READY;
//		} catch (javax.media.NoPlayerException e) {
//			logger.info("NoPlayerException : " + location);
//			return false;
//		} catch (Exception e) {
//			logger.severe("fatal error : " + location + " " + e.toString());
//			return false;
//		}
//		if (!loadAgain) {
//			double dur = getPlayerDuration();
//			mediaItemData.setMediaMaxTime(dur);
//			mediaItemData.setMediaStopTime(dur);
//			loadAgain = true;
//		}
//		return true;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.nishimotz.mmm.mediaitem.IMediaItemCaster#syncStart(double)
//	 */
//	public boolean syncStart(double sysTime) {
//		
//		if (status != STATUS.READY) {
//			logger.info("not ready");
//			return false;
//		}
//		
//		isCastDone = false;
//		
//		// processor.setRate(3.0f);
//		
//		if (gainControl != null) {
//			gainControl.setDB(mediaItemData.getGainAsDB());
//		} else { 
//			logger.severe("gainControl not available");
//		}
//
//		// startTime & stopTime : �͈͎w��̊J�n�A�I���ʒu
//		// mediaTime : �Đ��J�n�ʒu�@�K�������͈͎w��Ƃ͈�v���Ȃ�
//		double startTime = mediaItemData.getMediaStartTime();
//		double stopTime  = mediaItemData.getMediaStopTime();
//		
//		processor.setMediaTime(new Time(startTime));
//		processor.setStopTime(new Time(stopTime));
//		myMediaDuration = stopTime - startTime;
//		
//		double myCurrTime = getPlayerTime();
//		double sysCurrTime = getSystemTime();
//		
//		// syncStart �̎����� player �� TimeBase ����
//		myStartTime = sysTime - sysCurrTime + myCurrTime;
//		myEndTime = myStartTime + myMediaDuration;
//		processor.syncStart(new Time(myStartTime));
//		status = STATUS.PLAYING;
//		logger.info("playing");
//		return true;
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.nishimotz.mmm.mediaitem.IMediaItemCaster#syncStart(double, double, double)
//	 */
//	public boolean syncStart(double sysTime, double mstart, double mstop) {
//		
//		if (status != STATUS.READY) {
//			logger.info("not ready");
//			return false;
//		}
//		
//		isCastDone = false;
//		
//		// processor.setRate(3.0f);
//		
//		if (gainControl != null) {
//			gainControl.setDB(mediaItemData.getGainAsDB());
//		} else { 
//			logger.severe("gainControl not available");
//		}
//
//		// startTime & stopTime : �͈͎w��̊J�n�A�I���ʒu
//		// mediaTime : �Đ��J�n�ʒu�@�K�������͈͎w��Ƃ͈�v���Ȃ�
//		double startTime = mstart; //mediaItemData.getMediaStartTime();
//		double stopTime  = mstop; //mediaItemData.getMediaStopTime();
//		
//		processor.setMediaTime(new Time(startTime));
//		processor.setStopTime(new Time(stopTime));
//		myMediaDuration = stopTime - startTime;
//		
//		double myCurrTime = getPlayerTime();
//		double sysCurrTime = getSystemTime();
//		
//		// syncStart �̎����� player �� TimeBase ����
//		myStartTime = sysTime - sysCurrTime + myCurrTime;
//		myEndTime = myStartTime + myMediaDuration;
//		processor.syncStart(new Time(myStartTime));
//		status = STATUS.PLAYING;
//		logger.info("playing");
//		return true;
//	}
//	
//	// ���ۂɍĐ����ł���� true
//	/* (non-Javadoc)
//	 * @see com.nishimotz.mmm.mediaitem.IMediaItemCaster#isCasting()
//	 */
//	public boolean isCasting() {
//		if (status == STATUS.PLAYING) {
//			double playerTime = getPlayerTime();
//			playerTime += 0.3; // true �ɂȂ�^�C�~���O�𑁂߂ɂ���
//			if (myStartTime <= playerTime && playerTime <= myEndTime) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	// STOPPING ��� -> Thread.run �� READY �ɑJ�ڂ���B
//	// AWT �C�x���g�X���b�h����Ăяo���ꂽ�Ƃ��ɁA
//	// �u���b�L���O����Ɖ�ʍX�V���x�����Ă��܂��B
//	// �����������邽�߂� Thread �ŏ�������B
//	/* (non-Javadoc)
//	 * @see com.nishimotz.mmm.mediaitem.IMediaItemCaster#stop()
//	 */
//	public void stop() {
//		if (status == STATUS.PLAYING) {
//			status = STATUS.STOPPING;
//			new Thread() {
//				public void run() {
//					processor.stop();
//					status = STATUS.READY;
//					logger.info("PLAYER.READY");
//				}
//			}.start();
//			isCastDone = false;
//		}
//	}
//
//
//	/* (non-Javadoc)
//	 * @see com.nishimotz.mmm.mediaitem.IMediaItemCaster#getBaseTimeInSec()
//	 */
//	public double getBaseTimeInSec() {
//		TimeBase tb = processor.getTimeBase();
//		double now = tb.getTime().getSeconds();
//		return now;
//	}
//	
//	// DURATION_UNKNOWN �̂Ƃ��͂ǂ�����ׂ����H�H
//	/* (non-Javadoc)
//	 * @see com.nishimotz.mmm.mediaitem.IMediaItemCaster#getPlayerDuration()
//	 */
//	public double getPlayerDuration() {
//		Time time = processor.getDuration();
//		if (time == javax.media.Duration.DURATION_UNKNOWN) {
//			return 0.0;
//		}
//		return time.getSeconds();
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.nishimotz.mmm.mediaitem.IMediaItemCaster#isPlayerStarted()
//	 */
//	public boolean isPlayerStarted() {
//		if (processor == null) {
//			return false;
//		}
//		return processor.getState() == Controller.Started;
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.nishimotz.mmm.mediaitem.IMediaItemCaster#isPlaying()
//	 */
//	public boolean isPlaying() {
//		return (status == STATUS.PLAYING);		
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.nishimotz.mmm.mediaitem.IMediaItemCaster#isNotReady()
//	 */
//	public boolean isNotReady() {
//		return (status == STATUS.NOT_READY);
//	}
//
//	/* (non-Javadoc)
//	 * @see com.nishimotz.mmm.mediaitem.IMediaItemCaster#getStatusAsString()
//	 */
//	public String getStatusAsString() {
//		return status.toString();
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.nishimotz.mmm.mediaitem.IMediaItemCaster#isCastDone()
//	 */
//	public boolean isCastDone() {
//		return isCastDone;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.nishimotz.mmm.mediaitem.IMediaItemCaster#resetCastDone()
//	 */
//	public void resetCastDone() {
//		logger.info("resetCastDone");
//		isCastDone = false;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.nishimotz.mmm.mediaitem.IMediaItemCaster#unload()
//	 */
//	public void unload() {
//		new Thread() {
//			public void run() {
//				status = STATUS.DEALLOCATING;
//				if (processor != null) {
//					processor.deallocate();
//				}
////				if (mediaItemData.isFetched()) {
////					mediaItemData.deleteFetchedFile();
////				}
//				status = STATUS.NOT_READY;
//			}
//		}.start();
//	}
//	
//	
//	class CasterListener implements ControllerListener {
//		public void controllerUpdate(ControllerEvent event) {
//			Processor p = (Processor)event.getSourceController();
//			if (processor == p) {
//				String info = mediaItemData.getAuthor() + " " + mediaItemData.getTitle();
//				if (event instanceof StopAtTimeEvent) {
//					status = STATUS.READY;
//					isCastDone = true;
//					logger.info("StopAtTime " + info);
//				} else 	if (event instanceof EndOfMediaEvent) {
//					status = STATUS.READY;
//					isCastDone = true;
//					logger.info("EndOfMedia " + info);
//				} else if (event instanceof StopByRequestEvent) {
//					status = STATUS.READY;
//					isCastDone = false;
//					logger.info("StopByRequest " + info);
//				}
//			}
//		}
//	}
//
//
//	/* (non-Javadoc)
//	 * @see com.nishimotz.mmm.mediaitem.IMediaItemCaster#setGainAsDB(float)
//	 */
//	public void setGainAsDB(float db) {
//		if (gainControl != null) {
//			gainControl.setDB(db);
//		}
//	}
//
//}
