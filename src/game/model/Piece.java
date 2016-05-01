package game.model;

/**
 * Created by enry8 on 30/04/16.
 */
public class Piece {

    String name;
    Boolean eaten = false;
    Integer value = 0;
    Integer rowPosition;
    Integer colPosition;

    public Piece(String name, Boolean eaten, Integer value, Integer rowPosition, Integer colPosition) {
        this.name = name;
        this.eaten = eaten;
        this.value = value;
        this.colPosition = colPosition;
        this.rowPosition = rowPosition;
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
        if (eaten != null ? !eaten.equals(piece.eaten) : piece.eaten != null) return false;
        return value != null ? value.equals(piece.value) : piece.value == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (eaten != null ? eaten.hashCode() : 0);
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

    public Boolean getEaten() {
        return eaten;
    }

    public void setEaten(Boolean eaten) {
        this.eaten = eaten;
    }

    public Boolean canMove(Spot[][] board, String direction, String colour, Integer rowAlt, Integer colAlt){
        Boolean can = false;
        if(direction.equals("moveLeft")){
            if((colour.equals("b") && this.colPosition == 7) ||
                (colour.equals("w") && this.colPosition == 0)){ // bordo sx
                can = false;
            }
            else {
                if (Board.inBounds((this.rowPosition + rowAlt), (this.colPosition + colAlt)) &&
                        board[this.rowPosition + rowAlt][this.colPosition + colAlt] != null &&
                        board[this.rowPosition + rowAlt][this.colPosition + colAlt].getOccupier().equals(null)) {
                    can = true;
                }
            }
        } else if(direction.equals("moveRight")) {
            if ((colour.equals("w") && this.colPosition == 7) ||
                    (colour.equals("b") && this.colPosition == 0)) { // bordo sx
                can = false;
            } else {
                if (Board.inBounds((this.rowPosition + rowAlt), (this.colPosition + colAlt)) &&
                        board[this.rowPosition + rowAlt][this.colPosition + colAlt] != null &&
                        board[this.rowPosition + rowAlt][this.colPosition + colAlt].getOccupier().equals(null)) {
                    can = true;
                }
            }
        }
        return can;
    }

    public Spot[][] move(Spot[][] board, String direction, String colour){
        Integer rowAlt = this.rowAlter(colour, direction); //spostamento x
        Integer colAlt = this.colAlter(colour, direction); //spostamento y
        if(this.canMove(board, direction, colour, rowAlt, colAlt)){
            Integer newRow = this.rowPosition + rowAlt;
            Integer newCol = this.colPosition + colAlt;
            board[newRow][newCol].setOccupier(board[this.rowPosition][this.colPosition].getOccupier());
            board[this.rowPosition][this.colPosition].setOccupier(null);
            this.rowPosition = newRow;
            this.colPosition = newCol;
        }
        return board;
    }

    public Integer colAlter(String colour, String direction){
        Integer alter = 0;
        if(colour.equals("b")){
            alter = 1;
        }else{
            alter = -1;
        }
        return alter;
    }

    public Integer rowAlter(String colour, String direction){
        Integer alter = 0;
        if(colour.equals("b")){
            if(direction.equals("moveLeft")){
                alter = 1;
            } else if(direction.equals("moveRight")){
                alter = -1;
            }
        } else if(colour.equals("w")) {
            if(direction.equals("moveLeft")){
                alter = -1;
            } else if(direction.equals("moveRight")){
                alter = 1;
            }
        }
        return alter;
    }

}
