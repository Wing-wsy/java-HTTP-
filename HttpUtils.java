package com.annet.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.annet.utils.StrUtils;
import com.annet.common.wxException.ParameterErrorException;
import com.annet.common.wxException.RequestFailException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;



public class HttpUtils {

	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	/**
	 *	 发送 http post 请求
	 * @param url  请求地址
	 * @param params 请求行参数
	 * @param headers 请求头
	 * @param body 请求体
	 */
	public static  String doPost( String url ,  Map<String,String>  params , Map<String,String>  headers ,String body,ContentType  contentType ){
		if( logger.isDebugEnabled()  ) {
			logger.debug("请求{}_参数{}_请求头{}" ,url,JSONObject.toJSONString( params ), JSONObject.toJSONString( headers ) );
		}

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		/*
		 * String urlParam = mapToUrlParam(params); if( !StringUtil.isEmpty( urlParam )
		 * ) { url+= "?"+urlParam; }
		 */

		HttpPost httpPost = new HttpPost(url);


		if( params != null && params.keySet().size() != 0 ) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			Iterator<?> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
				list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
			}
			UrlEncodedFormEntity entity = null;
			try {
				entity = new UrlEncodedFormEntity(list,"UTF-8");
			} catch (UnsupportedEncodingException e1) {
				throw new ParameterErrorException( e1.getMessage() );
			}
			httpPost.setEntity(entity);
		}


		//设置请求头
		if( headers != null  ) {
			for(String header: headers.keySet() ) {
				Header  h = new BasicHeader( header , headers.get( header ) );
				httpPost.setHeader( h );
			}
		}

		// 设置报文和通讯格式
		if( !StrUtils.isEmpty( body ) ) {
			StringEntity stringEntity = new StringEntity( body , contentType );
	        httpPost.setEntity(stringEntity);
		}

		// 响应模型
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			if( response.getStatusLine().getStatusCode() != 200 ) {

				throw new RequestFailException( response.getStatusLine().getReasonPhrase() );
			}

			if (responseEntity != null) {
				String resp = EntityUtils.toString(responseEntity);

				if(logger.isDebugEnabled()) {
					logger.debug("响应{}_结果{}",url, resp );
				}

				System.out.println(resp);
				return resp;
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		throw new RequestFailException("http调用异常");
	}

	/**
	 *  发送一个普通 post 请求
	 * @param url
	 * @param
	 * @return
	 */
	public static  String doPost( String url ,  Map<String,String>  params ){
		String rt = doPost(url, params, null , null ,ContentType.TEXT_PLAIN);
		return rt;
	}

	/**
	 *	 发送 http get请求body
	 * @param url  请求地址
	 * @param params 请求行参数
	 * @return
	 */
	public static  String doGet( String url ,  Map<String,String>  params  ) {
		String rt = doGet(url, params,null);
		return rt;
	}


	/**
	 *	 发送 http get请求
	 * @param url  请求地址
	 * @param params 请求行参数
	 * @param headers 请求头
	 * @return
	 */
	public static  String doGet( String url ,  Map<String,String>  params , Map<String,String>  headers ) {
		if( logger.isDebugEnabled() ) {
			logger.debug("请求{}_参数{}_请求头{}" ,url,JSONObject.toJSONString( params ), JSONObject.toJSONString( headers ) );
		}

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		String urlParam = mapToUrlParam(params);
		if( !StrUtils.isEmpty( urlParam )  ) {
			url+= "?"+urlParam;
		}

		HttpGet httpGet = new HttpGet(url);

		//设置请求头
		if( headers != null  ) {
			for(String header: headers.keySet() ) {
				Header  h = new BasicHeader( header , headers.get( header ) );
				httpGet.setHeader( h );
			}
		}


		// 响应模型
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
			HttpEntity responseEntity = response.getEntity();
			if( response.getStatusLine().getStatusCode() != 200 ) {
				throw new RequestFailException( response.getStatusLine().getReasonPhrase() );
			}

			if (responseEntity != null) {
				String resp = EntityUtils.toString(responseEntity);
				if( logger.isDebugEnabled() ) {
					logger.debug("响应{}_结果{}",url, resp );
				}

				return resp;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 释放资源
				if (httpClient != null) {
					httpClient.close();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		throw new RequestFailException("http调用异常");
	}

	private static String mapToUrlParam(Map<String, String> params) {
		if(params == null || params.isEmpty()  ) {
			return null;
		}

		String urlParam = "";
		for(String param: params.keySet() ) {

			String encode = null;
			try {
				String value = params.get(param);
				if( !StrUtils.isEmpty( value ) ) {
					encode = URLEncoder.encode( params.get(param) ,"UTF-8");
				}
			} catch (UnsupportedEncodingException e1) {
				throw new ParameterErrorException( e1.getMessage() );
			}

			urlParam += param+"=" + encode +"&";
		}
		if( urlParam != null && urlParam.endsWith( "&" )  ) {
			urlParam = urlParam.substring(0,  urlParam.length() -1 );
		}

		return urlParam;
	}

	/**
	 *  发送一个json  post 请求
	 * @param url
	 * @param body
	 * @return
	 */
	public static  String jsonPost( String url ,  String body ){
		String rt = doPost(url, null, null, body, ContentType.APPLICATION_JSON);
		return rt;
	}

}
