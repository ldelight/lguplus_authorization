package lguplus.authorization;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class Refresh {
	private static final String CLIENT_ID = "e83a4bf4-60ca-4b37-81e0-9c5104b7b276";
	private static final String CLIENT_SCERET = "yI4bE4jY0uO2sE2hM1yE1rB8oG2wD5fD0qT5mF2wO5bK7aC0gJ";

	public static String getToken(String refleshToken) {
		String returnValue = "";
		BufferedReader in = null;
		HttpURLConnection conn = null;
		DataOutputStream wr = null;
		try {
			
			String urlStr = "https://sbxapi.lguplus.co.kr/uplus/intuser/oauth2/token";
			URL url = new URL(urlStr);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(5000);
			conn.setRequestProperty("Context-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			conn.setDoOutput(true);
			
			
			String param = String.format("grant_type=refresh_token&client_id=%s&client_secret=%s&refresh_token=%s", CLIENT_ID, CLIENT_SCERET, refleshToken);
			wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(param);
			wr.flush();
			wr.close();
				
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
			if( wr != null) {
				try {
					wr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			returnValue = ex.getMessage();
		}
		
		return returnValue;
	}
}
