package lguplus.authorization;

import java.beans.Encoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class Authorization {
	
	private static final String CLIENT_ID = "a4f04636-8eeb-4bb9-809e-081dcd320a4b";
	private static final String CLIENT_SCERET = "M2jC3kC8uC7fR1wR4bG2sF5cP5mS5gW6lA2gE4uW4lK0rN3uR0";
	
	ReturnObject returnObject;
	
	public static enum AuthorizationLoginType {
        HTML,
        LINK,
    }
	
	public static ReturnObject getAuthorizationCode(String userId, String redirectUrl, String ssoKey, AuthorizationLoginType type) {
		ReturnObject returnJson = new ReturnObject();
		
		BufferedReader in = null;
		HttpURLConnection conn = null;
		
		if(type == AuthorizationLoginType.HTML) {
			try {
				String returnValue = "";
				String urlStr =  String.format("https://sbxapi.lguplus.co.kr/uplus/intuser/oauth2/authorize?response_type=code&client_id=%s&scope=/intuser&auth_type=DAS&sso_key=%s&user_id=%s&service_cd=A03&redirect_uri=%s"
						,CLIENT_ID, ssoKey, userId, redirectUrl);
				URL url = new URL(urlStr);
				conn = (HttpURLConnection)url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				conn.setRequestProperty("Context-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				
				Charset charset = Charset.forName("UTF-8");
				
				String inputLine;
	            StringBuffer response = new StringBuffer();
	            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
	            
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();
	            returnValue = response.toString();
	            returnValue = returnValue.replace("j_security_check", "https://sbxapi.lguplus.co.kr/uplus/intuser/oauth2/j_security_check");
	            returnValue = returnValue.replace("172.21.70.129", "sbxapi.lguplus.co.kr");
	            
	            returnJson.returnCode = 100;
	            returnJson.dataType = ((userId == null|| userId.trim().equals("")) || (ssoKey == null|| ssoKey.trim().equals(""))? "ACCOUNT":"SSO") ;
	            returnJson.data = returnValue;

			}catch( Exception ex) {
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
				returnJson.returnCode = 101;
                returnJson.dataType = "ERROR";
                returnJson.data = ex.getMessage();
			}
		}else if (type == AuthorizationLoginType.LINK) {
			String uri = String.format("https://sbxapi.lguplus.co.kr/uplus/intuser/oauth2/authorize?response_type=code&client_id=%s&scope=/intuser&auth_type=DAS&sso_key=%s&user_id=%s&service_cd=A03&redirect_uri=%s", CLIENT_ID, ssoKey, userId, redirectUrl);

            returnJson.returnCode = 100;
            returnJson.dataType = "LINK";
            returnJson.data = uri;
		}
		
		return returnJson;
	}
}
