package game;

import game.*;

import java.util.Arrays;
import java.util.Iterator;


public class MoveGenerator {


    static void getMoves(String who, Spot[][] board, Tree tree, int currentDepth) {

//	 	System.out.println("current depth:"+ currentDepth);
        String nextTurn;
        int requiredDepth = 2;
        if (currentDepth <= requiredDepth) {
            String occupier = null;
            String[] pieces = new String[6];

            if (who == "player")    // if players move, get player's pieces' moves only.
            {
                pieces[0] = "w1";
                pieces[1] = "w2";
                pieces[2] = "w3";
                pieces[3] = "w4";
                pieces[4] = "w5";
                pieces[5] = "w6";
            } else {
                pieces[0] = "b1";
                pieces[1] = "b2";
                pieces[2] = "b3";
                pieces[3] = "b4";
                pieces[4] = "b5";
                pieces[5] = "b6";
            }
            //	System.out.println(who+"'s Turn");
            for (int row = 0; row <= 5; row++) {
                for (int column = 0; column <= 5; column++) {

                    Tree xscenario;
                    Tree yscenario;
                    Tree zscenario;
                    Tree qscenario;


                    if (board[row][column] != null) {
                        occupier = board[row][column].getOccupier();

                        if (Arrays.asList(pieces).contains(occupier)) {
//		            	System.out.println("***** for Occupier "+occupier+"******");
                            //	            	System.out.println("can move left?");
                            if (Move.canMoveLeft(who, board, row, column, false))    // if piece moves left, create scenario
                            {


                                Spot[][] x = copyBoard(board);

                                x = Move.moveLeft(who, x, row, column);
                                //        		 Main.printBoard(x);
                                xscenario = new Tree(x);
                                tree.getChildren().add(xscenario);

                                if (currentDepth == requiredDepth) {
                                    xscenario.setValue(Evaluation.evaluateBoardValue(who, xscenario, "move"));
                                    //            			System.out.println("Terminal Value: "+xscenario.getValue());

                                }
                                //	            		 	 Main.printBoard(x);


                            }


                            //            	System.out.println("can move Right?");
                            if (Move.canMoveRight(who, board, row, column, false))    // if piece moves right, create scenario
                            {
                                Spot[][] y = copyBoard(board);
                                //          		Main.printBoard(y);
                                y = Move.moveRight(who, y, row, column);
                                yscenario = new Tree(y);
                                tree.getChildren().add(yscenario);


                                if (currentDepth == requiredDepth) {
                                    yscenario.setValue(Evaluation.evaluateBoardValue(who, yscenario, "move"));
                                    //      			System.out.println("Terminal Value: "+yscenario.getValue());
                                }


                            }


                            //	            	System.out.println("can Capture left?");
                            if (Capture.canCaptureLeft(who, board, row, column, false))    // if piece captures left, create scenario
                            {
                                Spot[][] z = copyBoard(board);
                                z = Capture.captureLeft(who, z, row, column, false);

                                zscenario = new Tree(z);
                                zscenario.setIsCapture(true);
                                tree.getChildren().add(zscenario);

                                zscenario.setValue(Evaluation.evaluateBoardValue(who, zscenario, "capture"));
                                //            		System.out.println("Terminal Value:  "+zscenario.getValue());
                                //         		Main.printBoard(z);


                            }


                            //  	System.out.println("can Capture right?");
                            if (Capture.canCaptureRight(who, board, row, column, false))    // if piece captures right, create scenario
                            {

                                Spot[][] q = copyBoard(board);
                                q = Capture.captureRight(who, q, row, column, false);

                                qscenario = new Tree(q);
                                qscenario.setIsCapture(true);

                                tree.getChildren().add(qscenario);
                                //	            		 Main.printBoard(q);

                                qscenario.setValue(Evaluation.evaluateBoardValue(who, qscenario, "capture"));
                                //		System.out.println("Terminal Value:  "+qscenario.getValue());


                            }


                        }

                    }
                }

            }

            nextTurn = (who.equals("player")) ? "computer" : "player";
            for (Iterator<Tree> i = tree.getChildren().iterator(); i.hasNext(); ) {
                Tree child = (Tree) i.next();
                if (!child.isCapture()) getMoves(nextTurn, child.getBoard(), child, currentDepth + 1);
            }

        }

    }


    public static Spot[][] copyBoard(Spot[][] board) {
        Spot[][] newBoard = new Spot[6][6];
        Spot[][] y = board;
        for (int Row = 0; Row <= 5; Row++) {
            for (int Column = 0; Column <= 5; Column++) {
                if (y[Row][Column] != null) {
                    newBoard[Row][Column] = new Spot(y[Row][Column].getName(), y[Row][Column].getValue(), y[Row][Column].getOccupier());

                } else {
                    newBoard[Row][Column] = null;
                }

            }
        }

        return newBoard;
    }


}

	

