/*
 */
package com.nishimotz.util;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JFrame;

public class MultipleBuffers {
	
	/**
	 * TODO: class method Ç…ÇµÇƒÅAinit / iteration / finalize Ç…ï™äÑÅB 
	 */
	public static void main(String[] args) {
		GraphicsEnvironment graphicsEnvironment =
			GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice graphicsDevice =
			graphicsEnvironment.getDefaultScreenDevice();
		DisplayMode displayModes[] = graphicsDevice.getDisplayModes();
		DisplayMode originalDisplayMode = graphicsDevice.getDisplayMode();
		//
		JFrame frame = new JFrame();
		frame.setUndecorated(true);
		frame.setIgnoreRepaint(true);
		try {
			if (graphicsDevice.isFullScreenSupported()) {
				graphicsDevice.setFullScreenWindow(frame);
			}
			Random random = new Random();
			
			int nmodes = displayModes.length;
			int mode = 0;
			for (int i=0; i<nmodes; i++) {
				DisplayMode displayMode = displayModes[i];
/*				System.out.printf("%d %d %d %d %d\n",
						i, 
						displayMode.getWidth(),
						displayMode.getHeight(), 
						displayMode.getRefreshRate(), 
						displayMode.getBitDepth());
*/				
				if (displayMode.getWidth() == 1024
						&& displayMode.getHeight() == 768
						&& displayMode.getBitDepth() == 16) {
					mode = i;
				}
			}
			DisplayMode displayMode = displayModes[mode];
			if (graphicsDevice.isDisplayChangeSupported()) {
				graphicsDevice.setDisplayMode(displayMode);
			}
			frame.createBufferStrategy(2);
			BufferStrategy bufferStrategy = frame.getBufferStrategy();
			int width = frame.getWidth();
			int height = frame.getHeight();
			for (int i=0; i<200; i++) {
				Graphics g = bufferStrategy.getDrawGraphics();
				g.setColor(new Color(random.nextInt(0xffffff)));
				g.fillRect(random.nextInt(width),
						random.nextInt(height), 100, 100);
				bufferStrategy.show();
				g.dispose();
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
		} finally {
			graphicsDevice.setDisplayMode(originalDisplayMode);
			graphicsDevice.setFullScreenWindow(null);
		}
		System.exit(0);
	}
	
}
