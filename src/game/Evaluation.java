package game;

import game.Spot;

import java.util.Arrays;


public class Evaluation {


    static int evaluateBoardValue(String who, Tree tree, String command) {
        int total = 0;
        Spot[][] board = tree.getBoard();
        String[] whitePieces = {"w1", "w2", "w3", "w4", "w5", "w6"};
        String occupier = "";

        if (command.equals("move")) {
            for (int row = 0; row <= 5; row++) {
                for (int column = 0; column <= 5; column++) {
                    occupier = (board[row][column] != null) ? board[row][column].getOccupier() : "";
                    if (board[row][column] != null && !occupier.equals("X")) {
                        if (Arrays.asList(whitePieces).contains(occupier)) {

                            total += board[row][column].getValue() - 1;
                        } else {
                            //      		System.out.println(total);
                            total += board[row][column].getValue() * 1;
                        }
                    }
                }
            }

        } else {
            if (who.equals("player")) {
                total += -100;
            } else {
                total += +100;
            }
        }
        return total;
    }
}
