package sompo.mailenginecore.Dbconfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;




public class AegisConn {
		final static Logger logger = Logger.getLogger(AegisConn.class);
		private static Connection connection = null;
	    //1.jdbc driver name
	    private static String ora_jdbc_driver ="oracle.jdbc.driver.OracleDriver";
		
	    public static Connection openConnection(){
	    	
	    	GetDBAttribute.Aegis();
			
			String URL = "jdbc:oracle:thin:@"+ConnAttribute.getAegServer()+":1521:"+ConnAttribute.getAegDBName();
			String USERNAME = ConnAttribute.getAegDBUser();//UserName
		    String PASSWORD = ConnAttribute.getAegPassword();//Password
		    
			 try {
			        Class.forName(ora_jdbc_driver);// Register jdbc driver
			        
			        logger.info("****Open connection to Aegis Database****");
			       
			        //4. open a connection
			        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			        
			        logger.info("DataBase connect to: "+ connection.getMetaData().getDriverName());
			        logger.info("URL: "+ connection.getMetaData().getURL());

			        //setConnectionClose();
			    } catch (Exception e) {
			        e.printStackTrace();
			        logger.error("Exception in getLocalConection() "+e.getMessage());
//			       
			    }
			    return connection;
		} 
		
	    public static void closeConnection() throws SQLException {
		    if (connection != null) {
		        connection.close();
		        
		        logger.info("****Closed connection AeGIS Database****");

		    }
		}
	    
		public static void main(String[] args) {
			
			openConnection();
			try {
				closeConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


}
