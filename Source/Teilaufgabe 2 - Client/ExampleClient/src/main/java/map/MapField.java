package map;

public class MapField {
    private Terrain terrainType;
    private TreasureState treasureState;
    private FieldObject fieldObject;
    private PlayerPosition playerPosition;

    public Terrain getTerrainType() {
	return terrainType;
    }

    public void setTerrainType(Terrain terrainType) {
	this.terrainType = terrainType;
    }

    public void setFieldObject(FieldObject fieldObject) {
	this.fieldObject = fieldObject;
    }

    public boolean hasFortress() {
	return (fieldObject == FieldObject.PLAYER_FORTRESS);
    }

    public boolean playerPresent() {
	return (playerPosition == PlayerPosition.PRESENT || playerPosition == PlayerPosition.BOTH_PRESENT);
    }

    public MapField(Terrain terrainType, FieldObject fieldObject) {
	super();
	this.terrainType = terrainType;
	this.fieldObject = fieldObject;
    }

    public MapField(Terrain terrainType, FieldObject fieldObject, PlayerPosition playerPosition) {
	super();
	this.terrainType = terrainType;
	this.fieldObject = fieldObject;
	this.playerPosition = playerPosition;
    }

    public MapField(Terrain terrainType, FieldObject fieldObject, PlayerPosition playerPosition,
	    TreasureState treasureState) {
	super();
	this.terrainType = terrainType;
	this.fieldObject = fieldObject;
	this.playerPosition = playerPosition;
	this.treasureState = treasureState;
    }

    public String printOut() {
	if (fieldObject == FieldObject.PLAYER_FORTRESS) {
	    return "F ";
	} else if (playerPosition == PlayerPosition.PRESENT) {
	    return "+ ";
	} else if (playerPosition == PlayerPosition.ENEMY_PRESENT) {
	    return "- ";
	} else if (playerPosition == PlayerPosition.BOTH_PRESENT) {
	    return "# ";
	} else if (treasureState == TreasureState.PLAYER_TREASURE) {
	    return "$ ";
	} else if (terrainType == Terrain.WATER) {
	    return "~ ";
	} else if (terrainType == Terrain.MOUNTAIN) {
	    return "^ ";
	} else {
	    return "G ";
	}
    }

}
