package org.example.util;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.MouseListener;
import java.io.IOException;


public class BoardUI extends JFrame{
	
	public TextScrollPane text_pane;
	public Graphics graphics;
	public Listener listener;
	public InfoPanel info_panel;
	public BoardPanel board_panel;

	public boolean flag = false;

	public void constructUI(){
		this.setSize(1440, 900);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(3);
		this.setResizable(false);

        this.setTitle("棋类对战平台");
		this.setLayout(new BorderLayout());

		this.setBackground(Color.WHITE);

		//this.frame_listener = new FrameListener(this);
		this.listener = new Listener(this);

		this.info_panel = new InfoPanel();
		this.info_panel.constructPanel();

		int cnt = info_panel.getComponentCount();
		for(int i = 0; i < cnt; i++)
		{
			Component comp = info_panel.getComponent(i);
			if(comp instanceof JButton)
			{
				JButton btn = (JButton) comp;
				btn.addActionListener((ActionListener)listener);
			}
		}
		this.add(info_panel, BorderLayout.EAST);



		this.text_pane = new  TextScrollPane();
		this.text_pane.constructPane();
		this.add(text_pane, BorderLayout.SOUTH);

		this.setVisible(true);
	}

	public void createBoard(int size, boolean init){

		this.board_panel = new BoardPanel();
		board_panel.setInit(init);
		this.board_panel.setSize(size);
		this.add(board_panel, BorderLayout.CENTER);

		this.board_panel.addMouseListener((MouseListener)this.listener);
		this.listener.setGraphics(this.board_panel.getGraphics());

		this.setVisible(true);
	}
}
