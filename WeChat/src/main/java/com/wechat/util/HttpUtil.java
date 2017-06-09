package com.wechat.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;
/*import org.lrd.wechat.constant.Constants;*/

import com.alibaba.fastjson.JSONObject;

public class HttpUtil {
	private static Logger logger = Logger.getLogger(HttpUtil.class);
	private static boolean isproxy = true;
	private static String proxyHost = "172.17.8.21";
	private static int proxyPort = 8090;

	/**
	 * 请求微信接口链接专用
	 * 
	 * @param accessUrl
	 * @return
	 */
	public static JSONObject getHttpsGetReq(String accessUrl) {

		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;
		HttpsURLConnection connection = null;
		try {
			URL url = new URL(accessUrl);
			if (isproxy) {// 使用代理模式
				@SuppressWarnings("static-access")
				Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP,
						new InetSocketAddress(proxyHost, proxyPort));
				connection = (HttpsURLConnection) url.openConnection(proxy);
			} else {
				connection = (HttpsURLConnection) url.openConnection();
			}

			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.connect();
			/*
			 * Map<String, List<String>> map = connection.getHeaderFields(); for
			 * (String key : map.keySet()) { System.out.println(key + "--->" +
			 * map.get(key)); }
			 */
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			logger.error("HTTPS GET err:" + e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				connection.disconnect();
			} catch (Exception e) {
				logger.error("HTTPS GET [close resouse] err:" + e.getMessage());
			}
		}
		JSONObject jsonObject = JSONObject.parseObject(sb.toString());
		/*if (null != jsonObject.get(Constants.ERR_CODE)) {
			logger.error(jsonObject.get(Constants.ERR_CODE) + " : "
					+ jsonObject.get(Constants.ERR_MSG));
			return null;
		}*/
		return jsonObject;

	}

	public static String getHttpGetReq(String accessUrl) {
		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;
		HttpURLConnection httpConn = null;
		try {
			URL url = new URL(accessUrl);
			if (isproxy) {// 使用代理模式
				@SuppressWarnings("static-access")
				Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP,
						new InetSocketAddress(proxyHost, proxyPort));
				httpConn = (HttpURLConnection) url.openConnection(proxy);
			} else {
				httpConn = (HttpURLConnection) url.openConnection();
			}
			httpConn.setConnectTimeout(30000);
			httpConn.setReadTimeout(30000);
			httpConn.setDoInput(true);
			httpConn.setDoOutput(true);
			httpConn.connect();

			InputStream is = httpConn.getInputStream();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			logger.error("HTTP GET err:" + e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				httpConn.disconnect();
			} catch (Exception e) {
				logger.error("HTTP GET [close resouse] err:" + e.getMessage());
			}
		}
		return sb.toString();
	}

	public static String getHttpPostReq(String strURL, String params) {
		try {
			URL url = new URL(strURL);// 创建连接
			HttpURLConnection connection = null;
			if (isproxy) {// 使用代理模式
				@SuppressWarnings("static-access")
				Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP,
						new InetSocketAddress(proxyHost, proxyPort));
				connection = (HttpURLConnection) url.openConnection(proxy);
			} else {
				connection = (HttpURLConnection) url.openConnection();
			}

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST"); // 设置请求方式
			connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
			connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
			connection.connect();
			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream(), "UTF-8"); // utf-8编码
			out.append(params);
			out.flush();
			out.close();
			// 读取响应
			int length = (int) connection.getContentLength();// 获取长度

			InputStream is = connection.getInputStream();
			if (length != -1) {
				byte[] data = new byte[length];
				byte[] temp = new byte[512];
				int readLen = 0;
				int destPos = 0;
				while ((readLen = is.read(temp)) > 0) {
					System.arraycopy(temp, 0, data, destPos, readLen);
					destPos += readLen;
				}
				String result = new String(data, "UTF-8"); // utf-8编码
				System.out.println("HTTP post result==" + result
						+ "  resposeCode=" + connection.getResponseCode());
				return result;
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return "error"; // 自定义错误信息
	}
}
