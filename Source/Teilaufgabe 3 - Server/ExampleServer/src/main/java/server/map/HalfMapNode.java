package server.map;

import java.util.Objects;

public class HalfMapNode {
    private final int x;
    private final int y;
    private final boolean fortressPresent;
    private final Terrain terrain;

    public HalfMapNode(int x, int y, boolean fortressPresent, Terrain terrain) {
	this.x = x;
	this.y = y;
	this.fortressPresent = fortressPresent;
	this.terrain = terrain;
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public boolean fortressPresent() {
	return fortressPresent;
    }

    public Terrain getTerrain() {
	return terrain;
    }

    @Override
    public int hashCode() {
	return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	HalfMapNode other = (HalfMapNode) obj;
	return x == other.x && y == other.y;
    }

}
