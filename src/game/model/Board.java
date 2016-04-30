package game.model;

public class Board {

    private Spot[][] board = new Spot[8][8]; // 8x8 board
	
	private boolean capture= false;

    private Player p1;

    private Player p2;

	public Board(Player p1, Player p2)
	{
        int k=0; // serve per costruire l'id della poszione

        this.p1 = p1;
        this.p2 = p2;

        int bCount = 0; // id per pedine nere
        int wCount = 0; // id per pedine bianche

		for(int i=0; i<8; i++){

            for(int j=0; j<8; j++){

                if((i+j)%2 == 0){

                    board[i][j] = null; //casella non utilizzabile
                }
                else{

                    k++;

                    Piece pedina = null; //casella vuota
                    if(i<3){



                        pedina = p1.getPiece().get(bCount);
                        bCount++;
                    }
                    else {

                        if (i > 4) {
                            pedina = p2.getPiece().get(wCount);
                            System.out.println(pedina.getName());
                            wCount++;
                        }
                    }
                    String position = "S"+Integer.toString(k);
                    board[i][j] = new Spot(position, 1, pedina);
                }
            }
        }
	}	                                                                                                                        
	                                                                                                                            
	public Spot[][] getBoard()                                                                                                  
	{                                                                                                                           
		return board;                                                                                                           
	}                                                                                                                           
	                               
	public void setBoard(Spot[][] aboard)
	{
		this.board = aboard;
	}

	public void setCapture(boolean capture) {
		
		this.capture = capture;
		
	}
	
	public boolean getCapture()
	{
		return capture;
	}
	                                                                                                                            
	                                                                                                                            
}                                                                                                                               
                                                                                                                                