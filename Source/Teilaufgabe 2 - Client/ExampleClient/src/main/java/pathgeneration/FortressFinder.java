package pathgeneration;

import map.ClientFullMap;
import map.PartialMap;

public class FortressFinder extends AbstractFinder {
    private PartialMap enemyHalf;

    @Override
    public void setFullMap(ClientFullMap map) {
	this.map = map;
	this.map.findMapHalves();
	this.enemyHalf = this.map.getEnemyHalf();

    }

    @Override
    protected boolean checkIfLegalCoordinates(int x, int y) {
	return (x >= enemyHalf.getxMin() && x <= enemyHalf.getxMax() && y >= enemyHalf.getyMin()
		&& y <= enemyHalf.getyMax());
    }

    @Override
    protected boolean isOfInterest(int x, int y) {
	return (checkIfLegalCoordinates(x, y) && isPassable(x, y));
    }

}
