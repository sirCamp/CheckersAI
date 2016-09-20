package game.utils;

import game.model.Piece;
import game.model.Spot;

public class Utils {

    public static boolean isInAngle(int x, int y){
        boolean result = false;
        if(x == 0 && y == 0)
            result = true;
        else if(x == 0 && y == 7)
            result = true;
        else if(x == 7 && y == 7)
            result = true;
        else if(x == 0 && y == 7)
            result = true;
        return result;
    }

    public static boolean isInCenter(int x, int y){ return ( x > 2 && x < 6) && ( y > 2 && y < 6); }

    public static boolean isInCenterRows(int x){ return ( x > 2 && x < 6); }

    public static boolean isInCenterColumns(int y){ return ( y > 2 && y < 6); }

    public static boolean isInEdge(int y){ return (y < 1 || y >= 7); }

    public static boolean isInBackrow(int x){ return ( x < 2 || x > 5); }

    public static boolean isInMiddleBackrow(int x){ return ( (x > 1 && x < 4) || (x < 7 && x > 4)); }

    public static boolean isInBackrowByColor(int x, Piece piece){

        if(piece.isWhite() && x < 3){

            return true;
        }
        else if(piece.isBlack() && x > 5){

            return true;
        }
        else{
            return false;
        }
    }

    public static boolean isNearKing(int x, int y, Spot[][] board, Piece piece){
        boolean result;
        if(!piece.isKing()) {
            if (x == 0 || x == 7 || y == 0 || y == 7) {
                result = false;
            } else {
                Piece topLeft = board[x - 1][y - 1].getOccupier();
                Piece topRight = board[x + 1][y - 1].getOccupier();
                Piece bottomLeft = board[x - 1][y + 1].getOccupier();
                Piece bottomRight = board[x + 1][y + 1].getOccupier();
                if (
                        (topLeft != null && topLeft.isKing()) && !topLeft.getColour().equals(piece.getColour()) ||
                                (topRight != null && topRight.isKing()) && !topRight.getColour().equals(piece.getColour()) ||
                                (bottomLeft != null && bottomLeft.isKing()) && !bottomLeft.getColour().equals(piece.getColour()) ||
                                (bottomRight != null && bottomRight.isKing() && !bottomRight.getColour().equals(piece.getColour()))
                        ) {

                    result = true;
                } else {
                    result = false;
                }
            }
        }
        else{
            result = false;
        }
        return result;
    }

    public static boolean isInMiddleSide(int x){

        return ( x > 2 && x <5 );
    }

    public static boolean isInHighSide(int x){

        return ( x >= 5 );
    }


    public static boolean isInLowerSide(int x){

        return ( x <= 2 );
    }


    public static boolean isNearOneWhite(int x, int y, Spot[][] board){


        if(x > 6){

            return false;
        }

        if ( y < 1 ) {

            if(board[x+1][y+1].getOccupier() != null && board[x+1][y+1].getOccupier().isWhite() ){

                return true;
            }

            return false;


        }
        else if( y > 6 ){

            if(board[x+1][y-1].getOccupier() != null && board[x+1][y-1].getOccupier().isWhite() ){

                return true;
            }

            return false;
        }
        else{

            /*
            * if there are only one white near
            * */
            if( ( ( board[x+1][y-1].getOccupier() != null && board[x+1][y-1].getOccupier().isWhite() ) && ( board[x+1][y+1].getOccupier() == null || board[x+1][y+1].getOccupier().isBlack()) ) || (  (board[x+1][y-1].getOccupier() == null || board[x+1][y-1].getOccupier().isBlack()) && ( board[x+1][y+1].getOccupier() != null && board[x+1][y+1].getOccupier().isWhite()) )){

                return true;
            }
            return false;
        }

    }

    public static boolean isNearMoreThanOneWhite(int x, int y, Spot[][] board){


        if(x > 6){

            return false;
        }
        if ( y < 1 ) {


            return false;


        }
        else if( y > 6 ){

            return false;
        }
        else{

            /*
            * if there are only one white near
            * */
            if( ( ( board[x+1][y-1].getOccupier() != null && board[x+1][y-1].getOccupier().isWhite() ) && ( board[x+1][y+1].getOccupier() != null && board[x+1][y+1].getOccupier().isWhite()) ) ){

                return true;
            }
            return false;
        }
    }


    public static boolean isNearOneBlack(int x, int y, Spot[][] board){


        if(x < 1){

            return false;
        }
        if ( y < 1 ) {

            if(board[x-1][y+1].getOccupier() != null && board[x-1][y+1].getOccupier().isWhite() ){

                return true;
            }

            return false;


        }
        else if( y > 6 ){

            if(board[x-1][y-1].getOccupier() != null && board[x-1][y-1].getOccupier().isWhite() ){

                return true;
            }

            return false;
        }
        else{

            /*
            * if there are only one white near
            * */
            if( ( ( board[x-1][y-1].getOccupier() != null && board[x-1][y-1].getOccupier().isWhite() ) && ( board[x-1][y+1].getOccupier() == null || board[x-1][y+1].getOccupier().isBlack()) ) || (  (board[x-1][y-1].getOccupier() == null || board[x-1][y-1].getOccupier().isBlack()) && ( board[x-1][y+1].getOccupier() != null && board[x-1][y+1].getOccupier().isWhite()) )){

                return true;
            }
            return false;
        }
    }

    public static boolean isNearMoreThanOneBlack(int x, int y, Spot[][] board){



            if (x < 1) {

                return false;
            }
            if (y < 1) {


                return false;


            } else if (y > 6) {

                return false;
            } else {

            /*
            * if there are only one white near
            * */
                if (((board[x - 1][y - 1].getOccupier() != null && board[x - 1][y - 1].getOccupier().isWhite()) && (board[x - 1][y + 1].getOccupier() != null && board[x - 1][y + 1].getOccupier().isWhite()))) {

                    return true;
                }
                return false;
            }


    }

    public static boolean isNearOneOpponent(int x, int y, Spot[][] board, Piece piece){

        if(piece.isWhite()){

            return Utils.isNearOneBlack(x,y,board);
        }
        else{

            return Utils.isNearOneWhite(x,y,board);
        }

    }

    public static boolean isNearMoreThanOneOpponent(int x, int y, Spot[][] board, Piece piece){


        if(piece.isWhite()){

            return Utils.isNearMoreThanOneBlack(x,y,board);
        }
        else{

            return Utils.isNearMoreThanOneWhite(x,y,board);
        }
    }


    public static boolean isKingGoingNearOppositeKing(int x, int y, Spot[][] board, Piece piece){
        boolean result;
        if(piece.isKing()) {
            if (x == 0 || x == 7 || y == 0 || y == 7) {
                result = false;
            } else {
                Piece topLeft = board[x - 1][y - 1].getOccupier();
                Piece topRight = board[x + 1][y - 1].getOccupier();
                Piece bottomLeft = board[x - 1][y + 1].getOccupier();
                Piece bottomRight = board[x + 1][y + 1].getOccupier();
                if (
                        (topLeft != null && topLeft.isKing()) && !topLeft.getColour().equals(piece.getColour()) ||
                                (topRight != null && topRight.isKing()) && !topRight.getColour().equals(piece.getColour()) ||
                                (bottomLeft != null && bottomLeft.isKing()) && !bottomLeft.getColour().equals(piece.getColour()) ||
                                (bottomRight != null && bottomRight.isKing() && !bottomRight.getColour().equals(piece.getColour()))
                        ) {

                    result = true;
                } else {
                    result = false;
                }
            }
        }
        else{
            result = false;
        }
        return result;
    }
}
