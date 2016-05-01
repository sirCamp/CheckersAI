package game.model;

/**
 * Created by stefano on 30/04/16.
 */

public class BigPiece extends Piece{

    public BigPiece(String name, String colour, Boolean eaten, Integer value, Integer colPosition, Integer rowPosition){

        super(name, colour, eaten, value, colPosition, rowPosition);
    }

    @Override
    public Boolean canMove(Spot[][] board, String direction, Integer rowAlt, Integer colAlt) {
        return super.canMove(board, direction, rowAlt, colAlt);
    }

    @Override
    public Spot[][] move(Spot[][] board, String direction) {
        return super.move(board, direction);
    }

    @Override
    public Integer colAlter(String direction) {
        return super.colAlter(direction);
    }

    @Override
    public Integer rowAlter(String direction) {
        return super.rowAlter(direction);
    }

    @Override
    public Boolean canCapture(String direction, Spot[][] board, Boolean isPlayer) {
        return super.canCapture(direction, board, isPlayer);
    }

    @Override
    public Spot[][] capture(String direction, Spot[][] board, boolean isPlayer) {
        return super.capture(direction, board, isPlayer);
    }
}
