/*
 * $Id: Util.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.util;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.net.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Util 
{
	public Util() 
	{
	}
	
	
	public static int getPropertyInt(String name, int defval)
	{
		String s = System.getProperty(name, null);
		if ( s == null ) 
			return defval;
		return Integer.parseInt(s);
	}
	
	
	public static String getPropertyStr(String name, String defval)
	{
		String s = System.getProperty(name, null);
		if ( s == null ) 
			return defval;
		return s;
	}
	
	
	public static void halt(String msg)
	{
		System.err.println(msg);
		while(true) {
			try { 
				Thread.sleep(1000);
			} catch (Exception e) {e.printStackTrace();}
		}
	}
	
	
	public static void runtimeError(String msg)
	{
		System.err.println(msg);
	}
	
	
	public static String removeSpaces(String str)
	{
		StringBuffer retStr = new StringBuffer("");
		
		if(str == null)
			return "";
		
		StringTokenizer st = new StringTokenizer(str, " \t\n\r");
		while (st.hasMoreTokens()) {
			retStr.append(st.nextToken());
		}
		
		if(retStr.length() == 0)
			return "";
		
		return retStr.toString();
		
	}
	
	
	/**
	 */
	public static String xmlSafeRemoveSpaces(String str)
	{
		if (str == null)
			return "";
		
		StringBuffer retStr = new StringBuffer("");
		String token;
		boolean inTag = false;
		
		StringTokenizer st = new StringTokenizer(str, " \t\n\r");
		while (st.hasMoreTokens()) {
			token = st.nextToken();
			if (token.startsWith("<") ) {
				if (!token.equals("<")) {
					inTag = true;
				}
			}
			if (token.endsWith(">") ) {
				inTag = false;
			}
			retStr.append(token);
			if ( inTag ) {
				retStr.append(" ");
			}
		}
		
		if(retStr.length() == 0)
			return "";
		
		return retStr.toString();
	}
	
	
	public static String removeNewLines(String str)
	{
		StringBuffer retStr = new StringBuffer("");
		
		if(str == null)
			return "";
		
		StringTokenizer st = new StringTokenizer(str, "\n\r");
		while (st.hasMoreTokens()) {
			retStr.append(st.nextToken());
		}
		
		if(retStr.length() == 0)
			return "";
		
		return retStr.toString();
		
	}
	
	
	/**
	 */
	public static String encodeXmlChars(String s)
	{
		if (s==null) return "";
		StringBuffer t = new StringBuffer("");
		for ( int i = 0; i < s.length(); i++ ) {
			char ch = s.charAt(i);
			if ( ch == '<' ) {
				t.append("&lt;");
			} else if ( ch == '>' ) {
				t.append("&gt;");
			} else if ( ch == '&' ) {
				t.append("&amp;");
			} else {
				t.append(ch);
			}
		}
		return t.toString();
	}
	
	
	public static String decodeXmlChars(String s)
	{
		if ( s == null ) return "";
		String ret = s;
		ret = ret.replaceAll("&lt;", "<");
		ret = ret.replaceAll("&gt;", ">");
		ret = ret.replaceAll("&amp;", "&");
		return ret;
	}
	
	
	/**
	 * base   : http://aaa.bbb.com/index.vxml
	 * rel    : sub/next.vxml
	 * return : http://aaa.bbb.com/sub/next.vxml
	 *
	 * base   : http://aaa.bbb.com/index.vxml
	 * rel    : sub/next.vxml#hoge
	 * return : http://aaa.bbb.com/sub/next.vxml#hoge
	 */
	public static String resolveAdrs(String base, String rel)
	{
		String s;
		if ( base == null || rel == null ) {
			return "";
		}
		try {
			URI u1 = new URI(base);
			URI u2 = new URI(rel);
			URI u3 = u1.resolve(u2);
			s = u3.toString(); 
			
			/*
			 String fragment = Util.getUriFragment(rel);
			 if ( fragment != null ) {
			 s += "#" + fragment;
			 }
			 */
			
		} catch ( URISyntaxException e ) {
			s = "_error_resolveAdrs_";
		}
		
		return s;
	}
	
	
	// in:  hogehoge.vxml#fragment
	// out: fragment
	public static String getUriFragment(String uri)
	{
		String fragment = Util.getFirstGroup("^[^#]+#(.*)$", uri);
		if (fragment == null) {
			return "";
		}
		return fragment;
	}
	
	
	// in:  hogehoge.vxml#fragment
	// out: hogehoge.vxml
	public static String getUriWithoutFragment(String uri)
	{
		String path = Util.getFirstGroup("^([^#]+)#.*$", uri);
		if (path == null) {
			return uri;
		}
		return path;
	}
	
	
	/**
	 * Usage: Util.getFirstGroup("^[^#]+#(.*)$", uri)
	 */
	public static String getFirstGroup(String pat, String str)
	{
		Pattern p = Pattern.compile(pat);
		Matcher m = p.matcher(str);
		if (m.matches()) {
			return m.group(1);
		}
		return null;
	}
	
	
	/**
	 * f1: sub/file.vxml#a f2: sub/file.vxml#b -> true
	 */
	public static boolean isSameFile(String f1, String f2)
	{
		boolean b = false;
		try {
			URI u1 = new URI(f1);
			URI u2 = new URI(f2);
			
			URI u3 = new URI
			(u1.getScheme(), 
					u1.getUserInfo(),
					u1.getHost(),
					u1.getPort(),
					u1.getPath(),
					u1.getQuery(),
					u2.getFragment());
			if ( u2.equals(u3) )
				b = true;
		} catch ( URISyntaxException e ) {}
		return b;
	}
	
	
	public static void writeToFile(String file, String content) throws Exception
	{
		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(content);
		bw.close();
		osw.close();
		fos.close();
	}
	
	
	public static String loadFromFile(String file) throws Exception
	{
		String content = "";
		FileReader fr = new FileReader(file);
		BufferedReader reader = new BufferedReader(fr);
		String line;
		while ((line = reader.readLine()) != null) {
			content += line;
			content += "\n";
		}
		reader.close();
		return content;
	}
	
	
	/**
	 * In : "tokyo kyoto osaka"
	 * In : "tokyo   kyoto    osaka"
	 * Out: new Vector() of String
	 *
	 * Usage:
	 *   cmdlist: to @AM-MCL set Speak = xx;to @AM-MCL set Speak = xx
	 *
	 *   List v = Util.makeTokenizedList(cmdlist, ";");
	 *   for (int i = 0; i < v.size(); i++ ) {
	 *       String cmd = (String)v.get(i);
	 *       _send(cmd);
	 *   }
	 */
	@SuppressWarnings("unchecked")
	public static List makeTokenizedList(String src, String delim)
	{
		List v = new Vector();
		
		// If the flag is false, delimiter characters serve to separate tokens.
		StringTokenizer st = new StringTokenizer(src, delim, false);
		while (st.hasMoreTokens()) {
			v.add((Object)st.nextToken());
		}
		return v;
	}
	
	
	public static String removeTags(String str)
	{
		String s = str.replaceAll("<[^>]*>", "");
		return s;
	}
	
	
	public static String getStringHead(String str)
	{
		str = Util.removeNewLines(str);
		if ( str.length() > 40 ) {
			str = str.substring(0,40) + "...";
		}
		return str;
	}
	
	public static Element getRootElementFromUrl(String httpUrl) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbf.newDocumentBuilder();
		Document doc = builder.parse(httpUrl);
		Element root = doc.getDocumentElement();
		return root;
	}
	
	public static String getStringByTagName(Element root, String tagName) {
		NodeList list = root.getElementsByTagName(tagName);
		if (list.getLength() >= 1) {
			return list.item(0).getTextContent();
		}
		return "";
	}

	public static int getIntegerByTagName(Element root, String tagName, int df) {
		NodeList list = root.getElementsByTagName(tagName);
		if (list.getLength() >= 1) {
			String s = list.item(0).getTextContent();
			return Integer.parseInt(s);
		}
		return df;
	}

	public static float getFloatByTagName(Element root, String tagName, float df) {
		NodeList list = root.getElementsByTagName(tagName);
		if (list.getLength() >= 1) {
			String s = list.item(0).getTextContent();
			return Float.parseFloat(s);
		}
		return df;
	}

	public static double getDoubleByTagName(Element root, String tagName, double df) {
		NodeList list = root.getElementsByTagName(tagName);
		if (list.getLength() >= 1) {
			String s = list.item(0).getTextContent();
			return Double.parseDouble(s);
		}
		return df;
	}

	public static String doHttpGet(String httpUrl) throws Exception {
		URL url = new URL(httpUrl);
		HttpURLConnection uc;
		uc = (HttpURLConnection)url.openConnection();
		uc.setRequestMethod("GET");
		InputStreamReader isr = new InputStreamReader(uc.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		String buf = "", s;
		while((s = br.readLine()) != null) {
			buf += s;
		}
		br.close();
		uc.disconnect();
		return buf;
	}

	/**
	 * make Util.run
	 */
	public static void main(String args[]) throws Exception
	{
		/*
		String src, s, s1, s2;
		 src = "a\nb c < hoge > <br/><hr/> <audio href=xxx src=\"wav\"/> def <div class=\"ggg\">title</div>";
		 //s1 = Util.removeSpaces(src);
		  //System.out.println(s1);
		   s2 = Util.xmlSafeRemoveSpaces(src);
		   System.out.println(s2);
		   */
		
		/*
		 s = Util.resolveAdrs("http://aaa.bbb.com/index.vxml", "sub/next.vxml");
		 System.out.println(s); // http://aaa.bbb.com/sub/next.vxml
		 
		 s = Util.resolveAdrs("/lab/common/src/nishi/galatea/gdm-current/phoenix/", 
		 "../tests/audio/hoge.wav");
		 System.out.println(s);
		 
		 s = Util.resolveAdrs("/lab/common/src/nishi/galatea/gdm-current/phoenix", 
		 "../tests/audio/hoge.wav");
		 System.out.println(s);
		 */
		
		String s;
		s = "if (1<=num && 2>nantoka) {infiniteLoop}";
		System.out.println(s);
		s = Util.encodeXmlChars(s);
		System.out.println(s);
		s = Util.decodeXmlChars(s);
		System.out.println(s);
	}


	// [I] "http://hoge/hoge.wav"
	// [O] ".wav"
	public static String getSuffix(String str) {
		int pos = str.lastIndexOf('.');
		if (pos == -1) return "";
		return str.substring(pos);
	}

	
	/**
	 * @returns: path
	 */
	public static String mktemp(String suffix)
	{
		String ret = "";
		String prefix = "orabe";
		try { 
			File temp = File.createTempFile(prefix, suffix);
			temp.deleteOnExit();
			if ( temp != null ) {
				ret = temp.getAbsolutePath();
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static void saveUrlAsBinary(String filename, String dest)
	{
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
				e.printStackTrace();
			}
		} else {
			System.err.println("saveUrlAsBinary : unsupported function.");
		}
	}
}
