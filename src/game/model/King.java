package game.model;

/**
 * Created by stefano on 30/04/16.
 */

public class King extends Piece{

    public King(Piece p){
        super(p.getName(), p.getColour(), p.getEaten(), p.getValue(), p.getColPosition(), p.getColPosition());
    }

    @Override
    public Integer rowAlter(String direction){
        Integer alter = 0;
        if(((this.getColour().equals("b")) &&(direction.indexOf("Down")==-1))
                ||((this.getColour().equals("w"))&&(direction.indexOf("Down")>-1))){
            alter = 1;
        }else if(((this.getColour().equals("w")) &&(direction.indexOf("Down")==-1))
                ||((this.getColour().equals("b"))&&(direction.indexOf("Down")>-1))){
            alter = -1;
        }
        return alter;
    }

    @Override
    public Integer colAlter(String direction) {
        Integer alter = 0;
        if(direction.equals("moveLeft") || direction.equals("captureLeft")
                || direction.equals("moveDownLeft") || direction.equals("captureDownLeft")){
            alter = -1;
        } else if(direction.equals("moveRight")|| direction.equals("captureRight")
                || direction.equals("moveDownRight") || direction.equals("captureDownRight")){
            alter = 1;
        }
        return alter;
    }

    @Override
    public Boolean canMove(Spot[][] board, String direction, Integer eating){
        Integer newRow = this.getRowPosition() +  (this.rowAlter(direction)*eating); //spostamento x, 2x se si sta mangiando
        Integer newCol = this.getColPosition() + (this.colAlter(direction)*eating); //spostamento y, 2y se si sta mangiando
        Boolean can = false;
        if((( direction.equals("moveLeft") || direction.equals("moveDownLeft"))  && (this.getColPosition() < 0))
            || (( direction.equals("moveRight") || direction.equals("moveDownRight"))  && (this.getColPosition() > 7))){
                can = false;
            }
            else {
                if (Board.inBounds((newRow), (newCol)) &&
                        board[newRow][newCol] != null &&
                        board[newRow][newCol].getOccupier().equals(null)) {
                    can = true;
                }
            }
        return can;
    }

    @Override
    public Spot[][] move(Spot[][] board, String direction) {
        Integer newRow = this.getRowPosition() +  this.rowAlter(direction); //spostamento x
        Integer newCol = this.getColPosition() + this.colAlter(direction); //spostamento y
        if(this.canMove(board, direction, 1)){
            board[newRow][newCol].setOccupier(board[this.getRowPosition()][this.getColPosition()].getOccupier());
            board[this.getRowPosition()][this.getColPosition()].setOccupier(null);
            this.setRowPosition(newRow);
            this.setColPosition(newCol);
        }
        return board;
    }

    @Override
    public Boolean canCapture(Spot[][] board, String direction) {
        Boolean can = false;
        Integer newRow = this.getRowPosition() + this.rowAlter(direction );
        Integer newCol = this.getColPosition() + this.colAlter(direction);

        boolean victimExists = false;
        if (Board.inBounds(newRow, newCol) && board[newRow][newCol] != null
                && !(board[newRow][newCol].getOccupier() == null)
                && !(board[newRow][newCol].getOccupier().getColour().equals(this.getColour())) //colore pedina diverso
                && !this.canMove(board, direction, 1)){
            victimExists = true;
        }
        Integer beyondRow = newRow + this.rowAlter(direction); // position x beyond the piece that would be eaten
        Integer beyondCol = newCol + this.colAlter(direction); // position y beyond the piece that would be eaten
        Boolean safeLand = Board.inBounds(beyondRow, beyondCol) && this.canMove(board, direction, 2);
        if (victimExists && safeLand){
            can = true;
        }
        return can;
    }

    @Override
    public Spot[][] capture(String direction, Spot[][] board) {
        Integer newRow = this.getRowPosition() + this.rowAlter(direction); //spostamento x
        Integer newCol = this.getColPosition() + this.colAlter(direction); //spostamento y

        // kill victim
        board[newRow][newCol].setOccupier(null); // TODO: gestire i pezzi eaten del player
        // change killer position
        Integer beyondRow = newRow + this.rowAlter(direction); // position x beyond the piece that would be eaten
        Integer beyondCol = newCol + this.colAlter(direction); // position y beyond the piece that would be eaten
        board[beyondRow][beyondCol].setOccupier(this);
        board[this.getRowPosition()][this.getColPosition()].setOccupier(null);
        this.setRowPosition(beyondRow);
        this.setColPosition(beyondCol);
        return board;
    }

    @Override
    public Boolean canCaptureAgain(Spot[][] board) {

        // se il giocatore ha mangiato, si controlla se deve mangiare ancora

        Boolean anotherMove = false;
        anotherMove = canCapture(board, "left") || canCapture(board, "right");
        return anotherMove;
    }
}
