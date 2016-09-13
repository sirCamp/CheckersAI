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
		Player p1 = new Player("huma", "p1", "b");
		Player p2 = new Player("human", "p2", "w");
		Board board = new Board(p1,p2);
		board.printBoard();
		Boolean end = false;
		do {
			if(round == 0){
				System.out.println("p1 (black) has to move");
				p1.move(board);
				round = 1;
				end = endGame(p2);
			}
			else {
				System.out.println("p2 (white) has to move");
				p2.move(board);
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

}
