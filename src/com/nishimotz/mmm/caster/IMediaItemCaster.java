package com.nishimotz.mmm.caster;

import com.nishimotz.mmm.mediaitem.MediaItemData;

public interface IMediaItemCaster {

	public final static boolean FETCH_MODE = true;

	public abstract double getMediaTime();

	// success: return true
	public abstract boolean load(MediaItemData data);

	/**
	 * @param sysTime : systemStartTime
	 */
	public abstract boolean syncStart(double sysTime);

	/**
	 * @param sysTime : systemStartTime
	 */
	public abstract boolean syncStart(double sysTime, double mstart,
			double mstop);

	// ���ۂɍĐ����ł���� true
	public abstract boolean isCasting();

	// STOPPING ��� -> Thread.run �� READY �ɑJ�ڂ���B
	// AWT �C�x���g�X���b�h����Ăяo���ꂽ�Ƃ��ɁA
	// �u���b�L���O����Ɖ�ʍX�V���x�����Ă��܂��B
	// �����������邽�߂� Thread �ŏ�������B
	public abstract void stop();

	public abstract double getBaseTimeInSec();

	// DURATION_UNKNOWN �̂Ƃ��͂ǂ�����ׂ����H�H
	public abstract double getPlayerDuration();

	public abstract boolean isPlayerStarted();

	public abstract boolean isPlaying();

	public abstract boolean isNotReady();

	public abstract String getStatusAsString();

	public abstract boolean isCastDone();

	public abstract void resetCastDone();

	public abstract void unload();

	public abstract void setGainAsDB(float db);

}