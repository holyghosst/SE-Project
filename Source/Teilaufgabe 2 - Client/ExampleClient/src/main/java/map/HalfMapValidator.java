package map;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

public class HalfMapValidator {

    // TAKEN FROM 1
    // Quelle:
    // https://web.piyushgarg.in/2021/09/flood-fill-4-directional-using-java.html
    // Ich habe grundsätzlich die Idee mit Queue und while übernommen und
    // entsprechend angepasst.

    private boolean legalCoordinates(int x, int y) {
	return (x > -1 && x < 10 && y > -1 && y < 5);
    }

    private boolean legalCoordinates(Coordinates c) {
	return legalCoordinates(c.getX(), c.getY());
    }

    private boolean passableField(MapField field) {
	return (field.getTerrainType() != Terrain.WATER);
    }

    public boolean validateNoIslands(Map<Coordinates, MapField> fields, Coordinates startPosition) {
	List<Coordinates> passableFields = new ArrayList<Coordinates>();
	for (Entry<Coordinates, MapField> entry : fields.entrySet()) {
	    if (entry.getValue().getTerrainType() != Terrain.WATER) {
		passableFields.add(entry.getKey());
	    }
	}

	// TAKEN FROM START 1

	List<Coordinates> visitedCoordinates = new ArrayList<Coordinates>();
	Queue<Coordinates> queue = new ArrayDeque<Coordinates>();
	queue.add(startPosition);

	while (!queue.isEmpty()) {
	    Coordinates current = queue.poll();
	    int xCoordinate = current.getX();
	    int yCoordinate = current.getY();

	    if (passableField(fields.get(current)) && !visitedCoordinates.contains(current)) {
		visitedCoordinates.add(current);
		Coordinates leftField = new Coordinates(xCoordinate - 1, yCoordinate);
		Coordinates rightField = new Coordinates(xCoordinate + 1, yCoordinate);
		Coordinates downField = new Coordinates(xCoordinate, yCoordinate + 1);
		Coordinates upperField = new Coordinates(xCoordinate, yCoordinate - 1);

		if (legalCoordinates(leftField) && passableField(fields.get(leftField))) {
		    queue.add(leftField);
		}
		if (legalCoordinates(rightField) && passableField(fields.get(rightField))) {
		    queue.add(rightField);
		}
		if (legalCoordinates(downField) && passableField(fields.get(downField))) {
		    queue.add(downField);
		}
		if (legalCoordinates(upperField) && passableField(fields.get(upperField))) {
		    queue.add(upperField);
		}

	    } else {
		continue;
	    }
	    // TAKEN FROM END 1
	}
	return (visitedCoordinates.containsAll(passableFields));
    }

    public boolean validateMapEdges(Map<Coordinates, MapField> fields) {
	int horizontalEdgeMinimum = 6;
	int verticalEdgeMinimum = 3;

	int upperEdgeCount = 0;
	int lowerEdgeCount = 0;
	int leftEdgeCount = 0;
	int rightEdgeCount = 0;

	for (int xCoordinate = 0; xCoordinate < 10; xCoordinate++) {
	    if (passableField(fields.get(new Coordinates(xCoordinate, 0))))
		upperEdgeCount++;
	    if (passableField(fields.get(new Coordinates(xCoordinate, 4))))
		lowerEdgeCount++;
	}
	for (int yCoordinate = 0; yCoordinate < 5; yCoordinate++) {
	    if (passableField(fields.get(new Coordinates(0, yCoordinate))))
		leftEdgeCount++;
	    if (passableField(fields.get(new Coordinates(9, yCoordinate))))
		rightEdgeCount++;
	}
	return (upperEdgeCount >= horizontalEdgeMinimum && lowerEdgeCount >= horizontalEdgeMinimum
		&& rightEdgeCount >= verticalEdgeMinimum && leftEdgeCount >= verticalEdgeMinimum);

    }
}
