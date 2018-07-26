/**
 * Project Name document-generator
 * File Name HTTPUtils.java
 * Package Name db.utils
 * Date 2018年6月22日下午5:06:34
 * Copyright (c) 2018, izbk@163.com All Rights Reserved.
 *
*/

package db.utils;

import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPUtils {
	public static HttpURLConnection getConnection(String url) throws Exception {
		URL requestUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) requestUrl.openConnection();
		conn.setRequestMethod("GET"); // 提交模式
		conn.setRequestProperty("User-Agent", "Autoyol_gpsCenter");
		conn.setConnectTimeout(100000); // 连接超时 单位毫秒
		conn.setReadTimeout(100000); // 读取超时 单位毫秒
		conn.setDoOutput(true); // 是否输入参数
		conn.setDoInput(true); // 是否读取参数
		return conn;
	}
	
	public static void close(HttpURLConnection conn) {
		conn.disconnect();
	}
}
