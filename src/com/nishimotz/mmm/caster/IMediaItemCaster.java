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

	// 実際に再生中であれば true
	public abstract boolean isCasting();

	// STOPPING 状態 -> Thread.run で READY に遷移する。
	// AWT イベントスレッドから呼び出されたときに、
	// ブロッキングすると画面更新が遅延してしまう。
	// これを回避するために Thread で処理する。
	public abstract void stop();

	public abstract double getBaseTimeInSec();

	// DURATION_UNKNOWN のときはどうするべきか？？
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