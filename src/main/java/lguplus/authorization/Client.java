package lguplus.authorization;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class Client {
	private static final String CLIENT_ID = "3b1b5796-1b5d-4389-8acb-86fcfda9ddbb";
	private static final String CLIENT_SCERET = "K0aU0uE3hP4dN0lM6oN6pF4vX3cW6aO7bK4mI4lM8lA1xF2wJ8";
	
	public static ReturnObject getToken() {
		ReturnObject returnJson = new ReturnObject();
		String returnValue = "";
		
		BufferedReader in = null;
		HttpURLConnection conn = null;
		DataOutputStream wr = null;
		
		try {
			String param = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s&scope=/intuser", CLIENT_ID, CLIENT_SCERET);
			String urlStr =  "https://sbxapi.lguplus.co.kr/uplus/intuser/oauth2/token";
			URL url = new URL(urlStr);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(5000);
			conn.setRequestProperty("Context-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			conn.setDoOutput(true);
			wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(param);
			wr.flush();
			wr.close();
			
			//int responseCode = conn.getResponseCode();
				
			Charset charset = Charset.forName("UTF-8");
			
			String inputLine;
            StringBuffer response = new StringBuffer();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            returnValue = response.toString();
            
            returnJson.returnCode = 300;
            returnJson.dataType = "JSON";
            returnJson.data = returnValue;
		}catch(Exception ex) {
			ex.printStackTrace();
			if(in!=null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(conn != null) {
				conn = null;
			}
			if( wr != null) {
				try {
					wr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			returnJson.returnCode = 101;
            returnJson.dataType = "ERROR";
            returnJson.data = ex.getMessage();
		}
		
		return returnJson;
	}
}
