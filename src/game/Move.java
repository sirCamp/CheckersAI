package game;
import game.model.Board;
import game.model.Spot;
public class Move {


	static boolean canMove(String path, String who, Spot[][] board, int row, int column, boolean isPlayer) {

		if (path.equals("moveLeft")) {
			return canMoveLeft(who, board, row, column, isPlayer);
		} else if (path.equals("moveRight")) {
			return canMoveRight(who, board, row, column, isPlayer);
		} else {
			return false;
		}

	}


	public static boolean canMoveRight(String who, Spot[][] board, int pieceRow, int pieceCol, boolean isPlayer) {
		boolean can;
		int rowAlter;        // Alter  row to determine desired position
		int colAlter;        // Alter column to determine desired position
		if (who.equals("player")) {
			rowAlter = -1;
			colAlter = 1;
		} else {
			rowAlter = 1;
			colAlter = 1;
		}
		if (pieceCol == 5) {
			if (isPlayer)
				System.out.println("there are no right spots for" + board[pieceRow][pieceCol].getOccupier() + ", you are at edge");
			can = false;
		} else {
			if (Board.inBounds((pieceRow + rowAlter), (pieceCol + colAlter)) && board[pieceRow + rowAlter][pieceCol + colAlter] != null && board[pieceRow + rowAlter][pieceCol + colAlter].getOccupier().equals("X")) {
				can = true;
			} else {
				if (isPlayer && !board[pieceRow][pieceCol].getOccupier().equals("X"))
					System.out.println("the right spot of " + board[pieceRow][pieceCol].getOccupier() + ", is occupied");
				can = false;
			}


		}
		return can;
	}


	public static boolean canMoveLeft(String who, Spot[][] board, int pieceRow, int pieceCol, boolean isPlayer) {
		boolean can;
		int rowAlter;        // Alter  row to determine desired position
		int colAlter;        // Alter column to determine desired position
		if (who.equals("player")) {
			rowAlter = -1;
			colAlter = -1;
		} else {
			rowAlter = 1;
			colAlter = -1;
		}
		if (pieceCol == 0) {
			if (isPlayer && !board[pieceRow][pieceCol].getOccupier().equals("X"))
				System.out.println("there are no Left spots for " + board[pieceRow][pieceCol].getOccupier() + ", you are at edge");
			can = false;
		} else {

			if (Board.inBounds((pieceRow + rowAlter), (pieceCol + colAlter)) && board[pieceRow + rowAlter][pieceCol + colAlter] != null && board[pieceRow + rowAlter][pieceCol + colAlter].getOccupier().equals("X")) {
				can = true;
			} else {
				if (isPlayer && !board[pieceRow][pieceCol].getOccupier().equals("X"))
					System.out.println("the left spot of " + board[pieceRow][pieceCol].getOccupier() + "  is occupied");
				can = false;
			}


		}
		return can;
	}


	public static Spot[][] move(String path, String who, Spot[][] board, int pieceRow, int pieceCol) {
		if (path.equals("moveLeft")) {
			System.out.println("Move Left!");
			return moveLeft(who, board, pieceRow, pieceCol);

		} else //if(path.equals("moveRight"))
		{
			System.out.println("Move Right!");
			return moveRight(who, board, pieceRow, pieceCol);

		}
/*	else
	{
		System.out.println("This should never be reached");
		return null;
	}*/
	}


	public static Spot[][] moveLeft(String who, Spot[][] board, int pieceRow, int pieceCol) {
		int rowAlter;        // to specify spot to be used.
		int colAlter;

		if (who.equals("player")) {
			rowAlter = -1;
			colAlter = -1;
		} else {
			rowAlter = 1;
			colAlter = -1;
		}

		board[pieceRow + rowAlter][pieceCol + colAlter].setOccupier(board[pieceRow][pieceCol].getOccupier());
		board[pieceRow][pieceCol].setOccupier(null);
		return board;
	}


	public static Spot[][] moveRight(String who, Spot[][] board, int pieceRow, int pieceCol) {

		int rowAlter;        // to specify spot to be used.
		int colAlter;

		if (who.equals("player")) {
			rowAlter = -1;
			colAlter = 1;
		} else {
			rowAlter = 1;
			colAlter = 1;
		}

		board[pieceRow + rowAlter][pieceCol + colAlter].setOccupier(board[pieceRow][pieceCol].getOccupier());
		board[pieceRow][pieceCol].setOccupier(null);
		return board;

	}

	/////////////// MIE AGGIUNTE ///////////////


	public static Spot[][] moveNew(String path, String who, Spot[][] board, int pieceRow, int pieceCol) {


		if (path.equals("moveLeft")) {
			System.out.println("Move Left!");
			return doMove(who, board, pieceRow, pieceCol, "left");
		} else if (path.equals("moveRight")) {
			System.out.println("Move Right!");
			return doMove(who, board, pieceRow, pieceCol, "right");
		} else if (true) { //damona	 TODO
			if (path.equals("moveRightBack")) {
				System.out.println("Move right back!");
				return doMove(who, board, pieceRow, pieceCol, "rightB");
			} else { //if(path.equals("moveLeftBack")) {
				System.out.println("Move left back!");
				return doMove(who, board, pieceRow, pieceCol, "leftB");
			}
		}
        return null;
/*	else
	{
		System.out.println("This should never be reached");
		return null;
	}*/
	}

	public static Spot[][] doMove(String who, Spot[][] board, int pieceRow, int pieceCol, String direction) {

		int rowAlter = getRowAlter(who, direction);        // to specify spot to be used.
		int colAlter = getColAlter(who, direction);
		board[pieceRow + rowAlter][pieceCol + colAlter].setOccupier(board[pieceRow][pieceCol].getOccupier());
		board[pieceRow][pieceCol].setOccupier(null);
		return board;

	}

	public static int getRowAlter(String who, String direction) {
		/*
		if(player)
			case direction
		/*
		if (who.equals("player")) {
			rowAlter = -1;
			colAlter = 1;
		} else {
			rowAlter = 1;
			colAlter = 1;
		}
		if(pc)
			case direction
		 */
        return 0;
	}

    public static int getColAlter(String who, String direction) {
		/*
		if(player)
			case direction
		if(pc)
			case direction
		 */

        return 0;
	}

	public static boolean canMove(String who, Spot[][] board, int pieceRow, int pieceCol, boolean isPlayer, String direction) {
		boolean can;
		int rowAlter = getRowAlter(who, direction);        // to specify spot to be used.
		int colAlter = getColAlter(who, direction);
		if (pieceCol == 0) {
			if (isPlayer && !(board[pieceRow][pieceCol].getOccupier() == null))
				System.out.println("there are no Left spots for " + board[pieceRow][pieceCol].getOccupier() + ", you are at edge");
			can = false;
		} else {
			if (Board.inBounds((pieceRow + rowAlter), (pieceCol + colAlter)) && board[pieceRow + rowAlter][pieceCol + colAlter] != null && board[pieceRow + rowAlter][pieceCol + colAlter].getOccupier().equals("X")) {
				can = true;
			} else {
				if (isPlayer && !(board[pieceRow][pieceCol].getOccupier() == null))
					System.out.println("the left spot of " + board[pieceRow][pieceCol].getOccupier() + "  is occupied");
				can = false;
			}
		}
		return can;
	}
	
}
