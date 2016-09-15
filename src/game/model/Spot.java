package game.model;

import game.model.Piece;

public class Spot implements Cloneable{

	private String name;
	private Integer value;
	private Piece occupier;

	
	public Spot(String name,int value, Piece occupier)
	{
		this.name = name;
		this.value = value;
		this.occupier = occupier;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Spot s = (Spot) super.clone();
		s.name = new String(this.name);
		s.value = new Integer(this.value);
		s.occupier = (Piece) this.occupier.clone();
		return s;
	}

	public Board copy() throws CloneNotSupportedException {
		return (Board) this.clone();
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
