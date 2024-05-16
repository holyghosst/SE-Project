package pathgeneration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import map.ClientFullMap;
import map.Coordinates;
import map.Terrain;

public abstract class AbstractFinder {

    // TAKEN FROM 2
    // Quelle: https://en.wikipedia.org/wiki/Depth-first_search#Pseudocode
    // Die generelle Idee von der Tiefensuche war mir bekannt, ich wollte aber ganz
    // sicher sein und habe den Pseudocode in meiner Implementierung übernommen.
    // Natürlich wurde dieser an unsere Aufgabestellung angepasst, als Nachbarn
    // zählen z.B. die Wasserfelder nicht.
    // Den Algorithmus habe ich auch ergänzt, indem er die "Gegen"-Moves
    // erstellt(Backtracking),
    // damit die Spielfigur dann zurückgehen kann. Das war im Pseudocode, so wie ich
    // ihn verstanden habe, nicht
    // vorgesehen.

    protected ClientFullMap map;
    protected Set<Coordinates> visitedFields = new HashSet<Coordinates>();
    protected Stack<Coordinates> path = new Stack<Coordinates>();
    Queue<PlayerMoveDirection> movements = new LinkedList<PlayerMoveDirection>();

    public boolean isPassable(int x, int y) {
	Coordinates coordinates = new Coordinates(x, y);
	return (map.getField(coordinates).getTerrainType() == Terrain.GRASS
		|| map.getField(coordinates).getTerrainType() == Terrain.MOUNTAIN);
    }

    protected PlayerMoveDirection generateMovement(Coordinates current, Coordinates next) {
	int xCurrent = current.getX();
	int yCurrent = current.getY();

	int nextX = next.getX();
	int nextY = next.getY();

	if (nextX - xCurrent > 0) {
	    return PlayerMoveDirection.RIGHT;
	} else if (nextX - xCurrent < 0) {
	    return PlayerMoveDirection.LEFT;
	} else if (nextY - yCurrent > 0) {
	    return PlayerMoveDirection.DOWN;
	} else {
	    return PlayerMoveDirection.UP;
	}
    }

    protected void addMovements(Coordinates current, Coordinates next) {
	Terrain currentTerrain = map.getField(current).getTerrainType();
	Terrain nextTerrain = map.getField(next).getTerrainType();

	int movementCost = (currentTerrain == Terrain.GRASS ? 1 : 2);
	movementCost += (nextTerrain == Terrain.GRASS ? 1 : 2);

	for (; movementCost > 0; movementCost--) {
	    movements.add(generateMovement(current, next));
	}
    }

    public Queue<PlayerMoveDirection> getMovements() {
	return movements;
    }

    protected List<Coordinates> getNeighbors(Coordinates current) {
	List<Coordinates> neighbors = new ArrayList<Coordinates>();

	int xCurrent = current.getX();
	int yCurrent = current.getY();

	if (isOfInterest(xCurrent + 1, yCurrent)) {
	    neighbors.add(new Coordinates(xCurrent + 1, yCurrent));
	}
	if (isOfInterest(xCurrent, yCurrent + 1)) {
	    neighbors.add(new Coordinates(xCurrent, yCurrent + 1));
	}
	if (isOfInterest(xCurrent - 1, yCurrent)) {
	    neighbors.add(new Coordinates(xCurrent - 1, yCurrent));
	}
	if (isOfInterest(xCurrent, yCurrent - 1)) {
	    neighbors.add(new Coordinates(xCurrent, yCurrent - 1));
	}

	neighbors.sort((first, second) -> {
	    if (map.getField(first).getTerrainType() == Terrain.GRASS
		    && map.getField(second).getTerrainType() != Terrain.GRASS) {
		return -1;
	    } else if (map.getField(first).getTerrainType() != Terrain.GRASS
		    && map.getField(second).getTerrainType() == Terrain.GRASS) {
		return 1;
	    }
	    return 0;
	});
	return neighbors;
    }

    public void findGoal(Coordinates current) {
	// TAKEN FROM START 2

	if (visitedFields.contains(current))
	    return;
	path.push(current);
	visitedFields.add(current);

	List<Coordinates> neighbors = getNeighbors(current);
	for (Coordinates neighbor : neighbors) {
	    if (!visitedFields.contains(neighbor)) {
		addMovements(current, neighbor);
		findGoal(neighbor);
	    }
	}
	path.pop();
	if (!path.isEmpty()) {
	    addMovements(current, path.peek());
	}
	// TAKEN FROM END 2
    }

    public abstract void setFullMap(ClientFullMap map);

    protected abstract boolean checkIfLegalCoordinates(int x, int y);

    protected abstract boolean isOfInterest(int x, int y);

}
