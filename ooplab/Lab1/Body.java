package Lab1;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Body {

	public static void main(String[] args) {
	    Board.readBoard();
        Scanner scanner = new Scanner(System.in);
        while(true) {
    	      String command = scanner.nextLine();
    	      if(command.equals("print"))
    		       Board.printBoard();
    	      else if(command.startsWith("at")) {
    		       String[] input = command.split(" ");
    		       int x = Integer.parseInt(input[1]);
    		       int y = Integer.parseInt(input[2]);
    		       List<Coordinate>adjacentCoordinates = Board.getCoordinates(x,y);
    		       System.out.println(adjacentCoordinates);
    	      }
    	      else if(command.equals("exit"))
    		       break;
    	      else
    		       System.out.println("invalid input");
        }
	}

}

class Board {
	static char[][] board;
	public static void readBoard() {
	   board = new char[5][5];
	   Scanner scanner = new Scanner(System.in);
	   for(int i = 0;i < 5;i++) {
	    	String line = scanner.nextLine();
	        String[] item = line.split(",");
	        for(int j = 0;j < 5;j++)
	        board[i][j] = item[j].charAt(0);
	    	}
	    }
     public static void printBoard() {
	    for(int i = 0;i < 5;i++) {
	    	for(int j = 0;j < 5;j++) 
	    		System.out.print(board[i][j] + " ");

	    	System.out.println();
	    }
     }
	 public static List<Coordinate> getCoordinates(int x,int y){
	    List<Coordinate>adjacentCoordinates =  
	    	new ArrayList<>();
	    adjacentCoordinates.add(new Coordinate
				(x,y,board[x][y]));
	    if(y > 0)
	    	adjacentCoordinates.add(new Coordinate
	    				(x,y - 1,board[x][y - 1]));
	    if(y < 4)
	    	adjacentCoordinates.add(new Coordinate
	    				(x,y + 1,board[x][y + 1]));
	    if(x > 0)
	    	adjacentCoordinates.add(new Coordinate
	    				(x - 1,y,board[x - 1][y]));
	    if(x < 4)
	    	adjacentCoordinates.add(new Coordinate
	    				(x + 1,y,board[x + 1][y]));
	    return adjacentCoordinates;
	    }
}

class Coordinate {
	   private int x;
	   private int y;
	   private char item;
	   
	   public Coordinate(int x,int y,char item) {
		   this.x = x;
		   this.y = y;
		   this.item = item;
	   }
	   
	   @Override
	   public String toString() {
		   return "(" + x + "," + y + "," + item + ")";
		   }
	}
