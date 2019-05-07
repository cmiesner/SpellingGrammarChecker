package com.logicbig.example;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TextComponentWordAtCaretUtil {

    public static void main(String[] args) {
        JTextPane ta = new JTextPane();
        ta.addMouseListener(createPopupListener(ta));
        JFrame frame = createFrame("JTextComponent word at caret example");
        frame.add(new JScrollPane(ta));
        frame.setVisible(true);
    }

    private static MouseListener createPopupListener(JTextComponent tc) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menu = popupMenu.add(new JMenuItem("Test"));
        popupMenu.add(menu);
        menu.addActionListener(e -> {
            String word = getWordAtCaret(tc);
            System.out.println(word);

        });
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int rightClickPosition = tc.viewToModel2D(e.getPoint());
                    tc.setCaretPosition(rightClickPosition);
                    popupMenu.show(tc, e.getX(), e.getY());
                }
            }
        };
    }

    private static String getWordAtCaret(JTextComponent tc) {
        try {
            int caretPosition = tc.getCaretPosition();
            int start = Utilities.getWordStart(tc, caretPosition);
            int end = Utilities.getWordEnd(tc, caretPosition);
            return tc.getText(start, end - start);
        } catch (BadLocationException e) {
            System.err.println(e);
        }

        return null;
    }

    public static JFrame createFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(800, 700));
        return frame;
    }
}