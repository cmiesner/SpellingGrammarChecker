package com.testing.app;

import java.awt.*;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;

//Spencer Added
import java.text.BreakIterator;
import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;
import java.util.Locale;
import java.util.ArrayList;
import java.util.*;

//for processing PDF files
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
 
import org.apache.pdfbox.pdmodel.PDPageContentStream;
 
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

//for processing word docs
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;



public class App extends JFrame 
{

    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JTextArea textArea;
    private JTextPane output;
    private JPopupMenu popup;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    App frame = new App();
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

    public App() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        getContentPane().setBackground(new java.awt.Color(96, 146, 187));
        setBounds(100, 100, 850, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblEnterText = new JLabel("Enter Text:");
        lblEnterText.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblOutput = new JLabel("Output:");
        lblOutput.setHorizontalAlignment(SwingConstants.CENTER);

        textField = new JTextField("File Location");
        textField.setColumns(10);

        JButton btnSelectFile = new JButton("Select File");

        JLabel lblSimpleSpellingAnd = new JLabel("Simple Spelling and Grammar Checker");
        lblSimpleSpellingAnd.setHorizontalAlignment(SwingConstants.CENTER);
        lblSimpleSpellingAnd.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 15));	
	


        JButton btnSubmit = new JButton("Submit");


        //public Boolean isFileInput = false;   

        //File inputFile = "";

        //Spencer Added
        //Runs what is in the input field through the api
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
        //outputs a selected file to the input field
        btnSelectFile.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        final JFileChooser fc = new JFileChooser();

                        int returnVal = fc.showOpenDialog(App.this);

                                //Verify that the program is allowed to open the file chooser
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File inputFile = fc.getSelectedFile();
                            System.out.println(inputFile);
                            String filePath = inputFile.getAbsolutePath();
                            textField.setText(filePath);
			    if (filePath.indexOf(".pdf") > -1){
				    try {
					PDDocument document = PDDocument.load(new File(filePath));
					document.getClass();

					if (!document.isEncrypted()) {
						PDFTextStripperByArea stripper = new PDFTextStripperByArea();
						stripper.setSortByPosition(true);
						
						PDFTextStripper tStripper = new PDFTextStripper();
						
						String pdfFileInText = tStripper.getText(document);
						String lines[] = pdfFileInText.split("\\r?\\n");
						String completeString = "";
	
						for (String line : lines){
							completeString = completeString + line;
						}
						System.out.println(completeString);
						completeString = completeString.replaceAll("\\t", " ");
						textArea.setText(completeString);
						
					}
					document.close();
				    } catch (Exception err){
					System.out.println(err);
				    }
			    } else if (filePath.indexOf(".docx") > -1 || filePath.indexOf(".doc") > -1) {
				   try {
						XWPFDocument docx = new XWPFDocument(new FileInputStream(filePath));
						
						XWPFWordExtractor we = new XWPFWordExtractor(docx);
						System.out.println(we.getText());
		
						String collectedText = we.getText();
						collectedText = collectedText.replaceAll("\\n", " ");
						collectedText = collectedText.replaceAll("\\t", " ");
						collectedText = collectedText.replaceAll("\\s{2,}", " ");
						textArea.setText(collectedText);
						docx.close();
				   } catch (Exception err) {
					System.out.println(err);
				   }	
			    } else {
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
					fileAsString = fileAsString.replaceAll("\\t", " ");
					System.out.println(fileAsString);
					textArea.setText(fileAsString);

					makeConnection(fileAsString);
				    } catch (Exception exc) {
					System.out.println(exc);
				    }
			    }
                        }
                    }
                }
        );

        //Davis Added
        JScrollPane scrollPane_1 = new JScrollPane();

        JScrollPane scrollPane_2 = new JScrollPane();

        final Clipboard clip = getToolkit().getSystemClipboard();
        JButton btnCopyText = new JButton("Copy Text");

        //Copies text from Output box
        btnCopyText.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        String s = "";
                        try {
                            s = output.getDocument().getText(1, output.getDocument().getLength());
                        } catch (BadLocationException exc){
                            exc.printStackTrace();
                        }
                        StringSelection clipString = new StringSelection(s);
                        clip.setContents(clipString, clipString);
                    }
                }
        );

        JButton btnWriteToFile = new JButton("Write to File");

        btnWriteToFile.addActionListener(
                new ActionListener(){
                    public void actionPerformed(ActionEvent e) {

                        String toFile = output.getText();
                        String textFieldContents = textField.getText();

                        toFile = toFile.replaceAll("\\<[^>]*>","");
                        toFile = toFile.replaceAll("\\n", "");
                        toFile = toFile.replaceAll("\\t", "");
                        toFile = toFile.trim();
                        //Check if a file was input
                        if (textFieldContents.indexOf("File Location") == 0){
                                //get first few words and make it a file
                                int counter = 0;
                                String createdFilePath = "";
                                for (String word : toFile.split(" ")){
                                        counter++;
                                        if (counter == 4){
                                                createdFilePath = createdFilePath + ".txt";
                                                createdFilePath = System.getProperty("user.dir") + "/" + createdFilePath;
                                                textField.setText(createdFilePath);
                                                break;
                                        }
                                        if (counter == 1){
                                                createdFilePath = createdFilePath +  word;
                                        } else {
                                                createdFilePath = createdFilePath + "_" +  word;
                                        }
                                }
                                File revisedFile = new File(createdFilePath);
                                try{
                                        FileWriter writer = new FileWriter(revisedFile);
                                        writer.write(toFile);
                                        writer.close();
                                } catch (Exception err){
                                        System.out.println(err);
                                }
                        }else{
                                //remove the last part of the file
                                //example /User/bin/seniorProject/test.txt = /User/bin/seniorProject/test
                                //then add Revised.txt to the end
                                String newFilePath = "";
                                if (textFieldContents.indexOf(".txt") >= 0){
                                        newFilePath = textFieldContents.replace(".txt", "Revised.txt");
                                        System.out.println(newFilePath);
                                } else if (textFieldContents.indexOf(".pdf") >= 0){
                                        newFilePath = textFieldContents.replace(".pdf", "Revised.txt");
                                        System.out.println(newFilePath);
                                } else if (textFieldContents.indexOf(".docx") >= 0){
                                        newFilePath = textFieldContents.replace(".docx", "Revised.txt");
                                        System.out.println(newFilePath);
                                } else if (textFieldContents.indexOf(".doc") >= 0){
                                        newFilePath = textFieldContents.replace(".doc", "Revised.txt");
                                        System.out.println(newFilePath);
                                }
                                File revisedFile = new File(newFilePath);
                                try{
                                        FileWriter writer = new FileWriter(revisedFile);
                                        writer.write(toFile);
                                        writer.close();
                                } catch (Exception err){
                                        System.out.println(err);
                                }      
                        }
                    }
                }
        );

        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(264)
                                .addComponent(lblSimpleSpellingAnd, GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                                .addGap(273))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(500)
                                                .addComponent(btnWriteToFile, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(btnCopyText, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(116)
                                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING) 
                                                        .addComponent(lblEnterText, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                                                        .addComponent(lblOutput, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                                .addComponent(btnSubmit))
                                                        .addGroup(groupLayout.createSequentialGroup()
                                                                .addComponent(textField, GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(btnSelectFile))
                                                        .addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                                                        .addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))))
                                .addGap(146))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblSimpleSpellingAnd, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addComponent(lblEnterText)
                                .addGap(12)
                                .addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                                .addGap(12)
                                .addComponent(btnSubmit)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(lblOutput)
                                .addGap(12)
                                .addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(btnCopyText)
                                        .addComponent(btnWriteToFile))
                                .addGap(45)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(btnSelectFile)
                                        .addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(43))
        );

        textArea = new JTextArea();
        textArea.setRows(15);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        scrollPane_1.setViewportView(textArea);

        output = new JTextPane();
        output.setContentType("text/html");
        scrollPane_2.setViewportView(output);

        popup = new JPopupMenu();
//        output.addMouseListener(createPopupListener(output));
        addPopup(output, popup);

        getContentPane().setLayout(groupLayout);
    }

    //Davis Added
    // An inner class to show when popup events occur
    class PopupPrintListener implements PopupMenuListener {
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
//            System.out.println("Popup menu will be visible!");
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
//            System.out.println("Popup menu will be invisible!");
        }

        public void popupMenuCanceled(PopupMenuEvent e) {
//            System.out.println("Popup menu is hidden!");
        }
    }
    //Spencer Added
    public void makeConnection(String toCheck) throws Exception{
        //need to parse out bad characters
        System.out.println("---------------------------------------------------");

        //Sets up the connection and makes the POST
        String url = "http://api.grammarbot.io/v2/check?api_key=XYZ";
        String toCheckUrl = toCheck.replaceAll(" ","%20");
        String urlParameters = "&text=" + toCheckUrl + "&language=en-US";


        URL con = new URL(url + urlParameters);
        String line;
        try {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(con.openStream(), "UTF-8"));
            for (String newLine; (newLine = reader.readLine()) != null;) {
                System.out.println(newLine);
                line = newLine;
            }
        } catch (Exception error){
		System.out.println(error);
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
        int [] underline = new int[100];
        for (int i= 0; i<100; i++) {underline[i]=0;}
        String tempResponse = response.toString();
        int underlineX = 0;
        ArrayList<String> words = new ArrayList<String>();
        Map<String, String> offsetLengthMap = new HashMap<String, String>();
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
                    //added by Cole
                    //prints all text from the textArea in the output JLabel.
                    String offset = "";
                    String length = "";
                    if(toRemove.contains("context"))
                    {
                        offset = toRemove.substring(toRemove.indexOf("offset")+8, toRemove.indexOf("offset") + 13).replaceAll("([, A-Za-z\"])", "");
                        length = toRemove.substring(toRemove.indexOf("length")+8, toRemove.indexOf("length") + 13).replaceAll("([, A-Za-z\"])", "");
                        System.out.println("offset:" + offset);
                        System.out.println("length:" + length);

                        //Spencer added the line below
                        offsetLengthMap.put(offset, length);

                        underline[underlineX] = Integer.parseInt(offset);
                        underline[underlineX+1] = Integer.parseInt(length);
                        underlineX+=2;
                    }
                }
                else{
                    System.out.println("\nNo Error!");
                }
                toRemove = toRemove.substring(0, toRemove.length() - 1);
                toRemove = toRemove + "}}}";
                tempResponse = tempResponse.replace(toRemove, "");
                System.out.println("toRemove: " +  toRemove);
            }
        }

        //Added by Spencer
        //Add underlines to words that are incorrect
        String inputText = textArea.getText();
        String text = inputText;
        for (Map.Entry entry : offsetLengthMap.entrySet()){
            System.out.println("Key: " + entry.getKey());
            System.out.println("Value: " + entry.getValue());
            String offsetString = String.valueOf(entry.getKey());
            String lengthString = String.valueOf(entry.getValue());
            int offset = Integer.parseInt(offsetString);
            int length = Integer.parseInt(lengthString);

            inputText = textArea.getText();
            String toUnderline = inputText.substring(offset, length+offset);

            String underlineFormat = "<span color=\"red\"><U>" + toUnderline + "</U></span>";


            text = text.replace(toUnderline, underlineFormat);
        }
        text = "<html><body style='width:450px'><span>" + text + "</body></html>";

        String[] allWords = text.split(" ");

        String correctText = "";

        //Parses out the correct words from the string
        for (String word : allWords){
            if ((word.indexOf("<") == -1) && (word.indexOf(">") == -1 )){
                correctText += word + " ";
            }
        }
        System.out.println("correctText: " + correctText);
        output.setText(text);

        String correctTextLast = "";

        //Added by Monis
        for (int x=0; x<messageCount*2; x+=2) {

            inputText = textArea.getText();
            correctTextLast = inputText.substring(underline[x] + underline[x+1], inputText.length());

        }
        final String correctTextNew = correctText;
        final String correctTextLastNew = correctTextLast;
        for (int x = 0; x < messageCount; x++){
            //Added by Davis
            //Get the user selected value from the popup
            ActionListener menuListener = new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    System.out.println("Popup menu item ["
                            + event.getActionCommand() + "] was pressed.");

                    //Replace error with correction
                    output.setText("<html><body style='width: 450px'>" + correctTextNew
                            + event.getActionCommand()
                            + correctTextLastNew + "</body></html>");
                    //clears items from popup menu
                    popup.removeAll();
                }
            };

            //Added by Davis
            for (String w : words) {
                JMenuItem item;
                System.out.println(w);
                //suggested corrections are displayed in the popup menu
                popup.add(item = new JMenuItem(w));
                item.setHorizontalTextPosition(JMenuItem.CENTER);
                item.addActionListener(menuListener);
            }

            popup.setLabel("Justification");
            popup.setBorder(new BevelBorder(BevelBorder.RAISED));
            popup.addPopupMenuListener(new PopupPrintListener());
        }
    }
    //Added by Davis
    private static void addPopup(final JTextPane output, final JPopupMenu popup) {
        output.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }
            private void showMenu(MouseEvent e) {
                popup.show(output, e.getX(), e.getY());
            }
        });
    }
}

/*
	public static void main(String[] args) throws IOException {
        	System.out.println( "Hello World!" );

	}
}
*/
