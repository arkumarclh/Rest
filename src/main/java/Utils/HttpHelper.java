package Utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class HttpHelper {
	
	
	public static String getResponse(String payload,String url,String httpverb) throws IOException{
		
		
		URL endPointURL = null;
		try {
			endPointURL = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpURLConnection con = (HttpURLConnection) endPointURL.openConnection();
		
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("_soa_session_id", "");
		
		
		con.setRequestMethod(httpverb);
		
		OutputStreamWriter oStream = new OutputStreamWriter(con.getOutputStream());
		oStream.write(payload);
		oStream.close();
		
		String Response = null;
		StringBuffer sB = new StringBuffer();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		
		while ((Response=br.readLine()) != null){
			
			sB.append(Response);
			
		}
		System.out.println(con.getResponseCode());
		
		System.out.println(""+sB.toString());
		
		
		
		return sB.toString();
		
	}
	
	
	
	/*/
	 * {
	"id":2,
	"name":"Book",
	"category":"Stationary",
	"unitPrice":"200"
}
	 */
	
	@SuppressWarnings("unchecked")
	public static String returnJSONPayload(){
		
		String payload = null;
		try{
			
			JSONObject obj = new JSONObject();
			
			obj.put("id", 10);
			obj.put("name", "Book");
			obj.put("category", "Stationary");
			obj.put("unitPrice", "1000");
			
			 payload = obj.toString();
			
			
		}catch(Exception e){
			System.out.println(e.getMessage());
			
		}
		return payload;
		
		
	}
	
	
	
	public static String getJSONObjFromFile(){
		
		String payload = null;
		
		try{
			
			JSONParser parser = new JSONParser();
			
			JSONObject obj = (JSONObject) parser.parse(new FileReader("./test.json"));
			
			payload = obj.toString(); 
			
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return payload;
		
	}

}
