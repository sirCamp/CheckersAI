package Game;
import Game.Board;
import Game.Capture;
import Game.Move;
import Game.MoveGenerator;
import Game.Spot;
import Game.Tree;
import java.io.*;
import java.util.StringTokenizer;


public class Main {

	public static void main(String[]args) throws IOException
	{
		startGame();
	}

	public static void startGame() throws IOException
	{
		Board aBoard = new Board();
		printBoard(aBoard.getBoard());
		while(true)
		{
	//		aBoard.setBoard();
			aBoard = playerMove(aBoard);
			if(aBoard.getCapture()) 
			{printBoard(aBoard.getBoard()); System.out.println("player win?"+aBoard.getCapture()); endGame("player");}
			printBoard(aBoard.getBoard());
			aBoard = computerMove(aBoard);
			if(aBoard.getCapture()) 
			{printBoard(aBoard.getBoard());System.out.println("computer win?"+aBoard.getCapture()); ;endGame("computer");}
			printBoard(aBoard.getBoard());
		}
	}

	public static void endGame(String winner)
	{
		if(winner.equals("player"))
		{
			System.out.println("You Win :D");
			System.exit(0);
		}
		else
		{
			System.out.println("You Lose :(");
			System.exit(0);
		} 
	}

	private static Board computerMove(Board aBoard) {
		System.out.println("\n\n Computer's turn:" );
		Tree tree = new Tree(aBoard.getBoard());
		MoveGenerator.getMoves("computer", aBoard.getBoard(), tree, 0);
		aBoard.setBoard(tree.getMove(true).getBoard());
		aBoard.setCapture(tree.getMove(true).isCapture());
		return aBoard;
	}

	private static Board playerMove(Board board) throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		System.out.println("Enter a command in this format:  'w1 moveRight' ");
		System.out.println("available commands: moveLeft, moveRight, captureLeft, captureRight");
		String command = br.readLine();
		StringTokenizer commandTokens = new StringTokenizer(command);
		if(commandTokens.countTokens() != 2) 
		 	{
			System.out.println("Please enter a valid command");
			
			}
		String piece = commandTokens.nextToken();
		String path = commandTokens.nextToken();
		
		if(!(path.equals("moveLeft") || path.equals("moveRight")|| path.equals("captureLeft") || path.equals("captureRight"))) 
		{
			System.out.println("Please enter a valid command");
			playerMove(board);
		}
		else
		{
		if(path.equals("moveLeft") || path.equals("moveRight"))
		{
		 for (int row =0; row <= 5; row++)
	      {
	         for (int column =0; column <= 5; column++)
	         {
	       // 	 System.out.println(board[row][column].getOccupier().toString());
	        	 if(board.getBoard()[row][column]!=null && board.getBoard()[row][column].getOccupier().toString().equals(piece) )
		            {
	        		 if (Move.canMove(path,"player",board.getBoard(), row, column, true ))
	        		 {
	        			Spot[][] temp = MoveGenerator.copyBoard(board.getBoard());
	        			Move.move(path,"player",temp, row, column );
	        			board.setBoard(temp);
	        			return board;
	        		 }
	        		 else
	        		 {
	        			 playerMove(board);
	        		 }
	        	 }
	         }
	      } 
		}
		else
		{
			
			 for (int row =0; row <= 5; row++)
		      {
		         for (int column =0; column <= 5; column++)
		         {
		        	 if(board.getBoard()[row][column]!= null && board.getBoard()[row][column].getOccupier().equals(piece) )
			            {
		        		 if (Capture.canCapture(path,"player",board.getBoard(), row, column, true ) == true)
		        		 {
		        			 Capture.capture(path,"player",board.getBoard(), row, column, true );
		        			
		        			 board.setCapture(true);
		        			 return board;
		        		 }
		        		 else
		        		 {
		        			 System.out.println("you cannot capture");
		        			 playerMove(board);
		        		 }
			            }
		         }
		      }
		}
	}
		
	return board;	
	}

	static void printBoard(Spot[][] board) 
	{
		 for (int row =0; row <= 5; row++)
	      {
			 System.out.println();
	         for (int column =0; column <= 5; column++)
	         {
	            if(board!= null && board[row][column]!= null)
	            {
	            	System.out.print("["+board[row][column].getOccupier()+"]");
	            } 
	            else
	            {
	            	System.out.print(" * ");
	            }
	         }
	      }
		 System.out.println("\n___________________________________________");
	}
	static boolean inBounds(int row, int col)
	{
		return row<=5  && col <=5 && row>=0 && col>=0;
	}
}
