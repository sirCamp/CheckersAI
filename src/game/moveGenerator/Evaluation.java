package game.moveGenerator;

import game.model.*;
import game.utils.Utils;

import javax.rmi.CORBA.Util;
import java.util.Random;

/**
 * I would start with something dead simple: material difference.
 * Which is equal to: (# of my checkers on board) - (# of opponent's checkers on board).
 * Then you could add more features and begin to weight them, like "# of exposed checkers",
 * "# of protected checkers", or perhaps "# of squares controlled in middle of board", and so on.
 * Talk to a domain expert (i.e. a checkers player) and/or consult a checkers book to see what would work out well.
 */

public class Evaluation {

    private final static Float KINGS_EVAL = 1.4F;
    private final static Float KINGS_EVAL_DOUBLE = 2F;

    /**
     * Balanced: is influenced only by the difference of pieces between the two players and the wieght of the pieces: is not aggressive
     * @param node

     * @return
     */
    private static Float heuristicOne(Node node){
        Spot[][] board = node.getState().getBoard();

        float black = 0;
        float white = 0;
        float blackKings = 0;
        float whiteKings = 0;

        Player player = node.getPlayer();

        for(int i = 0; i <8; i++){
            for(int j = 0; j < 8; j++){
                if( board[i][j] != null && board[i][j].getOccupier() != null){

                    Piece piece = board[i][j].getOccupier();
                    if(piece.isKing()){

                        if(piece.isWhite()){
                            whiteKings += 1;
                        }
                        else{
                            blackKings += 1;
                        }
                    }
                    else{
                        if(piece.isWhite()){
                            white += 1;
                        }
                        else{
                            black += 1;
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

    /**
     * Randomized: Radomize the value of the current state
     * @param node

     * @return
     */
    private static Float heuristicTwo(Node node){


        Random rand = new Random();

        Integer randomNum = rand.nextInt((20 - 1) + 1) + 1;

        return randomNum.floatValue();
    }

    /**
     * Centrality: Influece the checkers to move into the middle of board
     * @param node

     * @return
     */
    private static Float heuristicThree(Node node){

        Spot [][] board = node.getState().getBoard();
        Float partial = Evaluation.heuristicOne(node);
        Float result = 0f;
        Player player = node.getPlayer();

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j ++){
                if( board[i][j] != null && board[i][j].getOccupier() != null){
                    Piece piece = board[i][j].getOccupier();
                    /* setting values for each move */
                    float negative = 0.5f;
                    float positive = 1f;
                    if( Utils.isInCenter(i,j) ) {
                        negative = 1;
                        positive = 2;
                    }
                    /* if piece is white and I want white */
                    if (piece.isWhite() && player.isWhite()) {
                        result += positive;
                    }
                    /* if piece is white and I want black */
                    else if (piece.isWhite() && player.isBlack()) {
                        result -= negative;
                    }
                    /* if piece is black and I want black */
                    else if (piece.isBlack() && player.isBlack()) {
                        result += positive;
                    }
                    /* if piece is black and I want white */
                    else {
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
     * @return
     */
    private static Float heuristicFour(Node node){
        Spot [][] board = node.getState().getBoard();
        Float partial = Evaluation.heuristicOne(node);
        Float result = 0f;
        Player player = node.getPlayer();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if( board[i][j] != null && board[i][j].getOccupier() != null){
                    Piece piece = board[i][j].getOccupier();
                    /* setting values for each move */
                    float negative = 0.5f;
                    float positive = 1f;
                    if( Utils.isInBackrow(i)) {
                        negative = 1;
                        positive = 2;
                    }
                    /* if piece is white and I want white */
                    if (piece.isWhite() && player.isWhite()) {
                        result += positive;
                    }
                    /* if piece is white and I want black */
                    else if (piece.isWhite() && player.isBlack()) {

                        result -= negative;
                    }
                    /* if piece is black and I want black */
                    else if (piece.isBlack() && player.isBlack()) {

                        result += positive;
                    }
                    /* if piece is black and I want white */
                    else {

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

     * @return
     */
    private static Float heuristicFive(Node node){
        Float partial = Evaluation.heuristicOne(node);
        Spot [][] board = node.getState().getBoard();
        Float result = 0f;
        Player player = node.getPlayer();
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if( board[i][j] != null && board[i][j].getOccupier() != null){
                    Piece piece = board[i][j].getOccupier();
                    /* setting values for each move */
                    float negative = 0.5f;
                    float positive = 1f;
                    float positve_king = 0;
                    float negative_king = 0;
                    //center
                    if (Utils.isInCenter(i, j)) {
                        positive = 5;
                        negative = 2;
                    }
                    if (Utils.isInEdge(j)) {
                        positive = 2;
                        negative = 1;
                    }
                    if(Utils.isNearKing(i, j, board, piece)){
                        positve_king = -3;
                        negative_king = 1;
                    }
                    /* if piece is white and I want white */
                    if (piece.isWhite() && player.isWhite()) {
                        result += positive;
                        result += positve_king;
                    }
                    /* if piece is white and I want black */
                    else if (piece.isWhite() && player.isBlack()) {
                        result -= negative;
                        result -= negative_king;
                    }
                    /* if piece is black and I want black */
                    else if (piece.isBlack() && player.isBlack()) {
                        result += positive;
                        result += positve_king;
                    }
                    /* if piece is black and I want white */
                    else {
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
     * @return
     */
    private static Float heuristicSix(Node node){
        Spot[][] board = node.getState().getBoard();

        float black = 0;
        float white = 0;
        float blackKings = 0;
        float whiteKings = 0;
        Float result = 0f;
        Player player = node.getPlayer();

        for(int i = 0; i <8; i++){
            for(int j = 0; j < 8; j++){

                if( board[i][j] != null && board[i][j].getOccupier() != null){

                    Piece piece = board[i][j].getOccupier();

                    if(piece.isKing()){

                        if(piece.isWhite()){
                            whiteKings += 1;
                        }
                        else{
                            blackKings += 1;
                        }
                    }
                    else{
                        if(piece.isWhite()){
                            white += 1;
                        }
                        else{
                            black += 1;
                        }
                    }

                    if (player.isWhite() && piece.isWhite()) {

                        if(Utils.isInMiddleSide(i)){

                            result += 2;
                        }

                        if(Utils.isInHighSide(i)){
                            result += 5;
                        }

                        if(node.getMove().contains("capture")){

                            result += 7;
                        }


                    } else if(player.isBlack() && piece.isBlack()) {

                        if(Utils.isInMiddleSide(i)){

                            result += 2;
                        }

                        if(Utils.isInLowerSide(i)){
                            result += 5;
                        }

                        if(node.getMove().contains("capture")){

                            result += 7;
                        }
                    }

                }
            }
        }

        if(player.isWhite()){
            return (white-black + Evaluation.KINGS_EVAL*(whiteKings-blackKings)) + result;
        }
        else{
            //if black
            return (black - white + Evaluation.KINGS_EVAL*(blackKings-whiteKings)) +result;
        }
    }


    /**
     * Defensive: this heuristic prefers to defend then eat the pieces of opponent
     * @param node
     * @return
     */
    public static Float heuristicSeven(Node node){


        Spot[][] board = node.getState().getBoard();

        float black = 0;
        float white = 0;
        float blackKings = 0;
        float whiteKings = 0;
        Float result = 0f;
        Player player = node.getPlayer();

        for(int i = 0; i <8; i++){
            for(int j = 0; j < 8; j++){

                if( board[i][j] != null && board[i][j].getOccupier() != null){

                    Piece piece = board[i][j].getOccupier();

                    if(piece.isKing()){

                        if(piece.isWhite()){
                            whiteKings += 1;
                        }
                        else{
                            blackKings += 1;
                        }
                    }
                    else{
                        if(piece.isWhite()){
                            white += 1;
                        }
                        else{
                            black += 1;
                        }
                    }

                    if (player.isWhite() && piece.isWhite()) {

                       if(Utils.isNearOneWhite(i,j,board)){

                           result += 1;
                       }
                       else if(Utils.isNearMoreThanOneWhite(i,j,board)){

                           result += 2;
                       }
                       else if(Utils.isNearOneOpponent(i,j,board,piece)){

                           result -= 1;

                       }
                       else if(Utils.isNearMoreThanOneOpponent(i,j,board,piece)){
                           result -= 2;
                       }


                    } else if(player.isBlack() && piece.isBlack()) {


                        if(Utils.isNearOneBlack(i,j,board)){
                            result += 1;
                        }

                        if(Utils.isNearMoreThanOneBlack(i,j,board)){
                            result += 2;
                        }
                        else if(Utils.isNearOneOpponent(i,j,board,piece)){

                            result -= 1;

                        }
                        else if(Utils.isNearMoreThanOneOpponent(i,j,board,piece)){
                            result -= 2;
                        }

                    }

                }
            }
        }

        if(player.isWhite()){
            return (white-black + Evaluation.KINGS_EVAL*(whiteKings-blackKings)) + result;
        }
        else{

            return (black - white + Evaluation.KINGS_EVAL*(blackKings-whiteKings)) +result;
        }
    }


    public static Float heuristicEight(Node node){



        Spot[][] board = node.getState().getBoard();

        float black = 0;
        float white = 0;
        float blackKings = 0;
        float whiteKings = 0;
        Float result = 0f;
        Player player = node.getPlayer();

        for(int i = 0; i <8; i++){
            for(int j = 0; j < 8; j++){

                if( board[i][j] != null && board[i][j].getOccupier() != null){

                    Piece piece = board[i][j].getOccupier();

                    if(piece.isKing()){

                        if(piece.isWhite()){
                            whiteKings += 1;
                        }
                        else{
                            blackKings += 1;
                        }
                    }
                    else{
                        if(piece.isWhite()){
                            white += 1;
                        }
                        else{
                            black += 1;
                        }
                    }

                    if(piece.isKing()) {


                        if (Utils.isNearMoreThanOneOpponent(i,j,board,piece)) {

                            result += 5;
                        }

                        if (Utils.isNearOneOpponent(i,j,board,piece)) {

                            result += 7;
                        }

                        if (Utils.isInCenter(i, j)) {

                            result += 7;
                        }

                        if(node.getMove().contains("capture")){

                            result += 15;
                        }

                        if (Utils.isInEdge(j)) {

                            result -= 4;
                        }

                        if (Utils.isInBackrowByColor(i, piece)) {

                            result -= 7;
                        }

                        if (Utils.isInAngle(i, j)) {

                            result -= 10;
                        }
                    }
                    else{
                        if(node.getMove().contains("capture")){

                            result += 10;
                        }
                    }

                }
            }
        }

        if(player.isWhite()){
            return (white-black + Evaluation.KINGS_EVAL_DOUBLE*(whiteKings-blackKings)) + result;
        }
        else{

            return (black - white + Evaluation.KINGS_EVAL_DOUBLE*(blackKings-whiteKings)) +result;
        }
    }

    public static Float getEvaluationValue(Node node, Integer heuristic){
        switch(heuristic){
            case 1:{
                return Evaluation.heuristicOne(node);
            }
            case 2:{
                return Evaluation.heuristicTwo(node);
            }
            case 3:{
                return Evaluation.heuristicThree(node);
            }
            case 4:{
                return Evaluation.heuristicFour(node);
            }
            case 5:{
                return Evaluation.heuristicFive(node);
            }
            case 6:{
                return Evaluation.heuristicSix(node);
            }
            case 7:{
                return Evaluation.heuristicSeven(node);
            }
            case 8:{
                return Evaluation.heuristicEight(node);
            }
            default:{
                return Evaluation.heuristicOne(node);
            }
        }
    }

}
