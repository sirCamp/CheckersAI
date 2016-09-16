package game.moveGenerator;

import game.model.*;
import java.io.IOException;
import java.util.*;

public class MiniMaxTree {
    private ArrayList<Node> tree = new ArrayList<>();
    private Integer depth = 1;
    private String algorithm;

    public MiniMaxTree(Board board, Integer depth, String player, String algorithm) throws IOException, CloneNotSupportedException {
        this.depth = depth;
        this.algorithm = algorithm;
        Board copyBoard = board.copy();
        Player p1 = copyBoard.getPlayerByName(player);
        Player otherPlayer = copyBoard.getOtherPlayer(p1);
        createTree(copyBoard, p1, otherPlayer);
    }

    private void createTree(Board board, Player player, Player otherP) throws IOException {
        tree.add(new Node(null, null, 0, otherP, board)); // current state
        Player currentPlayer;
        Boolean mustEat = false;
        for (int i = 1; i < depth; i++) { // foreach depth
            if(i % 2 == 1) // to understand if it is min or max
                currentPlayer = player;
            else currentPlayer = otherP;
            for (int j = 0; j < tree.size(); j++) {
                Node node = tree.get(j);
                if(tree.get(j).getDepth()==i-1){ //foreach node inserted on previous iteration (depth = depth -1 ->
                    if(currentPlayer.mustEat(node.getState().getBoard(), false)){
                        mustEat = true;
                    }
                    for (Piece piece : currentPlayer.getPieceList()) { // node that have tree.get(j) as father
                        if(!mustEat){
                            if (establishPossibleMovement(node.getState(), piece, "moveLeft")) {
                                this.createNode(piece, "moveLeft", i, currentPlayer, node, node.getState());
                            }
                            if (establishPossibleMovement(node.getState(), piece, "moveRight")) {
                                this.createNode(piece, "moveRight", i, currentPlayer, node, node.getState());
                            }
                        }
                        if (establishPossibleCapture(node.getState(), piece, "captureLeft")) {
                            this.createNode(piece, "captureLeft", i, currentPlayer, node, node.getState());
                        }
                        if (establishPossibleCapture(node.getState(), piece, "captureRight")) {
                            this.createNode(piece, "captureRight", i, currentPlayer, node, node.getState());
                        }
                        if(piece instanceof King){
                            if(!mustEat){
                                if (establishPossibleMovement(node.getState(), piece, "moveDownLeft")) {
                                    this.createNode(piece, "moveDownLeft", i, currentPlayer, node, node.getState());
                                }
                                if (establishPossibleMovement(node.getState(), piece, "moveDownRight")) {
                                    this.createNode(piece, "moveDownRight", i, currentPlayer, node, node.getState());
                                }
                            }
                            if (establishPossibleCapture(node.getState(), piece, "captureDownLeft")) {
                                this.createNode(piece, "captureDownLeft", i, currentPlayer, node, node.getState());
                            }
                            if (establishPossibleCapture(node.getState(), piece, "captureDownRight")) {
                                this.createNode(piece, "captureDownRight", i, currentPlayer, node, node.getState());
                            }
                        }
                    }
                }
            }
        }
    }

    private void createNode(Piece piece, String move, Integer depth, Player player, Node father, Board board) throws  IOException {
        Board copyOfBoard = null;
        try {
            copyOfBoard = board.copy();
        } catch (CloneNotSupportedException e) {}
        Player copyOfPlayer = copyOfBoard.getPlayerByName(player.getName());
        Piece copyOfPiece = copyOfPlayer.getPieceByName(piece.getName());
        Node tmp = new Node(piece.getName() +" "+ move, father, depth, copyOfPlayer, copyOfBoard);
        tmp.performAction(copyOfPiece, move);
        if(move.indexOf("capture")>-1 && copyOfPlayer.pcMustEatAgain(copyOfBoard, copyOfPiece)){
            continueCapture(tmp, depth);
        }else{
            tree.add(tmp);
        }
    }

    private void continueCapture(Node node, Integer depth) throws IOException {
        String move = " " +node.getMove();
        if (establishPossibleCapture(node.getState(), node.getPiece(), "captureLeft")) {
            this.createNode(node.getPiece(), "captureLeft"+ move, depth, node.getPlayer(), node.getFather(), node.getState());
        }
        if (establishPossibleCapture(node.getState(), node.getPiece(), "captureRight")) {
            this.createNode(node.getPiece(), "captureRight"+ move, depth, node.getPlayer(), node.getFather(), node.getState());
        }
        if(node.getPiece() instanceof King){
            if (establishPossibleCapture(node.getState(), node.getPiece(), "captureDownLeft")) {
                this.createNode(node.getPiece(), "captureDownLeft"+ move, depth, node.getPlayer(), node.getFather(), node.getState());
            }
            if (establishPossibleCapture(node.getState(), node.getPiece(), "captureDownRight")) {
                this.createNode(node.getPiece(), "captureDownRight"+ move, depth, node.getPlayer(), node.getFather(), node.getState());
            }
        }
    }

    private Boolean establishPossibleMovement(Board b, Piece piece, String direction){ return piece.canMove(b.getBoard(), direction, 1); }

    private Boolean establishPossibleCapture(Board b, Piece piece, String direction){ return piece.canCapture(b.getBoard(), direction); }

    public String decideMove() {
        if (this.tree.size() <= 1) {
            return null;
        } else {
            Node choice = this.exploreTree();
            return choice.getMove();
        }
    }

    private Node exploreTree(){ // backtracking
        Node result = null;
        int last = tree.size()-1;
        Integer currentDepth = tree.get(last).getDepth();
        int j = last;
        Boolean isEven = false; //(even row -> max, odd row -> min
        if(tree.get(last).getDepth()%2 == 0){
            isEven = true;
        }
        while(tree.get(j).getDepth().equals(currentDepth)){
            setEvaluationFunValue(tree.get(j), isEven);
            j--;
        }
        /* now, base on depth, we must know if it is even or odd,
        in order to apply findMin or findMax function to update father nodes: */
        for(int i = last; i>0; i--){
            if(!tree.get(i).getDepth().equals(currentDepth)){ // if depth is changed while exploring
                currentDepth = tree.get(i).getDepth();
                isEven = !isEven;
            }
            Node father = tree.get(i).getFather();
            int k=i;
            while(i > 0 && father.equals(tree.get(i).getFather())){
                i--;
            }
            i++;
            if(tree.get(i).getDepth()>0){
                if(isEven){
                    tree.get(tree.indexOf(tree.get(i).getFather())).setValue(findMinValue(i, k));
                } else{ //isOdd
                    tree.get(tree.indexOf(tree.get(i).getFather())).setValue(findMaxValue(i, k));
                }
            }

        }
        // the tree contains all the values
        // the following part of the algorithm, will choose the best node
        Integer index = searchIndexOfMaxNode();
        if(index != -1){
            result = tree.get(index);
        }
        return result;
    }

    private void setEvaluationFunValue(Node node, Boolean isEven){
        Random ran = new Random();
        Integer value = ran.nextInt(10);
        node.setValue(value);
        if(node.getMove().indexOf("capture")>-1) { // capture has priority
            if (isEven)
                node.setValue(value - 100);
            else // isOdd
                node.setValue(value + 100);
        }
    }

    private Integer findMinValue(int i, int k){
        int min = tree.get(i).getValue();
        for(int j= i; j<=k; j++){
            if(min>= tree.get(j).getValue()){
                min = tree.get(j).getValue();
            }
        }
        return min;
    }

    private Integer findMaxValue(int i, int k){
        int max = tree.get(i).getValue();
        for(int j= i; j<=k; j++){
            if(max<= tree.get(j).getValue()){
                max = tree.get(j).getValue();
            }
        }
        return max;
    }

    private Integer searchIndexOfMaxNode(){
        Integer max = tree.get(0).getValue();
        Integer index = 1;
        Boolean found = false;
        while(!found && index < tree.size() && tree.get(index).getDepth() == 1){
            if(tree.get(index).getValue().equals(max)){
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
