package game.model;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;

/**
 * Created by enry8 on 30/04/16.
 */
public class Player {
    ArrayList<Piece> piece = new ArrayList<Piece>();
    String algorithm = "default";
    String name;
    String colour;
    Integer eatenPieces = 0;

    public Player(String algorithm, String name, String colour) {
        this.piece = piece;
        this.algorithm = algorithm;
        this.name = name;
        this.colour = colour;
        int j = 1, k = 0;
        Boolean justChanged = true;
        if(this.colour.equals("w")){
            k+=5;
        }
        for(int i=0; i<12; i++){
            if(k%2 == 0){
                if(justChanged==true){
                    justChanged = false;
                    j=1;
                }
            }
            else{
                if(justChanged==true){
                    justChanged = false;
                    j=0;
                }
            }
            piece.add(new Piece(colour+Integer.toString(i), false, 1, k, j));
            //= colour+Integer.toString(bCount);
            j+=2;
            if(j>=8){
                k++;
                justChanged=true;
            }
        }
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
