package game.model;

public class King extends Piece implements Cloneable{

    public King(Piece p){
        super(p.getColour()+p.getName(), p.getColour(), p.getRowPosition(), p.getColPosition());
    } 

    public Board copy() throws CloneNotSupportedException {
        return (Board) this.clone();
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
    public Integer colAlter(String direction) { // left and right are absolute directions in the board: they are the same for each player, they are referred to the board orientation
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
        Integer newRow = this.getRowPosition() +  (this.rowAlter(direction)*eating); //move = x, 2x if the piece is moving to eat
        Integer newCol = this.getColPosition() + (this.colAlter(direction)*eating); //move = y, 2y if the piece is moving to eat
        Boolean can = false;
        if((( direction.equals("moveLeft") || direction.equals("moveDownLeft"))  && (this.getColPosition() < 0))
            || (( direction.equals("moveRight") || direction.equals("moveDownRight"))  && (this.getColPosition() > 7))){
                can = false;
            }
            else {
                if (Board.inBounds((newRow), (newCol)) &&
                        board[newRow][newCol] != null &&
                        board[newRow][newCol].getOccupier() == (null)) {
                    can = true;
                }
            }
        return can;
    }

    @Override
    public Spot[][] move(Spot[][] board, String direction) {
        Integer newRow = this.getRowPosition() +  this.rowAlter(direction); // x movement
        Integer newCol = this.getColPosition() + this.colAlter(direction); // y movement
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
                && !(board[newRow][newCol].getOccupier().getColour().equals(this.getColour())) // piece of different colour
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
    public Piece capture(String direction, Spot[][] board) {
        Integer newRow = this.getRowPosition() + this.rowAlter(direction); // x movement
        Integer newCol = this.getColPosition() + this.colAlter(direction); // y movement
        // kill victim
        Piece eatenPiece = board[newRow][newCol].getOccupier();
        board[newRow][newCol].setOccupier(null);
        // change killer position
        Integer beyondRow = newRow + this.rowAlter(direction); // position x beyond the piece that would be eaten
        Integer beyondCol = newCol + this.colAlter(direction); // position y beyond the piece that would be eaten
        board[beyondRow][beyondCol].setOccupier(this);
        board[this.getRowPosition()][this.getColPosition()].setOccupier(null);
        this.setRowPosition(beyondRow);
        this.setColPosition(beyondCol);
        return eatenPiece;
    }
}
