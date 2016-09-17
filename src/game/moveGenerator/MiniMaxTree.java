package game.moveGenerator;

import com.sun.org.apache.xml.internal.serializer.utils.SystemIDResolver;
import com.sun.org.apache.xpath.internal.operations.Bool;
import game.model.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MiniMaxTree {
    private ArrayList<Node> tree = new ArrayList<>();
    private Integer depth = 1;
    private String algorithm;
    private Integer heuristic;
    private Boolean pruning = false;
    private static Map<Integer, Node> cache = new HashMap<>();

    public MiniMaxTree(Board board, Integer depth, String player, String algorithm) throws IOException, CloneNotSupportedException {
        this.depth = depth;
        this.algorithm = algorithm;
        heuristic = Character.getNumericValue(algorithm.charAt(0));
        if(algorithm.indexOf("pruning")>-1){ pruning = true; }
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
/*                    if(currentPlayer.mustEat(node.getState().getBoard(), false)){
                        mustEat = true;
                    }*/
                    for (Piece piece : currentPlayer.getPieceList()) { // node that have tree.get(j) as father
                        if(!currentPlayer.mustEat(node.getState().getBoard(), false)){
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
                            if(!currentPlayer.mustEat(node.getState().getBoard(), false)){
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

    /*private Node memoizedCreateNode(Piece piece, String move, Integer depth, Player player, Node father, Board board) throws CloneNotSupportedException, IOException {

        Integer hash = Objects.hash(piece,move,depth,player,father,board);
        if(MiniMaxTree.cache.containsKey(hash)){
            return MiniMaxTree.cache.get(hash);
        }
        else{
            return createNode(piece, move, depth, player, father, board);
        }

    }*/

    private void createNode(Piece piece, String move, Integer depth, Player player, Node father, Board board) throws  IOException {
        Board copyOfBoard = null;
        try {
            copyOfBoard = board.copy();
        } catch (CloneNotSupportedException e) {}

        Player copyOfPlayer = copyOfBoard.getPlayerByName(player.getName());
        Piece copyOfPiece = copyOfPlayer.getPieceByName(piece.getName());

        Node tmp = null;

        Integer hash = Objects.hash(piece.getName() +" "+ move, father, depth, copyOfPlayer, copyOfBoard);
        if(MiniMaxTree.cache.containsKey(hash)){
            tmp =  MiniMaxTree.cache.get(hash);
        }
        else{
            tmp = new Node(piece.getName() +" "+ move, father, depth, copyOfPlayer, copyOfBoard);
        }

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

    private void setEvaluationFunValue(Node node, Boolean isEven){
        Integer value = Evaluation.getEvaluationValue(node, heuristic).intValue();
        node.setValue(value);
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
        int i = last;
        while(i>0){
            if(!tree.get(i).getDepth().equals(currentDepth)){ // if depth is changed while exploring
                currentDepth = tree.get(i).getDepth();
                isEven = !isEven;
            }
            Node father = tree.get(i).getFather();
            int lastSon = i;
            while(i > 0 && father.equals(tree.get(i).getFather())){
                if(tree.get(i).getFather() != null) System.out.print(tree.get(i).getFather().getMove()+" > ");
                System.out.println(tree.get(i).getMove());
                //tree.get(i).getState().printBoard();
                i--;
            }
            int firstSon = i+1;
            if(tree.get(firstSon).getDepth()>0){
                if(isEven){
                    tree.get(tree.indexOf(tree.get(firstSon).getFather())).setValue(findMinValue(firstSon, lastSon));
                } else{ //isOdd
                    tree.get(tree.indexOf(tree.get(firstSon).getFather())).setValue(findMaxValue(firstSon, lastSon));
                }
            }
        }
        /* the tree contains all the values the following part of the algorithm, will choose the best node */
        Integer index = searchIndexOfMaxNode();
        if(index != -1){
            result = tree.get(index);
        }
        return result;
    }

    private Integer findMinValue(int firstSon, int lastSon){
        int min = tree.get(firstSon).getValue();
        for(int j = firstSon; j<=lastSon; j++){
            if(min > tree.get(j).getValue()){
                min = tree.get(j).getValue();
            }
        }
        return min;
    }

    private Integer findMaxValue(int firstSon, int lastSon){
        int max = tree.get(firstSon).getValue();
        for(int j= firstSon; j<=lastSon; j++){
            if(max < tree.get(j).getValue()){
                max = tree.get(j).getValue();
            }
        }
        return max;
    }

    /* Pruning */
    private Node exploreTreePruning(){ // backtracking
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
        int i = last;
        int pruningValue = 0;
        while(i>0){
            if(!tree.get(i).getDepth().equals(currentDepth)){ // if depth is changed while exploring
                currentDepth = tree.get(i).getDepth();
                isEven = !isEven;
                if(isEven){
                    pruningValue = Integer.MIN_VALUE;
                }else{
                    pruningValue = Integer.MAX_VALUE;
                }
            }
            Node father = tree.get(i).getFather();
            int lastSon = i;
            while(i > 0 && father.equals(tree.get(i).getFather())){
                i--;
            }
            int firstSon = i+1;
            if(tree.get(firstSon).getDepth()>0){
                if(isEven){
                    tree.get(tree.indexOf(tree.get(firstSon).getFather())).setValue(findMinValuePruning(firstSon, lastSon, pruningValue));
                } else{ //isOdd
                    tree.get(tree.indexOf(tree.get(firstSon).getFather())).setValue(findMaxValuePruning(firstSon, lastSon, pruningValue));
                }
            }
            pruningValue = updatePruningValue(isEven, tree.get(firstSon).getFather().getValue(), pruningValue);
        }
        /* the tree contains all the values the following part of the algorithm, will choose the best node */
        Integer index = searchIndexOfMaxNode();
        if(index != -1){
            result = tree.get(index);
        }
        return result;
    }

    private int updatePruningValue(Boolean isEven, Integer value, int exPruningValue) {
        if(isEven && exPruningValue<value){
            return value;
        }else if(isEven && exPruningValue >= value){
            return exPruningValue;
        }else if(!isEven && exPruningValue > value){
            return value;
        }else{
            return exPruningValue;
        }
    }

    private Integer findMinValuePruning(int firstSon, int lastSon, int pruningValue){
        int min = tree.get(firstSon).getValue();
        int j = firstSon;
        while(j <= lastSon && min >= pruningValue){
            if(min > tree.get(j).getValue()){
                min = tree.get(j).getValue();
            }
            j++;
        }
        return min;
    }

    private Integer findMaxValuePruning(int firstSon, int lastSon, int pruningValue){
        int max = tree.get(firstSon).getValue();
        int j = firstSon;
        while(j <= lastSon && max <= pruningValue){
            if(max < tree.get(j).getValue()){
                max = tree.get(j).getValue();
            }
        }
        return max;
    }

}
