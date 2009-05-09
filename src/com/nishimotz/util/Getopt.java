/*
 * $Id: Getopt.java,v 1.1 2009/04/10 09:23:14 nishi Exp $
 */
package com.nishimotz.util;

import java.util.ArrayList;

public class Getopt
{
	private ArrayList<String> options_;
	private int optind_;
	private int optpos_;
	private String optarg_;
	
	public Getopt(String name, String argv[], String opt)
	{
		options_ = new ArrayList<String>();
		optind_ = 0;
		optpos_ = 0;
		optarg_ = "";
		
		for ( int i = 0; i < argv.length; i++ ) {
			String curr = argv[i];
			if ( curr.startsWith("-") && curr.length() == 2 ) {
				String currOpt = curr.substring(1);
				if ( opt.indexOf( currOpt + ":" ) >= 0 ) {
					if ( i+1 < argv.length ) {
						String item = currOpt + ":" + argv[i+1];
						options_.add(item);
						i++;
					}
				} else {
					options_.add(currOpt);
				}
			} else {
				optind_ = i;
				return;
			}
		}
		optpos_ = 0;
	}
	
	public int getOptind()
	{
		return optind_;
	}
	
	public int getopt()
	{
		if ( optpos_ < options_.size() ) {
			String curr = (String)(options_.get(optpos_));
			if ( curr.length() > 1 ) {
				optarg_ = curr.substring(2);
			} else {
				optarg_ = "";
			}
			if ( curr.length() > 0 ) {
				optpos_++;
				return curr.charAt(0);
			}
		} 
		return -1;
	}
	
	public String getOptarg()
	{
		return optarg_;
	}
	
	public void setOpterr(boolean opterr)
	{
		// ignored
	}
	
	
	public static void main(String argv[])
	{
		// -c xxxx 
		// -p (flag)
		Getopt g = new Getopt("", argv, "c:p");
		g.setOpterr(false);
		int c;
		while ((c = g.getopt()) != -1){
			switch (c)	{
			case 'c':
				System.out.println("option c:" + g.getOptarg());
				break;
			case 'p':
				System.out.println("option p=true");
				break;
			}
		}
		
		if (argv.length - g.getOptind() != 1){
			System.out.println("Usage: hoge");
			System.exit(1);
		} else {
			System.out.println("arg:" + argv[g.getOptind()]);
		}
	}
}
