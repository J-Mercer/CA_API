package com.sdca.api.util;

import com.sdca.api.ApiApplication;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/*  
 * 利用HttpClient进行post请求的工具类  
 */  
public class HttpClientUtil {
	private static Logger logger = LoggerFactory.getLogger(ApiApplication.class);
	public static String Post(String urlString, String data, String charset){
		String result = "";
		//String data = JSON.toJSONString(map);
		logger.info("---------------------------------------------请求参数--------------------------------------------------------");
		logger.info(data);
        BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			// 设置文件类型:
			conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
			// 设置接收类型否则返回415错误
			//conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;
			conn.setRequestProperty("accept","application/json");
			// 往服务器里面发送数据
			if (data != null && !TextUtils.isEmpty(data)) {
				byte[] writebytes = data.getBytes();
				// 设置文件长度
				conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
				OutputStream outwritestream = conn.getOutputStream();
				outwritestream.write(data.getBytes("UTF-8"));
				outwritestream.flush();
				outwritestream.close();
			}
			if (conn.getResponseCode() == 200) {
				reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				result = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(result);
		logger.info("---------------------------------------------响应参数--------------------------------------------------------");
		logger.info(result);
		return result;
	}
	public static String PostUrlEncoded(String urlString, String param){
		System.out.println(param);
		String result = "";
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			// 设置文件类型:
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			// 设置接收类型否则返回415错误
			//conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;
			conn.setRequestProperty("accept","application/json");
			// 往服务器里面发送数据
			if (param != null && !TextUtils.isEmpty(param)) {
				byte[] writebytes = param.getBytes();
				// 设置文件长度
				conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
				OutputStream outwritestream = conn.getOutputStream();
				outwritestream.write(param.getBytes("UTF-8"));
				outwritestream.flush();
				outwritestream.close();
			}
			if (conn.getResponseCode() == 200) {
				reader = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				result = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(result);
		return result;
	}

}  
