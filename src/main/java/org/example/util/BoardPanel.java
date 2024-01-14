package org.example.util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoardPanel extends JPanel
{
    public static int size;
    public static int x = 170;
    public static int y = 20;
    public static int width = 45;
    public static int height = 40;
    private boolean init = false;
    private ChessState state = null;
    private Image backgroundImage; // 背景图片

    public BoardPanel(){
        // 在构造函数中加载背景图片
        ClassLoader classLoader = BoardPanel.class.getClassLoader();
        backgroundImage = new ImageIcon(classLoader.getResource("bg.jpg")).getImage();
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public void setState(ChessState state)
    {
        this.state = state;
    }

    public void setInit(boolean init)
    {
        this.init = init;
    }


    @Override
    public void paint(Graphics graphics)
    {
        super.paint(graphics);

        // 绘制背景图片
        if (backgroundImage != null) {
            graphics.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        Font font = new Font("Times New Roman", Font.BOLD, 16);
        graphics.setColor(Color.white);
        graphics.setFont(font);
		for(int i=0; i<this.size; i++) {
            graphics.drawString(Character.toString((char)((int)'A'+i)), this.x-30, this.y+this.height*i+5);
			graphics.drawLine(this.x, this.y+this.height*i, this.x+this.width*(this.size-1), this.y+this.height*i);
		}
		for(int j=0; j<this.size; j++) {
            graphics.drawString(String.valueOf(j+1), this.x+this.width*j-5, this.y+this.height*(this.size-1)+30);
			graphics.drawLine(this.x+this.width*j, this.y, this.x+this.width*j, this.y+this.height*(this.size-1));
		}

        if(this.init)
        {
            ClassLoader classLoader = BoardPanel.class.getClassLoader();
            Image wchess = new ImageIcon(classLoader.getResource("white_chess.png")).getImage();
            Image bchess = new ImageIcon(classLoader.getResource("black_chess.png")).getImage();
//            Image bchess = new ImageIcon("../pic/black.png").getImage();
//            Image wchess = new ImageIcon("../pic/white.png").getImage();
            int x1 = 3 * BoardPanel.width + BoardPanel.x;
			int x2 = 4 * BoardPanel.width + BoardPanel.x;
			int y1 = 3 * BoardPanel.height + BoardPanel.y;
			int y2 = 4 * BoardPanel.height + BoardPanel.y;
            graphics.drawImage(bchess, x1-BoardPanel.width/2, y1-BoardPanel.height/2, BoardPanel.width, BoardPanel.height, null);
            graphics.drawImage(bchess, x2-BoardPanel.width/2, y2-BoardPanel.height/2, BoardPanel.width, BoardPanel.height, null);
            graphics.drawImage(wchess, x1-BoardPanel.width/2, y2-BoardPanel.height/2, BoardPanel.width, BoardPanel.height, null);
            graphics.drawImage(wchess, x2-BoardPanel.width/2, y1-BoardPanel.height/2, BoardPanel.width, BoardPanel.height, null);
        }

        if(state != null)
        {
            ArrayList<ArrayList<Integer>> chess_pos = state.getChessPos();
		//ui.board_panel.paint(graphics);
        
		Image chess;

        ClassLoader classLoader = BoardPanel.class.getClassLoader();
		for(int i=0; i<chess_pos.size(); i++)
		{
			for(int j=0; j<chess_pos.size(); j++)
			{

				if(chess_pos.get(i).get(j) == 1)
                    chess = new ImageIcon(classLoader.getResource("black_chess.png")).getImage();
				else if(chess_pos.get(i).get(j) == -1)
					chess = new ImageIcon(classLoader.getResource("white_chess.png")).getImage();
				else continue;
				int fix_x = i * BoardPanel.width + BoardPanel.x;
				int fix_y = j * BoardPanel.height + BoardPanel.y;
				
				graphics.drawImage(chess, fix_x-BoardPanel.width/2, fix_y-BoardPanel.height/2, BoardPanel.width, BoardPanel.height, this);
                //ui.board_panel.paintComponent(graphics);
			}
		}
        }
    }
}