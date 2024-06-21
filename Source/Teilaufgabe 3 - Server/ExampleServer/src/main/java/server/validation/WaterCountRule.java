package server.validation;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import server.exceptions.InvalidPlayerHalfMapException;

public class WaterCountRule implements Rule {
    private final int waterTerrainMinimum = 7;

    @Override
    public void validateMap(PlayerHalfMap halfMap) throws InvalidPlayerHalfMapException {
	int waterCount = 0;
	for (PlayerHalfMapNode node : halfMap.getMapNodes()) {
	    if (node.getTerrain() == ETerrain.Water)
		waterCount++;
	}
	if (waterCount < waterTerrainMinimum)
	    throw new InvalidPlayerHalfMapException("InvalidHalfMap",
		    "Expected at least" + waterTerrainMinimum + " water fields, but found " + waterCount);

    }

}
