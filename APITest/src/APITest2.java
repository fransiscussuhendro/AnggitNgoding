import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
 
 
public class APITest2 {
 
    public static void main(String[] args) {
 
        String usernameColonPassword = "SB-Mid-server-3eEV1LNxdkZU5xqFHXHhxd5N:";
        String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString(usernameColonPassword.getBytes());
 
        BufferedReader httpResponseReader = null;
        try {
            // Connect to the web server endpoint
            URL serverUrl = new URL("https://api.sandbox.midtrans.com/v2/ORDER-109/status");
        	//URL serverUrl = new URL("https://api.sandbox.midtrans.com/snap/v1/transactions");
            HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();
 
            // Set HTTP method as GET
            urlConnection.setRequestMethod("GET");
 
            // Include the HTTP Basic Authentication payload
            System.out.println(basicAuthPayload);
            urlConnection.addRequestProperty("Authorization", basicAuthPayload);
 
            // Read response from web server, which will trigger HTTP Basic Authentication request to be sent.
            httpResponseReader =
                    new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String lineRead;
            while((lineRead = httpResponseReader.readLine()) != null) {
                System.out.println(lineRead);
            }
 
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
 
            if (httpResponseReader != null) {
                try {
                    httpResponseReader.close();
                } catch (IOException ioe) {
                    // Close quietly
                }
            }
        }
 
    }
 
}