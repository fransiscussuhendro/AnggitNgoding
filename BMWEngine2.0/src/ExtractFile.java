import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sompo.utility.SendEmail;

public class ExtractFile {
	final static Logger logger = Logger.getLogger(ExtractFile.class);

	public static void main(String[] args) throws IOException, InterruptedException, SQLException, ParseException, JSONException, MessagingException {
		// TODO Auto-generated method stub
		
		logger.info("Starting extract data");
		
		Connection con;
		Statement st = null;
		con = new StagingConn().openConnection();
		try {
			st = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error DB Connection");
		}
		
		//read Path parameter
		String path="";
		path = new ReadParameter().getParameter("PATH","General");
		logger.info("Drop folder path : "+path);
		
		//end read Path parameter
		
		//start Folder watcher
		Path faxFolder = Paths.get(path);
		WatchService watchService = FileSystems.getDefault().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE); //only watch for create file
		boolean valid = true;
		do {
			WatchKey watchKey = watchService.take();
			for (WatchEvent event : watchKey.pollEvents()) {
				WatchEvent.Kind kind = event.kind();
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
					String fileName = event.context().toString();
					logger.info("Dropped file : "+fileName);
					if (fileName.substring(0, 7).equals("TIRERDA")) {
						TimeUnit.SECONDS.sleep(5);
						FileInputStream fis = new FileInputStream(new File(path + "\\" + fileName));
						HSSFWorkbook wb = new HSSFWorkbook(fis);
						HSSFSheet sheet = wb.getSheetAt(0);
						FormulaEvaluator formulaEvaluator = wb.getCreationHelper().createFormulaEvaluator();
						for (Row row : sheet) // iteration over row using for each loop
						{
							String value = ",'";
							if (row.getRowNum() > 0) {
								for (Cell cell : row) // iteration over cell using for each loop
								{
									switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
									case Cell.CELL_TYPE_NUMERIC: // field that represents numeric cell type
										// getting the value of the cell as a number
										// System.out.print(cell.getNumericCellValue()+ "\t\t");
										value += cell.getNumericCellValue() + "','";
										break;
									case Cell.CELL_TYPE_STRING: // field that represents string cell type
										// getting the value of the cell as a string
										// System.out.print(cell.getStringCellValue()+ "\t\t");
										value += cell.getStringCellValue() + "','";
										break;
									}
								}
								String queryPrefix = "INSERT INTO tempBMW VALUES(";
								String value2 = value.substring(1, value.length());
								String value3 = value2.substring(0, value2.length() - 2);
								String querySuffix = ");";
								String sql = queryPrefix + value3 + querySuffix;
								try {
									st.executeUpdate(sql);
									logger.info("Inert into temporary table : "+sql);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									logger.error("Error DB Connection");
								}
								
							}
						}
						logger.info("Get uploader parameter");
						String STYPE = new ReadParameter().getParameter("STYPE","Tire");
						String MASTERF = new ReadParameter().getParameter("MASTERF","Tire");
						String BRANCH = new ReadParameter().getParameter("BRANCH","Tire");
						String HOLDER = new ReadParameter().getParameter("HOLDER","Tire");
						String MO = new ReadParameter().getParameter("MO","Tire");
						String SEGMENT = new ReadParameter().getParameter("SEGMENT","Tire");
						String TOC = new ReadParameter().getParameter("TOC","Tire");
						String REGNO = new ReadParameter().getParameter("REGNO","Tire");
						String INSURED = new ReadParameter().getParameter("INSURED","Tire");
						String INSUREDNAME = new ReadParameter().getParameter("INSUREDNAME","Tire");
						String ToproCurrency = new ReadParameter().getParameter("ToproCurrency","Tire");
						String ToproTSI = new ReadParameter().getParameter("ToproTSI","Tire");
						String Topro = new ReadParameter().getParameter("Topro","Tire");
						String Discount = new ReadParameter().getParameter("Discount","Tire");
						String InforceF = new ReadParameter().getParameter("InforceF","Tire");
						String IMMEDIATEINFORCEF = new ReadParameter().getParameter("IMMEDIATEINFORCEF","Tire");
						String Asource = new ReadParameter().getParameter("Asource","Tire");
						String RefNo = new ReadParameter().getParameter("RefNo","Tire");
						String RefDate = new ReadParameter().getParameter("RefDate","Tire");
						String QQ = new ReadParameter().getParameter("QQ","Tire");
						String Remarks = new ReadParameter().getParameter("Remarks","Tire");
						String NeworUsed = new ReadParameter().getParameter("Remarks","Tire");
						String Interest1 = new ReadParameter().getParameter("Interest1","Tire");
						String InterestCurrency1 = new ReadParameter().getParameter("InterestCurrency1","Tire");
						String InterestSumInsured1 = new ReadParameter().getParameter("InterestSumInsured1","Tire");
						String Interest2 = new ReadParameter().getParameter("Interest2","Tire");
						String InterestCurrency2 = new ReadParameter().getParameter("InterestCurrency2","Tire");
						String InterestSumInsured2 = new ReadParameter().getParameter("InterestSumInsured2","Tire");
						String Interest3 = new ReadParameter().getParameter("Interest3","Tire");
						String InterestCurrency3 = new ReadParameter().getParameter("InterestCurrency3","Tire");
						String InterestSumInsured3 = new ReadParameter().getParameter("InterestSumInsured3","Tire");
						String Interest4 = new ReadParameter().getParameter("Interest4","Tire");
						String InterestCurrency4 = new ReadParameter().getParameter("InterestCurrency4","Tire");
						String InterestSumInsured4 = new ReadParameter().getParameter("InterestSumInsured4","Tire");
						String BsID1 = new ReadParameter().getParameter("BsID1","Tire");
						String BSFee1 = new ReadParameter().getParameter("BSFee1","Tire");
						String BSType1 = new ReadParameter().getParameter("BSType1","Tire");
						String BsID2 = new ReadParameter().getParameter("BsID2","Tire");
						String BSFee2 = new ReadParameter().getParameter("BSFee2","Tire");
						String BSType2 = new ReadParameter().getParameter("BSType2","Tire");
						
						logger.info("Get uploader header");
						String sqlGetHeader="SELECT value from ParameterBMW WHERE param LIKE 'Header%' AND type='Tire' ORDER by CAST(SUBSTRING(param,7,LEN(param)-5) AS int) ASC";
						ResultSet lsHeader=st.executeQuery(sqlGetHeader);
						String[] columns = new String[64];
						String hdr="";
						int i=0;
						while(lsHeader.next()) {
							columns[i]=lsHeader.getString(1);
							hdr+=lsHeader.getString(1)+",";
							i++;
						}
						logger.info("Header uploader : "+hdr);
						
						logger.info("Create uploader & JSON"+hdr);
						// write uploader
						Workbook workbook = new HSSFWorkbook();
						CreationHelper createHelper = workbook.getCreationHelper();
						// Create a Sheet
						Sheet uploadSheet = workbook.createSheet("Upload");

						// Create a Font for styling header cells
						Font headerFont = workbook.createFont();
						headerFont.setBold(true);
						//			        headerFont.setFontHeightInPoints((short) 14);
						//			        headerFont.setColor(IndexedColors.RED.getIndex());

						// Create a CellStyle with the font
						CellStyle headerCellStyle = workbook.createCellStyle();
						headerCellStyle.setFont(headerFont);

						// Create a Row
						Row headerRow = uploadSheet.createRow(0);
						// Create cells
						String jsonTireHeader="{\"FileType\":\""+new ReadParameter().getParameter("Uploader", "Tire")+"\",\r\n" + //create json
								"  \"PolicySType\":\"O\",\r\n" + //create json
								"  \"MapGroup\":\"UP\",\r\n" + //create json
								"  \"Data\":[";//create json
						String jsonTireContent="";//create json
						for (int headerNum = 0; headerNum < columns.length; headerNum++) {
							Cell cell = headerRow.createCell(headerNum);
							cell.setCellValue(columns[headerNum]);
							cell.setCellStyle(headerCellStyle);
						}

						// Create Cell Style for formatting Date
						CellStyle dateCellStyle = workbook.createCellStyle();
						dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

						// Create Other rows and cells with employees data
						int rowNum = 1;
						
						// start read RDA Tire by VIN
						String msg="";
						String inception = "";
						String expiry = "";
						String VehicleBrand = "";
						String VehicleModel = "";
						String VehicleSubModel = "";
						String ChasisNumber = "";
						String MachineNumber = "";
						String EngineCC = "";
						String RegistrationDate = "";
						String DealerCode = "";
						String DealerName = "";
						String DealerAddr = "";
						// FR
						String type1 = "";
						String nomor1 = "";
						String remark1 = "";
						// FR
						// FL
						String type2 = "";
						String nomor2 = "";
						String remark2 = "";
						// FL
						// RR
						String type3 = "";
						String nomor3 = "";
						String remark3 = "";
						// RR
						// RL
						String type4 = "";
						String nomor4 = "";
						String remark4 = "";
						// RL
						String TCCCode="";
						String sqlGetListTire = "SELECT distinct VinNo,CONVERT(varchar,convert(date, TyreCoverageStartDate, 103),105) as TyreCoverageStartDate,CONVERT(varchar,convert(date, TyreCoverageExpiryDate, 103),105) as TyreCoverageExpiryDate,Brand,Eseries,Model,EngineNumber,'0' EngineCC,  CONVERT(varchar,convert(date, WarrantyStartDate, 120),105) as WarrantyStartDate,DealerNo,TCCode from tempBMW ";
						ResultSet lsTire = st.executeQuery(sqlGetListTire);
						
						while (lsTire.next()) {
							Row row = uploadSheet.createRow(rowNum++);
							inception = lsTire.getString(2);
							expiry = lsTire.getString(3);
							VehicleBrand = lsTire.getString(4);
							VehicleModel = lsTire.getString(5);
							VehicleSubModel = lsTire.getString(6);
							ChasisNumber = lsTire.getString(1);
							MachineNumber = lsTire.getString(7);
							EngineCC = lsTire.getString(8);
							RegistrationDate = lsTire.getString(9);
							DealerCode = lsTire.getString(10);
							RefNo=lsTire.getString(1);
							TCCCode=lsTire.getString(11);
							// get dealer info
							DealerName = new ReadParameter().getDealer(DealerCode, "DEALER_NAME");
							DealerAddr = new ReadParameter().getDealer(DealerCode, "DEALER_ADDRESS");
							// end get dealer info

							// get tire info FR
							type1 = getTypeTire(ChasisNumber, "Front Right");
							nomor1 = getNoTire(ChasisNumber, "Front Right");
							remark1 = getRmkTire(ChasisNumber, "Front Right");
							// end get tire info FR
							// get tire info FL
							type2 = getTypeTire(ChasisNumber, "Front Left");
							nomor2 = getNoTire(ChasisNumber, "Front Left");
							remark2 = getRmkTire(ChasisNumber, "Front Left");
							// end get tire info FL
							// get tire info RR
							type3 = getTypeTire(ChasisNumber, "Rear Right");
							nomor3 = getNoTire(ChasisNumber, "Rear Right");
							remark3 = getRmkTire(ChasisNumber, "Rear Right");
							// end get tire info RR
							// get tire info RL
							type4 = getTypeTire(ChasisNumber, "Rear Left");
							nomor4 = getNoTire(ChasisNumber, "Rear Left");
							remark4 = getRmkTire(ChasisNumber, "Rear Left");
							// end get tire info RL
							String[] jsonTireontentValue=new String[64];
							row.createCell(0).setCellValue(STYPE);
							jsonTireontentValue[0]=STYPE;
							row.createCell(1).setCellValue(MASTERF);
							jsonTireontentValue[1]=MASTERF;
							row.createCell(2).setCellValue(BRANCH);
							jsonTireontentValue[2]=BRANCH;
							row.createCell(3).setCellValue(HOLDER);
							jsonTireontentValue[3]=HOLDER;
							row.createCell(4).setCellValue(MO);
							jsonTireontentValue[4]=MO;
							row.createCell(5).setCellValue(SEGMENT);
							jsonTireontentValue[5]=SEGMENT;
							row.createCell(6).setCellValue(TOC);
							jsonTireontentValue[6]=TOC;
							row.createCell(7).setCellValue(REGNO);
							jsonTireontentValue[7]=REGNO;
							row.createCell(8).setCellValue(INSURED);
							jsonTireontentValue[8]=INSURED;
							row.createCell(9).setCellValue(INSUREDNAME);
							jsonTireontentValue[9]=INSUREDNAME;
							//row.createCell(10).setCellValue(inception);
//							row.createCell(11).setCellValue(expiry);
													Cell inceptionCell = row.createCell(10);
													inceptionCell.setCellValue(new SimpleDateFormat("dd-MM-yyyy").parse(inception)); 
													inceptionCell.setCellStyle(dateCellStyle);
													jsonTireontentValue[10]=new SimpleDateFormat("MM-dd-yyyy").format(new SimpleDateFormat("dd-MM-yyyy").parse(inception));
													Cell expiryCell = row.createCell(11);
													expiryCell.setCellValue(new SimpleDateFormat("dd-MM-yyyy").parse(expiry));
													expiryCell.setCellStyle(dateCellStyle);
													jsonTireontentValue[11]=new SimpleDateFormat("MM-dd-yyyy").format(new SimpleDateFormat("dd-MM-yyyy").parse(expiry));;
							row.createCell(12).setCellValue(ToproCurrency);
							jsonTireontentValue[12]=ToproCurrency;
							row.createCell(13).setCellValue(ToproTSI);
							jsonTireontentValue[13]=ToproTSI;
							row.createCell(14).setCellValue(Topro);
							jsonTireontentValue[14]=Topro;
							row.createCell(15).setCellValue(Discount);
							jsonTireontentValue[15]=Discount;
							row.createCell(16).setCellValue(InforceF);
							jsonTireontentValue[16]=InforceF;
							row.createCell(17).setCellValue(IMMEDIATEINFORCEF);
							jsonTireontentValue[17]=IMMEDIATEINFORCEF;
							row.createCell(18).setCellValue(Asource);
							jsonTireontentValue[18]=Asource;
							row.createCell(19).setCellValue(RefNo);
							jsonTireontentValue[19]=RefNo;
							row.createCell(20).setCellValue(RefDate);
							jsonTireontentValue[20]=RefDate;
							row.createCell(21).setCellValue(QQ);
							jsonTireontentValue[21]=QQ;
							row.createCell(22).setCellValue(Remarks);
							jsonTireontentValue[22]=Remarks;
							row.createCell(23).setCellValue(VehicleBrand);
							jsonTireontentValue[23]=VehicleBrand;
							row.createCell(24).setCellValue(VehicleModel);
							jsonTireontentValue[24]=VehicleModel;
							row.createCell(25).setCellValue(VehicleSubModel);
							jsonTireontentValue[25]=VehicleSubModel;
							row.createCell(26).setCellValue(ChasisNumber);
							jsonTireontentValue[26]=ChasisNumber;
							row.createCell(27).setCellValue(MachineNumber);
							jsonTireontentValue[27]=MachineNumber;
							row.createCell(28).setCellValue(NeworUsed);
							jsonTireontentValue[28]=NeworUsed;
							row.createCell(29).setCellValue(DealerCode);
							jsonTireontentValue[29]=Asource;
							row.createCell(30).setCellValue(DealerName);
							jsonTireontentValue[30]=DealerName;
							row.createCell(31).setCellValue(DealerAddr);
							jsonTireontentValue[31]=DealerAddr;
							row.createCell(32).setCellValue(EngineCC);
							jsonTireontentValue[32]=EngineCC;
//							row.createCell(33).setCellValue(RegistrationDate);
													Cell regDateCell = row.createCell(33);
													regDateCell.setCellValue(new SimpleDateFormat("dd-MM-yyyy").parse(RegistrationDate));
													regDateCell.setCellStyle(dateCellStyle);
													jsonTireontentValue[33]=new SimpleDateFormat("MM-dd-yyyy").format(new SimpleDateFormat("dd-MM-yyyy").parse(RegistrationDate));
							row.createCell(34).setCellValue(Interest1);
							jsonTireontentValue[34]=Interest1;
							row.createCell(35).setCellValue(type1);
							jsonTireontentValue[35]=type1;
							row.createCell(36).setCellValue(nomor1);
							jsonTireontentValue[36]=nomor1;
							row.createCell(37).setCellValue(remark1);
							jsonTireontentValue[37]=remark1;
							row.createCell(38).setCellValue(InterestCurrency1);
							jsonTireontentValue[38]=InterestCurrency1;
							row.createCell(39).setCellValue(InterestSumInsured1);
							jsonTireontentValue[39]=InterestSumInsured1;
							row.createCell(40).setCellValue(Interest2);
							jsonTireontentValue[40]=Interest2;
							row.createCell(41).setCellValue(type2);
							jsonTireontentValue[41]=type2;
							row.createCell(42).setCellValue(nomor2);
							jsonTireontentValue[42]=nomor2;
							row.createCell(43).setCellValue(remark2);
							jsonTireontentValue[43]=remark2;
							row.createCell(44).setCellValue(InterestCurrency2);
							jsonTireontentValue[44]=InterestCurrency2;
							row.createCell(45).setCellValue(InterestSumInsured2);
							jsonTireontentValue[45]=InterestSumInsured2;
							row.createCell(46).setCellValue(Interest3);
							jsonTireontentValue[46]=Interest3;
							row.createCell(47).setCellValue(type3);
							jsonTireontentValue[47]=type3;
							row.createCell(48).setCellValue(nomor3);
							jsonTireontentValue[48]=nomor3;
							row.createCell(49).setCellValue(remark3);
							jsonTireontentValue[49]=remark3;
							row.createCell(50).setCellValue(InterestCurrency3);
							jsonTireontentValue[50]=InterestCurrency3;
							row.createCell(51).setCellValue(InterestSumInsured3);
							jsonTireontentValue[51]=InterestSumInsured3;
							row.createCell(52).setCellValue(Interest4);
							jsonTireontentValue[52]=Interest4;
							row.createCell(53).setCellValue(type4);
							jsonTireontentValue[53]=type4;
							row.createCell(54).setCellValue(nomor4);
							jsonTireontentValue[54]=nomor4;
							row.createCell(55).setCellValue(remark4);
							jsonTireontentValue[55]=remark4;
							row.createCell(56).setCellValue(InterestCurrency4);
							jsonTireontentValue[56]=InterestCurrency4;
							row.createCell(57).setCellValue(InterestSumInsured4);
							jsonTireontentValue[57]=InterestSumInsured4;
							row.createCell(58).setCellValue(BsID1);
							jsonTireontentValue[58]=BsID1;
							row.createCell(59).setCellValue(BSFee1);
							jsonTireontentValue[59]=BSFee1;
							row.createCell(60).setCellValue(BSType1);
							jsonTireontentValue[60]=BSType1;
							row.createCell(61).setCellValue(BsID2);
							jsonTireontentValue[61]=BsID2;
							row.createCell(62).setCellValue(BSFee2);
							jsonTireontentValue[62]=BSFee2;
							row.createCell(63).setCellValue(BSType2);
							jsonTireontentValue[63]=BSType2;
							String json1="{";
							String json3="";
							for (int headerJSON = 0; headerJSON < columns.length; headerJSON++) {
								json3+="\""+columns[headerJSON]+"\""+":"+"\""+jsonTireontentValue[headerJSON]+"\",";
										
							}
							String json2="\""+"ISType"+"\""+":"+"\""+"\""+","+"\""+"Policyno"+"\""+":"+"\""+"\""+"},";
							jsonTireContent+=json1+json3+json2;
							String key="SOMPO";
					        String vin=ChasisNumber;
					        String model=VehicleModel;
					        Date RegDate=new SimpleDateFormat("dd-MM-yyyy").parse(RegistrationDate);
					        String code=TCCCode;
					        Date startDate=new SimpleDateFormat("dd-MM-yyyy").parse(inception);
					        Date endDate=new SimpleDateFormat("dd-MM-yyyy").parse(expiry);
					        String polNum="";
					        String voucherNo="";
					        String batchNo="";
					        String errorMsg="";
					        String tipe="Tire";
					        Connection con2;
							Statement st2;
							con2 = new StagingConn().openConnection();
							st2 = con2.createStatement();
							 String insertReport="INSERT INTO ReportBMW VALUES('"+key+"','"+vin+"','"+voucherNo+"','"+polNum+"',GETDATE(),'"+tipe+"','"+batchNo+"','"+errorMsg+"')";
						     st2.executeUpdate(insertReport);
						}
						  // end Read RDA Tire by VIN
								String jsonReq=jsonTireHeader+jsonTireContent.substring(0, jsonTireContent.length()-1)+"]}";
								logger.info("JSON result : "+jsonReq);
								//System.out.println(jsonReq);
								for (int j = 0; j < columns.length; j++) {
									uploadSheet.autoSizeColumn(i);
								}

								// Write the output to a file
								LocalDate date = LocalDate.now();
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
								FileOutputStream fileOut = new FileOutputStream(new File(path + "\\UP\\" + new ReadParameter().getParameter("Uploader", "Tire")+"_"+date.format(formatter)+".xls"));
								logger.info("Uploader result : "+path + "\\UP\\" + new ReadParameter().getParameter("Uploader", "Tire")+"_"+date.format(formatter)+".xls");
								workbook.write(fileOut);
								fileOut.close();

								// Closing the workbook
								workbook.close();
								
								logger.info("JSON API Process");
								URL url = new URL ("http://172.30.5.42/ApiSompo/B2B/SaveData");
								HttpURLConnection conAPI = (HttpURLConnection)url.openConnection();
								conAPI.setRequestMethod("POST");
								conAPI.setRequestProperty("Content-Type", "application/json; utf-8");
								conAPI.setRequestProperty("Accept", "application/json");
								conAPI.setDoOutput(true);
								try(OutputStream os = conAPI.getOutputStream()) {
								    byte[] input = jsonReq.getBytes("utf-8");
								    os.write(input, 0, input.length);			
								}
								
								try(BufferedReader br = new BufferedReader(
										  new InputStreamReader(conAPI.getInputStream(), "utf-8"))) {
										    StringBuilder response = new StringBuilder();
										    String responseLine = null;
										    while ((responseLine = br.readLine()) != null) {
										        response.append(responseLine.trim());
										    }
										    logger.info("JSON Response : "+response.toString());
										    msg=response.toString();
										    JSONObject obj = new JSONObject(response.toString().replaceAll("null", "\\\"null\\\""));
										    //System.out.println(obj.getString("BatchNo"));
										    if(obj.getString("Code").equals("400")) {
										    	JSONArray arr = obj.getJSONArray("Data");
										    	String err="";
										    	for (int x = 0; x < arr.length(); x++) {
										    		err+=arr.getJSONObject(x).getString("ErrMsg")+"\n";
										    	}
										    	String message="<p>Error upload BMW Tire Insurance</p>\r\n" + 
										    			"<p>Error Message : "+obj.getString("ErrorMsg")+"</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; "+err+"</p>\r\n" + 
										    			"<p>Uploader File&nbsp;&nbsp; : "+path+"\\"+fileName+"</p>\r\n" + 
										    			"<p>&nbsp;</p>\r\n" + 
										    			"<p>Thanks</p>\r\n" + 
										    			"<p>&nbsp;</p>\r\n" + 
										    			"<p>BMW Engine</p>";
										    	SendEmail se=new SendEmail();
												se.sendEmailSingleRecipient(new ReadParameter().getParameter("EmailMKT", "Tire"), new ReadParameter().getParameter("EmailIT", "Tire"), "", "no-reply@sompo.co.id", "BMW-RDA Notification", "webmail.sompo.co.id", message, "Error Upload BMW-RDA Policy", null);
												String sqlTrunc="TRUNCATE TABLE tempBMW";
												st.executeUpdate(sqlTrunc);
										    }else {
										    JSONArray arr = obj.getJSONArray("Data");
										    String policyNo="";
										    String certNo="";
										    String refNo="";
										    String policyNumTemp="";
										    String voucherNum="";
										    String batchNo="";
										    String err="";
									        for (int x = 0; x < arr.length(); x++) {
									             policyNo = arr.getJSONObject(x).getString("PolicyNo");
									             certNo = arr.getJSONObject(x).getString("CertificateNo");
									             refNo= ChasisNumber;
									             voucherNum= arr.getJSONObject(x).getString("Voucher");
									             batchNo=obj.getString("BatchNo");
									             
									             err=arr.getJSONObject(x).getString("ErrMsg");
									             
									             policyNumTemp+=refNo+"#"+policyNo+"/"+certNo+"#"+voucherNum+"#"+batchNo+"#"+err+"]";
									        }
									        String[] policyNum=policyNumTemp.substring(0, policyNumTemp.length()-1).split("]");
									        for(int t=0;t<policyNum.length;t++) {
									        	String[] procReport=policyNum[t].split("#");
									        		String ref=procReport[0];
									        		String dataPol=procReport[1];
									        		String vcr=procReport[2];
									        		String batchNum=procReport[3];
									        		String errM=procReport[4];
									        		String updateReport="UPDATE ReportBMW SET polNum='"+dataPol+"',voucherNo='"+vcr+"',BatchNo='"+batchNum+"',ErrorMsg='"+errM+"' WHERE vin='"+ref+"' AND type='Tire'";
									        		st.executeUpdate(updateReport);
									        	
									        }
									        
										
								logger.info("Create CSV Report");
								FileOutputStream report = new FileOutputStream(new File(path + "\\CSV\\" + "SOMPORDA_"+date.format(formatter)+".csv"), false);
								Writer out = new OutputStreamWriter(new BufferedOutputStream(report));
								out.append("key;FIELD1;FIELD2;FIELD3;FIELD4;FIELD5;FIELD6;FIELD7;");
								out.append("\n");
								String generateReport="select distinct vin,key_report,inputDate from ReportBMW where inputDate=convert(varchar, getdate(), 23) AND ErrorMsg='null'";
								ResultSet rpt=st.executeQuery(generateReport);
								
								while(rpt.next()) {
									out.append(rpt.getString(2));
									out.append(";");
									out.append(rpt.getString(1));
									out.append(";");
									out.append(getPolNum(rpt.getString(1), "EW"));
									out.append(";");
									out.append(getPolNum(rpt.getString(1), "Tire"));
									out.append(";");
									out.append(getvcrNum(rpt.getString(1), "EW"));
									out.append(";");
									out.append(getvcrNum(rpt.getString(1), "Tire"));
									out.append(";");
									out.append(getInputDate(rpt.getString(1), "EW"));
									out.append(";");
									out.append(getInputDate(rpt.getString(1), "Tire"));
									out.append(";");
									out.append("\n");
									
								}
								out.flush();
								out.close();
								logger.info("CSV Report : "+path + "\\CSV\\" + "SOMPORDA_"+date.format(formatter)+".csv");
								
								String msgSQL="Select vin,polNum,ErrorMsg from ReportBMW where BatchNo='"+batchNo+"' ";
								ResultSet rst=st.executeQuery(msgSQL);
								String lsStatus="";
								while(rst.next()) {
									if(rst.getString(2).equals("")) {
										lsStatus+="<tr>\r\n" + 
												"<td style=\"width: 262.6px;\">"+rst.getString(1)+"</td>\r\n" + 
												"<td style=\"width: 262.6px;\">"+rst.getString(3)+"</td>\r\n" + 
												"</tr>\r\n";
									}else {
										lsStatus+="<tr>\r\n" + 
												"<td style=\"width: 262.6px;\">"+rst.getString(1)+"</td>\r\n" + 
												"<td style=\"width: 262.6px;\">"+rst.getString(2)+"</td>\r\n" + 
												"</tr>\r\n";
									}
									
								}
								
								String msgSuskes="<p>Proses Input Tire Insurance Berhasil</p>\r\n" + 
										"<p>&nbsp;</p>\r\n" + 
										"<p>File CSV&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : "+path + "\\CSV\\" + "SOMPORDA_"+date.format(formatter)+".csv"+"</p>\r\n" + 
										"<p>Nama File Upload : "+fileName+"</p>\r\n" + 
										"<p>Batch No&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : "+batchNo+"</p>\r\n" + 
										"<table style=\"width: 540px; height: 82px;\">\r\n" + 
										"<tbody>\r\n" + 
										"<tr>\r\n" + 
										"<td style=\"width: 262.6px;\">Vin</td>\r\n" + 
										"<td style=\"width: 262.6px;\">Status</td>\r\n" + 
										"</tr>\r\n" + 
										lsStatus + 
										"</tbody>\r\n" + 
										"</table>\r\n" + 
										"<p>&nbsp;</p>\r\n" + 
										"<p>&nbsp;</p>\r\n" + 
										"<p>Thanks</p>\r\n" + 
										"<p>&nbsp;</p>\r\n" + 
										"<p>BMW Engine</p>";
								SendEmail se=new SendEmail();
								se.sendEmailSingleRecipient(new ReadParameter().getParameter("EmailMKT", "Tire"), new ReadParameter().getParameter("EmailIT", "Tire"), "", "no-reply@sompo.co.id", "BMW-RDA Notification", "webmail.sompo.co.id", msgSuskes, "Error Upload BMW-RDA Policy", null);
								
								String sqlTrunc="TRUNCATE TABLE tempBMW";
								st.executeUpdate(sqlTrunc);
								}
								}
					
				
	
					}
				}
			}
			valid = watchKey.reset();
			
		} while (valid);
		
		//end Folder watcher

	}
		
		private static String getTypeTire(String vin, String pos) throws SQLException {
			Connection con;
			Statement st;
			con = new StagingConn().openConnection();
			st = con.createStatement();
			String sql = "select concat(TBRAND,' ',Type,' ',Size) typeT,DOT,concat(TBRAND,' ',Type,' ',Size,'-',DOT) remarkT from tempBMW WHERE VinNo='"
					+ vin + "' AND TyrePosition='" + pos + "'";

			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getString(1);
			}
			return "";
		}

		private static String getNoTire(String vin, String pos) throws SQLException {
			Connection con;
			Statement st;
			con = new StagingConn().openConnection();
			st = con.createStatement();
			String sql = "select concat(TBRAND,' ',Type,' ',Size) typeT,DOT,concat(TBRAND,' ',Type,' ',Size,'-',DOT) remarkT from tempBMW WHERE VinNo='"
					+ vin + "' AND TyrePosition='" + pos + "'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getString(2);
			}
			return "";
		}

		private static String getRmkTire(String vin, String pos) throws SQLException {
			Connection con;
			Statement st;
			con = new StagingConn().openConnection();
			st = con.createStatement();
			String sql = "select concat(TBRAND,' ',Type,' ',Size) typeT,DOT,concat(TBRAND,' ',Type,' ',Size,'-',DOT) remarkT from tempBMW WHERE VinNo='"
					+ vin + "' AND TyrePosition='" + pos + "'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getString(3);
			}
			return "";
		}
		
		private static String getCode(String vin, String tipe) throws SQLException {
			Connection con;
			Statement st;
			con = new StagingConn().openConnection();
			st = con.createStatement();
			String sql = "select code,startDate,endDate,polNum from ReportBMW where vin='"+vin+"' AND type='"+tipe+"'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getString(1);
			}
			return "";
		}
		private static String getPolNum(String vin, String tipe) throws SQLException {
			Connection con;
			Statement st;
			con = new StagingConn().openConnection();
			st = con.createStatement();
			String sql = "select polNum,voucherNo from ReportBMW where vin='"+vin+"' AND type='"+tipe+"'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getString(1);
			}
			return "";
		}
		
		private static String getvcrNum(String vin, String tipe) throws SQLException {
			Connection con;
			Statement st;
			con = new StagingConn().openConnection();
			st = con.createStatement();
			String sql = "select polNum,voucherNo from ReportBMW where vin='"+vin+"' AND type='"+tipe+"'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				return rs.getString(2);
			}
			return "";
		}
		
		private static String getInputDate(String vin, String tipe) throws SQLException {
			Connection con;
			Statement st;
			con = new StagingConn().openConnection();
			st = con.createStatement();
			String sql = "select InputDate from ReportBMW where vin='"+vin+"' AND type='"+tipe+"'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				return new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate(1));
			}
			return "";
		}
		
		private static String getStartDate(String vin, String tipe) throws SQLException {
			Connection con;
			Statement st;
			con = new StagingConn().openConnection();
			st = con.createStatement();
			String sql = "select code,startDate,endDate,polNum from ReportBMW where vin='"+vin+"' AND type='"+tipe+"'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				
				return new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate(2));
			}
			return "";
		}
		
		private static String getEndDate(String vin, String tipe) throws SQLException {
			Connection con;
			Statement st;
			con = new StagingConn().openConnection();
			st = con.createStatement();
			String sql = "select code,startDate,endDate,polNum from ReportBMW where vin='"+vin+"' AND type='"+tipe+"'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				
				return new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate(3));
			}
			return "";
		}
		
		private static String getPremi(String kode, String model) throws SQLException {
			Connection con;
			Statement st;
			con = new StagingConn().openConnection();
			st = con.createStatement();
			String sql = "select premi from PremiBMW where kode='"+kode+"' AND model='"+model+"'";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				
				return rs.getString(1);
			}
			return "0";
		}

}
