package map;

public class PartialMap {
    private int xMin;
    private int xMax;
    private int yMin;
    private int yMax;

    public PartialMap(int xMin, int xMax, int yMin, int yMax) {
	this.xMin = xMin;
	this.xMax = xMax;
	this.yMin = yMin;
	this.yMax = yMax;
    }

    public int getxMin() {
	return xMin;
    }

    public int getxMax() {
	return xMax;
    }

    public int getyMin() {
	return yMin;
    }

    public int getyMax() {
	return yMax;
    }

    public boolean included(int x, int y) {
	return (x >= xMin && x <= xMax && y >= yMin && y <= yMax);
    }

    public boolean included(Coordinates coordinates) {
	return (coordinates.getX() >= xMin && coordinates.getX() <= xMax && coordinates.getY() >= yMin
		&& coordinates.getY() <= yMax);
    }

}
