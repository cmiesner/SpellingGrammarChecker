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
import java.io.*;
import java.text.BreakIterator;


public class javaHttpTest{
	public static void main(String[] args) throws Exception {

		//Check if there were any command line args passed in
		//Then use a BufferedReader to read the contents of the file to a string
		//Then seperate the string out by sentences using BreakIterator
		//This will allow us to make sure that we are limiting the size of the string that is sent to the RestAPI
		//This is needed because the API only allows you to correct 30 mistakes at a time
		//
		//
		//May need to parse out all new line charachters unless they are for the structure of the text example below
		//
		//This is an example:
		//	Example cont.

		if (args.length > 0) {
			String inputFile = args[0];
			System.out.println(inputFile);
			BufferedReader inputFileRead = new BufferedReader(new FileReader(inputFile));
			String line;
			StringBuffer buffer = new StringBuffer();
			
			while ((line = inputFileRead.readLine()) != null) {
				buffer.append(line + "\n");
			}
			//System.out.println(buffer.toString());
			String fileAsString = buffer.toString();
			
			BreakIterator boundary = BreakIterator.getSentenceInstance();
			boundary.setText(fileAsString);
			int start = boundary.first();
			for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
				System.out.println(fileAsString.substring(start, end));
			}
		} else {
			System.out.println("No File was given");
		}

		/*
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
		*/	

		File file = new File("/Users/swerremeyer/bin/seniorProject/forFirewall.json");
	
		BufferedReader in = new BufferedReader(new FileReader(file));

		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null){
			response.append(inputLine);
		}
		//System.out.println(response.toString());
		
		
		
		String pattern = ".*(\"matches\":.*}])";
		String matches = response.toString().replaceAll(pattern, "$1");
		
		//System.out.println(matches);
	

		//Length is 30 because the api only allows for the correction of 30 mistakes
		String[] messages = new String[30];

		String toFind = "message";
		int index = 0;
		int messageCount = 0;
		
		while (index != -1) {
			index = matches.indexOf(toFind, index);
	
			if (index != -1) {
				messageCount++;
				index += toFind.length();
			}

		} 

		//System.out.println(messageCount);
		String matchesCopy = matches;

		//Gets all messages in the json
		for (int x = 0; x < messageCount; x++) {
			//Working on Regex here
			pattern = "(\"message\":\"[ \\w]*\".*)\\{\"message\"?";//|(?:(\"message\":\"[ \\w]*\".*)\\}\\]\\})";
			//pattern = ".*(\"message\":\"[\\w ]*\"),(.*e)(?:.*)";
			System.out.println("$1");
			String message = matchesCopy.replaceAll(pattern, "$1");
			matchesCopy = matchesCopy.replace(message, "");
			
			System.out.println(message + "\n");
		}

	
	}
	

}
