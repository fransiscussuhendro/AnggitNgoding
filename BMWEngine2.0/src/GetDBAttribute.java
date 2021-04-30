

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;





//import com.log.Log;

public class GetDBAttribute {
	
	final static Logger logger = Logger.getLogger(GetDBAttribute.class);
	
	
	public static void Aegis(){
		Properties prop = new Properties();
		InputStream input = null;
		try {

			input = new FileInputStream("./config.properties");

			// load a properties file
			prop.load(input);
			
			// get DB config
			ConnAttribute.setAegDBName(prop.getProperty("aegDBName"));
			ConnAttribute.setAegServer(prop.getProperty("aegServer"));
			ConnAttribute.setAegDBUser(prop.getProperty("aegDBUser"));
			ConnAttribute.setAegPassword(prop.getProperty("aegPassword"));

		} catch (IOException ex) {
			ex.printStackTrace();
			logger.error("set AeGIS Attribute: " + ex.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void Staging(){
		Properties prop = new Properties();
		InputStream input = null;
		try {

			input = new FileInputStream("./config.properties");

			// load a properties file
			prop.load(input);
			
			// get DB config
			ConnAttribute.setStgDBName(prop.getProperty("stgDBName"));
			ConnAttribute.setStgServer(prop.getProperty("stgServer"));
			ConnAttribute.setStgDBUser(prop.getProperty("stgDBUser"));
			ConnAttribute.setStgPassword(prop.getProperty("stgPassword"));

		} catch (IOException ex) {
			ex.printStackTrace();
			logger.error("set AeGIS Attribute: " + ex.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
 