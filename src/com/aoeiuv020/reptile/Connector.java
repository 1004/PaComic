/* ***************************************************
	^> File Name: Connector.java
	^> Author: AoEiuV020
	^> Mail: 490674483@qq.com
	^> Created Time: 2016/04/10 - 22:02:39
*************************************************** */
package com.aoeiuv020.reptile;
import com.aoeiuv020.tool.Tool;
import com.aoeiuv020.tool.Logger;
import com.aoeiuv020.tool.Stream;
import android.util.Log;
import android.webkit.*;
import android.content.Context;
import org.json.*;
import java.util.*;
import java.net.*;
import java.io.*;
public class Connector
{
	private static Connector mConnector=new Connector();
	private static final String TAG="aoeiuv020 Connector";
	private String mEncoding=null;
	private JSONObject mArgs=null;
	private URL mLastUrl=null;
	private Context mContext=null;
	private String mHtml=null;
	private Connector()
	{
		mArgs=new JSONObject();
		Tool.put(mArgs,"encoding","UTF-8");
		Tool.put(mArgs,"useragent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36");
	}
	public void setContext(Context context)
	{
		mContext=context;
	}
	/**
	 * 耗时方法，
	 * 后台开个WebView处理Js,
	 */
	public void loadWithJs(String url,int timeout)
	{
		//mLastUrl存起来作Referer伪装用，
		try
		{
			if(mLastUrl==null)
				mLastUrl=new URL(getBaseurl());
			mLastUrl=new URL(mLastUrl,url);
			url=mLastUrl.toString();
		}
		catch(MalformedURLException e)
		{
		}
		mHtml=WebViewDaemon.getInstance().load(url,timeout);
	}
	/**
	 * 不耗时方法，
	 */
	public String getHtml()
	{
		return mHtml;
	}
	/**
	 * 耗时方法，
	 */
	public void load(String url)
	{
		//mLastUrl存起来作Referer伪装用，
		try
		{
			if(mLastUrl==null)
				mLastUrl=new URL(getBaseurl());
			mLastUrl=new URL(mLastUrl,url);
		}
		catch(MalformedURLException e)
		{
			Logger.e(e);
		}
		InputStream input=getInputStream(url);
		mHtml=Stream.read(input,Tool.getString(mArgs,"encoding"));
	}
	public String getEncoding()
	{
		String encoding=Tool.getString(mArgs,"encoding");
		if(Tool.isEmpty(encoding))
			encoding="UTF-8";
		return encoding;
	}
	public InputStream getInputStream(String url)
	{
		if(Tool.isEmpty(url))
			return null;
		InputStream input=null;
		try
		{
			HttpURLConnection http=openConnection(url);
			input=http.getInputStream();
		}
		catch(Exception e)
		{}
		return input;
	}
	private HttpURLConnection openConnection(String sUrl)throws IOException,MalformedURLException
	{
		if(mLastUrl==null)
			mLastUrl=new URL(getBaseurl());
		URL url=new URL(mLastUrl,sUrl);
		HttpURLConnection http=(HttpURLConnection)url.openConnection();
		String useragent=Tool.getString(mArgs,"useragent");
		if(useragent!=null)
			http.setRequestProperty("User-Agent",useragent);
		if(mLastUrl!=null)
			http.setRequestProperty("Referer",mLastUrl.toString());
		http.setUseCaches(true);
		return http;
	}
	void putAll(JSONObject json)
	{
		Iterator<String> iterator=json.keys();
		while(iterator.hasNext())
		{
			String key=iterator.next();
			Tool.put(mArgs,key,Tool.getString(json,key));
		}
	}
	public String getBaseurl()
	{
		String baseurl="";
		try
		{
			if(mArgs.has("baseurl"))
			{
				baseurl=mArgs.getString("baseurl");
			}
		}
		catch(JSONException e)
		{
		}
		return baseurl;
	}
	public static Connector getInstance()
	{
		return mConnector;
	}
}