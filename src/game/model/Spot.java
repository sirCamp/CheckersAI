package game.model;

import game.model.Piece;

public class Spot {
	
	private int value;
	private Piece occupier;
	private String name;
	
	
	public Spot(String name,int value, Piece occupier)
	{
		this.value = value;
		this.occupier = occupier;
		this.name = name;
		
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Piece getOccupier() {
		return occupier;
	}

	public void setOccupier(Piece occupier) {
		this.occupier = occupier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
