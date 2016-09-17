package game.moveGenerator;

import game.model.Board;
import game.model.Piece;
import game.model.Player;
import java.io.IOException;

public class Node{
    private String move; // ex: "w1 moveLeft"
    private Integer value;
    private Node father = null;
    private Integer depth = 0;
    private Board state;

    private Player player;

    public Node(String move, Node father, Integer depth, Player player, Board state) {
        this.move = move;
        this.value = 0;
        this.father = father;
        this.depth = depth;
        this.state = state;
        this.player = player;
    }


    public Integer getDepth() {
        return depth;
    }

    public Board getState() {
        return state;
    }

    public String getMove() {
        return move;
    }

    public Piece getPiece() {
        String[] moveArray = move.split(" ");
        String name = moveArray[0];
        return player.getPieceByName(name);
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Node getFather() {
        return father;
    }

    public void performAction(Piece piece, String move) throws IOException {
        if(move.indexOf("move") > -1){ // more efficient than method String.contains
            piece.move(this.state.getBoard(), move);
        }
        else if(move.indexOf("capture") > -1){
            String[] splittedMove = move.split(" ");
            Piece eatenPiece;
            if(splittedMove.length > 1){
                eatenPiece = piece.capture(splittedMove[0], this.state.getBoard());
            }else{
                eatenPiece = piece.capture(move, this.state.getBoard());
            }
            //eatenpiece TODO: remove piece from pieceList
        }
        this.getPlayer().checkIfBecomeKing(piece, this.getState());
    }

    public Player getPlayer() {
        return player;
    }
}
