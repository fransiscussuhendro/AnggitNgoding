

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;



public class StagingConn {
	
	final static Logger logger = Logger.getLogger(StagingConn.class);
	
	private static Connection connection = null;
    //1.jdbc driver name
    private static String SQL_JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		openConnection();
		try {
			closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Connection openConnection(){
		GetDBAttribute.Staging();
		
		String URL = "jdbc:sqlserver://"+ConnAttribute.getStgServer()+":1433;databaseName="+ConnAttribute.getStgDBName();
		String USERNAME = ConnAttribute.getStgDBUser();//UserName
	    String PASSWORD = ConnAttribute.getStgPassword();//Password
	    	    
		try {
			Class.forName(SQL_JDBC_DRIVER);// Register jdbc driver
	        logger.info("****Open connection to Staging Database****");
	
	        //4. open a connection
	        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	        
	        logger.info("DataBase connect to: "+ connection.getMetaData().getDriverName());
	        logger.info("URL: "+ connection.getMetaData().getURL());
	
	        //setConnectionClose();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	logger.error("Exception in getLocalConection() "+e.getMessage());
	
		    }
		return connection;
	} 
	
	public static void closeConnection() throws SQLException {	   
	    try{
	    	 if (connection != null) {
	 	        connection.close();
	 	        logger.info("****Closed connection Staging Database****");

	 	    }
		}
		catch (Exception err){
			logger.error("closeConnection: " + err.getMessage());
		}
	}
	
	public List<Map<String, Object>> ExecuteQueryRS(StringBuilder sqlSelect){
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

		try{

			Connection conn = StagingConn.openConnection();
			Statement sqlStatement = conn.createStatement();

			rs = sqlStatement.executeQuery(sqlSelect.toString());
			rsmd = rs.getMetaData();
			int columns = rsmd.getColumnCount();
			while (rs.next())
			{
				Map<String, Object> row = new LinkedHashMap<String, Object>(columns);
		        for(int i = 1; i <= columns; ++i){
		            row.put(rsmd.getColumnName(i), rs.getObject(i));
		        }
		        rows.add(row);
			}
			conn.close();

			return rows;
		}
		catch (Exception err){
			logger.error("ExecuteQueryRS: " + err.getMessage());
			return rows;
		}
	}
	
}
