import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sii.sftp.SftpConnection;
import sompo.mailenginecore.Dbconfig.AegisConn;
import sompo.mailenginecore.Dbconfig.StagingConn;

public class main {
	
	static Connection con;
	static Statement st;
	static String sql = "";
	static String sql2 = "";
	
	static Connection conAg;
	static Statement stAg;
	static String sqlAg = "";

	public static void main(String[] args) throws SQLException, IOException {
		// TODO Auto-generated method stub
//		Connection con;
//		Statement st;
//		String sql = "";
//		String sql2 = "";
//
//		Connection conAg;
//		Statement stAg;
//		String sqlAg = "";
		String pth = "";
		try {
			while (true) {
				conAg = new AegisConn().openConnection();
				stAg = conAg.createStatement();
				sqlAg = "select gt_desc_1 from general_table where GT_CODE = 'DRCTY' and GT_KEY = '03'";
				ResultSet rsAg = stAg.executeQuery(sqlAg);
				while (rsAg.next()) {
					pth = rsAg.getString(1);
				}
				System.out.println(pth.substring(0,pth.length()-2));
				con = new StagingConn().openConnection();
				st = con.createStatement();
				sql = "SELECT SessionID FROM File_Received where ReportDateTime is null and JsonDateTime is not null and FileName like 'SOMPODH%'";
				System.out.println(sql);
				ResultSet rs = st.executeQuery(sql);
				System.out.println(rs.getRow());
				if (rs.getRow() > 0) {
					sql2="select File_Received.SessionID,Fire_Template_Temp.Policy_No,File_Received.JobNo,IntegrationSetup.IntegrationMethod,General_Table.Value1,IntegrationSetup.FtpAddress,IntegrationSetup.UserName,IntegrationSetup.Password,IntegrationSetup.Port,IntegrationSetup.SuccessFolder\r\n" + 
							"from  File_Received,IntegrationSetup,General_Table\r\n" + 
							"where  File_Received.JobNo=IntegrationSetup.JobNo\r\n" + 
							"and IntegrationSetup.IntegrationMethod=General_Table.GeneralKey\r\n" + 
							"and File_Received.SessionID='"+rs.getString(1)+"';";
					ResultSet rs2=st.executeQuery(sql2);
					String sftpAddr="";
					String sftpUser="";
					String sftpPwwd="";
					int sftpPort=0;
					String sftpRmtFldr="";
					while(rs2.next()) {
						sftpAddr=rs.getString(6);
						sftpUser=rs.getString(7);
						sftpPwwd=rs.getString(8);
						sftpPort=Integer.parseInt(rs.getString(9));
						sftpRmtFldr=rs.getString(10);
					}
					String SPsql = "EXEC spGenerateReport ?"; // for stored proc taking 2 parameters
					PreparedStatement ps = con.prepareStatement(SPsql);
					ps.setEscapeProcessing(true);
					ps.setQueryTimeout(30);
					ps.setString(1, rs.getString(1));
					Thread.sleep(5000);
					ps.execute();
					ResultSet rsReport = ps.getResultSet();
					ResultSetMetaData rsmd = rsReport.getMetaData();
					int col_index = rsmd.getColumnCount();
					Date date = Calendar.getInstance().getTime();
					DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
					String strDate = dateFormat.format(date);
					FileOutputStream fos = new FileOutputStream(
							new File(pth + "Report_" + rs.getString(1) + "_" + strDate + ".csv"), false);
					Writer out = new OutputStreamWriter(new BufferedOutputStream(fos));
					// System.out.println(col_index);
					for (int j = 1; j <= col_index; j++) {
						if (j < col_index) {
							out.append(rsmd.getColumnName(j));
							out.append("|");
						} else {
							out.append(rsmd.getColumnName(j));
							out.append("\n");
						}
					}
					while (rsReport.next()) {
						for (int i = 1; i <= col_index; i++) {
							if (i < col_index) {
								out.append(rsReport.getString(i));
								out.append("|");
							} else {
								out.append(rsReport.getString(i));
							}
						}
						out.append("\n");

					}
					out.flush();
					out.close();
//					SftpConnection.initConn(sftpAddr, sftpUser, sftpPwwd, sftpPort);
//					SftpConnection.initFolder(pth, sftpRmtFldr);
//					if (SftpConnection.openSftpConn()) {
//						// Thread.sleep(100000);
//						SftpConnection.uploadFile("Report_" + rs.getString(1) + "_" + strDate + ".csv");
//					}
//					// st.executeUpdate(sql);
//					System.out.println("Report_" + rs.getString(1) + "_" + strDate + ".csv saved");
				}
				Thread.sleep(5 * 1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
