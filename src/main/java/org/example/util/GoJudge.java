package org.example.util;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class GoJudge implements JudgeInterface
{
	public int checkGoOver(BoardUI ui, ChessState state, boolean tromp){
        ArrayList<ArrayList<Integer>> board = state.getChessPos();
        int score_white = 0;
        int score_black = 0;
        int winner = 0;

        for(int i=0; i<board.size(); i++)
            for(int j=0; j<board.size(); j++)
            {
                if(board.get(i).get(j) == 1) score_black++;
                else if(board.get(i).get(j) == -1) score_white++;
            }

        if(tromp)
        {
            int[] reach = getScore(board);
            score_white += reach[0];
            score_black += reach[1];
        }
        else
        {
            for(int i=0; i<board.size(); i++)
                for(int j=0; j<board.size(); j++)
                {
                    int cnt_black = 0;
                    int cnt_white = 0;
                    int cnt_ = 0;
                    if(board.get(i).get(j) == 0)
                    {
                        int[][] xy = {{i-1,j}, {i+1,j}, {i,j-1}, {i,j+1}};
                        for(int k=0; k<xy.length; k++)
                        {
                            int x = xy[k][0];
                            int y = xy[k][1];
                            if(x<0 || x>=board.size() || y<0 || y>=board.size()) continue;
                            if(board.get(x).get(y) == 1) cnt_black++;
                            if(board.get(x).get(y) == -1) cnt_white++;
                            cnt_++;
                        }
                    }
                    if(cnt_black == cnt_) score_black++;
                    else if(cnt_white == cnt_) score_white++;
                }
        }

        //System.out.printf("%d %d\n", score_black, score_white);
        if(score_black > score_white)
            JOptionPane.showMessageDialog(null, "黑方胜利", "游戏结束", JOptionPane.PLAIN_MESSAGE);
        else if(score_black < score_white)
            JOptionPane.showMessageDialog(null, "白方胜利", "游戏结束", JOptionPane.PLAIN_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, "平局", "游戏结束", JOptionPane.PLAIN_MESSAGE);
        
        
        state.restart();
        ui.board_panel.repaint();
        if(score_black > score_white)
            return 1;
        else if(score_black < score_white)
            return -1;
        else return 0;
    }

    public int[] getScore(ArrayList<ArrayList<Integer>> board){
        HashMap< Pair<Integer,Integer>, Pair<Boolean,Boolean> > reachability = new HashMap< Pair<Integer,Integer>, Pair<Boolean,Boolean> >();
        
        HashMap< Pair<Integer,Integer>, ArrayList<Pair<Integer,Integer>> > empty_neighbors = new HashMap< Pair<Integer,Integer>, ArrayList<Pair<Integer,Integer>> >();
        for(int i=0; i<board.size(); i++)
            for(int j=0; j<board.size(); j++)
            {
                if(board.get(i).get(j) == 0)
                {
                    reachability.put(new Pair<Integer,Integer>(i,j), new Pair<Boolean,Boolean>(false,false));
                    empty_neighbors.put(new Pair<Integer,Integer>(i,j), new ArrayList<Pair<Integer,Integer>>());
                }
            }
        for(int i=0; i<board.size(); i++)
            for(int j=0; j<board.size(); j++)
            {
                if(board.get(i).get(j) == 0)
                {
                    Pair<Integer,Integer> empty = new Pair<Integer,Integer>(i,j);
                    int[][] xy = {{i-1,j}, {i+1,j}, {i,j-1}, {i,j+1}};
                    for(int k=0; k<xy.length; k++)
                    {
                        Pair<Integer,Integer> pos = new Pair<Integer,Integer>(xy[k][0],xy[k][1]);
                        if(pos.getKey()<0 || pos.getKey()>=board.size() || pos.getValue()<0 || pos.getValue()>=board.size()) continue;
                        if(board.get(pos.getKey()).get(pos.getValue()) == 1)
                            reachability.put(empty, new Pair<Boolean,Boolean>(reachability.get(empty).getKey() || true, reachability.get(empty).getValue()));
                        else if(board.get(pos.getKey()).get(pos.getValue()) == -1)
                            reachability.put(empty, new Pair<Boolean,Boolean>(reachability.get(empty).getKey(), reachability.get(empty).getValue() || true));
                        else
                        {   
                            ArrayList<Pair<Integer,Integer>> array = empty_neighbors.get(empty);
                            array.add(pos);
                            empty_neighbors.put(empty, array);
                        }
                    }
                }
            }
        while(true)
        {
            boolean changed = false;
            for(int i=0; i<board.size(); i++)
                for(int j=0; j<board.size(); j++)
                {
                    if(board.get(i).get(j) == 0)
                    {
                        Pair<Integer,Integer> empty = new Pair<Integer,Integer>(i,j);
                        ArrayList<Pair<Integer,Integer>> array = empty_neighbors.get(empty);
                        for(int k=0; k<array.size(); k++)
                        {
                            Pair<Integer,Integer> neighber = array.get(k);
                            boolean black_reach = reachability.get(neighber).getKey();
                            boolean white_reach = reachability.get(neighber).getValue();
                            Pair<Boolean,Boolean> tmp_empty_reach = reachability.get(empty);
                            if((tmp_empty_reach.getKey() || black_reach) != tmp_empty_reach.getKey() || (tmp_empty_reach.getValue() || white_reach) != tmp_empty_reach.getValue())
                            {
                                reachability.put(empty, new Pair<Boolean,Boolean>(tmp_empty_reach.getKey() || black_reach, tmp_empty_reach.getValue() || white_reach));
                                changed = true;
                            }
                        }
                    }
                }
            if(!changed) break;
        }

        int black_score = 0;
        int white_score = 0;

        for(Pair<Boolean,Boolean> reach : reachability.values())
        {
            if(reach.getKey()==true && reach.getValue()==false) black_score++;
            if(reach.getKey()==false && reach.getValue()==true) white_score++;
        }

        return new int[]{black_score, white_score};
        
    }

    public boolean takeMove(BoardUI ui, ChessState state){
		ArrayList<ArrayList<Integer>> board = state.getChessPos();
		int size = board.size();
		Stack<Integer> x_take = new Stack<Integer>();
		Stack<Integer> y_take = new Stack<Integer>();

		for(int i=0; i<size; i++)
			for(int j=0; j<size; j++)
			{
				if(board.get(i).get(j) == 0)
					continue;
				else if(board.get(i).get(j) == -1 * state.getTurn() && isAlive(board, i, j) == false)
				{
					x_take.push(i);
					y_take.push(j);
				}
			}
		String player = "";	//取对方子
		if(state.getTurn() == -1)
			player = "黑方";
		else
			player = "白方";

		boolean flag = false;
		while(!x_take.empty())
		{
			flag = true;
			int x = x_take.pop();
			int y = y_take.pop();
			state.getChessPos().get(x).set(y,0);

			ui.text_pane.addText(String.format("取（%s，%d）" + player + "棋子\n", Character.toString((char)((int)'A'+y)), x+1));
		}

		return flag;
	}

	private boolean DFS(ArrayList<ArrayList<Integer>> board, boolean[][] visit, int x, int y){
		visit[x][y] = true;
		int[][] xy = {{x-1,y}, {x+1,y}, {x,y-1}, {x,y+1}};
		boolean flag = false;
		for(int i=0; i<xy.length; i++)
		{
			int dx = xy[i][0];
			int dy = xy[i][1];
			if(dx<0 || dx>=visit.length || dy<0 || dy>=visit.length)
				continue;
			else if(visit[dx][dy] == false)
			{
				if(board.get(dx).get(dy) == 0)
					flag = true;
				else if(board.get(dx).get(dy) == -1 * board.get(x).get(y))
					continue;
				else if(board.get(dx).get(dy) == board.get(x).get(y))
					flag = flag || DFS(board, visit, dx, dy);
			}
		}
		return flag;
	}

    private boolean isAlive(ArrayList<ArrayList<Integer>> board, int x, int y){
		int size = board.size();
		boolean[][] visit = new boolean[size][size];
		for(int i=0; i<size; i++)
			for(int j=0; j<size; j++)
				visit[i][j] = false;
		return DFS(board, visit, x, y);
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

		board.get(idx_x).set(idx_y, state.getTurn());

        if(!takeMove(ui, state))
        if(isAlive(board, idx_x, idx_y) == false)
        {
            board.get(idx_x).set(idx_y, 0);
            JOptionPane.showMessageDialog(null, "这个地方没有气", "落子非法", JOptionPane.ERROR_MESSAGE);
            return false;
        }

		Image chess;
		if(state.getTurn() == 1)
		{	
			ui.text_pane.addText(String.format("黑方在（%s，%d）落子\n", Character.toString((char)((int)'A'+idx_y)), idx_x+1));
//			chess = new ImageIcon("../pic/black.png").getImage();
            ClassLoader classLoader = GoJudge.class.getClassLoader();
            chess = new ImageIcon(classLoader.getResource("black_chess.png")).getImage();
		}
		else
		{
			ui.text_pane.addText(String.format("白方在（%s，%d）落子\n", Character.toString((char)((int)'A'+idx_y)), idx_x+1));
//			chess = new ImageIcon("../pic/white.png").getImage();
            ClassLoader classLoader = GoJudge.class.getClassLoader();
            chess = new ImageIcon(classLoader.getResource("white_chess.png")).getImage();
        }
		Graphics graphics = ui.board_panel.getGraphics();
		graphics.drawImage(chess, fix_x-BoardPanel.width/2, fix_y-BoardPanel.height/2, BoardPanel.width, BoardPanel.height, null);
		
		state.x_history.push(idx_x);
		state.y_history.push(idx_y);

		return true;
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