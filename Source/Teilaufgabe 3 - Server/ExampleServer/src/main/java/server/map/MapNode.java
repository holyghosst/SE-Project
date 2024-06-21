package server.map;

import java.util.Objects;

public class MapNode {
    private int x;
    private int y;
    private Terrain terrain;
    private TreasureState treasureState;
    private FortressState fortressState;
    private PlayerPositionState playerPositionState;

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public Terrain getTerrain() {
	return terrain;
    }

    public TreasureState getTreasureState() {
	return treasureState;
    }

    public void setTreasureState(TreasureState treasureState) {
	this.treasureState = treasureState;
    }

    public void setPlayerPositionState(PlayerPositionState state) {
	this.playerPositionState = state;
    }

    public void setFortressState(FortressState state) {
	this.fortressState = state;
    }

    public FortressState getFortressState() {
	return fortressState;
    }

    public PlayerPositionState getPlayerPositionState() {
	return playerPositionState;
    }

    public MapNode(int x, int y, Terrain terrain, TreasureState treasureState, FortressState fortressState,
	    PlayerPositionState playerPositionState) {
	super();
	this.x = x;
	this.y = y;
	this.terrain = terrain;
	this.treasureState = treasureState;
	this.fortressState = fortressState;
	this.playerPositionState = playerPositionState;
    }

    @Override
    public int hashCode() {
	return Objects.hash(fortressState, playerPositionState, terrain, treasureState, x, y);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	MapNode other = (MapNode) obj;
	if (x != other.x)
	    return false;
	if (y != other.y)
	    return false;
	return true;
    }

}
