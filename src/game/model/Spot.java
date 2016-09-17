package game.model;

public class Spot implements Cloneable{

	private String name;
	private Piece occupier;

	public Spot(String name, Piece occupier) {
		this.name = name;
		this.occupier = occupier;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Spot s = (Spot) super.clone();
		s.name = this.name;
		s.occupier = (Piece) this.occupier.clone();
		return s;
	}

	public Piece getOccupier() {
		return occupier;
	}

	public void setOccupier(Piece occupier) {
		this.occupier = occupier;
	}

	public String getName() { return name; }

}
