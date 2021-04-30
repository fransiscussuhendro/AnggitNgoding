import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;

class cobadecode {
  public static void main(String[] args) {
	  byte[] decodedBytes = Base64.getDecoder().decode("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");
	  String decodedString = new String(decodedBytes);
	  System.out.println(decodedString);
  }
}