package lab2;

import java.util.Scanner;

public class Gamebody {

	public static void main(String[] args) {
		Board bombLable = new Board();
		Scanner cmd = new Scanner(System.in);
		int rows = cmd.nextInt();
		int cols = cmd.nextInt();
		int bombs = cmd.nextInt();
		bombLable.setArgument(rows, cols, bombs);
		System.out.println("欢迎来到鲁蓉希制作的扫雷游戏!打印的地雷图如下:");
		bombLable.printBombLable();// TODO 自动生成的方法存根
     }

}

class Board {
	
	private int rows;
	private int cols;
	private int bombs;
	private char[][] bombLable;
	private boolean[][] state;
	   
	public Board() {
		
	}
	
	public void setArgument(int rows,int  cols,int bombs) {
		this.rows = rows;
		this.cols = cols;
		this.bombs = bombs;
	}
	
	
	private void creatArray() {
	   bombLable = new char[rows][cols];
	   state = new boolean[rows][cols];
	 }
	   
	private void randomBomb() {
		   for(int i = 0;i < bombs;i++) {
			   int bRow = (int)(Math.random() * rows);
			   int bCol = (int)(Math.random() * cols);
			   if(state[bRow][bCol])
				   i--;
			   bombLable[bRow][bCol] = '*';
			   state[bRow][bCol] = true;
		  }
	 }
	
	private void writeNumber() {
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
	
	public void printBombLable() {
		creatArray();
		randomBomb();
		writeNumber();
		for(int i = 0;i < rows;i++) {
			for(int j = 0;j < cols;j++)
				System.out.print(bombLable[i][j] + " ");
			System.out.println();
		}
	}

}

