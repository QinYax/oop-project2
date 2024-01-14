package org.example.util;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel
{
	public JLabel[] user =new JLabel[12];

    public void constructPanel()
    {
		this.setPreferredSize(new Dimension(300,0));//设置thisanel的大小
		this.setBackground(Color.LIGHT_GRAY);//设置右边的界面颜色为白色
		//this.add(this, BorderLayout.EAST);//添加到框架布局的东边部分
		this.setLayout(new FlowLayout());//设置thisanel为流式布局
		
		//编辑用户信息栏，包括头像，昵称，性别，等级
		//ImageIcon[] userPicture={USERPICTURE,USERNAME,USERSEX,USERLEVEL};
		
		String[] userMessage={"黑方","白方","游客", "游客","", "", "战绩","战绩", "0/0/0", "0/0/0", "", ""};
		for(int i=0; i<userMessage.length; i++){
			user[i]=new JLabel(userMessage[i]);
			user[i].setPreferredSize(new Dimension(145, 40));

			user[i].setFont(new Font(Font.MONOSPACED,Font.ITALIC,20));
			this.add(user[i]);
		}
        //接下来我们需要把按钮等组件依次加到那个thisanel上面
		//设置按钮数组
		//String[] butname= {"","",""};
		String[] butname= {"开始新游戏", "加载存档", "保存当前游戏", "过子", "悔棋","认输", "录像回放", "登陆账号"};

		JButton[] button = new JButton[butname.length];
		
		//依次把三个按钮组件加上去
		for(int i=0; i<butname.length; i++) {
			button[i]=new JButton(butname[i]);
			button[i].setPreferredSize(new Dimension(145, 45));
			this.add(button[i]);
		}
    }

	public void setUser(String type, String username, int[] his)
	{

		if(type.equals("黑方"))
		{
			user[2].setText(username);
			user[8].setText(String.valueOf(his[0])+"/"+String.valueOf(his[1])+"/"+String.valueOf(his[2]));
		}
		else
		{
			user[3].setText(username);
			user[9].setText(String.valueOf(his[0])+"/"+String.valueOf(his[1])+"/"+String.valueOf(his[2]));
		}
	}

	public boolean[] isAI()
	{
		return new boolean[]{user[2].getText().contains("AI"), user[3].getText().contains("AI")};
	}
}