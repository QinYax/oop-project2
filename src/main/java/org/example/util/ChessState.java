package org.example.util;

import java.util.ArrayList;
import java.util.Stack;

public class ChessState
{
    public ArrayList<ArrayList<Integer>> chess_pos;
    public int turn = 1;
    public String type;
    public Stack<Integer> x_history;
    public Stack<Integer> y_history;

    public ChessState(int size, String type)
    {
        this.chess_pos = new ArrayList<ArrayList<Integer>>();
        
        for(int i=0; i<size; i++)
        {
            ArrayList<Integer> tmp = new ArrayList<Integer>();
            for(int j=0; j<size; j++)
                tmp.add(0);
            this.chess_pos.add(tmp);
        }
        
        this.type = type;
        initHistory();
    }

    public void restart()
    {

        for(int i=0; i<this.chess_pos.size(); i++)
        {
            for(int j=0; j<this.chess_pos.size(); j++)
                this.chess_pos.get(i).set(j,0);
        }
        while(!this.x_history.empty())
        {
            x_history.pop();
            y_history.pop();
        }
    }

    private void initHistory()
    {
        this.x_history = new Stack<Integer>();
        this.y_history = new Stack<Integer>();
    }

    public ChessStateInterface createMemento()
    {
        return new ChessStateMemento(this.chess_pos, this.turn, this.type, this.x_history, this.y_history);
    }

    public void restoreMemento(ChessStateInterface memento)
    {
        ChessStateMemento saved_memento = (ChessStateMemento) memento;
        this.setState(saved_memento.getChessPos(), saved_memento.getTurn(), saved_memento.getType(), saved_memento.getX_history(), saved_memento.getY_history());
        //initHistory();
    }

    public void setState(ArrayList<ArrayList<Integer>> chess_pos, int turn, String type, Stack<Integer> x_history, Stack<Integer> y_history)
    {
        this.chess_pos = chess_pos;
        this.turn = turn;
        this.type = type;
        this.x_history = x_history;
        this.y_history = y_history;
    }

    public void setChessPos(ArrayList<ArrayList<Integer>> chess_pos)
    {
        this.chess_pos = chess_pos;
    }
    
    public void change()
    {
        this.turn = -1 * this.turn;
    }

    public ArrayList<ArrayList<Integer>> getChessPos()
    {
        return this.chess_pos;
    }

    public int getTurn()
    {
        return this.turn;
    }

    public String getType()
    {
        return this.type;
    }
}