package useless;

import game.model.Spot;
import java.lang.Math;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Tree {
    private LinkedList<Tree>  children = new LinkedList<Tree>();
    private Spot[][] board;
    private double value= 0;
    private boolean isCapture;

   public Tree(Spot[][] board)
   {
	   this.board = board;
   }
    
    
    
    public Spot[][] getBoard()
     {
    	 return this.board;
     }
    
    public List<Tree> getChildren()
    {
    	return this.children;
    }
    
    public double getValue()
    {
		return value;
    	
    }
    
    public void setValue(double value)
    {
    	this.value = value;
    }
    
    
    
    
    
    
    public double minimax(boolean isComputer)
    {
   	 
   	 
   		 if(this.getChildren().isEmpty())
   		 {
   			 return this.getValue();
   		 }
   		
   	  if(isComputer){  //if it is player1, we maximise the score
          double a = Double.MIN_VALUE;
          for(Iterator<Tree> i = children.iterator(); i.hasNext();){  //cycle through all of the possible resulting moves
              Tree tree =(Tree)i.next();
        	  a = Math.max(a,tree.minimax(!isComputer));
          }
          return a;
      }
      else{   //if it is player 2, we minimize the score
          double a = Double.MAX_VALUE;
          for(Iterator<Tree> i = children.iterator(); i.hasNext();){  //cycle through all of the possible resulting moves
        	  Tree tree = (Tree)i.next();
        	  a = Math.min(a, tree.minimax(!isComputer));
          }
          return a;

   		 
   	 }
   	 
   	 
   	
   	 
    }
    
    
    
    
    public Tree getMove(boolean isComputer){
        if(children.isEmpty()){
            return null;
        }

        Tree best = null;
        double maxScore = (isComputer ? Double.MIN_EXPONENT : Double.MAX_VALUE);  //highest or lowest, depending on what we want
        for(Iterator<Tree> i = children.iterator(); i.hasNext();){
        	Tree child = (Tree) i.next();
            double value = child.minimax(isComputer);
            if(best == null || value * (isComputer ? 1 : -1) > maxScore * (isComputer ? 1 : -1)){
                maxScore = value;
                best = child;
            }
        }
        
        return best;
    }
    
    public boolean isCapture()
    {
    	return isCapture;
    }

    public void setIsCapture(boolean isCapture)
    {
    	this.isCapture = isCapture;
    }
    
   	
   
    
    
}