package game.model;

public class Board implements Cloneable{

    private Spot[][] board = new Spot[8][8]; // 8x8 board
    private Player p1;
    private Player p2;

	public Board(Player p1, Player p2){
        int k=0; // used to build the id of the position
        this.p1 = p1;
        this.p2 = p2;
        int bCount = 0; // id for black pieces
        int wCount = 0; // id for white pieces

		for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if((i+j)%2 == 0){ // all usable tiles hav an odd value col+row
                    board[i][j] = null; // not usable tile
                }
                else{
                    k++;
                    Piece piece = null; // empty tile
                    if(i<3){
                        piece = p1.getPieceList().get(bCount);
                        bCount++;
                    }
                    else {
                        if (i > 4) {
                            piece = p2.getPieceList().get(wCount);
                            wCount++;
                        }
                    }
                    String position = "S"+Integer.toString(k);
                    board[i][j] = new Spot(position, piece);
                }
            }
        }
	}

    private Board(Player p1, Player p2, Boolean copy){
        int k=0; // used to build the id of the position
        this.p1 = p1;
        this.p2 = p2;
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if((i+j)%2 == 0){ // all usable tiles hav an odd value col+row
                    board[i][j] = null; // not usable tile
                }
                else{
                    k++;
                    String position = "S"+Integer.toString(k);
                    board[i][j] = new Spot(position, null); // null = empty tile
                }
            }
        }
        for(int i=0; i< this.p1.getPieceList().size(); i++){
            board[this.p1.getPieceList().get(i).getRowPosition()][this.p1.getPieceList().get(i).getColPosition()].setOccupier(this.p1.getPieceList().get(i));
        }
        for(int i=0; i< this.p2.getPieceList().size(); i++){
            board[this.p2.getPieceList().get(i).getRowPosition()][this.p2.getPieceList().get(i).getColPosition()].setOccupier(this.p2.getPieceList().get(i));
        }
    }
	                                                                                                                            
	public Spot[][] getBoard() { return board; }

    public Board copy() throws CloneNotSupportedException { return new Board((Player)this.p1.clone(), (Player) this.p2.clone(), true); }

    public static Boolean inBounds(Integer row, Integer col) { return (row<8  && col <8 && row>=0 && col>=0); }

    public void printBoard(){
        for (int row =0; row <8 ; row++){
            System.out.println();
            for (int column =0; column < 8; column++){
                if(board!= null && board[row][column]!= null){
                    if(board[row][column].getOccupier()!= null){
                        String name = board[row][column].getOccupier().getName();
                        switch (name.length()){
                            case 2:{
                                System.out.print("[ " + name + " ]");
                                break;
                            }
                            case 3:{
                                System.out.print("[ " + name + "]");
                                break;
                            }
                            case 4:{
                                System.out.print("[" + name + "]");
                                break;
                            }
                            default:{
                                System.out.print("[" + name + "]");
                                break;
                            }
                        }
                    }
                    else{
                        System.out.print("[____]");
                    }
                }
                else{
                    System.out.print("  *   ");
                }
            }
        }
        System.out.println("\n___________________________________________");
    }

    public Player getOtherPlayer(Player p){
        Player otherP;
        if(p.getName().equals(this.p1.getName()))
            otherP = this.p2;
        else otherP = this.p1;
        return otherP;
    }

    public Player getPlayerByName(String name){
        Player p;
        if(p1.getName().equals(name))
            p = p1;
        else p = p2;
        return p;
    }
}                                                                                                                               
                                                                                                                                