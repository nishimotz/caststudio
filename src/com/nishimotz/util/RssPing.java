///*
// * $Id: RssPing.java,v 1.2 2009/05/18 01:30:28 nishi Exp $
// * http://funyami.pya.jp/java/development_history.html
// */
//package com.nishimotz.util;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
//import java.net.URL;
//
//public class RssPing {
//    private URL[] pingURL=new URL[7];
//    private String pageTitleString;
//    private String pageURLString;
//    private String rssPingString;
//    private HttpURLConnection con;
//    private OutputStreamWriter osw;
//    
//    RssPing(){
//         this.cleanupFirst();
//         this.cleanup();
//    } 
//    //全ての必要情報を始めにセットアップ
//    private void cleanupFirst(){
//         pageTitleString=new String("はらほれひれはれ"); // サイトのトップページのタイトル
//         pageURLString=new String("http://harahore.hirehare/"); // サイトのトップページのアドレス
//         rssPingString=this.makeRssString();
//         //System.setProperty("http.proxyHost",""); // proxy経由の場合 proxy ホスト名をセット
//         //System.setProperty("http.proxyPort",""); // proxy経由の場合 proxy ポート番号をセット
//         
//         try { // ping受信サーバを登録
//              pingURL[0]=new URL("http://ping.cocolog-nifty.com/xmlrpc");
//              pingURL[1]=new URL("http://www.blogpeople.net/servlet/weblogUpdates");
//              pingURL[2]=new URL("http://ping.bloggers.jp/rpc/");
//              pingURL[3]=new URL("http://ping.myblog.jp");
//              pingURL[4]=new URL("http://bulkfeeds.net/rpc");
//              pingURL[5]=new URL("http://blog.goo.ne.jp/XMLRPC");
//         } catch (MalformedURLException e) {
//              e.printStackTrace();
//         }
//    }
//
//    private String makeRssString(){
//         StringBuffer rssPingString=new StringBuffer();
//         rssPingString.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><methodCall><methodName>");
//         rssPingString.append("weblogUpdates.ping</methodName><params><param><value>");
//         rssPingString.append(pageTitleString);
//         rssPingString.append("</value></param><param><value>");
//         rssPingString.append(pageURLString);
//         rssPingString.append("</value></param></params></methodCall>");
//
//         return rssPingString.toString();
//    }
//
//    private void cleanup(){
//         con=null;
//         osw=null;
//    }
//
//    private void getConnection(String method, URL url){
//         try {
//              con=(HttpURLConnection) url.openConnection();
//              con.setRequestMethod(method);
//              HttpURLConnection.setFollowRedirects(false);
//              con.setInstanceFollowRedirects(false);
//         } catch (ProtocolException e1) {
//                   e1.printStackTrace();
//         } catch (IOException e) {
//              e.printStackTrace();
//         }
//    }
//
//    private void putOutTheStream(String str){
//         con.setDoOutput(true);
//         try {
//              osw=new OutputStreamWriter(con.getOutputStream());
//              osw.write(str);
//              osw.flush();
//              osw.close();
//         } catch (IOException e) {
//              e.printStackTrace();
//         }
//    }
//    
//    private void connection(){
//         try {
//              con.connect();
//         } catch (IOException e) {
//              e.printStackTrace();
//         }
//    }
//    
//    private void wasteTheResponse(){
//         try {
//              Thread.sleep(500);
//         } catch (InterruptedException e1) {
//              e1.printStackTrace();
//         }
//         BufferedReader br;
//         String fld;
//         try {
//              br=new BufferedReader(new InputStreamReader(con.getInputStream()));
//              while((fld=br.readLine())!=null){
//                   System.out.print(fld);
//              }
//         } catch (IOException e) {
//              e.printStackTrace();
//         }
//         con.disconnect();
//    }
//
//    private void updateIt(){
//         for(int i=0;i<6;i++){ // ping受信サーバの数だけ繰り返し
//              cleanup();
//              getConnection("POST",pingURL[i]);
//              con.setRequestProperty("Content-type","text/xml");
//              putOutTheStream(rssPingString);
//              connection();
//              wasteTheResponse();
//              System.out.println();
//         }
//    }
//    
//    public static void main(String[] args) {
//         RssPing thisApp=new RssPing();
//         thisApp.updateIt();
//    }
//
//}
