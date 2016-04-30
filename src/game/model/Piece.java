package game.model;

import game.Spot;

/**
 * Created by enry8 on 30/04/16.
 */
public class Piece {
    String name;
    Boolean eaten = false;
    Integer value = 0;
    Integer colPosition;
    Integer rowPosition;

    public Piece(String name, Boolean eaten, Integer value, Integer colPosition, Integer rowPosition) {
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

    public Boolean canMove(Spot[][] board, String direction){
        return false;
    }

    public Spot[][] move(String direction, Spot[][] board){
        return null;
    }

    public String colAlter(String direction){
        return null;
    }

    public String rowAlter(String direction){
        return null;
    }

}
