import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Html {
	
	static String token = "vQxxFzrnTDhksNimCTcZGftwHqejMcrUungWtECD";
	static String year = "1991";
	static String country = "nigeria";
	static String genre = "hip-hop";
	static String format = "vinyl";
	static String startUrl = "https://api.discogs.com/database/search?";
	static String req = startUrl+"country="+country+"&year="+year+"&format="+format+"&genre="+genre+"&token="+token;
	static String startUrlItem = "https://www.discogs.com";
	
	public Html(){
	}
	
	public static void getJSON(){
		String stringJ = null;
	    System.out.println(req);

		try { 
		    URL url = new URL(req); 
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
			 values = JSON.getJSONArray("results");
			 int j = 0;
			 for (int i = 0; i < values.length(); i++) {
				
				 j = j+1;

				 for(int k=j; k == 4; k++){
					 System.out.println("J'attend 1 min");
					 try {
							Thread.sleep(1 * 60 * 1100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					 j = 0;
				 }
				 
				    String uri = values.getJSONObject(i).getString("uri");
				    
				    System.out.println("Disque "+(i+1));
				    System.out.println(values.getJSONObject(i).getString("title"));
				    System.out.println(findHigherPrice(getHtml(startUrlItem+uri)));
				    System.out.println("-------------------------------------");
				    
			 }
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    System.out.println("FIN");
		
		
	}
	
	
	public static String getHtml(String uri) { 

		String toreturn = null;
		  try { 
		    URL url = new URL(uri); 
		   URLConnection uc = url.openConnection(); 
		   InputStream in = uc.getInputStream(); 
		   int c = in.read(); 
		   StringBuilder build = new StringBuilder(); 
		   while (c != -1) { 
		    build.append((char) c); 
		    c = in.read(); 
		   } 
		   toreturn = build.toString(); 
		   
		  } catch (MalformedURLException e) { 
		  
		   e.printStackTrace(); 
		  } catch (IOException e) { 
		  
		   e.printStackTrace(); 
		  }
		   return toreturn;
		 }
	
	public static String findHigherPrice(String html){
		String higherPrice = null;
		
		String balise = "<h4>Highest:</h4>"; 
		 
		int begin = (html.indexOf(balise))+37;
		int end = html.indexOf(" ", begin);

		if(end == begin){
			return "Pas de prix";
		}
		else{
			higherPrice = html.substring(begin, end);	
			return higherPrice;
		}
	
	}
	
	
}
