package lab3;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Gamenbody3 {

	public static void main(String[] args) {
		Board3 newGame = new Board3();
		Scanner cmd = new Scanner(System.in);
		int rows = cmd.nextInt();
		int cols = cmd.nextInt();
		int bombs = cmd.nextInt();
		newGame.setArgument(rows,cols,bombs);
		newGame.createArray();
		newGame.printGameInterface();
		System.out.println("欢迎来到鲁蓉希制作的扫雷游戏!");
		System.out.println("Start your game,input like \"a b\" then");// TODO 自动生成的方法存根

		String tab = cmd.nextLine();
		while(!newGame.isVictory) {
			String coordinate = cmd.nextLine();
			newGame.click(coordinate);
		}
		System.out.println("恭喜你,扫雷完成!");
	}

}

class Board3 {
	private int rows;
	private int cols;
	private int bombs;
	private int rightClick;
	private int i = 0;
	private char[][] bombLable;
	private boolean[][] state;
	private char[][] gameInterface;
	public boolean isValidClick;
	public boolean isVictory;
	
    public Board3() {
		
	}
	
	public void setArgument(int rows,int cols,int bombs) {
		this.rows = rows;
		this.cols = cols;
		this.bombs = bombs;
		rightClick = rows * cols - bombs;
	}
	   
	public void createArray() {
		
	   bombLable = new char[rows][cols];
	   state = new boolean[rows][cols];
	   gameInterface = new char[rows][cols];
	   
	   for(int i = 0;i < rows;i++) {
		   for(int j = 0;j < cols;j++)
			   gameInterface[i][j] = '#';
	   }
	   
	   for(int i = 0;i < bombs;i++) {
		   int bRow = (int)(Math.random() * rows);
		   int bCol = (int)(Math.random() * cols);
		   if(state[bRow][bCol])
			   i--;
		   bombLable[bRow][bCol] = '*';
		   state[bRow][bCol] = true;
	  }
	   
	   for(int i = 0;i < rows;i++) {
			for(int j = 0;j < cols;j++) {
				if(state[i][j])
					continue;
				int bombCount = 0;
				for(int r = -1;r + i <rows & r < 2;r++) {
					if(r + i < 0)
						continue;
				for(int c = -1;c + j < cols & c < 2;c++) {
					if(c + j < 0)
						continue;
					if(state[r + i][c + j])
						bombCount++;
						}
					}
				bombLable[i][j] = String.valueOf(bombCount).charAt(0);
			  }
		}
	   
	}
	
	private void printBombLable() {
		for(int i = 0;i < rows;i++) {
			for(int j = 0;j < cols;j++)
				System.out.print(bombLable[i][j] + " ");
			System.out.println();
		}
	}

	public void printGameInterface() {
		for(int i = 0;i < rows;i++) {
			for(int j = 0;j < cols;j++)
				System.out.print(gameInterface[i][j] + " ");
			System.out.println();
		}
	}
	
	public void click(String s) {
		isValidClick = true;
		int r = 0;
		int c = 0;
		if(s.matches("\\d+\\s\\d+")) {
			Pattern pattern = Pattern.compile("(\\d+)\\s(\\d+)");
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
               r = Integer.parseInt(matcher.group(1));
               c = Integer.parseInt(matcher.group(2));
            }
		}
		else {
			System.out.println("Not a click,try again!");
			isValidClick = false;
		}
		if(r >= rows | c >= cols) {
		   System.out.println("Not within the map,click again!");
		   isValidClick = false;
		   return;
		}
	    else if(gameInterface[r][c] != '#') {
    	   System.out.println("You have clicked it,click others!");
    	   isValidClick = false;
	       return;
	    }
	    else if(state[r][c]) { 
	    	printBombLable();
    		System.out.println("踩中地雷,游戏结束!");
			System.exit(1);
		}
	    else {		
		    gameInterface[r][c] = bombLable[r][c];
			printGameInterface();
			i++;
			isVictory = (i == rightClick);
			if(!isVictory) 
				System.out.println("Good job,go on!"); 
		}
	}
}
	

