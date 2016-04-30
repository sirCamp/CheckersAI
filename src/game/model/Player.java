package game.model;

import java.util.ArrayList;

/**
 * Created by enry8 on 30/04/16.
 */
public class Player {
    ArrayList<Piece> piece = new ArrayList<Piece>();
    String algorithm = "default";
    String name;
    Integer eatenPieces = 0;

    public Player(ArrayList<Piece> piece, String algorithm, String name) {
        this.piece = piece;
        this.algorithm = algorithm;
        this.name = name;
    }

    public ArrayList<Piece> getPiece() {
        return piece;
    }

    public void setPiece(ArrayList<Piece> piece) {
        this.piece = piece;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEatenPieces() {
        return eatenPieces;
    }

    public void incrEatenPieces() {
        this.eatenPieces++;
    }

    public Boolean newBigPiece() {
        if (eatenPieces > 0) {
            this.eatenPieces--;
            return true;
        } //else
            return false;
    }

    public Boolean hasLost(){
        if(this.eatenPieces == 12){
            return true;
        } //else
        return false;
    }
}
