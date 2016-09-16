package game.utils;

import game.model.Piece;
import game.model.Spot;

/**
 * Created by stefano on 16/09/16.
 */
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

    public static boolean isInCenter(int x, int y){

        return ( x > 2 && x < 6) && ( y > 2 && y < 6);
    }

    public static boolean isInEdge(int y){

        return (y < 1 || y >= 7);
    }

    public static boolean isInBackrow(int x){

        return ( x < 3 && x > 5);
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
}
