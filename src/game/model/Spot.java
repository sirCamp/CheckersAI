package game.model;

import game.model.Piece;

public class Spot {

	private String name;
	private int value;
	private Piece occupier;

	
	public Spot(String name,int value, Piece occupier)
	{
		this.name = name;
		this.value = value;
		this.occupier = occupier;
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

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
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
