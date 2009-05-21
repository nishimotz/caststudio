package com.nishimotz.mmm.caster;

import com.nishimotz.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFormat.Encoding;

import com.nishimotz.mmm.CastStudio;
import com.nishimotz.mmm.mediaitem.MediaItemData;

public class JavaSoundMediaItemCaster implements IMediaItemCaster {

	// player status
	private enum STATUS {NOT_READY, READY, PLAYING, STOPPING, DEALLOCATING}
	private STATUS status = STATUS.NOT_READY;

	private String filename = ""; // share with threads
	private Logger logger = CastStudio.logger;

	private double myMediaDuration = 0.0; // sec
    private boolean active = false;
	private boolean isCastDone = false;

	private double myStartTime = 0.0; // playerTime
	private double myEndTime = 0.0;   // playerTime
	
	private double startedBaseTime = 0.0; // baseTime

	private float gainAsDb;
	private MediaItemData mediaItemData;

	private double startedMediaTime = 0.0; // mediaTime
	
	@Override
	public double getBaseTimeInSec() {
		//return (double)System.currentTimeMillis() / 1000.0;
		return SystemTimeProvider.getSystemTime();
	}

	@Override
	public double getMediaTime() {
		if (status == STATUS.PLAYING) {
			return getBaseTimeInSec() - startedBaseTime + startedMediaTime;
		}
		return 0.0;
	}

	private double getPlayerTime() {
		if (status == STATUS.PLAYING) {
			return getBaseTimeInSec() - startedBaseTime;
		}
		return 0.0;
	}

	@Override
	public double getPlayerDuration() {
		return myMediaDuration;
	}

	@Override
	public String getStatusAsString() {
		return status.toString();
	}

	@Override
	public boolean isCastDone() {
		return isCastDone;
	}

	public boolean isCasting() {
		if (status == STATUS.PLAYING) {
			double playerTime = getPlayerTime();
			playerTime += 0.3; // make earlier the time it becames true
			if (myStartTime <= playerTime && playerTime <= myEndTime) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isNotReady() {
		return (status == STATUS.NOT_READY);
	}

	@Override
	public boolean isPlayerStarted() {
		return active;
	}

	@Override
	public boolean isPlaying() {
		return (status == STATUS.PLAYING);		
	}

	@Override
	public boolean load(MediaItemData data) {
		mediaItemData = data;
		String src = mediaItemData.getLocation();
		if (src.startsWith("http:")) {
			String suffix = Util.getSuffix(src);
			filename = Util.mktemp(suffix);
			Util.saveUrlAsBinary(src, filename);
		} else if (src.startsWith("file:")){
			filename = src.replaceFirst("^file:", "");
		} else {
			filename = src;
		}
        AudioInputStream ais = null;
        SourceDataLine line = null;
        myMediaDuration = 0.0; 
        try {
            ais = AudioSystem.getAudioInputStream(new File(filename));
            line = (SourceDataLine)AudioSystem.getLine(new DataLine.Info(
            		SourceDataLine.class, ais.getFormat()));
            line.open();
            line.flush();
            AudioFormat format = ais.getFormat();
            int frameSize = format.getFrameSize();
            float sampleRate = format.getSampleRate();
            long frameLength = ais.getFrameLength();
            myMediaDuration = (double)frameLength / (double)sampleRate; 
			mediaItemData.setMediaMaxTime(myMediaDuration);
			mediaItemData.setMediaStopTime(myMediaDuration);
        } catch (UnsupportedAudioFileException e) {
        	logger.info("UnsupportedAudioFile: " + filename );
        } catch(IOException e) {
			e.printStackTrace();
        } catch(LineUnavailableException e) {
			e.printStackTrace();
        } finally {
        	try {
        		if (ais != null)
        			ais.close();
	        	if (line != null)
	        		line.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        status = STATUS.READY;
		return true;
	}

	@Override
	public void resetCastDone() {
		logger.info("resetCastDone");
		isCastDone = false;
	}

	@Override
	public void setGainAsDB(float db) {
		gainAsDb = db;
	}

	@Override
	public void stop() {
        active = false;
        status = STATUS.READY;
	}

    @Override
	public boolean syncStart(double sysTime) {
		logger.info("syncStart(1) " + sysTime );
		status = STATUS.PLAYING;
		startedBaseTime = getBaseTimeInSec();
		Thread th = new Thread(new Runnable() {
			public void run() {
		        AudioInputStream ais = null;
		        SourceDataLine line = null;
		        active = true;
		        try {
		    	    File file = new File(filename);
		            ais = AudioSystem.getAudioInputStream(file);
		            AudioFormat format = ais.getFormat();
		            line = (SourceDataLine)AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, format));
		            line.open();
		            line.flush();
		            int frameSize = format.getFrameSize(); // in bytes
		            long frameLength = ais.getFrameLength();
		            logger.info("frameSize " + frameSize + " frameLength " + frameLength);
		            byte[] buffer = new byte[512];
		            ArrayList<Byte> outputBuffer = new ArrayList<Byte>();
		            int len;
		            while ((len = ais.read(buffer)) > 0) {
		            	for (int i = 0; i < len; i++) {
		            		outputBuffer.add(buffer[i]);
		            	}
		            }
		            logger.info("outputBuffer.size " + outputBuffer.size());
		            int startPos = 0;
		            int endPos = outputBuffer.size(); // last index + 1
		            int currPos = startPos;
		            float sampleRate = format.getSampleRate();
		            // int sampleSizeInBits = format.getSampleSizeInBits();
		            int sampleSizeInBytes = frameSize; // sampleSizeInBits / 8; 
		            Encoding encoding = format.getEncoding();
		            logger.info(encoding.toString() + " " + sampleRate + " " + sampleSizeInBytes);
		            startedMediaTime = 0;
		            if (encoding == Encoding.PCM_SIGNED) {
			            double mediaStartTime = mediaItemData.getMediaStartTime();
			            double mediaStopTime = mediaItemData.getMediaStopTime();
			            startedMediaTime = mediaStartTime;
			            logger.info("mediaItemData " + mediaStartTime + " " + mediaStopTime);
		            	startPos = (int)(mediaStartTime * sampleRate) * sampleSizeInBytes;
		            	int samples = (int)((mediaStopTime - mediaStartTime) * sampleRate);
		            	endPos = startPos + samples * sampleSizeInBytes;
		            	if (outputBuffer.size() < endPos) {
		            		endPos = outputBuffer.size();
		            	}
		            	currPos = startPos;
		            }
	            	logger.info("pos " + startPos + " " + endPos);
		            int maxWriteSize = 256;
		            while (active && currPos < endPos) {
		            	int writeSize = maxWriteSize;
		            	if (endPos < currPos + writeSize) {
		            		writeSize = endPos - currPos;
		            	}
		            	byte[] b = new byte[writeSize];
		            	for (int i = 0; i < writeSize; i++) {
		            		b[i] = outputBuffer.get(currPos + i);
		            	}
		            	int size = line.write(b, 0, writeSize);
		            	currPos += writeSize;
	            		if (!line.isRunning()) {
	            			line.start();
	            		}
	            		try {
							Thread.sleep(0,1);
						} catch (InterruptedException e) {
						}
		            }
		            
//		            byte[] buffer = new byte[512];
//		            int len, off, l, pos = 0, prevPos = 0;
//		            while (active && (len = ais.read(buffer)) > 0) {
//		            	for (off = 0; active && len > 0; 
//		            		off += l, len -= l, prevPos = pos, pos += l) {
//		            		l = line.write(buffer, off, len);
//		            		if (!line.isRunning()) {
//		            			line.start();
//		            		}
//		            	}
//		            }
		            
		            if (active) {
		            	line.drain();
		            }
		        } catch (UnsupportedAudioFileException e) {
					e.printStackTrace();
		        } catch(IOException e) {
					e.printStackTrace();
		        } catch(LineUnavailableException e) {
					e.printStackTrace();
		        } finally {
		        	/*
		        	if (Util.isLinux()) {
			        	try {
							Thread.sleep(300);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
		        	}
		        	*/
		        	try {
		        		if (ais != null)
		        			ais.close();
			        	if (line != null)
			        		line.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }
		        isCastDone = true;
		        active = false;
				startedBaseTime = 0.0;
				status = STATUS.READY;
			}
		});
		th.start();
		return true;
	}

	@Override
	public boolean syncStart(double sysTime, double mstart, double mstop) {
		// FIXME
		logger.info("syncStart(3) " + sysTime + " (ignore " + mstart + " " + mstop + ")");
		return syncStart(sysTime);
	}

	@Override
	public void unload() {
		// FIXME
	}
}
