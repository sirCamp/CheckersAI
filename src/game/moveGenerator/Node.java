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
    private Map<String, Node> children;
    private Player player;

    public Node(String move, Integer value, Node father, Integer depth, Player player, Board state) {
        this.move = move;
        this.value = value;
        this.father = father;
        this.depth = depth;
        try {
            this.state = state.copy();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        this.children = children;
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

    public Map<String, Node> getChildren() {
        return children;
    }

    public void setChildren(Map<String, Node> children) {
        this.children = children;
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
        this.value = value; // TODO: implement evalFunction maybe with another function: this is used to set result of min and max
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
}
