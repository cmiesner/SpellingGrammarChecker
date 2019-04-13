import java.awt.*;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.*;

//Spencer Added
import java.text.BreakIterator;
import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.util.Locale;
import java.util.ArrayList;

public class SpellingGrammarChecker extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JTextArea textArea;
    private JLabel output;
    private JPopupMenu popup;

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
        setBounds(100, 100, 850, 800);
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

                        //Verify that the program is allowed to open the file chooser
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

        JScrollPane scrollPane_1 = new JScrollPane();

        JScrollPane scrollPane_2 = new JScrollPane();

        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(378)
                                .addComponent(lblEnterText, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                .addGap(392))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(264)
                                .addComponent(lblSimpleSpellingAnd, GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
                                .addGap(273))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(184)
                                .addComponent(lblOutput, GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                                .addGap(203))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(btnSubmit))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(116)
                                                .addComponent(scrollPane_1))
                                        .addGroup(groupLayout.createSequentialGroup()
                                                .addGap(116)
                                                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                                                        .addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
                                                                .addComponent(textField, GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                                                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                                                .addComponent(btnSelectFile))
                                                        .addComponent(scrollPane_2))))
                                .addGap(146))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblSimpleSpellingAnd, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addComponent(lblEnterText)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                                .addGap(12)
                                .addComponent(btnSubmit)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(lblOutput)
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                                .addGap(50)
                                .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSelectFile))
                                .addGap(362))
        );

        textArea = new JTextArea();
        scrollPane_1.setViewportView(textArea);
        textArea.setRows(15);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);

        output = new JLabel();
        scrollPane_2.setViewportView(output);

        popup = new JPopupMenu();
        addPopup(output, popup);

        getContentPane().setLayout(groupLayout);
    }

    // An inner class to check whether mouse events are the popup trigger
    class MousePopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            checkPopup(e);
        }

        public void mouseClicked(MouseEvent e) {
            checkPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            checkPopup(e);
        }

        private void checkPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(SpellingGrammarChecker.this, e.getX(), e.getY());
            }
        }
    }

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

                    //Added by Davis
                    //Get the user selected value from the popup
                    ActionListener menuListener = new ActionListener() {
                        public void actionPerformed(ActionEvent event) {
                            System.out.println("Popup menu item ["
                                    + event.getActionCommand() + "] was pressed.");
                            output.setText(event.getActionCommand());
                        }
                    };

                    //added by Cole
                    //prints all text from the textArea in the givenText JLabel.
                    String offset = "";
                    String length = "";
                    if(toRemove.contains("context"))
                    {
                        offset = toRemove.substring(toRemove.indexOf("offset")+8, toRemove.indexOf("offset") + 13).replaceAll("([, A-Za-z\"])", "");
                        length = toRemove.substring(toRemove.indexOf("length")+8, toRemove.indexOf("length") + 13).replaceAll("([, A-Za-z\"])", "");
                        System.out.println("offset:" + offset);
                        System.out.println("length:" + length);
                    }
                    String inputText = textArea.getText();
                    String correctText = inputText.substring(0, Integer.parseInt(offset));
                    String correctTextLast = inputText.substring(Integer.parseInt(offset) + Integer.parseInt(length), inputText.length());
                    output.setText("<html><span>" + correctText + "</span><span color=\"red\"><U>" + inputText.substring(Integer.parseInt(offset), Integer.parseInt(offset) + Integer.parseInt(length)) + "</U></span><span>" + correctTextLast + "</html>");
//                    output.setText(inputText);

                    for (String w : words) {
                        JMenuItem item;
                        System.out.println(w);
                        //suggested corrections are displayed in the popup menu
                        popup.add(item = new JMenuItem(w));
                        item.setHorizontalTextPosition(JMenuItem.RIGHT);
                        item.addActionListener(menuListener);
                    }

                    popup.setLabel("Justification");
                    popup.setBorder(new BevelBorder(BevelBorder.RAISED));
                    popup.addPopupMenuListener(new PopupPrintListener());

                    addMouseListener(new MousePopupListener());

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
    //Added by Davis
    private static void addPopup(Component component, final JPopupMenu popup) {
        component.addMouseListener(new MouseAdapter() {
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
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }
}
