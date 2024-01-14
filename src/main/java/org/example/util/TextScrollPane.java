package org.example.util;

import javax.swing.*;
import java.awt.*;

public class TextScrollPane extends JScrollPane
{
    public JTextArea textArea = new JTextArea(5, 5);

    public void constructPane()
    {
		textArea.setTabSize(4);
		textArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
		textArea.setLineWrap(true);// 激活自动换行功能
		textArea.setWrapStyleWord(true);
        textArea.setText("游戏开始！\n");

        this.setViewportView(textArea);
    }

    public void addText(String str)
    {
        String currentText = textArea.getText();
        textArea.setText(currentText+str);
    }
}