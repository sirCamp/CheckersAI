package game.model;

import com.sun.org.apache.xpath.internal.operations.Bool;
import game.Tree;
import game.moveGenerator.MiniMaxTree;

import java.util.ArrayList;

/**
 * Created by enry8 on 30/04/16.
 */
public class Player {
    private ArrayList<Piece> piece = new ArrayList<Piece>();
    private String algorithm = "default";
    private String name;
    private String colour;
    private Integer eatenPieces = 0;
    private Integer defaultDepth = 3;

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
            piece.add(new Piece(colour+Integer.toString(i), colour, false, 1, k, j));
            //= colour+Integer.toString(bCount);
            j+=2;
            if(j>=8){
                k++;
                justChanged=true;
            }
        }
    }

    public ArrayList<Piece> getPieceList() {
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

    public Piece getSinglePiece(Integer index){
        return piece.get(index);
    }

    public Piece getPieceByName(String name){
        Integer i=0; Boolean found = false;
        while(i<piece.size() && !found){
            if(piece.get(i).getName().equals(name)){
                found = true;
            }else{
                i++;
            }
        }
        return piece.get(i);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Boolean hasLost(){
        if(this.eatenPieces == 12){
            return true;
        } //else
        return false;
    }

    public void move(Board board) throws CloneNotSupportedException {
        MiniMaxTree tree = new MiniMaxTree(board, defaultDepth, this);
        String[] move = tree.decideMove().split(" ");
        Piece singlePiece = getPieceByName(move[0]);
        if(move[1].indexOf("move") > -1){ // more efficient than method String.contains
            singlePiece.move(board.getBoard(), move[1]);
        }
        else if(move[1].indexOf("capture") > -1){
            singlePiece.capture(move[1], board.getBoard(), false);
        }
    }
}
