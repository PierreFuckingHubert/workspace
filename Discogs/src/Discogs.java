import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class Discogs {
	
	static String startUrlItem = "https://www.discogs.com";
	static int q;
	static int count = 0;

	
	public Discogs(){
	}
	
	public static void getJSON(String country, String genre, Connection conn, String req){
		
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
			String urlNext = JSON.getJSONObject("pagination").getJSONObject("urls").getString("next");
		    System.out.println(urlNext);
			 values = JSON.getJSONArray("results");
			 		 			 
			 int j = 0;
			 for (int i = 0; i < values.length(); i++) {
				 
				 Object master ="null";
			     master = values.getJSONObject(i).get("master_url");

			     
			     j = j+1;
			 	
				 for(int k=j; k == 60; k++){
					 System.out.println("J'attend 1 min");
					 try {
							Thread.sleep(1 * 60 * 1100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					 j = 0;
				 }
			     
			     if(master.toString() == "null"){
				 		     
					 

				    String uri = values.getJSONObject(i).getString("uri");
				    int have = values.getJSONObject(i).getJSONObject("community").getInt("have");
				    int want = values.getJSONObject(i).getJSONObject("community").getInt("want");
				    Record record = findHigherPrice(getHtml(startUrlItem+uri));
				    String name = values.getJSONObject(i).getString("title");
				    String names[]  = name.split("-");
				    names[1] = names[1].substring(1);			    
				    names[1] = names[1].replaceAll("'", " ");

				    
				    String titles[] = uri.split("/");
				    String title = titles[1];			    		
				    //title = title.replaceAll("-", "&");
				    
				    int ratio;
				    	    
				    
				    System.out.println("Disque "+(count+i+1));				    
				    if(record != null){
				    	System.out.println("Url : "+ uri);
				    	System.out.println("Artist : "+ names[0]);
				    	System.out.println("Album : "+ names[1]);
				    	System.out.println("Lower price : "+ record.getLowerPrice());
				    	System.out.println("Median price : "+ record.getMedianPrice());
				    	System.out.println("Higher price : "+ record.getHigherPrice());
				    	System.out.println("Have : "+ have);
				    	System.out.println("Want : "+ want);
				    	//System.out.println("Ratio : "+ want);
				    }
				    System.out.println("-------------------------------------");
			    

				    if(record != null && have<want && want>40){
				    	
				    	Statement state;
				    	try {
				    		state = conn.createStatement();
				    		state.executeUpdate("INSERT INTO records (title, artist, album, lowerprice, medianprice, higherprice, have, want) VALUES ('"+ title +"','"+ names[0] +"', '"+ names[1] +"','"+ record.getLowerPrice() +"','"+ record.getMedianPrice() +"','"+ record.getHigherPrice() +"','"+ have +"','"+ want +"')");      
				    		//state.executeUpdate("INSERT INTO record (title, artist, album, lowerprice, medianprice, higherprice, have, want) VALUES ('"+ title +"','"+ names[0] +"', '"+ names[1] +"','"+ record.getLowerPrice() +"','"+ record.getMedianPrice() +"','"+ record.getHigherPrice() +"','"+ have +"','"+ want +"')");      
	      
				    	} catch (SQLException e) {
				    		e.printStackTrace();
				    	}
				    }				    
			     }			      
				    
			 }
			try {
		    	System.out.println("On attend 1 minute");
				Thread.sleep(1 * 60 * 1100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			count = count + 100;
			getJSON(country,genre,conn,urlNext);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public static String getHtml(String uri) { 
	
		String toreturn = null;
		  try { 
		    URL url = new URL(uri); 
		    
		    HttpURLConnection uc = (HttpURLConnection)url.openConnection();		

		   String string = "your bot 0.1"+(q+1);
		   
		   uc.setRequestProperty("User-agent", string);
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
	
	public static Record findHigherPrice(String html){
		String low, med, high = null;
		
		Document doc = Jsoup.parse(html);
		Element price, higher, median, lower;
		price = doc.select("ul.last").first();
		if(price == null){
		    System.out.println("HTML CASSE :(");
			return null;
		}else{
			
			lower = price.select("li:eq(1)").first().select("h4").first();
			median = price.select("li:eq(2)").first().select("h4").first();
			higher = price.select("li:eq(3)").first().select("h4").first();
			
			low = lower.nextSibling().toString();
			med = median.nextSibling().toString();
			high = higher.nextSibling().toString();	

			if(low.contains(",")){low = low.replaceAll(",", "");}
			if(med.contains(",")){med = med.replaceAll(",", "");}			
			if(high.contains(",")){high = high.replaceAll(",", "");}
			
			low = low.substring(4,low.length());
			med = med.substring(4,med.length());
			high = high.substring(4,high.length());

			
			Record record = new Record();
			
			if(high.length() != 0){
				Double l = Double.parseDouble(low); 
				Double m = Double.parseDouble(med); 
				Double h = Double.parseDouble(high); 
			
				int lowerPrice, medianPrice, higherPrice;
				lowerPrice = l.intValue();
				medianPrice = m.intValue();
				higherPrice = h.intValue();
								
				record.setLowerPrice(lowerPrice);	
				record.setMedianPrice(medianPrice);
				record.setHigherPrice(higherPrice);
			}else{
				record.setLowerPrice(-1);	
				record.setMedianPrice(-1);
				record.setHigherPrice(-1);
			}
			
			return record;
		}
	}
	
	public static String findHigherPrice2(String html){
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
