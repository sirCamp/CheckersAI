package game.model;

/**
 * Created by stefano on 30/04/16.
 */

public class BigPiece extends Piece{

    public BigPiece(String name, Boolean eaten, Integer value, Integer colPosition, Integer rowPosition){

        super(name, eaten, value, colPosition, rowPosition);
    }

    @Override
    public Boolean canMove(Spot[][] board, String direction) {
        return super.canMove(board, direction);
    }

    @Override
    public Spot[][] move(String direction, Spot[][] board) {
        return super.move(direction, board);
    }

    @Override
    public String colAlter(String direction) {
        return super.colAlter(direction);
    }

    @Override
    public String rowAlter(String direction) {
        return super.rowAlter(direction);
    }

}
