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
					makeConnection(contents);
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
						
						makeConnection(fileAsString);	
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
	String url = "http://api.grammarbot.io/v2/check?api_key=9JMF2Y56";
	String toCheckUrl = toCheck.replaceAll(" ","%20");
	String urlParameters = "?&text=" + toCheckUrl + "&language=en-US";


	URL con = new URL(url + urlParameters);
	String line;
	try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.openStream(), "UTF-8"))) {
		for (String newLine; (newLine = reader.readLine()) != null;) {
			System.out.println(newLine);
			line = newLine;
		}
	}

		
    	// Added by Monis
	// Edited by Spencer

	BufferedReader in = new BufferedReader(new InputStreamReader(con.openStream(), "UTF-8"));
	
	String inputLine;
    	StringBuffer response = new StringBuffer();

    	while ((inputLine = in.readLine()) != null){
		response.append(inputLine);
    	}
	
	System.out.println(response.toString());

	//Added by Spencer
	String toFind = "message";
	int index = 0;
	int messageCount = 0;
	
	while (index != -1) {
		index = response.indexOf(toFind, index);

		if (index != -1) {
			messageCount++;
			index += toFind.length();
		}

	}

	String tempResponse = response.toString();
	
	//Added by Monis
	//Edited by Spencer
	//parse out the possible fixes for each mistake found.
	for (int x = 0; x < messageCount; x++){
		if (tempResponse.indexOf("\"message\"") > 0) {
			String[] op = tempResponse.substring(tempResponse.indexOf("\"message\"")-1, tempResponse.indexOf("}}}")).split(",");
			String toRemove = "";

			for (int i = 0; i < op.length; i++) {
				toRemove = toRemove + op[i] + ",";
			}

			if (toRemove.indexOf("replacements") > 0) {
				String[] substrings = toRemove.substring(toRemove.indexOf("replacements")+15, toRemove.indexOf("offset")-3).split(",");
				ArrayList<String> words = new ArrayList<String>();


				for (int i = 0; i < substrings.length; i++) {
					if (substrings[i].indexOf("}") > 0){
						words.add(substrings[i].substring(substrings[i].indexOf(":\"")+2, substrings[i].indexOf("\"}")));
					} else {
						substrings[i] = substrings[i] + ">";
						String toAdd = substrings[i].substring(substrings[i].indexOf(":\"")+2, substrings[i].indexOf(">")-1) + " "  + substrings[i+1].substring(substrings[i+1].indexOf(":\"")+2, substrings[i+1].indexOf("\"}"));
						words.add(toAdd);
						i++;
					}
				}
				System.out.println();
				for (String w : words) {
					System.out.println(w);
				}
			}
			else{
				System.out.println("\nNo Error!");
			}
			toRemove = toRemove.substring(0, toRemove.length() - 1);
			toRemove = toRemove + "}}}";
			tempResponse = tempResponse.replace(toRemove, "");

		}
	}

    }
}
