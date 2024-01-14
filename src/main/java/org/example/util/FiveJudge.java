package org.example.util;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class FiveJudge implements JudgeInterface
{

    private HashSet<Point> toJudge=new HashSet<Point>();
    private int dr[]=new int[]{-1,1,-1,1,0,0,-1,1};
    private int dc[]=new int[]{1,-1,-1,1,-1,1,0,0};
    public final int MAXN=1<<28;  
    public final int MINN=-MAXN;    
    private int searchDeep = 4;
    public int[][] chessBoard;
    public int size;

    public boolean isFive(ChessState state) {
        int x = state.x_history.peek();
        int y = state.y_history.peek();
        int size = state.getChessPos().size();
        int color = state.getTurn();
        ArrayList<ArrayList<Integer>> board = state.getChessPos();

        int count = 1;      //本身一点为 1
        int posX = 0;
        int posY = 0;
        /**判断水平方向上的胜负
        /* 将水平方向以传入的点x上的y轴作为分隔线分为两部分
         * 先向左边遍历，判断到的相同的连续的点  count++
         */
        for(posX = x - 1; posX >= 0 ; posX--) {
            if (board.get(posX).get(y) == color) {
                count++;
                if (count >= 5) {
                    return true;
                }
            }else {
                break;
            }
        }    //向右边遍历
        for(posX = x + 1; posX < size; posX++) {
            if (board.get(posX).get(y) == color) {
                count++;
                if (count >= 5) {
                    return true;
                }
            }else {
                break;
            }
        }
        count = 1;
        /**判断垂直方向上的胜负
        /* 将垂直方向以传入的点y上的x轴作为分隔线分为两部分
         * 先向上遍历，判断到的相同的连续的点  count++
         */
        for(posY = y - 1; posY >= 0; posY--) {
            if (board.get(x).get(posY) == color) {
                count++;
                if (count >= 5) {
                    return true;
                }
            }else {
                break;
            }
        }//向下遍历
        for(posY = y + 1; posY < size; posY++) {
            if (board.get(x).get(posY) == color) {
                count++;
                if (count >= 5) {
                    return true;
                }
            }else {
                break;
            }
        }
        count = 1;
        /**判断左上右下方向上的胜负
         * 以坐标点为分割线，将棋盘分为左右两个等腰三角形
         * 先判断左边的
         */
        for(posX = x - 1, posY = y - 1; posX >= 0 && posY >= 0; posX--, posY--) {
            if (board.get(posX).get(posY) == color) {
                count++;
                if (count >= 5) {
                    return true;
                }
            }else {
                break;
            }
        }//判断右边的
        for(posX = x + 1, posY = y + 1; posX < size && posY < size; posX++, posY++) {
            if (board.get(posX).get(posY) == color) {
                count++;
                if (count >= 5) {
                    return true;
                }
            }else {
                break;
            }
        }
        count = 1;
        /**判断右下左下方向上的胜负
         * 以坐标点为分割线，将棋盘分为左右两个等腰三角形
         * 先判断左边的
         */
        for(posX = x + 1, posY = y - 1; posX < size && posY >= 0; posX++, posY--) {
            if (board.get(posX).get(posY) == color) {
                count++;
                if (count >= 5) {
                    return true;
                }
            }else {
                break;
            }
        }//判断右边的
        for(posX = x - 1, posY = y + 1; posX >= 0 && posY < size; posX--, posY++) {
            if (board.get(posX).get(posY) == color) {
                count++;
                if (count >= 5) {
                    return true;
                }
            }else {
                break;
            }
        }
        return false;
    }

    public int checkFive(BoardUI ui, ChessState state){
        String name = "";
        if(state.getTurn()==1) name = "黑方";
        if(state.getTurn()==-1) name = "白方";
        if(isFive(state))
        {
            JOptionPane.showMessageDialog(null, name + "胜利", "游戏结束", JOptionPane.PLAIN_MESSAGE);
            state.restart();
            ui.board_panel.repaint();
            return state.getTurn();
        }
        return -2;
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
            JOptionPane.showMessageDialog(null, "平局", "游戏结束", JOptionPane.PLAIN_MESSAGE);
            state.restart();
            ui.board_panel.repaint();
            return 0;
		}
        return -2;
	}

    public boolean makeMove(BoardUI ui, ChessState state, int x, int y){
        
        int fix_x = (x- BoardPanel.x+ BoardPanel.width/2) / BoardPanel.width * BoardPanel.width + BoardPanel.x;
		int fix_y = (y- BoardPanel.y+ BoardPanel.height/2) / BoardPanel.height * BoardPanel.height + BoardPanel.y;

		int idx_x = (fix_x- BoardPanel.x) / BoardPanel.width;
		int idx_y = (fix_y- BoardPanel.y) / BoardPanel.height;

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

		Image chess;
		if(state.getTurn() == 1)
		{	
			ui.text_pane.addText(String.format("黑方在（%s，%d）落子\n", Character.toString((char)((int)'A'+idx_y)), idx_x+1));
//			chess = new ImageIcon("../pic/black.png").getImage();
			ClassLoader classLoader = FiveJudge.class.getClassLoader();
			chess = new ImageIcon(classLoader.getResource("black_chess.png")).getImage();
		}
		else
		{
			ui.text_pane.addText(String.format("白方在（%s，%d）落子\n", Character.toString((char)((int)'A'+idx_y)), idx_x+1));
//			chess = new ImageIcon("../pic/white.png").getImage();
			ClassLoader classLoader = FiveJudge.class.getClassLoader();
			chess = new ImageIcon(classLoader.getResource("white_chess.png")).getImage();
		}
		Graphics graphics = ui.board_panel.getGraphics();
		graphics.drawImage(chess, fix_x- BoardPanel.width/2, fix_y- BoardPanel.height/2, BoardPanel.width, BoardPanel.height, null);
		
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

    public ChessState autoStep(int turn, BoardUI ui, ChessState state){
        int size = state.getChessPos().size();
        Random random = new Random();
        int cnt = 0;
        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++)
            {
                if(state.getChessPos().get(i).get(j) == 0)
                    cnt++;
            }
        int n = random.nextInt(cnt);
        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++)
            {
                if(state.getChessPos().get(i).get(j) == 0)
                    n--;
                if(n==0)
                {
                    state.getChessPos().get(i).set(j,turn);
                    state.x_history.push(i);
                    state.y_history.push(j);
                    ui.board_panel.setState(state);
                    ui.board_panel.repaint();
                    return state;
                }
            }
                
        return state;
        
    }

    public ChessState aiStep(int turn, BoardUI ui, ChessState state) {
        int size = state.getChessPos().size();
        HashMap<String, Integer> hm = new HashMap<String, Integer>();
        int[][] chessArray = new int[size][size];
        int [][] chessValue = new int[size][size];
        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++)
                chessArray[i][j] = -1*state.getChessPos().get(i).get(j);

		hm.put("-1", 20);
		hm.put("-1-1", 400);
		hm.put("-1-1-1", 420);
		hm.put("-1-1-1-1", 3000);
		hm.put("-11", 4);
		hm.put("-1-11", 40);
		hm.put("-1-1-11", 400);
		hm.put("-1-1-1-11", 10000);
		hm.put("1-1-1-1-1", 10000);
		hm.put("1", 8);
		hm.put("11", 80);
		hm.put("111", 1000);
		hm.put("1111", 5000);
		hm.put("1111-1", 5000);
		hm.put("1111-1-1", 5000);
		hm.put("1-1", 6);
		hm.put("11-1", 60);
		hm.put("111-1", 600);
		hm.put("-11-1", 5);
		hm.put("-111-1", 5);
		hm.put("1-1-11", 5);
		hm.put("1-11", 5);
        
        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++)
                chessValue[i][j] = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (chessArray[i][j] == 0) {
					String code = "";
					int color = 0;
					// 向右
					for (int k = i + 1; k < size; k++) {
						if (chessArray[Math.abs(k)][j] == 0) {
							break;
						} else {
							if (color == 0) { // 右边第一颗棋子
								color = chessArray[Math.abs(k)][j]; // 保存颜色
								code += chessArray[Math.abs(k)][j]; // 保存棋子相连情况
							} else if (chessArray[Math.abs(k)][j] == color) {
								code += chessArray[Math.abs(k)][j]; // 保存棋子相连情况
							} else {
								code += chessArray[Math.abs(k)][j]; // 保存棋子相连情况
								break;
							}
						}
						if (chessArray[i][j] != 0) {
							chessValue[i][j] = 0;
						}
					}
					Integer value = hm.get(code);
					if (value != null) {
						chessValue[i][j] += value;
					}
					if (value != null) {
						chessValue[i][j] += value;
					}
					// 向左
					code = "";
					color = 0;
					for (int k = i - 1; k >= 0; k--) {
						if (chessArray[Math.abs(k)][j] == 0) {
							break;
						} else {
							if (color == 0) { // 左边第一颗棋子
								color = chessArray[Math.abs(k)][j]; // 保存颜色
								code += chessArray[Math.abs(k)][j]; // 保存棋子相连情况
							} else if (chessArray[Math.abs(k)][j] == color) {
								code += chessArray[Math.abs(k)][j]; // 保存棋子相连情况
							} else {
								code += chessArray[Math.abs(k)][j]; // 保存棋子相连情况
								break;
							}
						}

					}
					value = hm.get(code);
					if (value != null) {
						chessValue[i][j] += value;
					}
					if (chessArray[i][j] != 0) {
						chessValue[i][j] = 0;
					}
					// 向上
					code = "";
					color = 0;
					for (int k = j - 1; k >= 0; k--) {
						if (chessArray[i][Math.abs(k)] == 0) {
							break;
						} else {
							if (color == 0) { // 左边第一颗棋子
								color = chessArray[i][Math.abs(k)]; // 保存颜色
								code += chessArray[i][Math.abs(k)]; // 保存棋子相连情况
							} else if (chessArray[i][Math.abs(k)] == color) {
								code += chessArray[i][Math.abs(k)]; // 保存棋子相连情况
							} else {
								code += chessArray[i][Math.abs(k)]; // 保存棋子相连情况
								break;
							}
						}

					}
					value = hm.get(code);
					if (value != null) {
						chessValue[i][j] += value;
					}
					if (chessArray[i][j] != 0) {
						chessValue[i][j] = 0;
					}
					// 向下
					code = "";
					color = 0;
					for (int k = j + 1; k < size; k++) {
						if (chessArray[i][Math.abs(k)] == 0) {
							break;
						} else {
							if (color == 0) { // 左边第一颗棋子
								color = chessArray[i][Math.abs(k)]; // 保存颜色
								code += chessArray[i][Math.abs(k)]; // 保存棋子相连情况
							} else if (chessArray[i][Math.abs(k)] == color) {
								code += chessArray[i][Math.abs(k)]; // 保存棋子相连情况
							} else {
								code += chessArray[i][Math.abs(k)]; // 保存棋子相连情况
								break;
							}
						}

					}
					value = hm.get(code);
					if (value != null) {
						chessValue[i][j] += value;
					}
					if (chessArray[i][j] != 0) {
						chessValue[i][j] = 0;
					}
					// 右下
					code = "";
					color = 0;
					for (int k = i + 1, l = j + 1; k < size && l < size; k++, l++) {
						if (chessArray[Math.abs(k)][Math.abs(l)] == 0) {
							break;
						} else {
							if (color == 0) { // 左边第一颗棋子
								color = chessArray[Math.abs(k)][Math.abs(l)]; // 保存颜色
								code += chessArray[Math.abs(k)][Math.abs(l)]; // 保存棋子相连情况
							} else if (chessArray[Math.abs(k)][Math.abs(l)] == color) {
								code += chessArray[Math.abs(k)][Math.abs(l)]; // 保存棋子相连情况
							} else {
								code += chessArray[Math.abs(k)][Math.abs(l)]; // 保存棋子相连情况
								break;
							}
						}

					}
					value = hm.get(code);
					if (value != null) {
						chessValue[i][j] += value;
					}
					if (chessArray[i][j] != 0) {
						chessValue[i][j] = 0;
					}
					// 右上
					code = "";
					color = 0;
					for (int k = i + 1, l = j - 1; k < size && l >= 0; k++, l--) {
						if (chessArray[Math.abs(k)][Math.abs(l)] == 0) {
							break;
						} else {
							if (color == 0) { // 左边第一颗棋子
								color = chessArray[Math.abs(k)][Math.abs(l)]; // 保存颜色
								code += chessArray[Math.abs(k)][Math.abs(l)]; // 保存棋子相连情况
							} else if (chessArray[Math.abs(k)][Math.abs(l)] == color) {
								code += chessArray[Math.abs(k)][Math.abs(l)]; // 保存棋子相连情况
							} else {
								code += chessArray[Math.abs(k)][Math.abs(l)]; // 保存棋子相连情况
								break;
							}
						}

					}
					value = hm.get(code);
					if (value != null) {
						chessValue[i][j] += value;
					}
					if (chessArray[i][j] != 0) {
						chessValue[i][j] = 0;
					}
					// 左上
					code = "";
					color = 0;
					for (int k = i - 1, l = j - 1; k >= 0 || l >= 0; k--, l--) {
						if (chessArray[Math.abs(k)][Math.abs(l)] == 0) {
							break;
						} else {
							if (color == 0) { // 左边第一颗棋子
								color = chessArray[Math.abs(k)][Math.abs(l)]; // 保存颜色
								code += chessArray[Math.abs(k)][Math.abs(l)]; // 保存棋子相连情况
							} else if (chessArray[Math.abs(k)][Math.abs(l)] == color) {
								code += chessArray[Math.abs(k)][Math.abs(l)]; // 保存棋子相连情况
							} else {
								code += chessArray[Math.abs(k)][Math.abs(l)]; // 保存棋子相连情况
								break;
							}
						}

					}
					value = hm.get(code);
					if (value != null) {
						chessValue[i][j] += value;
					}
					if (chessArray[i][j] != 0) {
						chessValue[i][j] = 0;
					}
					// 左下
					code = "";
					color = 0;
					for (int k = i - 1, l = j + 1; k >= 0 && l < size; k--, l++) {
						if (chessArray[Math.abs(k)][Math.abs(l)] == 0) {
							break;
						} else {
							if (color == 0) { // 左边第一颗棋子
								color = chessArray[Math.abs(k)][Math.abs(l)]; // 保存颜色
								code += chessArray[Math.abs(k)][Math.abs(l)]; // 保存棋子相连情况
							} else if (chessArray[Math.abs(k)][Math.abs(l)] == color) {
								code += chessArray[Math.abs(k)][Math.abs(l)]; // 保存棋子相连情况
							} else {
								code += chessArray[Math.abs(k)][Math.abs(l)]; // 保存棋子相连情况
								break;
							}
						}

					}
					value = hm.get(code);
					if (value != null) {
						chessValue[i][j] += value;
					}
					if (chessArray[i][j] != 0) {
						chessValue[i][j] = 0;
					}
				}
			}
		}

        // 判断权值最大的位置，并在该位置下棋。
		int maxv = 0;
        int maxi=-1, maxj=-1;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (maxv < chessValue[i][j]) {
					maxv = chessValue[i][j];
					maxi = i;
					maxj = j;
				}
			}
		}
		//画白棋
        if(maxi==-1 || maxj==-1)
            return autoStep(turn, ui, state);
		state.getChessPos().get(maxi).set(maxj, turn);
        state.x_history.push(maxi);
        state.y_history.push(maxj);
        //System.out.printf("%d白方下%d %d\n",turn, maxi, maxj);
        ui.board_panel.setState(state);
        ui.board_panel.repaint();
        return state;
    }

    private void dfs(int deep,Node root,int alpha,int beta,Point p){  
        if(deep==searchDeep){  
            root.mark=getMark();  
            // System.out.printf("%d\t%d\t%d\n",p.x,p.y,root.mark);  
            return;  
        }  
        ArrayList<Point> judgeSet=new ArrayList<Point>();  
        Iterator it=toJudge.iterator();  
        while(it.hasNext()){  
            Point now=new Point((Point)it.next());  
            judgeSet.add(now);  
        }  
        it=judgeSet.iterator();  
        while(it.hasNext()){  
            Point now=new Point((Point)it.next());  
            Node node=new Node();  
            node.setPoint(now);  
            root.addChild(node);  
            boolean flag=toJudge.contains(now);  
            chessBoard[now.y][now.x]=((deep&1)==1)?-1:1;  
            //if(isEnd(now.x,now.y)){  
            //    root.bestChild=node;  
            //    root.mark=MAXN*chessBoard[now.y][now.x];  
            //    chessBoard[now.y][now.x]=0;  
            //    return;  
            //}
  
            boolean flags[]=new boolean[8]; //标记回溯时要不要删掉  
            Arrays.fill(flags,true);  
            for(int i=0;i<8;++i){  
                Point next=new Point(now.x+dc[i],now.y+dr[i]);  
                if(0<=now.x+dc[i] && now.x+dc[i]<size && 0<=now.y+dr[i] && now.y+dr[i]<size && chessBoard[next.y][next.x]==0){  
                    if(!toJudge.contains(next)){  
                        toJudge.add(next);  
                    }  
                    else flags[i]=false;  
                }  
            }  
              
            if(flag)   
                toJudge.remove(now);  
            dfs(deep+1,root.getLastChild(),alpha,beta,now);  
            chessBoard[now.y][now.x]=0;  
            if(flag)  
                toJudge.add(now);  
            for(int i=0;i<8;++i)  
                if(flags[i])  
                    toJudge.remove(new Point(now.x+dc[i],now.y+dr[i]));  
            // alpha beta剪枝  
            // min层  
            if((deep&1)==1){  
                if(root.bestChild==null || root.getLastChild().mark<root.bestChild.mark){  
                    root.bestChild=root.getLastChild();  
                    root.mark=root.bestChild.mark;  
                    if(root.mark<=MINN)  
                        root.mark+=deep;  
                    beta=Math.min(root.mark,beta);  
                }  
                if(root.mark<=alpha)  
                    return;  
            }  
            // max层  
            else{  
                if(root.bestChild==null || root.getLastChild().mark>root.bestChild.mark){  
                    root.bestChild=root.getLastChild();  
                    root.mark=root.bestChild.mark;  
                    if(root.mark==MAXN)  
                        root.mark-=deep;  
                    alpha=Math.max(root.mark,alpha);  
                }  
                if(root.mark>=beta)  
                    return;  
            }  
        }  
        //if(deep==0) System.out.printf("******************************************\n");
        //System.out.println(root.bestChild);
    }  
  
    public int getMark(){  
        int res=0;  
        for(int i=0;i<size;++i){  
            for(int j=0;j<size;++j){  
                if(chessBoard[i][j]!=0){  
                    // 行  
                    boolean flag1=false,flag2=false;  
                    int x=j,y=i;  
                    int cnt=1;  
                    int col=x,row=y;  
                    while(--col>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt;  
                    if(col>0 && chessBoard[row][col]==0) flag1=true;  
                    col=x;row=y;  
                    while(++col<size && chessBoard[row][col]==chessBoard[y][x]) ++cnt;  
                    if(col<size && chessBoard[row][col]==0) flag2=true;  
                    if(flag1 && flag2)  
                        res+=chessBoard[i][j]*cnt*cnt;  
                    else if(flag1 || flag2) res+=chessBoard[i][j]*cnt*cnt/4;   
                    if(cnt>=5) res=MAXN*chessBoard[i][j];  
                    // 列  
                    col=x;row=y;  
                    cnt=1;flag1=false;flag2=false;  
                    while(--row>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt;  
                    if(row>0 && chessBoard[row][col]==0) flag1=true;  
                    col=x;row=y;  
                    while(++row<size && chessBoard[row][col]==chessBoard[y][x]) ++cnt;  
                    if(row<size && chessBoard[row][col]==0) flag2=true;  
                    if(flag1 && flag2)  
                        res+=chessBoard[i][j]*cnt*cnt;  
                    else if(flag1 || flag2)  
                        res+=chessBoard[i][j]*cnt*cnt/4;  
                    if(cnt>=5) res=MAXN*chessBoard[i][j];  
                    // 左对角线  
                    col=x;row=y;  
                    cnt=1;flag1=false;flag2=false;  
                    while(--col>0 && --row>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt;  
                    if(col>0 && row>0 && chessBoard[row][col]==0) flag1=true;  
                    col=x;row=y;  
                    while(++col<size && ++row<size && chessBoard[row][col]==chessBoard[y][x]) ++cnt;  
                    if(col<size && row<size && chessBoard[row][col]==0) flag2=true;  
                    if(flag1 && flag2)    
                        res+=chessBoard[i][j]*cnt*cnt;  
                    else if(flag1 || flag2) res+=chessBoard[i][j]*cnt*cnt/4;  
                    if(cnt>=5) res=MAXN*chessBoard[i][j];  
                    // 右对角线  
                    col=x;row=y;  
                    cnt=1;flag1=false;flag2=false;  
                    while(++row<size && --col>0 && chessBoard[row][col]==chessBoard[y][x]) ++cnt;  
                    if(row<size && col>0 && chessBoard[row][col]==0) flag1=true;  
                    col=x;row=y;  
                    while(--row>0 && ++col<size && chessBoard[row][col]==chessBoard[y][x]) ++cnt;  
                    if(row>0 && col<size && chessBoard[i][j]==0) flag2=true;  
                    if(flag1 && flag2)  
                        res+=chessBoard[i][j]*cnt*cnt;  
                    else if(flag1 || flag2) res+=chessBoard[i][j]*cnt*cnt/4;  
                    if(cnt>=5) res=MAXN*chessBoard[i][j];  
                      
                }  
            }  
        }  
        return res;  
    }

    public ChessState advanceStep(int turn, ChessState state){  
        Node node=new Node();
        size = state.getChessPos().size();
        chessBoard = new int[size][size];
        
        boolean init = true;
        
        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++)
            {
                chessBoard[i][j] = state.getChessPos().get(i).get(j);
                if(chessBoard[i][j]!=0)
                    init = false;
            }

        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++)
            {
                if(chessBoard[i][j]!=0)
                {
                    Point p=new Point(j,i);  
                    if(toJudge.contains(p))  
                        toJudge.remove(p);
                    for(int k=0;k<8;++k){
                        Point now = new Point(p.x+dc[k],p.y+dr[k]);
                        if(0<=now.x && now.x<size && 0<=now.y && now.y<size && chessBoard[now.y][now.x]==0)
                            if(!toJudge.contains(now))  
                                toJudge.add(now);  
                    }
                }
            }
        
        

        if(init)
        {
            System.out.println("初始化");
            Random r = new Random();
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            chessBoard[y][x] = 1;
            for(int i=0;i<8;++i)  
            if(0<=x+dc[i] && x+dc[i]<size && 0<=y+dr[i] && y+dr[i]<size){  
                Point now=new Point(x+dc[i],y+dr[i]);  
                if(!toJudge.contains(now))  
                    toJudge.add(now);
            }
        }

        dfs(0,node,MINN,MAXN,null);  
        Point now=node.bestChild.p;
        state.getChessPos().get(now.y).set(now.x, turn);
        state.x_history.push(now.y);
        state.y_history.push(now.x);
        toJudge.clear();
        System.out.printf("%d黑方下%d %d\n", turn, now.y, now.x);
        return state;
    } 

    class Node{  
        public Node bestChild=null;  
        public ArrayList<Node> child=new ArrayList<Node>();  
        public Point p=new Point();  
        public int mark;  
        Node(){  
            this.child.clear();  
            bestChild=null;  
            mark=0;  
        }  
        public void setPoint(Point r){  
            p.x=r.x;  
            p.y=r.y;  
        }  
        public void addChild(Node r){  
            this.child.add(r);  
        }  
        public Node getLastChild(){  
            return child.get(child.size()-1);  
        }  
    }
}