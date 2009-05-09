package com.nishimotz.mmm.caster;

public class SystemTimeProvider {
	// for JavaSoundMediaItemCaster
	
	private static double startupTime = (double)System.currentTimeMillis() / 1000.0;
	
	public static double getSystemTime() { 
		return (double)System.currentTimeMillis() / 1000.0 - startupTime;
	}
}
