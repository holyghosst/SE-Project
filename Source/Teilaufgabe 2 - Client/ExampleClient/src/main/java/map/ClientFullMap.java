package map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ClientFullMap extends AbstractMap {
    private static int maximalX = 0;
    private static int maximalY = 0;
    private Map<Coordinates, MapField> fields = new HashMap<Coordinates, MapField>();

    private void setMaxCoordinates(Coordinates coordinates) {
	if (coordinates.getX() > maximalX) {
	    maximalX = coordinates.getX();
	}
	if (coordinates.getY() > maximalY) {
	    maximalY = coordinates.getY();
	}
    }

    public MapField getField(int xPosition, int yPosition) {
	return fields.get(new Coordinates(xPosition, yPosition));
    }

    public void addField(Coordinates coordinates, MapField field) {
	setMaxCoordinates(coordinates);
	fields.put(coordinates, field);
    }

    public Map<Coordinates, MapField> getFields() {
	return Collections.unmodifiableMap(fields);
    }

    public void printOut() {
	StringBuilder stringBuilder = new StringBuilder();
	for (int y = 0; y < maximalY + 1; y++) {
	    for (int x = 0; x < maximalX + 1; x++) {
		stringBuilder.append(getField(x, y).toString());
	    }
	}
	String mapString = stringBuilder.toString();
	for (int i = 0; i < mapString.length(); i++) {
	    if (i % (maximalX + 1) == 0 && i != 0) {
		System.out.println();
	    }
	    System.out.print(mapString.charAt(i));
	}
	System.out.println();
    }

}
