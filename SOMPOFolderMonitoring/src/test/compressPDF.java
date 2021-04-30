package test;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfStream;
import com.lowagie.text.pdf.PdfWriter;

public class compressPDF {
  public static void main(String[] args) throws Exception {
    PdfReader reader = new PdfReader("L:\\OPERATION\\IT\\01.COMMON\\ANGGIT\\FIRE_POLICY\\JK-FCF-0000193-00016-2019-01;PLS.PDF");
    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("my.pdf"),PdfWriter.VERSION_1_5);
    stamper.setFullCompression();
    stamper.getWriter().setCompressionLevel(PdfStream.NO_COMPRESSION);
    stamper.close();

    reader = new PdfReader("L:\\OPERATION\\IT\\01.COMMON\\ANGGIT\\FIRE_POLICY\\JK-FCF-0000193-00016-2019-01;PLS.PDF");
    stamper = new PdfStamper(reader, new FileOutputStream("myDecompressed.pdf"), '1');
    Document.compress = false;
    int total = reader.getNumberOfPages() + 1;
    for (int i = 1; i < total; i++) {
      reader.setPageContent(i, reader.getPageContent(i));
    }
    stamper.close();

    showFileSize("L:\\OPERATION\\IT\\01.COMMON\\ANGGIT\\FIRE_POLICY\\JK-FCF-0000193-00016-2019-01;PLS.PDF");
    showFileSize("my.pdf");
    showFileSize("myDecompressed.pdf");

  }

  private static void showFileSize(String filename) throws IOException {
    PdfReader reader = new PdfReader(filename);
    System.out.print("Size ");
    System.out.print(filename);
    System.out.print(": ");
    System.out.println(reader.getFileLength());
  }
}
