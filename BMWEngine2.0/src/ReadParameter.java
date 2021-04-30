import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class ReadParameter {
	
	Connection con;
	Statement st;
	final static Logger logger = Logger.getLogger(ReadParameter.class);
	public String getParameter(String param,String type) {
		con = new StagingConn().openConnection();
		try {
			st = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error DB Connection");
		}
		String sql="SELECT value FROM ParameterBMW WHERE param='"+param+"' AND type='"+type+"'";
		ResultSet rs = null;
		try {
			rs = st.executeQuery(sql);
			logger.info("Get parameter : "+sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error DB Connection");
		}
		String rst="";
		try {
			while(rs.next()) {
				rst=rs.getString(1);
				logger.info("Result parameter : "+rst);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error DB Connection");
		}
		return rst;
	}
	
	public String getDealer(String kode,String search) {
		con = new StagingConn().openConnection();
		try {
			st = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error DB Connection");
		}
		String sql="select DEALER_CODE,DEALER_NAME,DEALER_ADDRESS from DealerBMW where DEALER_CODE='"+kode+"'";
		ResultSet rs = null;
		try {
			rs = st.executeQuery(sql);
			logger.info("Get dealer : "+sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error DB Connection");
		}
		String rst="";
		try {
			while(rs.next()) {
				rst=rs.getString("\\\""+search+"\\\"");
				logger.info("Result "+search+" : "+rst);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error DB Connection");
		}
		return rst;
	}

}
