package org.example.util;

import javax.swing.*;
import java.io.*;

public class ChessStateCareTaker
{
    public ChessStateInterface memento;
    
    public ChessStateInterface retrieveMemento(){
        return this.memento;
    }

    public void loadMemento(String file_name){
        try
        {
            System.out.println("target/data/" + file_name + ".ser");
            FileInputStream fileIn = new FileInputStream("target/data/" + file_name + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.memento = (ChessStateInterface) in.readObject();
            in.close();
            fileIn.close();
        }
        catch(IOException i)
        {
            i.printStackTrace();
            return;
        }
        catch(ClassNotFoundException c)
        {
            System.out.println(file_name + " not found");
            c.printStackTrace();
            return;
        }
    }

    public void saveMemento(ChessStateInterface memento, String file_name){
        try
        {
            FileOutputStream fileOut = new FileOutputStream("target/data/" + file_name + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(memento);
            out.close();
            fileOut.close();
            JOptionPane.showMessageDialog(null, "当前游戏已成功保存为" + file_name, "保存成功", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(IOException i)
        {
            i.printStackTrace();
        }
    }
}