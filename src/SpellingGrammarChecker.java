import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.awt.event.*;

//Spencer Added
import java.text.BreakIterator;
import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.util.Locale;
import java.util.ArrayList;

public class SpellingGrammarChecker extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
//    private JScrollPane scrollPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SpellingGrammarChecker frame = new SpellingGrammarChecker();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public SpellingGrammarChecker() {
        setBounds(100, 100, 838, 608);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        textArea.setRows(15);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);

        //not currently being used
//        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JLabel lblEnterText = new JLabel("Enter Text:");
        lblEnterText.setHorizontalAlignment(SwingConstants.CENTER);

        textField = new JTextField("File Location");
        textField.setColumns(10);

        JButton btnSelectFile = new JButton("Select File");

        JLabel lblSimpleSpellingAnd = new JLabel("Simple Spelling and Grammar Checker");
        lblSimpleSpellingAnd.setHorizontalAlignment(SwingConstants.CENTER);
        lblSimpleSpellingAnd.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 15));

        JButton btnSubmit = new JButton("Submit");

	//Spencer Added
	btnSubmit.addActionListener(
		new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String contents = textArea.getText();
				System.out.println(contents);
				
				try {
					//Parse each sentence of the text area
					contents = contents.replaceAll("\\n", " ");
					BreakIterator boundary = BreakIterator.getSentenceInstance(Locale.US);
					boundary.setText(contents);
					int start = boundary.first();
					for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
						String currentSentence = contents.substring(start, end);

						makeConnection(currentSentence);
					}
				} catch (Exception exc) {
					System.out.println(exc);
				}
			}
		}
	);

	//Spencer Added
	btnSelectFile.addActionListener(
		new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();

				int returnVal = fc.showOpenDialog(SpellingGrammarChecker.this);
				
				//Verify that the progam is allowed to open the file chooser
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					String filePath = file.getAbsolutePath();
					
					//Read the selected file and parse out each sentence
					try {
						BufferedReader inputFileRead = new BufferedReader(new FileReader(filePath));
						String line; 
						StringBuffer buffer = new StringBuffer();
						
						while ((line = inputFileRead.readLine()) != null) {
							buffer.append(line + "\n");
						}
							
						String fileAsString = buffer.toString();
						fileAsString = fileAsString.replaceAll("\\n", " ");
						System.out.println(fileAsString);
						
						BreakIterator boundary = BreakIterator.getSentenceInstance(Locale.US);
						boundary.setText(fileAsString);
						int start = boundary.first();
						for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
							String currentSentence = fileAsString.substring(start, end);
							
							makeConnection(currentSentence);	
						}
						  
					} catch (Exception exc) {
						System.out.println(exc);
					}
					
				}
			}
		}
	);

        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(378)
                                .addComponent(lblEnterText, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(392))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(btnSubmit))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(116)
                                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                                        .addComponent(textArea)
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addComponent(textField, GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                                                                .addGap(18)
                                                                .addComponent(btnSelectFile)))))
                                .addGap(113))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(264)
                                .addComponent(lblSimpleSpellingAnd, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(273))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblSimpleSpellingAnd, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addComponent(lblEnterText)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(textArea)
                                .addGap(18)
                                .addComponent(btnSubmit)
                                .addGap(36)
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                        .addComponent(btnSelectFile)
                                        .addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(83))
        );
        getContentPane().setLayout(groupLayout);

    }

    //Spencer Added
    public void makeConnection(String toCheck) throws Exception{
	//need to parse out bad characters
	System.out.println("---------------------------------------------------");
		
	//Sets up the connection and makes the POST
	String url = "https://languagetool.org/api/v2/check";
	toCheck = toCheck.replaceAll("\\n", "");
	System.out.println(toCheck);
	String toCheckUrl = toCheck.replaceAll(" ","%20");
	System.out.println(toCheckUrl);

	String urlParameters = "?&text=" + toCheckUrl + "&language=en-US";
	
	URL obj = new URL(url + urlParameters);
	HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	
	con.setRequestMethod("POST");
	con.setRequestProperty("User-Agent", "Mozilla/5.0");
	con.setRequestProperty("Accept-Language", "en-US,en;q=.05");


	
	int responseCode = con.getResponseCode();
	System.out.println("\nSending 'POST' request to URL : " + url);
	System.out.println("Post parameters : " + urlParameters);
	System.out.println("Response Code : " + responseCode);
//	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	
	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null){
        response.append(inputLine);
    }
    // Added by Monis
    if (response.indexOf("replacements")>0) {
    	
	    String[] op = response.substring(response.indexOf("replacements")+15,response.indexOf("offset")-3).split(",");
	    ArrayList<String> words = new ArrayList<String>();
	    
	    for (int i=0; i<op.length; i++) {
	    
	    	words.add(op[i].substring(op[i].indexOf(":\"")+2, op[i].indexOf("\"}")));	
	    }
	    for (String w : words) {
	    	
	    	System.out.println(w);    	
	    }
    }
    else{System.out.println("\nNo Error!");}
    }
}