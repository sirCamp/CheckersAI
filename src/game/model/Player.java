package game.model;

import game.moveGenerator.MiniMaxTree;
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
    private Boolean lost = false;

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
        p.lost = this.lost;
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

    private void incrEatenPieces() {
        this.eatenPieces++;
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

    private void setLost(){
        lost = true;
    }

    public Boolean hasLost(){
        if(this.eatenPieces == 12 || this.lost){
            lost = true;
            return true;
        } //else
        return false;
    }

    public void move(Board board) throws CloneNotSupportedException, IOException {
        String[] move;
        Boolean can;
        Piece singlePiece;
        if (this.algorithm.equals("human")) {
            do {
                move = this.readMove();
                singlePiece = getPieceByName(move[0]);
                can = isAValidMove(board.getBoard(), singlePiece, move[1], this.getAlgorithm().equals("human"));
            } while (!can);
            // all checks are passed -> action is possible
            if (move[1].indexOf("move") > -1) { // more efficient than method String.contains
                singlePiece.move(board.getBoard(), move[1]);
                this.checkIfBecomeKing(singlePiece, board, false);
            } else if (move[1].indexOf("capture") > -1) {
                Piece eatenPiece = singlePiece.capture(move[1], board.getBoard());
                board.getOtherPlayer(this).getPieceList().remove(eatenPiece);
                board.getOtherPlayer(this).incrEatenPieces();
                mustEatAgain(board, singlePiece);
                this.checkIfBecomeKing(singlePiece, board, false);
            }
        }else{ // computer
                MiniMaxTree tree = new MiniMaxTree(board, defaultDepth, this.getName(), this.getAlgorithm());
                String possibleMove = tree.decideMove();
                if(possibleMove == null){
                    System.out.println("No more moves for "+this.getName());
                    this.setLost();
                }else{
                    move = possibleMove.split(" "); // the array contains the piece name and its move-direction
                    computerMove(move, board);
                }
        }
    }

    private void computerMove(String[] move, Board board) throws IOException {
        Integer size = move.length;
        int j = size-1;
        int i = size-2;
        while(i>=0 && j >= 1){
            System.out.println("PC: "+move[i]+" "+move[j]);
            Piece singlePiece = getPieceByName(move[i]);
            if(move[j].indexOf("move") > -1){ // more efficient than method String.contains
                singlePiece.move(board.getBoard(), move[j]);
                this.checkIfBecomeKing(singlePiece, board, false);
            }
            if(move[j].indexOf("capture") > -1){
                Piece eatenPiece = singlePiece.capture(move[j], board.getBoard());
                board.getOtherPlayer(this).getPieceList().remove(eatenPiece);
                board.getOtherPlayer(this).incrEatenPieces();
                this.checkIfBecomeKing(singlePiece, board, false);
            }
            j = j - 2;
            i = i - 2;
        }
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
                    if ((this.colour.equals("b") && move[0].indexOf("b") > -1) || (this.colour.equals("w") && move[0].indexOf("w") > -1)) // move ordered by the player to his pieces
                        correct = true;
            }
        }while(!correct);
        return move;
    }

    private Boolean isAValidMove(Spot[][] board, Piece piece, String move, Boolean isHuman){
        Boolean can = false;
        if((move.indexOf("move") > -1) && (piece.canMove(board, move, 1))
            || ((move.indexOf("capture") > -1) && (piece.canCapture(board, move)))){
            can = true;
        }
        // it controls if it is possible to eat and consequently mandatory
        if((move.indexOf("capture") == -1) && this.mustEat(board, isHuman)){
            System.out.println("Not a valid move. One of these pieces must eat!");
            can = false;
        }
        return can;
    }

    public Boolean mustEat(Spot[][] board, Boolean isHuman){
        Boolean must = false;
        for(Piece piece : this.getPieceList()){
            if(piece.canCapture(board, "captureLeft") ||  piece.canCapture(board, "captureRight")){
                must = true;
                if(isHuman)
                    System.out.println(piece.getName() + " can eat.");
            }
            if((piece instanceof King) && (piece.canCapture(board, "captureDownLeft") ||  piece.canCapture(board, "captureDownRight"))){
                if(isHuman)
                    System.out.println(piece.getName() + " can eat.");
                must = true;
            }
        }
        return must;
    }

    private void mustEatAgain(Board board, Piece piece) throws IOException {
        String[] move;
        while((piece.canCapture(board.getBoard(), "captureLeft") ||  piece.canCapture(board.getBoard(), "captureRight"))
        || ((piece instanceof King) && (piece.canCapture(board.getBoard(), "captureDownLeft") ||  piece.canCapture(board.getBoard(), "captureDownRight")))){
            // repeated while piece can eat (concatenated eating)
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


    public Boolean pcMustEatAgain(Board board, Piece piece) {
        return (piece.canCapture(board.getBoard(), "captureLeft") || piece.canCapture(board.getBoard(), "captureRight"))
                || ((piece instanceof King) && (piece.canCapture(board.getBoard(), "captureDownLeft") || piece.canCapture(board.getBoard(), "captureDownRight")));
    }

    public Boolean checkIfBecomeKing(Piece piece, Board board, Boolean exploring) throws IOException {
        Boolean become = false;
        if(!(piece instanceof King) && ((piece.getColour().equals("b") && piece.getRowPosition() == 7) ||
            (piece.getColour().equals("w") && piece.getRowPosition() == 0))){
            King king = new King(piece);
            this.getPieceList().remove(piece);
            this.getPieceList().add(king);
            board.getBoard()[king.getRowPosition()][king.getColPosition()].setOccupier(king);
            if(!exploring && this.getAlgorithm().equals("human"))
                mustEatAgain(board, piece);
            become = true;
        }
        return become;
    }


    public boolean isWhite(){

        return this.colour.equals("w");
    }

    public boolean isBlack(){

        return this.colour.equals("b");
    }

}
