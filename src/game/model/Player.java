package game.model;

import com.sun.org.apache.xpath.internal.operations.Bool;
import game.Tree;
import game.moveGenerator.MiniMaxTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by enry8 on 30/04/16.
 */
public class Player {

    private ArrayList<Piece> pieces = new ArrayList<Piece>();
    private String algorithm = "human";
    private String name;
    private String colour;
    private Integer eatenPieces = 0;
    private Integer defaultDepth = 3;

    public Player(String algorithm, String name, String colour) {
        this.algorithm = algorithm;
        this.name = name;
        this.colour = colour;
        int col = 1, row = 0;
        Boolean isNewRow = true;
        if(this.colour.equals("w")){
            row+=5;
        }
        for(int pieceCount =0; pieceCount<12; pieceCount++){
            /* in base alla riga, pari o dispari, le pedine si disporranno nelle posizioni pari o dispari. Lo si fa invocando*/
            if(isNewRow==true){
                if(row%2 == 0){
                    isNewRow = false;
                    col=1;
                }
                else{
                    isNewRow = false;
                    col=0;
                }
            }

            pieces.add(new Piece(colour+Integer.toString(pieceCount), colour, false, 1, row, col));
            // colour+Integer.toString(pieceCount) =>il nome della pedina: b1, w3, ...
            col+=2;
            if(col>=8){ // finita l'inizializzazione di una riga, si passa alla successiva
                row++;
                isNewRow=true;
            }
        }
    }

    public ArrayList<Piece> getPieceList() {
        return pieces;
    }

    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
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

    public Boolean newKing() {
        if (eatenPieces > 0) {
            this.eatenPieces--;
            return true;
        } //else
            return false;
    }

    public Piece getPieceByName(String name){
        Integer i=0; Boolean found = false;
        while(i<pieces.size() && !found){
            if(pieces.get(i).getName().equals(name)){
                found = true;
            }else{
                i++;
            }
        }
        if(!found)
            return null;
        return pieces.get(i);
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
        String[] move = null;
        if(this.algorithm.equals("human")) {
            move = this.readMove();
        }
        else {
            MiniMaxTree tree = new MiniMaxTree(board, defaultDepth, this);
            move = tree.decideMove().split(" "); // l'array contiene il nome della pedina e la direzione da seguire
        }
        Piece singlePiece = getPieceByName(move[0]);
        if(move[1].indexOf("move") > -1){ // more efficient than method String.contains
            singlePiece.move(board.getBoard(), move[1]);
        }
        else if(move[1].indexOf("capture") > -1){
            singlePiece.capture(move[1], board.getBoard(), false);
        }
    }

    private String[] readMove() {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String[] move = null;
        Boolean correct = false;
        do {
            System.out.println("Choose your movement");
            try {
                move = br.readLine().split(" ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if((move[1].indexOf("move") > -1) || (move[1].indexOf("capture") > -1))
                if((this.colour.equals("b") && move[0].indexOf("b")>-1) || (this.colour.equals("w") && move[0].indexOf("w")>-1))
                    correct = true;
        }while(!correct);

        return move;
    }

}
