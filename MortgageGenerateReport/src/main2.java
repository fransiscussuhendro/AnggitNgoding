import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sompo.mailenginecore.Dbconfig.StagingConn;

public class main2 {
	
	static Connection con;
	static Statement st;
	static String sql = "";
	static String sql2 = "";
	static String sql3 = "";
	
	static Connection conAg;
	static Statement stAg;
	static String sqlAg = "";

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		con = new StagingConn().openConnection();
		st = con.createStatement();
		sql = "select UPPER(Excel_Field),Database_Field from source_mapping where SourceID=1 order by CAST(substring(Database_Field,12,len(Database_Field)-11) AS int) ASC";
		ResultSet rs = st.executeQuery(sql);
		String fieldDB="";
		String fieldDB2="";
		while(rs.next()) {
			fieldDB+="'"+rs.getString(1)+"' AS "+rs.getString(1)+",";
			fieldDB2+=""+rs.getString(2)+",";
		}
		
		String fieldDbfix=fieldDB.substring(0,fieldDB.length()-1);
		String fieldDbfix2=fieldDB2.substring(0,fieldDB2.length()-1);
		
		sql2="SELECT "+fieldDbfix+",'POLICY_NO' AS POLICY_NO,'STATUS' AS STATUS,'MESSAGE' AS MESSAGE UNION ALL SELECT "+fieldDbfix2+",Fire_template_temp.Policy_No,Fire_template_temp.status,Fire_template_temp.message FROM Table_Upload_Temp INNER JOIN Fire_template_temp ON Table_Upload_Temp.FieldString23=Fire_template_temp.RI_CO_REF WHERE Fire_template_temp.SessionID = '1288912177' AND Table_Upload_Temp.FieldString26 NOT LIKE ''";
		System.out.println(sql2);
		
		
		
		
		

	}

}
