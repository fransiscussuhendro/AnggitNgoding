//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Base64;
// 
// 
//public class HttpBasicAuthenticationExample {
// 
//    public static void main(String[] args) {
// 
//        String usernameColonPassword = "SB-Mid-server-3eEV1LNxdkZU5xqFHXHhxd5N:";
//        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString(usernameColonPassword.getBytes());
// 
//        BufferedReader httpResponseReader = null;
//        try {
//            // Connect to the web server endpoint
//            //URL serverUrl = new URL("https://api.sandbox.midtrans.com/v2/SANDBOX-M007292-112 /status");
//        	//URL serverUrl = new URL("https://api.sandbox.midtrans.com/snap/v1/transactions");
//            HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();
// 
//            // Set HTTP method as GET
//            urlConnection.setRequestMethod("GET");
// 
//            // Include the HTTP Basic Authentication payload
//            urlConnection.addRequestProperty("Authorization", basicAuthPayload);
// 
//            // Read response from web server, which will trigger HTTP Basic Authentication request to be sent.
//            httpResponseReader =
//                    new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//            String lineRead;
//            while((lineRead = httpResponseReader.readLine()) != null) {
//                System.out.println(lineRead);
//            }
// 
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        } finally {
// 
//            if (httpResponseReader != null) {
//                try {
//                    httpResponseReader.close();
//                } catch (IOException ioe) {
//                    // Close quietly
//                }
//            }
//        }
// 
//    }
// 
//}


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Map;

/**
 * Usage:
 *
 * Download this file and unzip it on a lib folder (If it is on another folder, make sure to change it on the commands)
 * http://www.java2s.com/Code/Jar/j/Downloadjavajsonjar.htm (java-json.jar)
 * Download this file and put it on the folder
 * Compile this file with this command
 * javac -cp lib/java-json.jar JavaRestClientTest.java
 * Run the class with the command
 * java -cp lib/java-json.jar:./ JavaRestClientTest
 */
public class HttpBasicAuthenticationExample {

    public static void main(String[] args) {

        try {
            // Construct manually a JSON object in Java, for testing purposes an object with an object
            

            // URL and parameters for the connection, This particulary returns the information passed
          String usernameColonPassword = "SB-Mid-server-3eEV1LNxdkZU5xqFHXHhxd5N:";
          String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString(usernameColonPassword.getBytes());

            URL url = new URL("https://app.sandbox.midtrans.com/snap/v1/transactions");
            HttpURLConnection httpConnection  = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Authorization", basicAuthPayload);
            // Not required
            // urlConnection.setRequestProperty("Content-Length", String.valueOf(input.getBytes().length));

            // Writes the JSON parsed as string to the connection
            DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
            String data="{\r\n" + 
            		"  \"transaction_details\": {\r\n" + 
            		"    \"order_id\": \"ORDER-104\",\r\n" + 
            		"    \"gross_amount\": 10000\r\n" + 
            		"  }\r\n" + 
            		"}";
            wr.write(data.toString().getBytes());
            Integer responseCode = httpConnection.getResponseCode();

            BufferedReader bufferedReader;

            // Creates a reader buffer
            if (responseCode > 199 && responseCode < 300) {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
            }

            // To receive the response
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();

            // Prints the response
            System.out.println(content.toString());

        } catch (Exception e) {
            System.out.println("Error Message");
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }

}