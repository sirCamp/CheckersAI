package game.model;

import java.util.Arrays;

/**
 * Created by enry8 on 30/04/16.
 */
public class Piece implements Cloneable{

    private String name;
    private Integer value = 0;
    private Integer rowPosition;
    private Integer colPosition;
    private String colour;

    public Piece(String name, String colour, Integer value, Integer rowPosition, Integer colPosition) {
        this.name = name;
        this.colour = colour;
        this.value = value;
        this.rowPosition = rowPosition;
        this.colPosition = colPosition;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Piece p = (Piece) super.clone();
        p.name = new String(this.name);
        p.value = new Integer(this.value);
        p.rowPosition = new Integer(this.rowPosition);
        p.colPosition = new Integer(this.colPosition);
        return p;
    }

    public Board copy() throws CloneNotSupportedException {
        return (Board) this.clone();
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Integer getColPosition() {
        return colPosition;
    }

    public void setColPosition(Integer colPosition) {
        this.colPosition = colPosition;
    }

    public Integer getRowPosition() {
        return rowPosition;
    }

    public void setRowPosition(Integer rowPosition) {
        this.rowPosition = rowPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece)) return false;

        Piece piece = (Piece) o;

        if (name != null ? !name.equals(piece.name) : piece.name != null) return false;
        return (value != null ? value.equals(piece.value) : piece.value == null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }


    public Integer rowAlter(String direction){
        Integer alter = 0;
        if(this.colour.equals("b")){
            alter = 1;
        }else{
            alter = -1;
        }
        return alter;
    }

    public Integer colAlter(String direction){ // left e right si intendono rispetto alla scacchiera
        Integer alter = 0;
        if(direction.equals("moveLeft") || direction.equals("captureLeft")){
            alter = -1;
        } else if(direction.equals("moveRight")|| direction.equals("captureRight")){
            alter = 1;
        }
        return alter;
    }

    public Boolean canMove(Spot[][] board, String direction, Integer eating){
        Integer newRow = this.rowPosition +  (this.rowAlter(direction)*eating); //spostamento x, 2x se si sta mangiando
        Integer newCol = this.colPosition + (this.colAlter(direction)*eating); //spostamento y, 2y se si sta mangiando
        Boolean can = false;
        //System.out.println(this.getName() + " d "+ direction + " p " + this.colPosition);
        if(((direction.equals("moveLeft")) && (this.colPosition <= 0))
            || ((direction.equals("moveRight")) && (this.colPosition >= 7))){ // bordo sx
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
        Integer newRow = this.rowPosition +  this.rowAlter(direction); //spostamento x
        Integer newCol = this.colPosition + this.colAlter(direction); //spostamento y
        board[newRow][newCol].setOccupier(board[this.rowPosition][this.colPosition].getOccupier());
        board[this.rowPosition][this.colPosition].setOccupier(null);
        this.rowPosition = newRow;
        this.colPosition = newCol;
        return board;
    }


    public Boolean canCapture(Spot[][] board, String direction) {
        Boolean can = false;
        Integer newRow = this.rowPosition + this.rowAlter(direction );
        Integer newCol = this.colPosition + this.colAlter(direction);

        boolean victimExists = false;
        if (Board.inBounds(newRow, newCol) && board[newRow][newCol] != null
                && !(board[newRow][newCol].getOccupier() == null)
                && !(board[newRow][newCol].getOccupier().getColour().equals(this.colour)) //colore pedina diverso
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
        Integer newRow = this.rowPosition + this.rowAlter(direction); //spostamento x
        Integer newCol = this.colPosition + this.colAlter(direction); //spostamento y

        // kill victim

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

    public Boolean canCaptureAgain(Spot[][] board) {

        // se il giocatore ha mangiato, si controlla se deve mangiare ancora

        Boolean anotherMove = false;
        anotherMove = canCapture(board, "left") || canCapture(board, "right");
        return anotherMove;
    }


}
