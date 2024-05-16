package pathgeneration;

import java.util.Queue;

import map.ClientFullMap;
import map.Coordinates;

public class Pathfinder extends AbstractFinder {
    private TreasureFinder treasureFinder = new TreasureFinder();
    private FortressFinder fortressFinder = new FortressFinder();

    public void findTreasure(Coordinates currentPosition) {
	treasureFinder.findGoal(currentPosition);
    }

    public Queue<PlayerMoveDirection> getTreasureFinderMovements() {
	return treasureFinder.getMovements();
    }

    public void findFortress(Coordinates currentPosition) {
	fortressFinder.findGoal(currentPosition);
    }

    public Queue<PlayerMoveDirection> getFortressFinderMovements() {
	return fortressFinder.getMovements();
    }

    @Override
    public void setFullMap(ClientFullMap map) {
	treasureFinder.setFullMap(map);
	fortressFinder.setFullMap(map);
	this.map = map;
    }

    @Override
    protected boolean checkIfLegalCoordinates(int x, int y) {
	return (x >= 0 && x <= map.getMaximalX() && y >= 0 && y <= map.getMaximalY());
    }

    @Override
    protected boolean isOfInterest(int x, int y) {
	return (checkIfLegalCoordinates(x, y) && isPassable(x, y));
    }

    public boolean isOnEnemyTerritory() {
	return (map.getCurrentPosition().getX() >= map.getEnemyHalf().getxMin()
		&& map.getCurrentPosition().getX() <= map.getEnemyHalf().getxMax()
		&& map.getCurrentPosition().getY() >= map.getEnemyHalf().getyMin()
		&& map.getCurrentPosition().getY() <= map.getEnemyHalf().getyMax());
    }

}
