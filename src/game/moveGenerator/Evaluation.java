package game.moveGenerator;

import game.model.*;
import game.utils.Utils;

import java.util.Random;

/**
 * @created by enry8 on 15/09/16.
 *
 * @updated by sirCamp
 */

/**
 * I would start with something dead simple: material difference. Which is equal to: (# of my checkers on board) - (# of opponent's checkers on board). Then you could add more features and begin to weight them, like "# of exposed checkers", "# of protected checkers", or perhaps "# of squares controlled in middle of board", and so on. Talk to a domain expert (i.e. a checkers player) and/or consult a checkers book to see what would work out well.
 */
public class Evaluation {


    private final static Float KINGS_EVAL = 1.4F;

    private Integer choice = 0;


    /**
     * Balanced: is influenced only by the difference of pieces between the two players and the wieght of the pieces: is not aggressive
     * @param node
     * @param choice
     * @return
     */
    private static Float heuristicOne(Node node, Integer choice){
        Spot[][] board = node.getState().getBoard();

        float black = 0;
        float white = 0;
        float blackKings = 0;
        float whiteKings = 0;

        Player player = node.getPlayer();

        for(int i = 0; i <8; i++){

            for(int j = 0; j < 8; j++){

                Piece piece = board[i][j].getOccupier();
                if( piece != null){

                    if(piece.isKing()){

                        if(piece.isWhite()){
                            whiteKings += 1;
                        }
                        else{
                            blackKings += 1;
                        };
                    }
                    else{
                        if(piece.isWhite()){
                            white += 1;
                        }
                        else{
                            black += 1;
                        };
                    }
                }
            }
        }

        if(player.isWhite()){

            return (white-black + Evaluation.KINGS_EVAL*(whiteKings-blackKings));
        }
        else{

            //if black
            return (black - white + Evaluation.KINGS_EVAL*(blackKings-whiteKings));
        }
    }

    /**
     * Randomized: Radomize the value of the current state
     * @param node
     * @param choice
     * @return
     */
    private static Float heuristicTwo(Node node, Integer choice){


        Random rand = new Random();

        Integer randomNum = rand.nextInt((20 - 1) + 1) + 1;

        return randomNum.floatValue();
    }

    /**
     * Centrality: Influece the checkers to move into the middle of board
     * @param node
     * @param choice
     * @return
     */
    private static Float heuristicThree(Node node, Integer choice){

        Spot [][] board = node.getState().getBoard();

        Float partial = Evaluation.heuristicOne(node, choice);

        Float result = 0f;

        Player player = node.getPlayer();

        for(int i = 0; i < 8; i++){

            for(int j = 0; j < 8; j ++){

                Piece piece = board[i][j].getOccupier();
                if( piece != null){

                    /*Setto valori per mosse*/
                    float negative = 0.5f;
                    float positive = 1f;

                    if( Utils.isInCenter(i,j) ) {

                        negative = 1;
                        positive = 2;

                    }

                    /*se bianco e cerco bianco*/
                    if (piece.isWhite() && player.isWhite()) {
                        result += positive;
                    }
                    /*se bianco e cerco nero*/
                    else if (piece.isWhite() && player.isBlack()) {

                        result -= negative;
                    }
                    /*se nero e cerco nero*/
                    else if (piece.isBlack() && player.isBlack()) {

                        result += positive;
                    } else {

                        result -= negative;
                    }

                }

            }
        }

        return partial + result;

    }


    /**
     * Backrow: encourage some player to stay behind and keep covered the border
     *
     * @param node
     * @param choice
     * @return
     */
    private static Float heuristicFour(Node node, Integer choice){

        Spot [][] board = node.getState().getBoard();

        Float partial = Evaluation.heuristicOne(node, choice);

        Float result = 0f;

        Player player = node.getPlayer();

        for(int i = 0; i < 8; i++){

            for(int j = 0; j < 8; j++){

                Piece piece = board[i][j].getOccupier();
                if( piece != null){

                    /*Setto valori per mosse*/
                    float negative = 0.5f;
                    float positive = 1f;

                    if( Utils.isInBackrow(i)) {

                        negative = 1;
                        positive = 2;

                    }

                    /*se bianco e cerco bianco*/
                    if (piece.isWhite() && player.isWhite()) {
                        result += positive;
                    }
                    /*se bianco e cerco nero*/
                    else if (piece.isWhite() && player.isBlack()) {

                        result -= negative;
                    }
                    /*se nero e cerco nero*/
                    else if (piece.isBlack() && player.isBlack()) {

                        result += positive;
                    } else {

                        result -= negative;
                    }


                }
            }
        }

        return partial + result;

    }


    /**
     * Complete: this heuristic uses different factor  ex:
     *
     * Difference between pieces number
     * +5 for center squares
     * +2 for edge squares
     * -3 for squares near to kings
     *
     * @param node
     * @param choice
     * @return
     */
    private static Float heuristicFive(Node node, Integer choice){

        Float partial = Evaluation.heuristicOne(node,choice);

        Spot [][] board = node.getState().getBoard();

        Float result = 0f;

        Player player = node.getPlayer();

        for(int i = 0; i < 8; i++) {

            for (int j = 0; j < 8; j++) {

                Piece piece = board[i][j].getOccupier();
                if(piece != null) {


                    /*Setto valori per mosse*/
                    float negative = 0.5f;
                    float positive = 1f;

                    float positve_king = 0;
                    float negative_king = 0;


                    //center
                    if (Utils.isInCenter(i, j)) {

                        positive =5;
                        negative = -1;
                    }

                    //
                    if (Utils.isInEdge(j)) {

                        positive = 2;
                        negative = -0.5f;
                    }

                    if(Utils.isNearKing(i, j, board, piece)){

                        positve_king = -3;
                        negative_king = 1;
                    }


                    /*se bianco e cerco bianco*/
                    if (piece.isWhite() && player.isWhite()) {
                        result += positive;
                        result += positve_king;
                    }
                    /*se bianco e cerco nero*/
                    else if (piece.isWhite() && player.isBlack()) {

                        result -= negative;
                        result -= negative_king;
                    }
                    /*se nero e cerco nero*/
                    else if (piece.isBlack() && player.isBlack()) {

                        result += positive;
                        result += positve_king;
                    } else {

                        result -= negative;
                        result -= negative_king;
                    }


                }
            }

        }

        return partial+result;
    }


    /**
     * Aggressive: this heuristic prefers to attack and eat the pieces of opponent
     *
     *
     * @param node
     * @param choice
     * @return
     */
    private static Float heuristicSix(Node node, Integer choice){

        Spot[][] board = node.getState().getBoard();

        float black = 0;
        float white = 0;
        float blackKings = 0;
        float whiteKings = 0;

        Player player = node.getPlayer();

        for(int i = 0; i <8; i++){

            for(int j = 0; j < 8; j++){

                Piece piece = board[i][j].getOccupier();
                if( piece != null){

                    if(piece.isKing()){

                        //cerco bianco ho bianco
                        if(piece.isWhite() && player.isWhite()){
                            whiteKings += 2;
                        }
                        else if(piece.isWhite() &&  player.isBlack()){
                            blackKings += 1;
                        }
                        else if(piece.isBlack() &&  player.isBlack()){
                            blackKings +=2;
                        }
                        else{
                            whiteKings += 1;
                        }
                    }
                    else{

                        if(piece.isWhite() && player.isWhite()){
                            white += 2;
                        }
                        else if(piece.isWhite() &&  player.isBlack()){
                            black += 1;
                        }
                        else if(piece.isBlack() &&  player.isBlack()){
                            black +=2;
                        }
                        else{
                            white += 1;
                        }

                    }
                }
            }
        }

        if(player.isWhite()){

            return (white-black + Evaluation.KINGS_EVAL*(whiteKings-blackKings));
        }
        else{

            //if black
            return (black - white + Evaluation.KINGS_EVAL*(blackKings-whiteKings));
        }
    }



    public static Float getEvaluationValue(Node node, Integer choice){


        return Evaluation.heuristicOne(node,choice);

    }


}
