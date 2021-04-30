import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {

            File f = new File("src/input1.txt");

            BufferedReader b = new BufferedReader(new FileReader(f));

            String readLine = "";
            List<String> kingName=new ArrayList<String>();

            while ((readLine = b.readLine()) != null) {
            	kingName.add(readLine);
            }
            try {
                FileWriter myWriter = new FileWriter("src/output1.txt");
                
                
             
            List<String> supplierNames2=new RoyalRumble().getSortedList(kingName);
            for(int i=0;i<supplierNames2.size();i++) {
            	myWriter.write(supplierNames2.get(i).toString());
            	myWriter.write("\n");
      	  }
            myWriter.close();
            
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
              }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

	}


