package test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
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

public class WatchDir {
	
	static Connection con;
	static Statement st;
	static String sql = "";
	static String sql2 = "";
	static String sql3 = "";
	
	static Connection conAg;
	static Statement stAg;
	static String sqlAg = "";
	

	public static void main(String[] args) throws IOException,
			InterruptedException, SQLException {
		String sessionID="";
		String sessionIDCmp="a";
		
		String pth="";
		//String pth="L:\\OPERATION\\IT\\01.COMMON\\ANGGIT\\FIRE_POLICY";
		
		con = new StagingConn().openConnection();
		conAg= new AegisConn().openConnection();
		stAg=conAg.createStatement();
		sqlAg="select gt_desc_1 from general_table where GT_CODE = 'DRCTY' and GT_KEY = '03'";
		ResultSet rsAg=stAg.executeQuery(sqlAg);
		while(rsAg.next()){
			pth=rsAg.getString(1);
		}
		System.out.println(pth.substring(0,pth.length()-2));
		Path faxFolder = Paths.get(pth.substring(0,pth.length()-2));
		WatchService watchService = FileSystems.getDefault().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

		boolean valid = true;
		do {
			WatchKey watchKey = watchService.take();

			for (WatchEvent event : watchKey.pollEvents()) {
				WatchEvent.Kind kind = event.kind();
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
					String fileName = event.context().toString();
					if(fileName.contains("FCF")) {
					System.out.println("File Created:" + fileName);
					String key=fileName.substring(0,fileName.indexOf("."));
					String policyNum=key.substring(0,key.indexOf(";"));
					//System.out.println("Key query:" + fileName.substring(0,fileName.indexOf(".")));
					System.out.println("Policy number:" + policyNum);
					st = con.createStatement();
//					sql = "Insert TestBlob(tbName, tbDesc, tbBin) Select \r\n" + 
//							"'"+fileName+"','PDF file', BulkColumn from Openrowset( Bulk \r\n" + 
//							"'"+pth+"\\"+fileName+"', Single_Blob) as tb ";
					sql="select Fire_Template_Temp.SessionID,Fire_Template_Temp.Policy_No,File_Received.JobNo,IntegrationSetup.IntegrationMethod,General_Table.Value1,IntegrationSetup.FtpAddress,IntegrationSetup.UserName,IntegrationSetup.Password,IntegrationSetup.Port,IntegrationSetup.SuccessFolder\r\n" + 
							"from Fire_Template_Temp, File_Received,IntegrationSetup,General_Table\r\n" + 
							"where Fire_Template_Temp.SessionID=File_Received.SessionID\r\n" + 
							"and File_Received.JobNo=IntegrationSetup.JobNo\r\n" + 
							"and IntegrationSetup.IntegrationMethod=General_Table.GeneralKey\r\n" + 
							"and Fire_Template_Temp.Policy_No='"+policyNum+"';";
					ResultSet rs=st.executeQuery(sql);
					String sftpAddr="";
					String sftpUser="";
					String sftpPwwd="";
					int sftpPort=0;
					String sftpRmtFldr="";
					while(rs.next()) {
						SftpConnection.initConn(rs.getString(6),rs.getString(7),rs.getString(8),Integer.parseInt(rs.getString(9)));
						SftpConnection.initFolder(pth,rs.getString(10));
						if(SftpConnection.openSftpConn()) {
							Thread.sleep(5000);
						SftpConnection.uploadFile(fileName);
						}
						System.out.println(fileName+" move to "+rs.getString(10)+" successfully");
						sessionID=rs.getString(1);
						sftpAddr=rs.getString(6);
						sftpUser=rs.getString(7);
						sftpPwwd=rs.getString(8);
						sftpPort=Integer.parseInt(rs.getString(9));
						sftpRmtFldr=rs.getString(10);
					}
					sql2 = "select UPPER(Excel_Field),Database_Field from source_mapping where SourceID=1 order by CAST(substring(Database_Field,12,len(Database_Field)-11) AS int) ASC";
					ResultSet rs2 = st.executeQuery(sql2);
					String fieldDB="";
					String fieldDB2="";
					while(rs2.next()) {
						fieldDB+="'"+rs2.getString(1)+"' AS "+rs2.getString(1)+",";
						fieldDB2+=""+rs2.getString(2)+",";
					}
					
					String fieldDbfix=fieldDB.substring(0,fieldDB.length()-1);
					String fieldDbfix2=fieldDB2.substring(0,fieldDB2.length()-1);
					
					sql3="SELECT "+fieldDbfix+",'POLICY_NO' AS POLICY_NO,'STATUS' AS STATUS,'MESSAGE' AS MESSAGE UNION ALL SELECT "+fieldDbfix2+",Fire_template_temp.Policy_No,Fire_template_temp.status,REPLACE(REPLACE(Fire_template_temp.message, CHAR(13), ' '), CHAR(10), ' ') FROM Table_Upload_Temp INNER JOIN Fire_template_temp ON Table_Upload_Temp.FieldString23=Fire_template_temp.RI_CO_REF WHERE Fire_template_temp.SessionID = '"+sessionID+"' AND Table_Upload_Temp.FieldString26 NOT LIKE ''";
					//System.out.println(sql3);
					ResultSet rs3 = st.executeQuery(sql3);
					ResultSetMetaData rsmd = rs3.getMetaData();
					int col_index=rsmd.getColumnCount();  
					Date date = Calendar.getInstance().getTime();  
			        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");  
			        String strDate = dateFormat.format(date);
					FileOutputStream fos=new FileOutputStream(new File(pth+"Report_"+sessionID+"_"+strDate+".csv"),false);  
		            Writer out = new OutputStreamWriter(new BufferedOutputStream(fos)); 
		            while(rs3.next()) {
			        	   for(int i=1;i<=col_index;i++) {
			        		   if(i<col_index) {
			        		   out.append(rs3.getString(i));
			        		   out.append("|");
			        		   }else {
			        			   out.append(rs3.getString(i));
			        		   }
			        	   }
			        	   out.append("\n");
//			            	int k=1;
//			            if (k == col_index && (multiLine = rs.getString(k)) != null)
//			                out.append(multiLine.replaceAll("\\n", ""));
//			            else
//			                out.append(rsReport.getString(k));
			        	   
			            }
			           out.flush();
			           out.close();
			           SftpConnection.initConn(sftpAddr,sftpUser,sftpPwwd,sftpPort);
						SftpConnection.initFolder(pth,sftpRmtFldr);
						if(SftpConnection.openSftpConn()) {
							//Thread.sleep(100000);
						SftpConnection.uploadFile("Report_"+sessionID+"_"+strDate+".csv");
						}
						//st.executeUpdate(sql);
						System.out.println(fileName+" saved");
					}
		            
		            
		            
//					System.out.println(sessionID +"  "+sessionIDCmp);
//					if(sessionID.compareTo(sessionIDCmp)<0) {
//						String SPsql = "EXEC spGenerateReport ?";   // for stored proc taking 2 parameters
//						PreparedStatement ps = con.prepareStatement(SPsql);
//						ps.setEscapeProcessing(true);
//						ps.setQueryTimeout(30);
//						ps.setString(1, sessionID);
//						Thread.sleep(5000);
//						ps.execute();
//						ResultSet rsReport = ps.getResultSet();
//						ResultSetMetaData rsmd = rsReport.getMetaData();
//						int col_index=rsmd.getColumnCount();  
//						Date date = Calendar.getInstance().getTime();  
//				        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");  
//				        String strDate = dateFormat.format(date);
//						FileOutputStream fos=new FileOutputStream(new File(pth+"Report_"+sessionID+"_"+strDate+".csv"),false);  
//			            Writer out = new OutputStreamWriter(new BufferedOutputStream(fos)); 
//			            //System.out.println(col_index);
//			            for(int j=1;j<=col_index;j++) {
//			            	if(j<col_index) {
//			        		   out.append(rsmd.getColumnName(j));
//			        		   out.append("|");
//			            	}else {
//			            		out.append(rsmd.getColumnName(j));
//			            		out.append("\n");
//			            	}
//			        	   }
//			           while(rsReport.next()) {
//			        	   for(int i=1;i<=col_index;i++) {
//			        		   if(i<col_index) {
//			        		   out.append(rsReport.getString(i));
//			        		   out.append("|");
//			        		   }else {
//			        			   out.append(rsReport.getString(i));
//			        		   }
//			        	   }
//			        	   out.append("\n");
////			            	int k=1;
////			            if (k == col_index && (multiLine = rs.getString(k)) != null)
////			                out.append(multiLine.replaceAll("\\n", ""));
////			            else
////			                out.append(rsReport.getString(k));
//			        	   
//			            }
//			           out.flush();
//			           out.close();
//			           SftpConnection.initConn(sftpAddr,sftpUser,sftpPwwd,sftpPort);
//						SftpConnection.initFolder(pth,sftpRmtFldr);
//						if(SftpConnection.openSftpConn()) {
//							//Thread.sleep(100000);
//						SftpConnection.uploadFile("Report_"+sessionID+"_"+strDate+".csv");
//						}
//						//st.executeUpdate(sql);
//						System.out.println(fileName+" saved");
//					}
					
					//ResultSet rsReport=st.executeQuery("exec spGenerateReport '"+sessionID+"' ");
//					if(sessionID.equals(sessionIDCmp)) {
//						
//					}else {
//					System.out.println(sessionID);
//					String SPsql = "EXEC spGenerateReport ?";   // for stored proc taking 2 parameters
//					PreparedStatement ps = con.prepareStatement(SPsql);
//					ps.setEscapeProcessing(true);
//					ps.setQueryTimeout(30);
//					ps.setString(1, sessionID);
//					ResultSet rsReport = ps.executeQuery();
//					ResultSetMetaData rsmd = rsReport.getMetaData();
//					int col_index=rsReport.getMetaData().getColumnCount();  
//					Date date = Calendar.getInstance().getTime();  
//			        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");  
//			        String strDate = dateFormat.format(date);
//					FileOutputStream fos=new FileOutputStream(new File(pth+"Report_"+sessionID+"_"+strDate+".csv"),false);  
//		            Writer out = new OutputStreamWriter(new BufferedOutputStream(fos)); 
//		            //System.out.println(col_index);
//		            for(int j=1;j<=col_index;j++) {
//		            	if(j<col_index) {
//		        		   out.append(rsmd.getColumnName(j));
//		        		   out.append("|");
//		            	}else {
//		            		out.append(rsmd.getColumnName(j));
//		            		out.append("\n");
//		            	}
//		        	   }
//		           while(rsReport.next()) {
//		        	   for(int i=1;i<=col_index;i++) {
//		        		   if(i<col_index) {
//		        		   out.append(rsReport.getString(i));
//		        		   out.append("|");
//		        		   }else {
//		        			   out.append(rsReport.getString(i));
//		        		   }
//		        	   }
//		        	   out.append("\n");
////		            	int k=1;
////		            if (k == col_index && (multiLine = rs.getString(k)) != null)
////		                out.append(multiLine.replaceAll("\\n", ""));
////		            else
////		                out.append(rsReport.getString(k));
//		        	   
//		            }
//		           out.flush();
//		           out.close();
//		           SftpConnection.initConn(sftpAddr,sftpUser,sftpPwwd,sftpPort);
//					SftpConnection.initFolder(pth,sftpRmtFldr);
//					if(SftpConnection.openSftpConn()) {
//						//Thread.sleep(100000);
//					SftpConnection.uploadFile("Report_"+sessionID+"_"+strDate+".csv");
//					}
//					st.executeUpdate(sql);
//					System.out.println(fileName+" saved");
					sessionIDCmp=sessionID;
//				}
//					}
				}
				
			}
			valid = watchKey.reset();

		} while (valid);

	}
}