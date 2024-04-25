package map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HalfMap {
    private Map<Coordinates, MapField> fields = new HashMap<Coordinates, MapField>();
    private Coordinates startPosition;

    public Coordinates getStartPosition() {
	return startPosition;
    }

    public void setFortress(Coordinates startPosition) {
	this.startPosition = startPosition;
	fields.put(startPosition, new MapField(Terrain.GRASS, FieldObject.PLAYER_FORTRESS));
    }

    public Map<Coordinates, MapField> getFields() {
	return Collections.unmodifiableMap(fields);
    }

    public MapField getField(Coordinates coordinates) {
	return fields.get(coordinates);
    }

    public MapField getField(int xPosition, int yPosition) {
	return fields.get(new Coordinates(xPosition, yPosition));
    }

    public void addFieldToCoordinates(Coordinates coordinates, MapField field) {
	fields.put(coordinates, field);

    }

    public HalfMap(Map<Coordinates, MapField> fields, Coordinates startPosition) {
	super();
	this.fields = fields;
	setFortress(startPosition);
    }

    @Override
    public String toString() {
	StringBuilder stringBuilder = new StringBuilder();

	fields.forEach((key, value) -> stringBuilder.append(value.toString()));
	return stringBuilder.toString();
    }

    public void printOut() {
	String mapString = this.toString();
	for (int i = 0; i < mapString.length(); i++) {
	    if (i % 10 == 0 && i != 0) {
		System.out.println();
	    }
	    System.out.print(mapString.charAt(i));
	}
	System.out.println();
    }
}
