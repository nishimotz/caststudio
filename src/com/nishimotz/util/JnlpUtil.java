// $Id: JnlpUtil.java,v 1.2 2009/05/18 01:30:28 nishi Exp $
/*
package com.nishimotz.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.jnlp.BasicService;
import javax.jnlp.FileContents;
import javax.jnlp.PersistenceService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

import com.nishimotz.mmm.CastStudio;

public class JnlpUtil {
	private static Logger logger = CastStudio.logger;
	
	static final boolean use = true;
	
	static final long MUFIN_DEFAULT_SIZE = 1024;
	
	static BasicService bs ;
	static PersistenceService ps;
	
	public static void init() {
		try {
			bs = null;
			ps = null;
			bs = (BasicService)
			ServiceManager.lookup("javax.jnlp.BasicService");
			ps = (PersistenceService)
			ServiceManager.lookup("javax.jnlp.PersistenceService");
			logger.info("use JNLP API");
		} catch (UnavailableServiceException e) {
			//e.printStackTrace();
			logger.info("This application is NOT launched by Java Web Start, so NOT use JNLP API");
		}
	}
	
	public static boolean canUse(){
		if(use && ps != null && bs != null)
			return true;
		else
			return false;
	}
	
	//Webブラウザにurlを表示
	static boolean showDocument(URL url){
		return bs.showDocument(url);
	}
	
	static boolean isWebBrowserSupported(){
		if(bs == null)
			return false;
		return bs.isWebBrowserSupported();
	}
	
	static InputStream getMuffinAsInputStream(String mufin){
		try {
			URL baseURL = bs.getCodeBase();
			URL mufinURL = new URL(baseURL, mufin);
			FileContents fc = ps.get(mufinURL);
			return fc.getInputStream();
		} catch (MalformedURLException e){
			e.printStackTrace();
		} catch (FileNotFoundException e){
			//ファイルがなければ無視すればいい。
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;//dummy
	}
	
	*//**
	 * 
	 * @param mufin : "hoge" "../hoge" etc.
	 * @param maxsize : バイト数
	 * @return
	 *//*
	static OutputStream getMuffinAsOutputStream(String mufin, long maxsize){
		try {
			URL baseURL = bs.getCodeBase();
			URL mufinURL = new URL(baseURL, mufin);
			try{
				ps.delete(mufinURL);
			} catch (NullPointerException e){
				//存在しないものを消そうとすると発生？
				e.printStackTrace();
				System.out.println("only ignore");
			}
			@SuppressWarnings("unused") long size = ps.create(mufinURL, maxsize);
			FileContents fc = ps.get(mufinURL);
			return fc.getOutputStream(true);
		} catch (MalformedURLException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;//dummy
	}
	
	public static OutputStream getMuffinAsOutputStream(String mufin){
		return getMuffinAsOutputStream(mufin, MUFIN_DEFAULT_SIZE);
	}

	public static URL getMuffinURL(String mufin, long maxsize){
		try {
			URL baseURL = bs.getCodeBase();
			URL mufinURL = new URL(baseURL, mufin);
			try{
				ps.delete(mufinURL);
			} catch (FileNotFoundException e) {
				// 
				System.out.println("file not found : ignored");
			} catch (NullPointerException e){
				//存在しないものを消そうとすると発生？
				e.printStackTrace();
				System.out.println("only ignore");
			}
			@SuppressWarnings("unused") long size = ps.create(mufinURL, maxsize);
			FileContents fc = ps.get(mufinURL);
			String fileName = "file:" + fc.getName();
			System.out.println("fileName " + fileName);
			return new URL(fileName);
//			return fc.getOutputStream(true);
		} catch (MalformedURLException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;//dummy
	}
	
}
*/
