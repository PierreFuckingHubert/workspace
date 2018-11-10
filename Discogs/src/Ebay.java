import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Ebay {

	
	
	public static void getJSON(String search, int higherprice){
			
		
		String stringJ = null;

		try { 
		    URL url = new URL(search); 
		    
		   URLConnection uc = url.openConnection(); 
		   InputStream in = uc.getInputStream(); 
		   int c = in.read(); 
		   StringBuilder build = new StringBuilder(); 
		   while (c != -1) { 
		    build.append((char) c); 
		    c = in.read(); 
		   } 
		   stringJ = build.toString(); 
		   
		  } catch (MalformedURLException e) { 
		  
		   e.printStackTrace(); 
		  } catch (IOException e) { 
		  
		}
				
		
		JSONObject JSON = new JSONObject();
		try {
			JSON = new JSONObject(stringJ);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		JSONArray values = null;
		try {
			 			 
			 values = JSON.getJSONArray("findItemsAdvancedResponse").getJSONObject(0).getJSONArray("searchResult").getJSONObject(0).getJSONArray("item");

			 for (int i = 0; i < values.length(); i++) {
							 	
				 String url = (String) values.getJSONObject(i).getJSONArray("viewItemURL").get(0);
				 
				 if(!url.contains("reissue")){
					String p = (String) values.getJSONObject(i).getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).get("__value__");
					//Double price = p;
					Double price = Double.parseDouble(p); 

					if(((higherprice > 50) || (higherprice == -1)) && (((higherprice/price)>2) || ((higherprice/price)<0))){
						System.out.println(url);
						System.out.println(price);
						System.out.println(values.getJSONObject(i).getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).get("@currencyId"));
						System.out.println("HigherPrice : " + higherprice);
						System.out.println("==================================");
					}
				 }
			 }
		} catch (JSONException e) {
			//e.printStackTrace();
		}
		
		
	}
	
}
