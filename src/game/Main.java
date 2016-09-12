package game;

import game.model.Board;
import game.model.Player;
import game.model.Spot;
import useless.Capture;
import useless.Move;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.StringTokenizer;


public class Main {

	public static void main(String[]args) throws IOException, CloneNotSupportedException {
		startGame();
	}

	public static void startGame() throws IOException, CloneNotSupportedException {
		Random ran = new Random();
		Integer round = ran.nextInt(2); // who starts?
		Player p1 = new Player("human", "p1", "b");
		Player p2 = new Player("human", "p2", "w");
		Board board = new Board(p1,p2);
		board.printBoard();
		Boolean end = false;
		do {
			//		aBoard.setBoard();
			if(round == 0){
				System.out.println("p1 (black) has to move");
				p1.move(board);
				//aBoard = playerMove(aBoard);
				//p1.move();
				/*if(aBoard.getCapture())
				{
					printBoard(aBoard.getBoard());
					System.out.println("player win?"+aBoard.getCapture());
					endGame("player");
				}*/
				//end = endGame(p2);
				round = 1;
				end = endGame(p2);
			}
			else {
				System.out.println("p2 (white) has to move");
				p2.move(board);

				// aBoard = computerMove(aBoard);
				//p2.move();
				/*if(aBoard.getCapture())
				{
					printBoard(aBoard.getBoard());
					System.out.println("computer win?"+aBoard.getCapture());
					endGame("computer");
				}*/
				round = 0;
				end = endGame(p1);
			}
			board.printBoard();
		}while(!end);
		endGameMessage(round);
	}

	public static Boolean endGame(Player loser){
		if(loser.hasLost()==true)
		{
			return true;
		} //else
		return false;
	}

	public static void endGameMessage(Integer round){
		String winner = "";
		if(round == 1){
			winner = "p1 (black) ";
		}else winner = "p2 (white) ";
		System.out.println(winner + "won the game!");
		return;
	}

	/*

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
*/

}
