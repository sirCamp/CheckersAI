package game;

import game.model.Board;
import game.model.Player;
import game.utils.CSVExporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Main {

	public static void main(String[]args) throws IOException, CloneNotSupportedException {
		startGame();
	}

	private static void startGame() throws IOException, CloneNotSupportedException {
		Random ran = new Random();
		Integer round = 0; // black starts
		Integer roundCounter = 0;
		System.out.println("Players creation...");

		Player p1 = createPlayer(true);
		Player p2 = createPlayer(false);

		System.out.println("["+p1.getName()+", "+p1.getAlgorithm()+"] VS ["+p2.getName()+", "+p2.getAlgorithm()+"]");
		if(round == 0){
			System.out.println("p1 (black) starts the game!");
		}else{
			System.out.println("p2 (white) starts the game!");
		}
		Board board = new Board(p1,p2);
		board.printBoard();
		Boolean end;
		do {
			if(round == 0){
				System.out.println("p1 (black) has to move...");
				p1.play(board, roundCounter);
				round = 1;
			}
			else {
				System.out.println("p2 (white) has to move...");
				p2.play(board, roundCounter);
				round = 0;
			}
            end = endGame(p1) || endGame(p2);
			roundCounter++;
			board.printBoard();
		}while(!end && roundCounter<200);
		endGameMessage(p1, roundCounter);
		CSVExporter.printCSV();
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
		System.out.println("The game ended in " + roundCounter + " rounds.");
	}

	private static Player createPlayer(Boolean isFirst) throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		if (isFirst) {
			System.out.println("First player (black):");
		} else {
			System.out.println("Second player (white):");
		}
		Boolean done = false;
		Integer choice;
		do {
			System.out.println("Is it a human player (1) or an AI player (2)?");
			choice = Character.getNumericValue(br.readLine().charAt(0));
			if(choice == 1 || choice == 2){
				done = true;
			}
			else{
				System.out.println("Insert a valid number!");
			}
		} while (!done);
		if(choice == 1) {
			if (isFirst) {
				return new Player("human", "p1 (black)", "b");
			} else {
				return new Player("human", "p2 (white)", "w");
			}
		}
		// is AI :
		Integer heuristic = 1;
		done = false;
		do {
			System.out.println("Which evaluation function should it use? (1)-(7)");
			heuristic = Character.getNumericValue(br.readLine().charAt(0));
			if(heuristic>=1 && heuristic<=7) {
				done = true;
			}else{
					System.out.println("Insert a valid number!");
			}
		} while (!done);
		done = false;
		Integer depth = 3;
		do {
			System.out.println("Which depth should it have the decision tree? [Suggested: (3)-(7)]");
			depth = Character.getNumericValue(br.readLine().charAt(0));
			if(depth>=2 && depth<=20){
				done = true;
			}else{
				System.out.println("Insert a valid number!");
			}
		} while (!done);
		String pruning = "";
		System.out.println("Should it use pruning? (y) or (n)");
		Character decision = br.readLine().charAt(0);
		if(decision.equals('y')){
			pruning = "-pruning";
		}
		if (isFirst) {
			return new Player(heuristic+"-pc"+pruning, "p1 (black)", "b", depth);
		} else {
			return new Player(heuristic+"-pc"+pruning, "p2 (white)", "w", depth);
		}
	}

}
