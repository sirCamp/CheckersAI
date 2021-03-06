package game.model;

import game.moveGenerator.MiniMaxTree;
import game.utils.CSVExporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Player implements Cloneable{

    private ArrayList<Piece> pieces = new ArrayList<>();
    private String algorithm = "human";
    private String name;
    private String colour;
    private Integer eatenPieces = 0;
    private Integer defaultDepth;

    public Player(String algorithm, String name, String colour){
        this(algorithm, name, colour, 3);
    }

    public Player(String algorithm, String name, String colour, Integer defaultDepth) {
        this.algorithm = algorithm;
        this.name = name;
        this.colour = colour;
        this.defaultDepth = defaultDepth;
        int col = 1, row = 0;
        Boolean isNewRow = true;
        if(this.colour.equals("w")){
            row+=5;
        }
        for(int pieceCount =0; pieceCount<12; pieceCount++){
            /* base on the row number, even or odd, pieces are located on even or odd position.
               This is made considering the variable isNewRow */
            if(isNewRow){
                if(row%2 == 0){
                    isNewRow = false;
                    col=1;
                }
                else{
                    isNewRow = false;
                    col=0;
                }
            }
            pieces.add(new Piece(colour+Integer.toString(pieceCount), colour, row, col));
            // colour+Integer.toString(pieceCount) => piece name: b1, w3, ...
            col+=2;
            if(col>=8){ // when a row initialization is finished, next one has to be done
                row++;
                isNewRow=true;
            }
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Player p = (Player) super.clone();
        p.eatenPieces = this.eatenPieces;
        p.algorithm = this.algorithm;
        p.name = this.name;
        p.colour = this.colour;
        p.defaultDepth = this.defaultDepth;
        p.pieces = new ArrayList<>(this.pieces.size());
        for (Piece piece: this.pieces) p.pieces.add((Piece) piece.clone());
        return p;
    }

    public ArrayList<Piece> getPieceList() {
        return pieces;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getName() {
        return name;
    }

    public String getColour() { return colour; }

    public Integer getEatenPieces() {
        return eatenPieces;
    }

    public void incrEatenPieces() {
        this.eatenPieces++;
    }

    public Piece getPieceByName(String name){
        int i=0; Boolean found = false;
        while(i<pieces.size()){
            if(pieces.get(i).getName().equals(name)){
                return pieces.get(i);
            }else{
                i++;
            }
        }
        return null;
    }

    public void play(Board board, Integer roundCounter) throws CloneNotSupportedException, IOException {
        String[] move;
        if (this.algorithm.equals("human")) {
            playerPlay(board, roundCounter);
        }else{ // computer
            Long start = System.currentTimeMillis();
            computerPlay(board, roundCounter);
            Long total = System.currentTimeMillis() - start;
            CSVExporter.CONTAINER+=","+total.toString()+";\n";
            //System.out.println("Spent Time: "+ total.toString());
        }
    }

    private void playerPlay(Board board, Integer roundCounter) throws IOException, CloneNotSupportedException {
        String[] move;
        Piece piece;
        MiniMaxTree tree = new MiniMaxTree(board, 2, this, "1-pc-pruning");
        String possibleMove = tree.decideMove(roundCounter);
        if(possibleMove != null) {
            Boolean mustEat = this.mustEat(board.getBoard(), true);
            do {
                move = this.readMove();
                piece = getPieceByName(move[0]);
            } while (!isAValidMove(board.getBoard(), piece, move[1], mustEat));
            // all checks are passed -> action will be now performed
            if (move[1].indexOf("move") > -1) { // more efficient than method String.contains
                piece.move(board.getBoard(), move[1]);
                this.checkIfBecomeKing(piece, board);
            } else if (move[1].indexOf("capture") > -1) {
                this.performCapture(board, piece, move[1]);
                Boolean checkAgain = true; // if it's possible to eat again
                do {
                    King king = this.checkIfBecomeKing(piece, board);
                    if (king != null) {
                        checkAgain = mustEatAgain(board, king);
                    } else {
                        checkAgain = mustEatAgain(board, piece); // in which we control if there's a new king
                    }
                } while (checkAgain);
            }
        }else{
            this.eatenPieces = 12;
        }
    }

    public void performCapture(Board board, Piece piece, String move){
        String eatenPiece = piece.capture(move, board.getBoard());
        board.getOtherPlayer(this).getPieceList().remove(board.getOtherPlayer(this).getPieceByName(eatenPiece));
        board.getOtherPlayer(this).incrEatenPieces();
    }

    public Boolean hasLost(){
        if(this.eatenPieces == 12 ){
            return true;
        } //else
        return false;
    }

    private void computerPlay(Board board, Integer roundCounter) throws IOException, CloneNotSupportedException {
        MiniMaxTree tree = new MiniMaxTree(board, this.defaultDepth, this, this.getAlgorithm());
        String possibleMove = tree.decideMove(roundCounter);
        if(possibleMove != null) {
            String[] move = possibleMove.split(" "); // the array contains the piece name and its move-direction
            Integer size = move.length;
            int j = size - 1;
            int i = size - 2;
            String temp = "";
            while (i >= 0 && j >= 1) {
                temp += move[i] + " " + move[j]+" ";
                System.out.println("PC: " + move[i] + " " + move[j]);
                Piece piece = getPieceByName(move[i]);
                if (move[j].indexOf("move") > -1) { // more efficient than method String.contains
                    piece.move(board.getBoard(), move[j]);
                } else if (move[j].indexOf("capture") > -1) {
                    performCapture(board, piece, move[j]);
                }
                this.checkIfBecomeKing(piece, board);
                j = j - 2;
                i = i - 2;
            }

            CSVExporter.CONTAINER+=temp;
        }else{
            this.eatenPieces = 12;
        }
    }

    public Boolean pcMustEatAgain(Board board, Piece piece) {
        return (piece.canCapture(board.getBoard(), "captureLeft") || piece.canCapture(board.getBoard(), "captureRight"))
                || ((piece instanceof King) && (piece.canCapture(board.getBoard(), "captureDownLeft") || piece.canCapture(board.getBoard(), "captureDownRight")));
    }

    /* the following function reads the user input and checks if it is correct*/
    private String[] readMove() throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String[] move;
        Boolean correct = false;
        do {
            System.out.println("Choose your movement:");
            move = br.readLine().split(" ");
            if(move.length>1) { // to avoid wrong command that contain only one word
                if ((move[1].indexOf("move") > -1) || (move[1].indexOf("capture") > -1))
                    if(this.getPieceByName(move[0]) != null) // move ordered by the player to his pieces
                        correct = true;
            }
        }while(!correct);
        return move;
    }

    private Boolean isAValidMove(Spot[][] board, Piece piece, String move, Boolean mustEat){
        // it controls if it is possible to eat and consequently mandatory
        // if it isn't possibile, it controls if the movement is correct
        return (((move.indexOf("move") > -1) && (piece.canMove(board, move, 1)) && !mustEat) ||
                ((move.indexOf("capture") > -1) && (piece.canCapture(board, move))) && mustEat);
    }

    public Boolean mustEat(Spot[][] board, Boolean isHuman){
        Boolean must = false;
        for(Piece piece : this.getPieceList()){
            if((piece.canCapture(board, "captureLeft") || piece.canCapture(board, "captureRight")) ||
                ((piece instanceof King) && (piece.canCapture(board, "captureDownLeft") ||  piece.canCapture(board, "captureDownRight")))){
                must = true;
                if(isHuman)
                    System.out.println(piece.getName() + " can eat.");
            }
        }
        return must;
    }

    private Boolean mustEatAgain(Board board, Piece piece) throws IOException {
        String[] move;
        Boolean done = false;
        Boolean checkAgain = true;
        if((piece.canCapture(board.getBoard(), "captureLeft") ||  piece.canCapture(board.getBoard(), "captureRight")) ||
            ((piece instanceof King) && (piece.canCapture(board.getBoard(), "captureDownLeft") || piece.canCapture(board.getBoard(), "captureDownRight")))){
            // repeated while piece can eat (concatenated eating)
            board.printBoard();
            System.out.println(piece.getName() + " must eat again.");
            do{
                move = this.readMove();
                if((move[1].indexOf("capture") > -1) && ((move[0]).equals(piece.getName())) && (piece.canCapture(board.getBoard(), move[1]))){
                    performCapture(board, piece, move[1]);
                    done = true;
                }else{
                    System.out.println("Some mistakes encountered. Please re-write your movement.");
                }
            }while(!done);
        }else{
            checkAgain = false;
        }
        return checkAgain;
    }

    public King checkIfBecomeKing(Piece piece, Board board) throws IOException {
        King aragorn = null;
        if(!(piece instanceof King) && ((piece.getColour().equals("b") && piece.getRowPosition() == 7) ||
            (piece.getColour().equals("w") && piece.getRowPosition() == 0))){
            King king = new King(piece);
            board.getBoard()[piece.getRowPosition()][piece.getColPosition()].setOccupier(null);
            ArrayList<Piece> newList = new ArrayList<>(this.pieces.size()-1);
            for (Piece oldPiece: this.getPieceList()){ // it creates a new list, removing the old piece: Garbage collector is too much slow
                if(!oldPiece.getName().equals(piece))
                    newList.add(oldPiece);
            }
            this.getPieceList().remove(piece);
            this.pieces = newList;
            this.getPieceList().add(king);
            board.getBoard()[king.getRowPosition()][king.getColPosition()].setOccupier(king);
            aragorn = king;
        }
        return aragorn;
    }

    public boolean isWhite(){ return this.colour.equals("w"); }

    public boolean isBlack(){ return this.colour.equals("b"); }

}
