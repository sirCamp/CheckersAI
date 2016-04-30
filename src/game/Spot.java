package game;

public class Spot {

    private int value;
    private String occupier;
    private String name;


    public Spot(String name, int value, String occupier) {
        this.value = value;
        this.occupier = occupier;
        this.name = name;

    }


    public int getValue() {
        return value;
    }

    public String getOccupier() {
        return occupier;
    }

    public void setOccupier(String newOccupier) {
        occupier = newOccupier;
    }

    public String getName() {
        return name;
    }

}
