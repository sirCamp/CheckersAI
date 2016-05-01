package game.model;

/**
 * Created by stefano on 30/04/16.
 */

public class BigPiece extends Piece{

    public BigPiece(String name, Boolean eaten, Integer value, Integer colPosition, Integer rowPosition){

        super(name, eaten, value, colPosition, rowPosition);
    }

    @Override
    public Boolean canMove(Spot[][] board, String direction, String colour, Integer rowAlt, Integer colAlt) {
        return super.canMove(board, direction, colour, rowAlt, colAlt);
    }

    @Override
    public Spot[][] move(Spot[][] board, String direction, String colour) {
        return super.move(board, direction, colour);
    }

    @Override
    public Integer colAlter(String colour, String direction) {
        return super.colAlter(colour, direction);
    }

    @Override
    public Integer rowAlter(String colour, String direction) {
        return super.rowAlter(colour, direction);
    }

}
