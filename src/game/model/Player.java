package game.model;

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

            pieces.add(new Piece(colour+Integer.toString(pieceCount), colour, 1, row, col));
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

    public Boolean hasLost(){
        if(this.eatenPieces == 12){
            return true;
        } //else
        return false;
    }

    public void move(Board board) throws CloneNotSupportedException, IOException {
        String[] move = null;
        Boolean can = false;
        Piece singlePiece = null;
        do {
            if (this.algorithm.equals("human")) {
                move = this.readMove();
            } else {
                MiniMaxTree tree = new MiniMaxTree(board, defaultDepth, this.getName());
                move = tree.decideMove().split(" "); // l'array contiene il nome della pedina e la direzione da seguire
            }
            singlePiece = getPieceByName(move[0]);
            can = isAValidMove(board.getBoard(), singlePiece, move[1]);
        }while(!can);
        // all checks are passed -> action is possible
        if(move[1].indexOf("move") > -1){ // more efficient than method String.contains
            singlePiece.move(board.getBoard(), move[1]);
            this.checkIfBecomeKing(singlePiece, board);
        }
        else if(move[1].indexOf("capture") > -1){
            Piece eatenPiece = singlePiece.capture(move[1], board.getBoard());
            board.getOtherPlayer(this).getPieceList().remove(eatenPiece);
            board.getOtherPlayer(this).incrEatenPieces();
            mustEatAgain(board, singlePiece);
            this.checkIfBecomeKing(singlePiece, board);
        }
    }

    /* the following function reads the user input and checks if it is correct*/
    private String[] readMove() throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String[] move = null;
        Boolean correct = false;
        do {
            System.out.println("Choose your movement");
            move = br.readLine().split(" ");
            if(move.length>1) { // per evitare comandi che contengono una sola parola -> errati
                if ((move[1].indexOf("move") > -1) || (move[1].indexOf("capture") > -1))
                    if ((this.colour.equals("b") && move[0].indexOf("b") > -1) || (this.colour.equals("w") && move[0].indexOf("w") > -1)) // mossa ordinata dal giocatore alle proprie pedine
                        correct = true;
            }
        }while(!correct);
        return move;
    }

    private Boolean isAValidMove(Spot[][] board, Piece piece, String move){
        Boolean can = false;
        if((move.indexOf("move") > -1) && (piece.canMove(board, move, 1))
            || ((move.indexOf("capture") > -1) && (piece.canCapture(board, move)))){
            can = true;
        }
        // controlla se è possibile mangiare e di conseguenza obbligatorio
        if((move.indexOf("capture") == -1) && this.mustEat(board)){
            System.out.println("Not a valid move. One of these pieces must eat!");
            can = false;
        }
        return can;
    }

    private Boolean mustEat(Spot[][] board){
        Boolean must = false;
        for(Piece piece : this.getPieceList()){
            if(piece.canCapture(board, "captureLeft") ||  piece.canCapture(board, "captureRight")){
                must = true;
                System.out.println(piece.getName() + " can eat.");
            }
            if((piece instanceof King) && (piece.canCapture(board, "captureDownLeft") ||  piece.canCapture(board, "captureDownRight"))){
                System.out.println(piece.getName() + " can eat.");
                must = true;
            }
        }
        return must;
    }

    private void mustEatAgain(Board board, Piece piece) throws IOException {
        String[] move = null;
        while((piece.canCapture(board.getBoard(), "captureLeft") ||  piece.canCapture(board.getBoard(), "captureRight"))
        || ((piece instanceof King) && (piece.canCapture(board.getBoard(), "captureDownLeft") ||  piece.canCapture(board.getBoard(), "captureDownRight")))){
            // ripetuto finchè la pedina può mangiare
            board.printBoard();
            System.out.println(piece.getName() + " must eat again. Choose your movement.");
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            move = br.readLine().split(" ");
            if(move[1].indexOf("capture") > -1) {
                if ((this.colour.equals("b") && move[0].indexOf("b") > -1) ||
                (this.colour.equals("w") && move[0].indexOf("w") > -1)) {
                    if((move[1].indexOf("capture") > -1) && (piece.canCapture(board.getBoard(), move[1]))){
                        Piece eatenPiece = piece.capture(move[1], board.getBoard());

                        board.getOtherPlayer(this).getPieceList().remove(eatenPiece);
                        board.getOtherPlayer(this).incrEatenPieces();
                    }
                }
            }
        }
    }

    private Boolean checkIfBecomeKing(Piece piece, Board board) throws IOException {
        Boolean become = false;
        if(!(piece instanceof King) && ((piece.getColour().equals("b") && piece.getRowPosition() == 7) ||
            (piece.getColour().equals("w") && piece.getRowPosition() == 0))){
            King king = new King(piece);
            this.getPieceList().remove(piece);
            this.getPieceList().add(king);
            board.getBoard()[king.getRowPosition()][king.getColPosition()].setOccupier(king);
            System.out.println(king.getName());
            mustEatAgain(board, piece);
            become = true;
        }
        return become;
    }

}
