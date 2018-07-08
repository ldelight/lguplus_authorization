package lguplus.authorization;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class Implicit {
	private static final String CLIENT_ID = "a4f04636-8eeb-4bb9-809e-081dcd320a4b";
	private static final String CLIENT_SCERET = "K0aU0uE3hP4dN0lM6oN6pF4vX3cW6aO7bK4mI4lM8lA1xF2wJ8";

	public static String getToken(String userId, String redirectUri, String ssoKey) {
		String returnValue = "";
		
		
		BufferedReader in = null;
		HttpURLConnection conn = null;
		
		try {
			String uri    = String.format("https://sbxapi.lguplus.co.kr/uplus/intuser/oauth2/authen?response_type=token&client_id=%s&scope=/intuser&auth_type=DAS&sso_key=%s&user_id=%s&service_cd=A03&redirect_uri=%s", CLIENT_ID, ssoKey, userId, redirectUri);	
			URL url = new URL(uri);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setRequestProperty("Context-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			conn.setDoOutput(true);
			
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
		}catch( Exception ex ) {
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
		}
		
		return returnValue;
	}
}
