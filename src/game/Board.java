package Game;
import Game.Spot;

public class Board {
	private Spot[][] board = new Spot[8][8]; // 8x8 board
	
	private boolean capture= false;

	public Board()
	{
        int k=0; // serve per costruire l'id della poszione
		for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if((i+j)%2 == 0){
                    board[i][j] = null; //casella vuota
                }
                else{
                    k++;
                    bCount = 0; // id per pedine nere
                    wCount = 0; // id per pedine bianche
                    String pedina ="X";
                    if(i<3){
                        bCount++;
                        pedina = "b"+Integer.toString(bCount); //per inserire pedine nere
                    }else
                        if(i>4){
                            wCount++;
                            pedina = "w"+Integer.toString(wCount); //per inserire pedine bianche
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
                                                                                                                                