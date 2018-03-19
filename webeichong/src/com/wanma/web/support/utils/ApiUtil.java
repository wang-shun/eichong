package com.wanma.web.support.utils;

import java.util.Map;

import sun.misc.BASE64Encoder;

import com.bluemobi.product.common.MessageManager;


public class ApiUtil {
	
	
	public static String getToken() throws Exception{
		MessageManager manager=MessageManager.getMessageManager();
		String apiRoot=manager.getSystemProperties("apiRoot");
		String apiTokenPath=manager.getSystemProperties("apiTokenPath");
		String token= HttpsUtil.getResponseParam(apiRoot+apiTokenPath, "data");
		String s=token+System.currentTimeMillis();
		s=replace(s.getBytes());
		return new BASE64Encoder().encode(s.getBytes());
	}
	
	public static String encode(String s){
		s=replace(s.getBytes());
		return new BASE64Encoder().encode(s.getBytes());
	}
	
	private static String replace(byte[] a){
		String s="";
		for(byte b:a){
			s+=replace(b);
		}
		return s;
	}
	private static String replace(byte a) {
		String str = String.valueOf((char)a);
		if (a >= 48 && a <= 57) {
			if ("1".equals(str)) {
				str = str.replaceAll("1", "7");
			} else if ("2".equals(str)) {
				str = str.replaceAll("2", "5");
			} else if ("3".equals(str)) {
				str = str.replaceAll("3", "8");
			} else if ("5".equals(str)) {
				str = str.replaceAll("5", "2");
			} else if ("6".equals(str)) {
				str = str.replaceAll("6", "9");
			} else if ("7".equals(str)) {
				str = str.replaceAll("7", "1");
			} else if ("8".equals(str)) {
				str = str.replace("8", "3");
			} else if ("9".equals(str)) {
				str = str.replace("9", "6");
			}
		}else if (a >= 65 && a <= 90) {
			//大写转小写 +32
			char b = (char) (a +32);
			b = (char) (b + 3);
			if(b > 122){
				b = (char) (b-26);
			}
			str = String.valueOf(b);
		}else if (a >= 97 && a <= 122) {
			char b = (char)(a-32);
			b = (char) (b + 3);
			if (b > 90) {
				b = (char) (b - 26);
			}
			str = String.valueOf(b);
		}
		return str;
	}

	public static String sendRequest(Map<String, String> params) throws Exception{
		MessageManager manager=MessageManager.getMessageManager();
		String apiRoot=manager.getSystemProperties("apiRoot");
		int requestType=new Integer((String)params.get("requestType"));
		String path=getPath(requestType);
		String s=HttpRequest.post(apiRoot+path,params);
		return s;
	}
	
	private static String getPath(int requestType){
		String path="";
		switch (requestType) {
		case 1:
			path="/app/bespoke/selectBespoke.do";
			break;
		case 2:
			path="/app/bespoke/insertBespoke.do";
			break;
		case 3:
			path="/app/bespoke/selectBespokes.do";
			break;
		case 4:
			path="/app/bespoke/updateBespStatus.do";
			break;
		default:
			break;
		}
		return path;
	}
}