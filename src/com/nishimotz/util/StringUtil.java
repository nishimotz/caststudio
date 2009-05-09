/*
 * $Id: StringUtil.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.util;

//import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class StringUtil {

    /**
     * •¶š—ñ‚Ì•ªŠ„i‹æØ‚è•¶šw’èj
     * @param str ‘ÎÛ•¶š—ñ
     * @param delim ‹æØ‚è•¶š—ñ
     * @return •ªŠ„Œã‚Ì•¶š—ñ
     */
	public static String[] split(String str, String delim) {
		StringTokenizer st = new StringTokenizer(str, delim);
		int length = st.countTokens();
		String[] result = new String[length];
		for (int i = 0; i < length; i++) {
			result[i] = st.nextToken();
		}
		return result;
	}

//	private static DecimalFormat formatter1 = new DecimalFormat("00");
//	private static DecimalFormat formatter2 = new DecimalFormat("00"); // 00.0

	// TODO: test ‚ğ‘‚­
    public static String formatTime(double d)
    {
    	double min = Math.floor(d / 60.0);
    	double sec = d - min * 60.0;
    	// return formatter1.format(min) + ":" + formatter2.format(sec);
        return String.format("%2.0f:%02.0f", min, sec);
    }

    public static String formatTime2(double d)
    {
    	double min = Math.floor(d / 60.0);
    	double sec = d - min * 60.0;
        return String.format("%2.0f:%04.1f", min, sec);
    }

	// I: file:/c:/recordings/2005-11/koshira051113/ic_d_014.mp3
	// O: ic_d_014.mp3
	public static String getFileBody(String urlstr) {
		int pos = urlstr.lastIndexOf("/");
		return urlstr.substring(pos + 1);
	}

	// I: file:/c:/recordings/2005-11/koshira051113/ic_d_014.mp3
	// O: ic_d_014
	public static String getFileBodyWithoutExt(String urlstr) {
		int pos = urlstr.lastIndexOf("/");
		int pos2 = urlstr.lastIndexOf(".");
		return urlstr.substring(pos + 1, pos2);
	}

	// 
	public static String formatDate(Date date) {
		String ret;
		SimpleDateFormat df = new SimpleDateFormat("MM/dd hh:mm");
		ret = df.format(date);
		return ret;
	}

}
