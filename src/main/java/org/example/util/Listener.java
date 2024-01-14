package org.example.util;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Listener implements MouseListener, ActionListener
{
    private BoardUI ui;
    private ChessState state;
    private ChessStateCareTaker state_taker;
	private BaseJudge base_judge;
	private JudgeInterface judge;
	private int cnt = 0;
	private boolean[] isAI = new boolean[2];
	private Graphics graphics;

	public void setGraphics(Graphics g) {
		this.graphics=g;
	}

    public Listener(BoardUI ui){
		this.ui = ui;
        this.state_taker = new ChessStateCareTaker();
		this.base_judge = new BaseJudge();
	}

    public void mousePressed(MouseEvent e){
		if(this.state.getType().equals("围棋"))
		{
			boolean result = ((GoJudge)judge).makeMove(this.ui, this.state, e.getX(), e.getY());
			if(!result) return;	
			ui.board_panel.setState(state);
        	ui.board_panel.repaint();
			cnt = 0;
			this.state.change();
		}
		else if(this.state.getType().equals("五子棋"))
		{
			boolean res = ((FiveJudge)judge).makeMove(this.ui, this.state, e.getX(), e.getY());
			if(!res) return;
			int result1 = ((FiveJudge)judge).checkFive(this.ui, this.state);
			int result2 = ((FiveJudge)judge).checkFull(this.ui, this.state);
			if(result1==-2 && result2==-2);
			else if(result1==-2)
			{
				base_judge.setHis(ui, result2);
				return;
			}
			else
			{
				base_judge.setHis(ui, result1);
				return;
			}
			this.state.change();
			if(isAI[0] && state.getTurn() == 1)
				startGame(1);
			else if(isAI[1] && state.getTurn() == -1)
				startGame(-1);
		}
		else if(this.state.getType().equals("黑白棋"))
		{			
			boolean res = ((BWJudge)judge).makeMove(this.ui, this.state, e.getX(), e.getY());
			if(!res) return;

			ui.board_panel.setState(state);
            ui.board_panel.repaint();
			int result = ((BWJudge)judge).checkFull(this.ui, this.state);
			if(result != -2)
			{
				base_judge.setHis(ui, result);
				return;
			}
			this.state.change();
		}
		
	}

	public void changeJudge()
	{
		if(this.state.getType().equals("五子棋"))
			this.judge = new FiveJudge();
		else if(this.state.getType().equals("围棋"))
			this.judge = new GoJudge();
		else if(this.state.getType().equals("黑白棋"))
			this.judge = new BWJudge();
	}

	public void startGame(int turn)
	{
		int pos;
		if(turn == 1) pos = 2;
		else pos = 3;
                if(ui.info_panel.user[pos].getText().contains("普通"))
					state = ((FiveJudge)judge).aiStep(turn, ui, state);
				else if(ui.info_panel.user[pos].getText().contains("简单"))
					state = ((FiveJudge)judge).autoStep(turn, ui, state);
				else if(ui.info_panel.user[pos].getText().contains("困难"))
					state = ((FiveJudge)judge).advanceStep(turn, state);
				ui.board_panel.setState(state);
				ui.board_panel.repaint();	

				int result1 = ((FiveJudge)judge).checkFive(ui, state);
				int result2 = ((FiveJudge)judge).checkFull(ui, state);
				if(result1==-2 && result2==-2);
				else if(result1==-2)
				{
					base_judge.setHis(ui, result2);
					return;
				}
				else
				{
					base_judge.setHis(ui, result1);
					return;
				}
		state.change();
	}

	public void autoGame()
	{
		final long timeInterval = 200;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while(true){
					try{
						Thread.sleep(timeInterval);
						if(ui.info_panel.user[2].getText().contains("普通"))
							state = ((FiveJudge)judge).aiStep(1, ui, state);
						else if(ui.info_panel.user[2].getText().contains("简单"))
							state = ((FiveJudge)judge).autoStep(1, ui, state);
						else if(ui.info_panel.user[2].getText().contains("困难"))
							state = ((FiveJudge)judge).advanceStep(1, state);
						ui.board_panel.setState(state);
						ui.board_panel.repaint();
						Thread.sleep(timeInterval);
						//debug.print(state.getChessPos());

						int result1 = ((FiveJudge)judge).checkFive(ui, state);
						int result2 = ((FiveJudge)judge).checkFull(ui, state);
						if(result1==-2 && result2==-2);
						else if(result1==-2)
						{
							base_judge.setHis(ui, result2);
							return;
						}
						else
						{
							base_judge.setHis(ui, result1);
							return;
						}

						state.change();
						Thread.sleep(timeInterval);
						
						if(ui.info_panel.user[3].getText().contains("普通"))
							state = ((FiveJudge)judge).aiStep(-1, ui, state);
						else if(ui.info_panel.user[3].getText().contains("简单"))
							state = ((FiveJudge)judge).autoStep(-1, ui, state);
						else if(ui.info_panel.user[3].getText().contains("困难"))
							state = ((FiveJudge)judge).advanceStep(-1, state);
						ui.board_panel.setState(state);
						ui.board_panel.repaint();
						Thread.sleep(timeInterval);

						//debug.print(state.getChessPos());
						result1 = ((FiveJudge)judge).checkFive(ui, state);
						result2 = ((FiveJudge)judge).checkFull(ui, state);
						if(result1==-2 && result2==-2);
						else if(result1==-2)
						{
							base_judge.setHis(ui, result2);
							return;
						}
						else
						{
							base_judge.setHis(ui, result1);
							return;
						}
						}catch(InterruptedException e){}
						state.change();
					}
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
	}

	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand())
		{
			case "开始新游戏": 
				this.state = base_judge.selectGame(this.ui);
				changeJudge();
				isAI = ui.info_panel.isAI();
				if(isAI[0] && isAI[1]) autoGame();
				else if(isAI[0]) startGame(1);
				break;
			case "加载存档":
				this.state = base_judge.loadGame(this.state_taker, this.state);
				ui.board_panel.setState(state);
                ui.board_panel.repaint();
				changeJudge();
				break;
			case "保存当前游戏": base_judge.saveGame(this.state_taker, this.state); break;
			case "过子": 
				base_judge.pass(this.state);
				cnt++;
				int result = -2;
				if(cnt>1)
					result = ((GoJudge)judge).checkGoOver(ui, state, true);
				if(result!=-2)
					base_judge.setHis(ui, result);
				break;
			case "悔棋":
				this.state = base_judge.regret(this.state);
				ui.board_panel.setState(state);
                ui.board_panel.repaint();
				break;
			case "认输":
				base_judge.setHis(ui, -state.getTurn());
				base_judge.giveUp(this.ui, this.state);
				state.turn = 1;
				break;
			case "录像回放":
				if(this.state.getType().equals("五子棋"))
					((FiveJudge)judge).play(this.ui, this.state);
				else if(this.state.getType().equals("围棋"))
					((GoJudge)judge).play(this.ui, this.state);
				else if(this.state.getType().equals("黑白棋"))
					((BWJudge)judge).play(this.ui, this.state);
				break;
			case "登陆账号":
				base_judge.login(this.ui);
				break;
				
		}
	}
    
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}