import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedWriter;
import javax.net.ssl.HttpsURLConnection;
import java.util.ArrayList;
import java.io.OutputStreamWriter;
import java.util.List;

public class javaHttpTest{
	public static void main(String[] args) throws Exception {
		//Sets up the connection and makes the POST
		String url = "https://languagetool.org/api/v2/check";
		String urlParameters = "?&text=this%20iss%20a%20test&language=en-US";
		
		URL obj = new URL(url + urlParameters);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=.05");


		//This section prints the response information	
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
	
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null){
			response.append(inputLine);
		}
		System.out.println(response.toString());


		
	}
	

}
