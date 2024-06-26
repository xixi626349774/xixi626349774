package lab4;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Gamebody4 {

	public static void main(String[] args) {
		while(true) {
	        Function newGame = new Function();
	        Scanner cmd = new Scanner(System.in);
	        System.out.println("欢迎来到鲁蓉希制作的扫雷游戏!选择游戏难度:(行数 列数 炸弹数)");
	        System.out.println("行*列不超过24*30");
	        do{
			   String loads = cmd.nextLine();
			   newGame.load(loads);
			   if(!newGame.isValidLoad)
				  continue;
		    } while(!newGame.isValidLoad);
	        newGame.createArray();
		    newGame.printGameInterface();
		    System.out.println("Start your game.\n\"a b\" means a click.\n"
				+ "\"mark a b\" means a mark,mark twice means cancel the mark.\n"
				+"Mark sign is \"&\".\n"
				+"\"N\" means end the game\n\"Y\" means restart a game\n"
				+"Now,start timing!");
		
		    while(!newGame.isVictory & !newGame.isFail & !newGame.startOver){
			    String command = cmd.nextLine();
			    newGame.commandType(command);
		    }
		
		    newGame.timer();
		
		    if(newGame.isVictory)
			     System.out.println("恭喜你,扫雷完成!");
		    else 
			     System.out.println("Although you didn't finish it,you are still excellent!");
		
		    while(!newGame.startOver){
		    	System.out.println("Would you like to play one more time?Y/N");
			    String s = cmd.nextLine();
			    newGame.endOrStartOver(s);
		    } 
		}
	}
}

class Function {
	
	private int rows;
	private int cols;
	private int bombs;
	private int rightClick;
	private int rightMark;
	private int leftBomb;
	private int i;
	private long startTime;
	private long endTime;
	private char[][] bombLable;
	private boolean[][] state;
	private char[][] gameInterface;
	public boolean isValidLoad;
	public boolean startOver;
	public boolean isVictory;
	public boolean isFail;
	private boolean invincibility;
	
    public Function() {
		
	}
	
    public void load(String s) {
		isValidLoad = true;
		if(s.matches("\\d+\\s\\d+\\s\\d+")) {
			Pattern pattern = Pattern.compile("(\\d+)\\s(\\d+)\\s(\\d+)");
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
               rows = Integer.parseInt(matcher.group(1));
               cols = Integer.parseInt(matcher.group(2));
               bombs = Integer.parseInt(matcher.group(3));
            }
			leftBomb = bombs;
			startOver = false;
			isVictory = false;
			isFail = false;
			invincibility = true;
	        i = 0;
	        rightClick = rows * cols - bombs;
	        }
		else {
			System.out.println("Not right form,try again!");
			isValidLoad = false;
			return;
		}
	    if(rows > 24 | cols > 30 | rows == 0 | cols == 0) {
		    System.out.println("The range is not reasonable,set again!");
			isValidLoad = false;
			return;
		}
		else if(bombs >= rows * cols | bombs == 0) {
			System.out.println("The number of bombs is not reasonable,set again!");
			isValidLoad = false;
			return;
		}
		 
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
	   startTime = System.currentTimeMillis();
	}
	
	private void printBombLable() {
		for(int i = 0;i < rows;i++) {
			for(int j = 0;j < cols;j++)
				System.out.print(bombLable[i][j] + " ");
			System.out.println();
		}
		System.out.println("踩中地雷,游戏结束!");
		isFail = true;
	}

	public void printGameInterface() {
		for(int i = 0;i < rows;i++) {
			for(int j = 0;j < cols;j++)
				System.out.print(gameInterface[i][j] + " ");
			System.out.println();
		}
		System.out.println("Still " + leftBomb + " bombs have not been marked!");
    }
	
	public void commandType(String s) {
		if(s.equals("N") | s.equals("Y")) {
			endOrStartOver(s);
			return;
		}
		else if(s.startsWith("mark ")) {
			mark(s);
			return;
		}
		else
			click(s);
	}
	
	private void click(String s) {
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
			return;
		}
		if(r >= rows | c >= cols) {
			System.out.println("Not within the map,click again!");
	        return;
			}
		else if(gameInterface[r][c] != '#') {
	    	System.out.println("You have clicked or marked it,click others!");
		    return;
		    }
	    else if(state[r][c]){
		    if(!invincibility) { 
			    printBombLable();
			    return;
		    }
		    else {
		        System.out.println("This is a bomb!");
			    mark("mark " + s);
			    invincibility = false;
                return;
		    }
		 }
		 else{
		    gameInterface[r][c] = bombLable[r][c];
			if(bombLable[r][c] == '0')
			    open(r,c);
            printGameInterface();
			i++; 
			isVictory = (i == rightClick | bombs == rightMark);
			if(!isVictory) 
				System.out.println("Good job,go on!");
		    }
	}
	
	private void open(int r,int c) {
		for(int i = r -1;i <rows & (i < r + 2);i++) {
			if(i < 0)
				continue;
		    for(int j = c -1;j < cols & (j <c + 2);j++) {
			    if(j < 0)
				     continue;
			    if(!Character.isDigit(gameInterface[i][j])) {
				     gameInterface[i][j] = bombLable[i][j];
				     this.i++;
			         if(bombLable[i][j] == '0' ) 
			    	 open(i,j);
			    }
		    }
		 }
	 }

	 private void mark(String s) {
			int r = 0;
			int c = 0;
			if(s.matches("mark\\s\\d+\\s\\d+")) {
				Pattern pattern = Pattern.compile("mark\\s(\\d+)\\s(\\d+)");
	            Matcher matcher = pattern.matcher(s);
	            if (matcher.find()) {
	               r = Integer.parseInt(matcher.group(1));
	               c = Integer.parseInt(matcher.group(2));
	            }					
			}
			else {
				System.out.println("Not a mark,try again!");
				return;
			}
		    if(r >= rows | c >= cols ) {
				System.out.println("Not within the map,click again!");
				return;
				}
			else if(Character.isDigit(gameInterface[r][c])) {
				System.out.println("You have clicked it,mark others!");
				return;
			}
			else if(leftBomb == 0 & gameInterface[r][c] != '&') {
				System.out.println("The number of bombs you marked reaches the limit!");
				return;
			}
			else {
				if(gameInterface[r][c] == '#') {
				      leftBomb--;
				      gameInterface[r][c] = '&';
				      if(state[r][c])
				          rightMark++;
			   	      }
				else{
					  leftBomb++;
					  gameInterface[r][c] = '#'; 
					  if(state[r][c])
						  rightMark--;
					  }
			    printGameInterface();
		   	    isVictory = (i == rightClick | bombs == rightMark);
				if(!isVictory) 
					System.out.println("Good job,go on!");
            }
	}
	 
	 public void endOrStartOver(String s) {
		if(!s.equals("Y") & !s.equals("N"))  {
			System.out.println("Invalid input!");
			return; 
			}
		if(s.equals("N"))
			System.exit(1);
		else if(s.equals("Y"))
			startOver = true;
	    }
		   

	 public void timer() {
		endTime = System.currentTimeMillis();
		long total = (endTime - startTime) / 1000;
		long s = total % 60;
		long totalm = total / 60;
		long m = totalm % 60;
		long h = totalm / 60;
		System.out.println("The duration of the game ground -- " + h + ":" + m + ":" + s);
	}

}


