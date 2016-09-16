package game.moveGenerator;

import game.model.Board;
import game.model.Piece;
import game.model.Player;
import game.model.Spot;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by enry8 on 03/05/16.
 */


public class Node{
    private String move; // ex: "w1 moveLeft"
    private Integer value;
    private Node father = null;
    private Integer depth = 0; // Max
    private Board state;
    private Player player;

    public Node(String move, Integer value, Node father, Integer depth, Player player, Board state) {
        this.move = move;
        this.value = value;
        this.father = father;
        this.depth = depth;
        this.state = state;
        this.player = player;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public Board getState() {
        return state;
    }

    public void setState(Board state) {
        this.state = state;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public Piece getPiece() {
        return player.getPieceByName(move.substring(0,1));
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

    public void setFather(Node father) {
        this.father = father;
    }

    public void performAction(Piece piece, String move) {
        if(move.indexOf("move") > -1){ // more efficient than method String.contains
            piece.move(this.state.getBoard(), move);
        }
        else if(move.indexOf("capture") > -1){
            piece.capture(move, this.state.getBoard());
        }
    }

    public Player getPlayer() {
        return player;
    }
}
