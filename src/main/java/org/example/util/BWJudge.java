package org.example.util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class BWJudge implements JudgeInterface
{
    public boolean changeMove(BoardUI ui, ChessState state, int x, int y){
        ArrayList<ArrayList<Integer>> board = state.getChessPos();
        int turn = state.getTurn();
        int size = board.size();
        boolean changed = false;
        boolean can = false;
        int i,j;

        for(i=x-1; i>=0; i--)
        {
            if(board.get(x-1).get(y) != -turn)
                break;
            if(board.get(i).get(y) == turn)
            {
                can = true;
                break;
            }
        }
        if(can)
        {
            for(int tt = x-1; tt>i; tt--)
                board.get(tt).set(y,turn);
            changed = true;
        }
        can = false;

        for(i=x+1; i<size; i++)
        {
            if(board.get(x+1).get(y) != -turn)
                break;
            if(board.get(i).get(y) == turn)
            {
                can = true;
                break;
            }
        }
        if(can)
        {
            for(int tt = x+1; tt<i; tt++)
                board.get(tt).set(y,turn);
            changed = true;
        }
        can = false;

        for(i=y+1; i<size; i++)
        {
            if(board.get(x).get(y+1) != -turn)
                break;
            if(board.get(x).get(i) == turn)
            {
                can = true;
                break;
            }
        }
        if(can)
        {
            for(int tt = y+1; tt<i; tt++)
                board.get(x).set(tt,turn);
            changed = true;
        }
        can = false;

        for(i=y-1; i>=0; i--)
        {
            if(board.get(x).get(y-1) != -turn)
                break;
            if(board.get(x).get(i) == turn)
            {
                can = true;
                break;
            }
        }
        if(can)
        {
            for(int tt = y-1; tt>i; tt--)
                board.get(x).set(tt,turn);
            changed = true;
        }
        can = false;

        for(i=x-1, j=y-1; i>=0 && j>=0; i--,j--)
        {
            if(board.get(x-1).get(y-1) != -turn)
                break;
            if(board.get(i).get(j) == turn)
            {
                can = true;
                break;
            }
        }
        if(can)
        {
            for(int ti=x-1, tj=y-1; ti>i && tj>j; ti--, tj--)
                board.get(ti).set(tj,turn);
            changed = true;
        }
        can = false;

        for(i=x+1, j=y+1; i<size && j<size; i++,j++)
        {
            if(board.get(x+1).get(y+1) != -turn)
                break;
            if(board.get(i).get(j) == turn)
            {
                can = true;
                break;
            }
        }
        if(can)
        {
            for(int ti=x+1, tj=y+1; ti<i && tj<j; ti++,tj++)
                board.get(ti).set(tj,turn);
            changed = true;
        }
        can = false;

        for(i=x+1, j=y-1; i<size && j>=0; i++,j--)
        {
            if(board.get(x+1).get(y-1) != -turn)
                break;
            if(board.get(i).get(j) == turn)
            {
                can = true;
                break;
            }
        }
        if(can)
        {
            for(int ti=x+1, tj=y-1; ti<i && tj>j; ti++,tj--)
                board.get(ti).set(tj,turn);
            changed = true;
        }
        can = false;

        for(i=x-1, j=y+1; i>=0 && j<size; i--,j++)
        {
            if(board.get(x-1).get(y+1) != -turn)
                break;
            if(board.get(i).get(j) == turn)
            {
                can = true;
                break;
            }
        }
        if(can)
        {
            for(int ti=x-1, tj=y+1; ti>i && tj<j; ti--,tj++)
                board.get(ti).set(tj,turn);
            changed = true;
        }
        can = false;

        return changed;
    }

    public boolean makeMove(BoardUI ui, ChessState state, int x, int y){
        int fix_x = (x-BoardPanel.x+BoardPanel.width/2) / BoardPanel.width * BoardPanel.width + BoardPanel.x;
		int fix_y = (y-BoardPanel.y+BoardPanel.height/2) / BoardPanel.height * BoardPanel.height + BoardPanel.y;

		int idx_x = (fix_x-BoardPanel.x) / BoardPanel.width;
		int idx_y = (fix_y-BoardPanel.y) / BoardPanel.height;

		ArrayList<ArrayList<Integer>> board = state.getChessPos();

		if((idx_x<0)||(idx_y<0)||(idx_x>=board.size())||(idx_y>=board.size()))
		{
			JOptionPane.showMessageDialog(null, "不能在棋盘外面落子", "落子非法", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if(board.get(idx_x).get(idx_y) != 0)
		{
			JOptionPane.showMessageDialog(null, "此处已经有棋子了，不能重复落子", "落子非法", JOptionPane.ERROR_MESSAGE);
			return false;
		}

        if(!changeMove(ui, state, idx_x, idx_y))
        {
			JOptionPane.showMessageDialog(null, "无法翻转对方棋子", "落子非法", JOptionPane.ERROR_MESSAGE);
			return false;
		}

        board.get(idx_x).set(idx_y, state.getTurn());

		Image chess;
		if(state.getTurn() == 1)
		{	
			ui.text_pane.addText(String.format("黑方在（%s，%d）落子\n", Character.toString((char)((int)'A'+idx_y)), idx_x+1));
//			chess = new ImageIcon("../pic/black.png").getImage();
            // 使用类加载器加载资源文件（从classpath中查找）
            ClassLoader classLoader = BWJudge.class.getClassLoader();
            ImageIcon icon = new ImageIcon(classLoader.getResource("black_chess.png"));
            chess = icon.getImage();
            int loadStatus = icon.getImageLoadStatus();

            if (loadStatus == MediaTracker.COMPLETE) {
                System.out.println("图标加载成功！");

                // 在这里可以使用加载的图像进行操作
            } else {
                System.err.println("图标加载失败！");
            }
		}
		else
		{
			ui.text_pane.addText(String.format("白方在（%s，%d）落子\n", Character.toString((char)((int)'A'+idx_y)), idx_x+1));
//			chess = new ImageIcon("../pic/white.png").getImage();
            // 使用类加载器加载资源文件（从classpath中查找）
            ClassLoader classLoader = BWJudge.class.getClassLoader();
            chess = new ImageIcon(classLoader.getResource("white_chess.png")).getImage();
		}
		Graphics graphics = ui.board_panel.getGraphics();
		graphics.drawImage(chess, fix_x-BoardPanel.width/2, fix_y-BoardPanel.height/2, BoardPanel.width, BoardPanel.height, null);
		
		state.x_history.push(idx_x);
		state.y_history.push(idx_y);

		return true;
    }

    public int checkFull(BoardUI ui, ChessState state){
		boolean isOver = true;
		for(int i=0; i<state.getChessPos().size(); i++)
		{
			for(int j=0; j<state.getChessPos().size(); j++)
				if(state.getChessPos().get(i).get(j)==0)
					isOver = false;
		}
		if(isOver)
		{
            int black = 0, white = 0;
            for(int i=0; i<state.getChessPos().size(); i++)
            {
                for(int j=0; j<state.getChessPos().size(); j++)
                {
                    if(state.getChessPos().get(i).get(j)==1)
                        black++;
                    if(state.getChessPos().get(i).get(j)==-1)
                        white++;
                }
            }
            if(white>black)
                JOptionPane.showMessageDialog(null, "白方胜利", "游戏结束", JOptionPane.PLAIN_MESSAGE);
            else if(white<black)
                JOptionPane.showMessageDialog(null, "黑方胜利", "游戏结束", JOptionPane.PLAIN_MESSAGE);
            else
                JOptionPane.showMessageDialog(null, "平局", "游戏结束", JOptionPane.PLAIN_MESSAGE);
            
            state.restart();
            state.getChessPos().get(3).set(3,1);
            state.getChessPos().get(4).set(4,1);
            state.getChessPos().get(3).set(4,-1);
            state.getChessPos().get(4).set(3,-1);
            ui.board_panel.repaint();

            if(white>black) return -1;
            else if(black>white) return 1;
            else return 0;
		}
        return -2;
	}

    public void play(BoardUI ui, ChessState state){
        Stack<Integer> x_history = state.x_history;
        Stack<Integer> y_history = state.y_history;

        int size = x_history.size();
        int[] x = new int[size];
        int[] y = new int[size];

        while(!x_history.empty())
        {
            x[x_history.size()-1] = x_history.pop() * BoardPanel.width + BoardPanel.x - BoardPanel.width/2;
            y[y_history.size()-1] = y_history.pop() * BoardPanel.height + BoardPanel.y - BoardPanel.height/2;
        }

        state.restart();
        state.getChessPos().get(3).set(3,1);
        state.getChessPos().get(4).set(4,1);
        state.getChessPos().get(3).set(4,-1);
        state.getChessPos().get(4).set(3,-1);
        ui.board_panel.repaint();

        final long timeInterval = 1000;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<size; i++)
                {
                    try {
                        Thread.sleep(timeInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    makeMove(ui, state, x[i], y[i]);
                    ui.board_panel.setState(state);
                    ui.board_panel.repaint();
                    state.change();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}