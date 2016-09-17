package game.model;

public class Piece implements Cloneable{

    private String name;
    private Integer rowPosition;
    private Integer colPosition;
    private String colour;

    public Piece(String name, String colour, Integer rowPosition, Integer colPosition) {
        this.name = name;
        this.colour = colour;
        this.rowPosition = rowPosition;
        this.colPosition = colPosition;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Piece p = (Piece) super.clone();
        p.name = this.name;
        p.colour = this.colour;
        p.rowPosition = this.rowPosition;
        p.colPosition = this.colPosition;
        return p;
    }

    public String getColour() {
        return colour;
    }

    public Integer getColPosition() {
        return colPosition;
    }

    void setColPosition(Integer colPosition) {
        this.colPosition = colPosition;
    }

    public Integer getRowPosition() {
        return rowPosition;
    }

    void setRowPosition(Integer rowPosition) {
        this.rowPosition = rowPosition;
    }

    public String getName() {
        return name;
    }

    Integer rowAlter(String direction){
        Integer alter;
        if(this.colour.equals("b")){
            alter = 1;
        }else{
            alter = -1;
        }
        return alter;
    }

    Integer colAlter(String direction){ // left and right are absolute directions in the board: they are the same for each player, they are referred to the board orientation
        Integer alter = 0;
        if(direction.equals("moveLeft") || direction.equals("captureLeft")){
            alter = -1;
        } else if(direction.equals("moveRight")|| direction.equals("captureRight")){
            alter = 1;
        }
        return alter;
    }

    public Boolean canMove(Spot[][] board, String direction, Integer eating){
        Integer newRow = this.getRowPosition() +  (this.rowAlter(direction)*eating); //move = x, 2x if the piece is moving to eat
        Integer newCol = this.getColPosition() + (this.colAlter(direction)*eating); //move = y, 2y if the piece is moving to eat
        Boolean can = false;
        if((newCol < 0) || (newCol > 7)){
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

    public Spot[][] move(Spot[][] board, String direction){
        Integer newRow = this.rowPosition +  this.rowAlter(direction); // x movement
        Integer newCol = this.colPosition + this.colAlter(direction); // y movement
        board[newRow][newCol].setOccupier(board[this.rowPosition][this.colPosition].getOccupier());
        board[this.rowPosition][this.colPosition].setOccupier(null);
        this.rowPosition = newRow;
        this.colPosition = newCol;
        return board;
    }

    public Boolean canCapture(Spot[][] board, String direction) {
        Boolean can = false;
        Integer newRow = this.rowPosition + this.rowAlter(direction);
        Integer newCol = this.colPosition + this.colAlter(direction);
        boolean victimExists = false;
        if (Board.inBounds(newRow, newCol) && board[newRow][newCol] != null
                && !(board[newRow][newCol].getOccupier() == null)
                && !(board[newRow][newCol].getOccupier().getColour().equals(this.colour)) // piece of different colour
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

    public Piece capture(String direction, Spot[][] board) {
        Integer newRow = this.rowPosition +  this.rowAlter(direction); // x movement
        Integer newCol = this.colPosition + this.colAlter(direction); // y movement
        Piece eatenPiece = board[newRow][newCol].getOccupier();
        board[newRow][newCol].setOccupier(null);
        // change killer position
        Integer beyondRow = newRow + this.rowAlter(direction); // position x beyond the piece that would be eaten
        Integer beyondCol = newCol + this.colAlter(direction); // position y beyond the piece that would be eaten
        board[beyondRow][beyondCol].setOccupier(this);
        board[this.rowPosition][this.colPosition].setOccupier(null);
        this.rowPosition = beyondRow;
        this.colPosition = beyondCol;
        return eatenPiece;
    }

    public boolean isKing(){ return (this instanceof Piece); }

    public boolean isWhite(){ return  this.colour.equals("w"); }

    public boolean isBlack(){ return  this.colour.equals("b"); }

}
