package map;

public class MapField {
    private Terrain terrainType;
    private FieldObject fieldObject;

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

    public MapField(Terrain terrainType, FieldObject fieldObject) {
	super();
	this.terrainType = terrainType;
	this.fieldObject = fieldObject;
    }

    @Override
    public String toString() {
	if (fieldObject == FieldObject.PLAYER_FORTRESS) {
	    return "F";
	} else if (fieldObject == FieldObject.PLAYER_TREASURE) {
	    return "$";
	} else if (terrainType == Terrain.WATER) {
	    return "~";
	} else if (terrainType == Terrain.MOUNTAIN) {
	    return "M";
	} else {
	    return "G";
	}
    }

}
