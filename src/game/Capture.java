package game;

import game.model.Spot;

import java.util.Arrays;


public class Capture {


    public static boolean canCapture(String path, String who, Spot[][] board, int row, int column, boolean isPlayer) {

        if (path.equals("captureLeft")) {
            System.out.println("I was in cancapture left path");
            return canCaptureLeft(who, board, row, column, isPlayer);
        } else if (path.equals("captureRight")) {
            System.out.println("I was in cancapture right path");
            return canCaptureRight(who, board, row, column, isPlayer);
        } else {
            System.out.println("This should never appear. im canCapture Method");
            return false;
        }

    }

    public static boolean canCaptureLeft(String who, Spot[][] board, int row, int column, boolean isPlayer) {
        String[] pieces = new String[6];
        int rowAlter;
        int colAlter;
        if (who == "player") {
            rowAlter = -1;
            colAlter = -1;
        } else {
            rowAlter = 1;
            colAlter = -1;
        }

        if (Main.inBounds(row + rowAlter, column + colAlter) && board[row + rowAlter][column + colAlter] != null
                ) {

        }


        boolean victimExists = false;
        if (Main.inBounds(row + rowAlter, column + colAlter) && board[row + rowAlter][column + colAlter] != null
                && !board[row + rowAlter][column + colAlter].getOccupier().equals("X")
                && !Move.canMoveLeft(who, board, row, column, isPlayer)) {
            victimExists = true;
        }


        //*******************

        boolean safeLand = Main.inBounds(row + rowAlter, column + colAlter) && Move.canMoveLeft(who, board, row + rowAlter, column + colAlter, isPlayer);

        if (who == "player")    // if player, check if victim is actually a black piece
        {
            pieces[0] = "b1";
            pieces[1] = "b2";
            pieces[2] = "b3";
            pieces[3] = "b4";
            pieces[4] = "b5";
            pieces[5] = "b6";
        } else {
            pieces[0] = "w1";
            pieces[1] = "w2";
            pieces[2] = "w3";
            pieces[3] = "w4";
            pieces[4] = "w5";
            pieces[5] = "w6";
        }

        if (victimExists && safeLand && Arrays.asList(pieces).contains(board[row + rowAlter][column + colAlter].getOccupier())) {
            return true;
        } else {
            return false;
        }


    }

    public static boolean canCaptureRight(String who, Spot[][] board, int row, int column, boolean isPlayer) {
        String[] pieces = new String[6];
        int rowAlter;
        int colAlter;
        if (who == "player") {
            rowAlter = -1;
            colAlter = 1;
        } else {
            rowAlter = 1;
            colAlter = 1;
        }

        boolean victimExists = false;
        if (Main.inBounds(row + rowAlter, column + colAlter) &&
                board[row + rowAlter][column + colAlter] != null
                && !board[row + rowAlter][column + colAlter].getOccupier().equals("X")
                && !Move.canMoveLeft(who, board, row, column, isPlayer)) {
            victimExists = true;
        }

        boolean safeLand = Main.inBounds(row + rowAlter, column + colAlter) && Move.canMoveRight(who, board, row + rowAlter, column + colAlter, isPlayer);

        if (who == "player")    // if player, check if victim is actually a black piece
        {
            pieces[0] = "b1";
            pieces[1] = "b2";
            pieces[2] = "b3";
            pieces[3] = "b4";
            pieces[4] = "b5";
            pieces[5] = "b6";
        } else {
            pieces[0] = "w1";
            pieces[1] = "w2";
            pieces[2] = "w3";
            pieces[3] = "w4";
            pieces[4] = "w5";
            pieces[5] = "w6";
        }

        if (victimExists && safeLand && Arrays.asList(pieces).contains(board[row + rowAlter][column + colAlter].getOccupier())) {
            return true;
        } else {
            return false;
        }


    }


    public static void capture(String path, String who, Spot[][] board, int pieceRow, int pieceCol, boolean isPlayer) {

        if (path.equals("captureLeft")) {
            captureLeft(who, board, pieceRow, pieceCol, isPlayer);
            //	 if(isPlayer){ Main.endGame("player");}
        } else if (path.equals("captureRight")) {
            captureRight(who, board, pieceRow, pieceCol, isPlayer);
            //	if(isPlayer){ Main.endGame("player");}
        } else {
            System.out.println("This should never appear. im Capture() Method");


        }

    }


    public static Spot[][] captureLeft(String who, Spot[][] board, int pieceRow, int pieceCol, boolean isPlayer) {
        int rowAlter;        // to specify spot to be used.
        int colAlter;

        if (who.equals("player")) {
            rowAlter = -1;
            colAlter = -1;
        } else {
            rowAlter = 1;
            colAlter = -1;
        }

        // kill victim
        board[pieceRow + rowAlter][pieceCol + colAlter].setOccupier(null);
        // change killer position
        board[pieceRow + (2 * rowAlter)][pieceCol + (colAlter * 2)].setOccupier(board[pieceRow][pieceCol].getOccupier());
        board[pieceRow][pieceCol].setOccupier(null);
        return board;
    }


    public static Spot[][] captureRight(String who, Spot[][] board, int pieceRow, int pieceCol, boolean isPlayer) {
        int rowAlter;        // to specify spot to be used.
        int colAlter;

        if (who == "player") {
            rowAlter = -1;
            colAlter = 1;
        } else {
            rowAlter = 1;
            colAlter = 1;
        }

        // kill victim
        board[pieceRow + rowAlter][pieceCol + colAlter].setOccupier(null);
        // change killer position
        board[pieceRow + (2 * rowAlter)][pieceCol + (colAlter * 2)].setOccupier(board[pieceRow][pieceCol].getOccupier());
        board[pieceRow][pieceCol].setOccupier(null);
        return board;
    }


}
