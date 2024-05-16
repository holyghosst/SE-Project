package map;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ClientFullMap {
    private int maximalX = 0;
    private int maximalY = 0;
    private PartialMap myHalf;
    private PartialMap enemyHalf;

    private Map<Coordinates, MapField> fields = new HashMap<Coordinates, MapField>();
    private Coordinates startPosition;
    private Coordinates currentPosition;

    private void setMaxCoordinates(Coordinates coordinates) {
	if (coordinates.getX() > maximalX) {
	    maximalX = coordinates.getX();
	}
	if (coordinates.getY() > maximalY) {
	    maximalY = coordinates.getY();
	}
    }

    public int getMaximalX() {
	return maximalX;
    }

    public int getMaximalY() {
	return maximalY;
    }

    public MapField getField(int xPosition, int yPosition) {
	return fields.get(new Coordinates(xPosition, yPosition));
    }

    public MapField getField(Coordinates coordinates) {
	return fields.get(coordinates);
    }

    public Coordinates getCurrentPosition() {
	return currentPosition;
    }

    public Coordinates getStartPosition() {
	return startPosition;
    }

    public void findMapHalves() {

	int xMaxForVerticalMap = 9, yMaxForVerticalMap = 9, horizontalMapDivider = 9;
	int verticalMapDivider = 4;

	if (maximalX == xMaxForVerticalMap) {
	    if (startPosition.getY() <= verticalMapDivider) {
		myHalf = new PartialMap(0, maximalX, 0, verticalMapDivider);
		enemyHalf = new PartialMap(0, maximalX, verticalMapDivider + 1, yMaxForVerticalMap);

	    } else {
		myHalf = new PartialMap(0, maximalX, verticalMapDivider + 1, yMaxForVerticalMap);
		enemyHalf = new PartialMap(0, maximalX, 0, verticalMapDivider);
	    }
	} else {
	    if (startPosition.getX() < horizontalMapDivider) {
		myHalf = new PartialMap(0, horizontalMapDivider, 0, maximalY);
		enemyHalf = new PartialMap(horizontalMapDivider, maximalX, 0, maximalY);
	    } else {
		myHalf = new PartialMap(horizontalMapDivider, maximalX, 0, maximalY);
		enemyHalf = new PartialMap(0, horizontalMapDivider, 0, maximalY);
	    }
	}
    }

    public PartialMap getMyHalf() {
	return myHalf;
    }

    public PartialMap getEnemyHalf() {
	return enemyHalf;
    }

    public boolean onEnemyTerritory() {
	int xCurrent = currentPosition.getX();
	int yCurrent = currentPosition.getY();
	return (xCurrent >= enemyHalf.getxMin() && xCurrent <= enemyHalf.getxMax() && yCurrent >= enemyHalf.getyMin()
		&& yCurrent <= enemyHalf.getyMax());
    }

    public void addField(Coordinates coordinates, MapField field) {
	setMaxCoordinates(coordinates);
	if (field.playerPresent()) {
	    currentPosition = coordinates;
	}
	if (field.hasFortress()) {
	    startPosition = coordinates;
	}
	fields.put(coordinates, field);
    }

    public Map<Coordinates, MapField> getFields() {
	return Collections.unmodifiableMap(fields);
    }

}
