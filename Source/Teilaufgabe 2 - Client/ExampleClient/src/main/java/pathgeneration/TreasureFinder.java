package pathgeneration;

import map.ClientFullMap;
import map.PartialMap;

public class TreasureFinder extends AbstractFinder {
    private PartialMap myHalf;

    @Override
    public void setFullMap(ClientFullMap map) {
	this.map = map;
	this.map.findMapHalves();
	this.myHalf = map.getMyHalf();
    }

    @Override
    protected boolean checkIfLegalCoordinates(int x, int y) {
	return (x >= myHalf.getxMin() && x <= myHalf.getxMax() && y >= myHalf.getyMin() && y <= myHalf.getyMax());
    }

    @Override
    protected boolean isOfInterest(int x, int y) {
	return (checkIfLegalCoordinates(x, y) && isPassable(x, y));
    }

}
