package sompo.mailenginecore.Dbconfig;



public class ConnAttribute {
	private static String stgDBUser;
	private static String stgPassword;
	private static String stgDBName;
	private static String stgServer;
	private static String aegDBUser;
	private static String aegPassword;
	private static String aegDBName;
	private static String aegServer;
	
	
	public static String getStgDBUser() {
		return stgDBUser;
	}
	public static void setStgDBUser(String stgDBUser) {
		ConnAttribute.stgDBUser = stgDBUser;
	}
	public static String getStgPassword() {
		return stgPassword;
	}
	public static void setStgPassword(String stgPassword) {
		ConnAttribute.stgPassword = stgPassword;
	}
	public static String getStgDBName() {
		return stgDBName;
	}
	public static void setStgDBName(String stgDBName) {
		ConnAttribute.stgDBName = stgDBName;
	}
	public static String getStgServer() {
		return stgServer;
	}
	public static void setStgServer(String stgServer) {
		ConnAttribute.stgServer = stgServer;
	}
	public static String getAegDBUser() {
		return aegDBUser;
	}
	public static void setAegDBUser(String aegDBUser) {
		ConnAttribute.aegDBUser = aegDBUser;
	}
	public static String getAegPassword() {
		return aegPassword;
	}
	public static void setAegPassword(String aegPassword) {
		ConnAttribute.aegPassword = aegPassword;
	}
	public static String getAegDBName() {
		return aegDBName;
	}
	public static void setAegDBName(String aegDBName) {
		ConnAttribute.aegDBName = aegDBName;
	}
	public static String getAegServer() {
		return aegServer;
	}
	public static void setAegServer(String aegServer) {
		ConnAttribute.aegServer = aegServer;
	}

}
