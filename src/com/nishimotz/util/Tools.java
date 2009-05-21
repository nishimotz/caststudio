/*
 * $Id: Tools.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Tools
{
	private static String _error = "";
	
	public static String getError()
	{
		return _error;
	}
	
	public static String readURL(String filename)
	{
		_error = "";
		String buf = "";
		if (filename.startsWith("http:")) {
			try {
				URL url = new URL(filename);
				HttpURLConnection uc;
				uc = (HttpURLConnection)url.openConnection();
				uc.setRequestMethod("GET");
				InputStreamReader isr = new InputStreamReader(uc.getInputStream());
				BufferedReader br = new BufferedReader(isr);
				while(true) {
					String line = br.readLine();
					if (line == null) 
						break;
					buf += line;
				}
				br.close();
				uc.disconnect();
			} catch ( Exception e ) {
				_error = e.toString();
			}
		} else {
			_error = "readURL() : unsupported function.";
		}
		return buf;
	}
	
	/**
	 * @returns: 0 (OK) / 1 (ERROR)
	 */
	public static int saveURL(String src, String dest)
	{
		_error = "";
		int ret = 0;
		String content = readURL(src);
		if (_error.equals("")) {
			try { 
				Util.writeToFile(dest, content);
			} catch ( Exception e ) {
				_error = e.toString();
				ret = 1;
			}
		} else {
			ret = 1;
		}
		return ret;
	}
	
	public static void saveUrlAsBinary(String filename, String dest)
	{
		_error = "";
		if (filename.startsWith("http:")) {
			try {
				URL url = new URL(filename);
				HttpURLConnection uc;
				uc = (HttpURLConnection)url.openConnection();
				uc.setRequestMethod("GET");
				InputStream is = uc.getInputStream();

				FileOutputStream fos = new FileOutputStream(dest);

				final int SIZE = 8196;
				byte[] buf = new byte[SIZE];
				while(true) {
					int n = is.read(buf,0,SIZE);
					if (n == -1) break;
					fos.write(buf,0,n);
				}
				
				fos.close();

				is.close();
				uc.disconnect();

			} catch ( Exception e ) {
				_error = e.toString();
			}
		} else {
			_error = "readURL() : unsupported function.";
		}
	}
		
	/**
	 * @returns: path
	 */
	public static String mktemp()
	{
		return mktemp("");
	}
	
	
	/**
	 * @returns: path
	 */
	public static String mktemp(String suffix)
	{
		_error = "";
		String ret = "";
		
		String prefix = "mmm";
		
		try { 
			//File temp = File.createTempFile(prefix, suffix);
			File temp = TempFileManager.createTempFile(prefix, suffix);
			temp.deleteOnExit();
			if ( temp != null ) {
				ret = temp.getAbsolutePath();
			}
		} catch ( Exception e ) {
			_error = e.toString();
		}
		return ret;
	}
	
	
	public static void send(String msg)
	{
		System.out.println(msg);
		System.out.flush();
	}

}
