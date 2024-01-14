package org.example.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;

public class ChessStateMemento implements ChessStateInterface, Serializable
{
    public ArrayList<ArrayList<Integer>> state;
    public int turn = 1;
    public String type;
    public Stack<Integer> x_history;
    public Stack<Integer> y_history;

    public ChessStateMemento(ArrayList<ArrayList<Integer>> state, int turn, String type, Stack<Integer> x_history, Stack<Integer> y_history)
    {
        this.state = state;
        this.turn = turn;
        this.type = type;
        this.x_history = x_history;
        this.y_history = y_history;
    }

    public ArrayList<ArrayList<Integer>> getChessPos()
    {
        return this.state;
    }

    public int getTurn()
    {
        return this.turn;
    }

    public String getType()
    {
        return this.type;
    }

    public Stack<Integer> getX_history()
    {
        return this.x_history;
    }

    public Stack<Integer> getY_history()
    {
        return this.y_history;
    }
}