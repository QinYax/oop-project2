package org.example.util;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class BaseJudge implements JudgeInterface
{
    public void saveGame(ChessStateCareTaker state_taker, ChessState state){
        String file_name = JOptionPane.showInputDialog("请输入存档名字：");
        state_taker.saveMemento(state.createMemento(), file_name);
    }

    public ChessState loadGame(ChessStateCareTaker state_taker, ChessState state){
        String file_name = JOptionPane.showInputDialog("请输入要读取的存档名字：");
        state_taker.loadMemento(file_name);
        state.restoreMemento(state_taker.retrieveMemento());
        
        return state;
    }

    public ChessState selectGame(BoardUI ui){
        String[] game_types = {"围棋", "五子棋", "黑白棋"};
		int type = JOptionPane.showOptionDialog(null, "您是想玩什么棋呢？", "请选择您的游戏", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, game_types, game_types[0]);

        int idx = 0;

        if(game_types[type] != "黑白棋")
        {
            Object[] size_options = new Object[12];
            for(int i=8; i<=19; i++)
                size_options[i-8] = String.valueOf(i)+"*"+String.valueOf(i);
            Object board_size = JOptionPane.showInputDialog(null, "棋盘大小可以从8*8到19*19之间选择", "请选择棋盘大小", JOptionPane.PLAIN_MESSAGE, null, size_options, size_options[0]);

            
            for(; idx<size_options.length; idx++)
                if(size_options[idx].equals(board_size))
                    break;
            ui.createBoard(idx+8, false);
        }
        
        ChessState state = new ChessState(idx+8, game_types[type]);
        if(game_types[type] == "黑白棋")
        {
            ui.createBoard(idx+8, true);

			state.getChessPos().get(3).set(3,1);
            state.getChessPos().get(4).set(4,1);
            state.getChessPos().get(3).set(4,-1);
            state.getChessPos().get(4).set(3,-1);

        }
        return state;
    }

    public void giveUp(BoardUI ui, ChessState state){
        String name = "";
        if(state.getTurn()==-1) name = "黑方";
        if(state.getTurn()==1) name = "白方";

        JOptionPane.showMessageDialog(null, name + "获胜", "游戏结束", JOptionPane.PLAIN_MESSAGE);
        state.restart();
        if(state.getType().equals("黑白棋"))
        {
            state.getChessPos().get(3).set(3,1);
            state.getChessPos().get(4).set(4,1);
            state.getChessPos().get(3).set(4,-1);
            state.getChessPos().get(4).set(3,-1);
        }
        ui.board_panel.repaint();
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

    public void pass(ChessState state){
		state.change();
	}

	public ChessState regret(ChessState state){
		ArrayList<ArrayList<Integer>> chess_pos = state.getChessPos();

		for(int i=0; i<2; i++)
		{
			int x = state.x_history.pop();
			int y = state.y_history.pop();
			chess_pos.get(x).set(y, 0);
		}
		state.setChessPos(chess_pos);
		return state;
	}

    public void login(BoardUI ui)
    {
        String[] login_types = {"用户登录", "AI登录"};
		int ptype = JOptionPane.showOptionDialog(null, "请选择您的登录类型", "请登录", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, login_types, login_types[0]);

        String[] user_types = {"黑方", "白方"};
		int utype = JOptionPane.showOptionDialog(null, "请选择您的登录阵营", "请登录", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, user_types, user_types[0]);
        
        if(login_types[ptype].equals("用户登录"))
        {
            String username = JOptionPane.showInputDialog(null, "请输入账号，如账号不存在将自动注册");
            String password = JOptionPane.showInputDialog(null, "请输入密码");
            int[] his = checkUserInfo(username, password);
            if(his!=null)
                ui.info_panel.setUser(user_types[utype], username, his);
        }
        if(login_types[ptype].equals("AI登录"))
        {
            String[] ai_types = {"简单", "普通", "困难"};
		    int aitype = JOptionPane.showOptionDialog(null, "请选择AI难度", "请选择", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, ai_types, ai_types[0]);
            if(ai_types[aitype].equals("简单"))
            {
                int[] his = checkUserInfo("简单AI", "level1");
                if(his!=null)
                    ui.info_panel.setUser(user_types[utype], "简单AI", his);
            }
            else if(ai_types[aitype].equals("普通"))
            {
                int[] his = checkUserInfo("普通AI", "level3");
                if(his!=null)
                    ui.info_panel.setUser(user_types[utype], "普通AI", his);
            }
            else if(ai_types[aitype].equals("困难"))
            {
                int[] his = checkUserInfo("困难AI", "level5");
                if(his!=null)
                    ui.info_panel.setUser(user_types[utype], "困难AI", his);
            }
        }
    }

     public int[] checkUserInfo(String u, String p) {
        String fileName = "target/data/user.txt";
        File file = new File(fileName);
        boolean checked = false;
        boolean reg = true;
        BufferedReader reader = null;
        int[] his = {0,0,0};
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;

            while ((line = reader.readLine()) != null) {
                // 显示行号
                int idx = line.indexOf("\t");
                String username = line.substring(0, idx);
                String password = line.substring(idx+1, line.length());
                int pidx = password.indexOf("\t");
                String hist = password.substring(pidx+1, password.length());
                password = password.substring(0, pidx);
                
                if(username.equals(u))
                {
                    reg = false;
                    if(password.equals(p)) 
                    {
                        checked = true;
                        int hidx = hist.indexOf("\t");
                        his[0] = Integer.parseInt(hist.substring(0,hidx));
                        hist = hist.substring(hidx+1, hist.length());
                        hidx = hist.indexOf("\t");
                        his[1] = Integer.parseInt(hist.substring(0, hidx));
                        his[2] = Integer.parseInt(hist.substring(hidx+1, hist.length()));
                    }
                    if(!password.equals(p));
                }
                
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        if(checked)
            JOptionPane.showMessageDialog(null, "登录成功", "登录成功", JOptionPane.PLAIN_MESSAGE);
        else if(reg)
        {
            JOptionPane.showMessageDialog(null, "账号不存在，注册成功", "登录成功", JOptionPane.PLAIN_MESSAGE);

            String content = u + "\t" + p + "\t0\t0\t0\n";
            BufferedWriter out = null;

            try {
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true)));
                out.write(content);
            }
            catch (Exception e) {e.printStackTrace();} 
            finally {
                try {
                    out.close();
                }
                catch (IOException e) {e.printStackTrace();}
            }
        }
        else
            JOptionPane.showMessageDialog(null, "密码错误", "登录失败", JOptionPane.ERROR_MESSAGE);
        if(checked || reg)
            return his;
        return null;
    }
    
    public void setHis(BoardUI ui, int result)
    {
        String black_user = ui.info_panel.user[2].getText();
        String white_user = ui.info_panel.user[3].getText();

        File file = new File("target/data/user.txt");

        String[] info = new String[5];
        try {
        FileInputStream intput = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(intput));
        String tempString;
        ArrayList<String> list = new ArrayList<String>();
        
        while ((tempString = reader.readLine()) != null) {
            list.add(tempString);
        }
        
        FileWriter fd = new FileWriter(file, false);
        
        fd.write("");
        fd.close();

        FileWriter fw = new FileWriter(file, true);
        
        for (String infoitem : list) {
            info = infoitem.split("\t");
            
            if (black_user.equals(info[0])) {
                if(result == -1) info[4] = String.valueOf(Integer.parseInt(info[4])+1);
                else if(result == 1) info[2] = String.valueOf(Integer.parseInt(info[2])+1);
                else info[3] = String.valueOf(Integer.parseInt(info[3])+1);
                ui.info_panel.user[8].setText(info[2]+"/"+info[3]+"/"+info[4]);
            }
            if (white_user.equals(info[0])) {
                if(result == 1) info[4] = String.valueOf(Integer.parseInt(info[4])+1);
                else if(result == -1) info[2] = String.valueOf(Integer.parseInt(info[2])+1);
                else info[3] = String.valueOf(Integer.parseInt(info[3])+1);
                ui.info_panel.user[9].setText(info[2]+"/"+info[3]+"/"+info[4]);
            }
            fw.write(info[0]+"\t"+info[1]+"\t"+info[2]+"\t"+info[3]+"\t"+info[4]+"\n");
        }
        
            fw.close();
        }
        catch (IOException e) {e.printStackTrace();}
    }
}