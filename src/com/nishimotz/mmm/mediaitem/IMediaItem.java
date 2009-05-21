/*
 * $Id: IMediaItem.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.mmm.mediaitem;

public interface IMediaItem {
	public static enum TYPE {VOICE, STICKER}
	public void setType(TYPE a);
	public TYPE getType();
}
