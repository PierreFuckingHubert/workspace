import java.sql.Connection;
import java.sql.DriverManager;

public class bdd {
	
	  public static Connection connection() {  
		  Connection conn = null;
	    try {
	      Class.forName("org.postgresql.Driver");
	      System.out.println("Driver O.K.");

	      String url = "jdbc:postgresql://localhost:5432/Discogs";
	      String user = "postgres";
	      String passwd = "supercool";

	      conn = DriverManager.getConnection(url, user, passwd);
	      System.out.println("Connexion effective !");         
	         	     
	      
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
		return conn;      
	  }
	  
	
}
