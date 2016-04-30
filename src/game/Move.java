package Game;
import Game.Main;
import Game.Spot;
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


	public static boolean canMoveRight(String Who, Spot[][] board, int pieceRow, int pieceCol, boolean isPlayer) {
		boolean can;
		int rowAlter;        // Alter  row to determine desired position
		int colAlter;        // Alter column to determine desired position
		if (Who.equals("player")) {
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
			if (Main.inBounds((pieceRow + rowAlter), (pieceCol + colAlter)) && board[pieceRow + rowAlter][pieceCol + colAlter] != null && board[pieceRow + rowAlter][pieceCol + colAlter].getOccupier().equals("X")) {
				can = true;
			} else {
				if (isPlayer && !board[pieceRow][pieceCol].getOccupier().equals("X"))
					System.out.println("the right spot of " + board[pieceRow][pieceCol].getOccupier() + ", is occupied");
				can = false;
			}


		}
		return can;
	}


	public static boolean canMoveLeft(String Who, Spot[][] board, int pieceRow, int pieceCol, boolean isPlayer) {
		boolean can;
		int rowAlter;        // Alter  row to determine desired position
		int colAlter;        // Alter column to determine desired position
		if (Who.equals("player")) {
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

			if (Main.inBounds((pieceRow + rowAlter), (pieceCol + colAlter)) && board[pieceRow + rowAlter][pieceCol + colAlter] != null && board[pieceRow + rowAlter][pieceCol + colAlter].getOccupier().equals("X")) {
				can = true;
			} else {
				if (isPlayer && !board[pieceRow][pieceCol].getOccupier().equals("X"))
					System.out.println("the left spot of " + board[pieceRow][pieceCol].getOccupier() + "  is occupied");
				can = false;
			}


		}
		return can;
	}


	public static Spot[][] move(String path, String Who, Spot[][] board, int pieceRow, int pieceCol) {
		if (path.equals("moveLeft")) {
			System.out.println("Move Left!");
			return moveLeft(Who, board, pieceRow, pieceCol);

		} else //if(path.equals("moveRight"))
		{
			System.out.println("Move Right!");
			return moveRight(Who, board, pieceRow, pieceCol);

		}
/*	else
	{
		System.out.println("This should never be reached");
		return null;
	}*/
	}


	public static Spot[][] moveLeft(String Who, Spot[][] board, int pieceRow, int pieceCol) {
		int rowAlter;        // to specify spot to be used.
		int colAlter;

		if (Who.equals("player")) {
			rowAlter = -1;
			colAlter = -1;
		} else {
			rowAlter = 1;
			colAlter = -1;
		}

		board[pieceRow + rowAlter][pieceCol + colAlter].setOccupier(board[pieceRow][pieceCol].getOccupier());
		board[pieceRow][pieceCol].setOccupier("X");
		return board;
	}


	public static Spot[][] moveRight(String Who, Spot[][] board, int pieceRow, int pieceCol) {

		int rowAlter;        // to specify spot to be used.
		int colAlter;

		if (Who.equals("player")) {
			rowAlter = -1;
			colAlter = 1;
		} else {
			rowAlter = 1;
			colAlter = 1;
		}

		board[pieceRow + rowAlter][pieceCol + colAlter].setOccupier(board[pieceRow][pieceCol].getOccupier());
		board[pieceRow][pieceCol].setOccupier("X");
		return board;

	}

	/////////////// MIE AGGIUNTE ///////////////


	public static Spot[][] move(String path, String Who, Spot[][] board, int pieceRow, int pieceCol) {
		if (path.equals("moveLeft")) {
			System.out.println("Move Left!");
			return doMove(Who, board, pieceRow, pieceCol, "left");
		} else if (path.equals("moveRight")) {
			System.out.println("Move Right!");
			return doMove(Who, board, pieceRow, pieceCol, "right");
		} else if (true) { //damona	 TODO
			if (path.equals("moveRightBack")) {
				System.out.println("Move right back!");
				return doMove(Who, board, pieceRow, pieceCol, "rightB");
			} else { //if(path.equals("moveLeftBack")) {
				System.out.println("Move left back!");
				return doMove(Who, board, pieceRow, pieceCol, "leftB");
			}
		}
/*	else
	{
		System.out.println("This should never be reached");
		return null;
	}*/
	}

	public static Spot[][] doMove(String Who, Spot[][] board, int pieceRow, int pieceCol, String direction) {

		int rowAlter getRowAlter(Who, direction);        // to specify spot to be used.
		int colAlter getColAlter(Who, direction);
		board[pieceRow + rowAlter][pieceCol + colAlter].setOccupier(board[pieceRow][pieceCol].getOccupier());
		board[pieceRow][pieceCol].setOccupier("X");
		return board;

	}

	int getRowAlter() {
		/*
		if(player)
			case direction
		/*
		if (Who.equals("player")) {
			rowAlter = -1;
			colAlter = 1;
		} else {
			rowAlter = 1;
			colAlter = 1;
		}*/
		if(pc)
			case direction
		 */
	}

	int getColAlter() {
		/*
		if(player)
			case direction
		if(pc)
			case direction
		 */
	}

	public static boolean canMove(String Who, Spot[][] board, int pieceRow, int pieceCol, boolean isPlayer, direction) {
		boolean can;
		int rowAlter getRowAlter(Who, direction);        // to specify spot to be used.
		int colAlter getColAlter(Who, direction);
		if (pieceCol == 0) {
			if (isPlayer && !board[pieceRow][pieceCol].getOccupier().equals("X"))
				System.out.println("there are no Left spots for " + board[pieceRow][pieceCol].getOccupier() + ", you are at edge");
			can = false;
		} else {
			if (Main.inBounds((pieceRow + rowAlter), (pieceCol + colAlter)) && board[pieceRow + rowAlter][pieceCol + colAlter] != null && board[pieceRow + rowAlter][pieceCol + colAlter].getOccupier().equals("X")) {
				can = true;
			} else {
				if (isPlayer && !board[pieceRow][pieceCol].getOccupier().equals("X"))
					System.out.println("the left spot of " + board[pieceRow][pieceCol].getOccupier() + "  is occupied");
				can = false;
			}
		}
		return can;
	}
	
}
