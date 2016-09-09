package game.moveGenerator;

import game.model.Board;
import game.model.Piece;
import game.model.Player;
import game.model.Spot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by enry8 on 03/05/16.
 */

public class MiniMaxTree {
    private ArrayList<Node> tree = new ArrayList<Node>();
    private Board board;
    private double value = 0;
    private Integer depth = 1;
    private boolean isCapture;

    public MiniMaxTree(Board board, Integer depth, Player player) throws CloneNotSupportedException {
        this.board = board;
        this.depth = depth;
        createTree(player);
    }

    public Board getBoard() {
        return this.board;
    }

    public ArrayList<Node> getTree() {
        return this.tree;
    }

    public double getValue() {
        return value;

    }

    private void createTree(Player player) throws CloneNotSupportedException { //TODO: to be extended with Kings
        tree.add(new Node(null, 0, null, 0, board.copy(), player)); // current state
        for (int i = 1; i < depth; i++) { // foreach depth
            for (int j = 0; j < tree.size(); j++) {
                if(tree.get(j).getDepth()==i-1){ //foreach node inserted on previous iteration (depth = depth -1 ->
                    for (Piece piece : player.getPieceList()) { // node that have tree.get(j) as father
                        if (establishPossibleMovement(tree.get(j).getState().getBoard(), piece, "moveLeft")) {
                           tree.add(this.createNode(piece, "moveLeft", depth, player, tree.get(j)));
                        }
                        if (establishPossibleMovement(tree.get(j).getState().getBoard(), piece, "moveRight")) {
                            tree.add(this.createNode(piece, "moveRight", depth, player, tree.get(j)));
                        }
                        if (establishPossibleCapture(tree.get(j).getState().getBoard(), piece, "captureLeft")) {
                            tree.add(this.createNode(piece, "captureLeft", depth, player, tree.get(j)));
                        }
                        if (establishPossibleCapture(tree.get(j).getState().getBoard(), piece, "captureRight")) {
                            tree.add(this.createNode(piece, "captureRight", depth, player, tree.get(j)));
                        }
                    }
                }
            }
        }
    }


    private Node createNode(Piece piece, String move, Integer depth, Player player, Node father) throws CloneNotSupportedException {
        Node tmp = new Node(piece.getName() + move, 0, father, depth, board.copy(), player);
        tmp.performAction(piece, move);
        tmp.setValue(0);
        return tmp;
    }

    private Boolean establishPossibleMovement(Spot[][] board, Piece piece, String direction){
        Integer newRow = piece.getRowPosition() +  piece.rowAlter(direction); //spostamento x
        Integer newCol = piece.getColPosition() + piece.colAlter(direction); //spostamento y
        if(piece.canMove(board, direction, newRow, newCol)){
            return true;
        }
        return false;
    }

    private Boolean establishPossibleCapture(Spot[][] board, Piece piece, String direction){
        if(piece.canCapture(board, direction, false)){
            return true;
        }
        return false;
    }

    public String decideMove(){
        Node choice = this.exploreTree();
        String move = choice.getMove();
        return move;
    }

    private Node exploreTree(){ // backtracking
        //Node tmp = this.tree.get(0); //TODO: to be implemented
        Node result = null;
        int last = tree.size()-1;
        if(last<=0){ //last = 0 -> no moves, <0 -> problems
            //error TODO
        }else{
            Boolean isEven = false;
            Integer currentDepth = tree.get(last).getDepth();
            if(tree.get(last).getDepth()%2 == 0){
                isEven = true; //(even row -> max, odd row -> min
            }
            for(int i = last;  i>0; i--){
                /* now, base on depth, we must know if it is even or odd,
                in order to apply findMin or findMax function: */
                if(currentDepth != tree.get(i).getDepth()){
                    if(isEven == true){
                        isEven = false;
                    } else{ // isOdd
                        isEven = true;
                    }
                }
                Node father = tree.get(i).getFather();
                int k=i;
                while(i>0 && father == tree.get(i).getFather()){
                    i--;
                }
                if(isEven){
                    tree.get(tree.indexOf(tree.get(i).getFather())).setValue(findMinValue(i, k));
                } else{ //isOdd
                    tree.get(tree.indexOf(tree.get(i).getFather())).setValue(findMaxValue(i, k));
                }
            }
            // the tree contains all the values
            // the following part of the algorithm, will choose the best node
            Integer index = searchIndexOfMaxNode();
            if(index!= -1 ){
                result = tree.get(index);
            }
        }
        return result;
    }

    private Integer findMinValue(int i, int k){
        int min = tree.get(i).getValue();
        for(int j= i+1; j<=k; j++){
            if(min>= tree.get(j).getValue()){
                min = tree.get(j).getValue();
            }
        }
        return min;
    }

    private Integer findMaxValue(int i, int k){
        int max = tree.get(i).getValue();
        for(int j= i+1; j<=k; j++){
            if(max<= tree.get(j).getValue()){
                max = tree.get(j).getValue();
            }
        }
        return max;
    }


    private Integer searchIndexOfMaxNode(){
        Integer depth = 1;
        Integer max = tree.get(0).getValue();
        Integer index = 1;
        Boolean found = false;
        while(index < tree.size() && tree.get(index).getDepth() == depth){
            if(tree.get(index).getValue() == max){
                found = true;
            }else {
                index++;
            }
        }
        if(found){
            return index;
        } else {
            return -1;
        }
    }

}
