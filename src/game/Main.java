package game;

import game.model.Board;
import game.model.Player;
import java.io.IOException;
import java.util.Random;

public class Main {

	public static void main(String[]args) throws IOException, CloneNotSupportedException {
		startGame();
	}

	private static void startGame() throws IOException, CloneNotSupportedException {
		Random ran = new Random();
		Integer round = ran.nextInt(2); // who starts?
		Integer roundCounter = 0;
		Player p1 = new Player("pc", "p1", "b", 3);
		Player p2 = new Player("pc", "p2", "w", 3);
		// Player p2 = new Player("human", "p2", "w"); pc or human, last value is the tree depth
		Board board = new Board(p1,p2);
		board.printBoard();
		Boolean end;
		do {
			if(round == 0){
				System.out.println("p1 (black) has to move...");
				p1.move(board);
				round = 1;
			}
			else {
				System.out.println("p2 (white) has to move...");
				p2.move(board);
				round = 0;
			}
            end = endGame(p1) || endGame(p2);
			roundCounter++;
			board.printBoard();
		}while(!end && roundCounter<200);
		endGameMessage(p1, roundCounter);
	}

	private static Boolean endGame(Player loser){ return loser.hasLost(); }

	private static void endGameMessage(Player p1, Integer roundCounter){
		if(roundCounter >= 200){
			System.out.println("It's a draw!");
		}else{
			String winner;
			if(p1.hasLost()){
				winner = "p2 (white) ";
			}else winner = "p1 (black) ";
            System.out.println(winner + "won the game!");
		}
	}

}
