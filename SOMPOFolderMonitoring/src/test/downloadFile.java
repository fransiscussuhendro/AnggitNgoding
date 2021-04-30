package test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sompo.mailenginecore.Dbconfig.StagingConn;

public class downloadFile {
	static Connection con;
	static Statement st;
	static ResultSet rs;
	static String sql = "";
	public static void main(String args[]) throws SQLException, IOException {
		con = new StagingConn().openConnection();
		st = con.createStatement();
		sql="SELECT tbName,tbBin from TestBlob Where tbId=5";
		rs = st.executeQuery(sql);
		while(rs.next()) {
			String filename = rs.getString(1);
	        Blob blob = rs.getBlob(2);
	        InputStream is = blob.getBinaryStream();
	        FileOutputStream fos = new FileOutputStream("D:\\anggit\\monitoring\\download"+ "\\" + filename);
	        int b = 0;
	        while ((b = is.read()) != -1)
	        	{
	        		fos.write(b); 
	        	}
	        }
	    } 

}
