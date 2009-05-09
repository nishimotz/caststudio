/*
 * $Id: CastStudioFrame.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.mmm;

// TODO: drawXXXX �� Drawable �I�u�W�F�N�g�̃��X�g�Ƃ��ĊǗ��H

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.logging.Logger;

import com.nishimotz.mmm.cuesheet.CueSheet;
import com.nishimotz.mmm.inspector.Inspector;
import com.nishimotz.mmm.mediaitem.MediaItem;
import com.nishimotz.mmm.porter.Porter;
import com.nishimotz.util.Messages;

/**
 *
 */
class CastStudioFrame extends Frame {
	private Logger logger = CastStudio.logger;

	private final int systemInfoPosX = 600;
	private final int systemInfoPosY = 730;
	
	private final Color workspaceColor         = new Color(0x474a4f);
	private final Color noMemoryWorkspaceColor = new Color(0x880000);
	private final Color infoFontColor2 = new Color(0x888888);
	private final Font itemFont 
		= new Font(Messages.getString("ItemFont"), Font.BOLD, 14);

	private static final long serialVersionUID = 1L;
	
	private static final int noMemoryWarningThres = 10;
	
	private final CastStudio castStudio;
	private Image bufferImage = null;
	private Graphics ct = null;
	private Dimension dim = null;

	private Runtime runtime = null;

	private boolean repaintByRedraw;

	public CastStudioFrame(CastStudio studio) {
		super(CastStudio.getAppTitle());
		this.castStudio = studio;
		runtime = Runtime.getRuntime();
	}
    
	// �`�惍�W�b�N�̎���
    private synchronized void drawMediaItems(Graphics ct) {
    	try {
	    	List<MediaItem> items = castStudio.getMediaItems();
	    	if (items == null) return;
			MediaItem draggingItem = castStudio.getDraggingMediaItem();
	    	// Iterator �I�u�W�F�N�g�𓯊�������
	    	synchronized (items) {
		    	for (MediaItem mi : items) {
		    		if (mi != draggingItem) {
		    			mi.draw(ct);
		    		}
		    	}
			}
	    	if (draggingItem != null) {
	    		draggingItem.draw(ct);
	    	}
    	} catch (ConcurrentModificationException e) {
    		logger.severe("drawMediaItems: ConcurrentModificationException");
    	}
    }
    
    private void drawCueSheet(Graphics ct) {
    	CueSheet cs;
    	cs = this.castStudio.getCueSheet(0);
    	if(cs != null) {
    		cs.drawCueSheet(ct);
    	}
    	cs = this.castStudio.getCueSheet(1);
    	if(cs != null) {
    		cs.drawCueSheet(ct);
    	}
    }
    
    private void drawRecycler(Graphics ct) {
    	Porter p = this.castStudio.getRecycler();
    	if(p != null) {
    		p.draw(ct);
    	}
    }
    
    private void drawStickerHolder(Graphics ct) {
    	Porter p = this.castStudio.getStickerHolder();
    	if(p != null) {
    		p.draw(ct);
    	}
    }
    
    private void drawPorter(Graphics ct) {
    	Porter p = this.castStudio.getPorter();
    	if(p != null) {
    		p.draw(ct);
    	}
    }
    
    private void drawInspector(Graphics ct) {
    	Inspector in = this.castStudio.getInspector();
    	if(in != null) {
    		in.draw(ct);
    	}
    }
    
    private void drawInspectorProperty(Graphics ct) {
    	Inspector in = this.castStudio.getInspector();
    	if (in != null) {
    		in.drawPropertyView(ct);
    	}
    }
    
    public int getFreeMemoryPercent() {
		long freeMem = runtime.freeMemory();
		long totalMemory = runtime.totalMemory();
		return (int)Math.floor(freeMem * 100.0 / totalMemory);
    }
    
	private void drawSystemInfo(Graphics ct) {
		long freeMem = runtime.freeMemory();
		long totalMemory = runtime.totalMemory();
		long usedMemory = totalMemory - freeMem;
		String txt1 = String.format("Total:%,14d UsedMemory:%,14d Free:%3d",  
				totalMemory,
				usedMemory, 
				getFreeMemoryPercent()
				);
		ct.setFont(itemFont);
		ct.setColor(infoFontColor2);
		ct.drawString(txt1, systemInfoPosX, systemInfoPosY);
	}
    
    public void update(Graphics g) {
    	drawFrame(g);
	}

	public void paint(Graphics g) {
		if (repaintByRedraw) {
	    	drawFrame(g);
	    	repaintByRedraw = false;
		} else {
			if (bufferImage != null) {
				g.drawImage(bufferImage, 0, 0, this);
			}
		}
    }
	
    public synchronized void drawFrame(Graphics g) {
    	if (bufferImage == null) {
    		dim = getSize();
	    	bufferImage = createImage(dim.width, dim.height);
	    	ct = bufferImage.getGraphics();
    	} else {
    		Dimension d2 = getSize();
    		if (!dim.equals(d2)) {
    			// resized!!
    			bufferImage = null;
        		dim = getSize();
    	    	bufferImage = createImage(dim.width, dim.height);
    	    	ct = bufferImage.getGraphics();
    		}
    	}
    	if (getFreeMemoryPercent() < noMemoryWarningThres) {
    		ct.setColor(noMemoryWorkspaceColor);
    	} else {
    		ct.setColor(workspaceColor);
    	}
		ct.fillRect(0, 0, dim.width, dim.height);
		
		// �������d�v�B�������Ɍ������ĕ`�悷��
		drawSystemInfo(ct);
		drawRecycler(ct);
		drawStickerHolder(ct);
		drawPorter(ct);
        drawCueSheet(ct);
		drawInspector(ct);
        drawMediaItems(ct);
    	drawInspectorProperty(ct);
        
        // ���z�o�b�t�@�����ʂɕ`��
        g.drawImage(bufferImage, 0, 0, this);
    }

	public void setRepaintByRedraw(boolean b) {
		repaintByRedraw = b;
	}

}