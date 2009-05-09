/*
 * $Id: MediaItemListener.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.mmm.mediaitem;

import com.nishimotz.mmm.caster.CasterEvent;
// import javax.media.ControllerEvent;

public interface MediaItemListener {
	// javax.media.ControllerEvent;
	public void update(CasterEvent e);
}
