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
		Integer round = 0;//(ran.nextInt(10) % 2); // who starts?
		Integer roundCounter = 0;
		System.out.println("Players creation...");

		Player p1 = new Player("5-pc-pruning", "p1", "b", 8); //createPlayer(true);
		Player p2 = new Player("4-pc", "p2", "w", 3); //createPlayer(false);

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
			System.gc();
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
		System.out.println(roundCounter);
	}

	private static Player createPlayer(Boolean isFirst) throws IOException {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		if (isFirst) {
			System.out.println("First player:");
		} else {
			System.out.println("Second player:");
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
			System.out.println("Which heuristic should it use? (1)-(6)");
			heuristic = Character.getNumericValue(br.readLine().charAt(0));
			if(heuristic>=1 && heuristic<=6) {
				done = true;
			}else{
					System.out.println("Insert a valid number!");
			}
		} while (!done);
		done = false;
		Integer depth = 3;
		do {
			System.out.println("Which depth should it have the decision tree? [Suggested: (3)-(7)]");
			heuristic = Character.getNumericValue(br.readLine().charAt(0));
			if(heuristic>=2 && heuristic<=20){
				done = true;
			}else{
				System.out.println("Insert a valid number!");
			}
		} while (!done);
		done = false;
		String pruning = "";
		System.out.println("Should it use pruning? (y) or (n)");
		char decision = br.readLine().charAt(0);
		if(decision == 'y'){
			pruning = "-pruning";
		}
		if (isFirst) {
			return new Player(heuristic+"-pc"+pruning, "p1 (black)", "b", depth);
		} else {
			return new Player(heuristic+"-pc"+pruning, "p2 (white)", "w", depth);
		}
	}

}
