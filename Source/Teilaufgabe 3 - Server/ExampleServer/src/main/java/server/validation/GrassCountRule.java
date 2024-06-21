package server.validation;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import server.exceptions.InvalidPlayerHalfMapException;

public class GrassCountRule implements Rule {
    private final int grassTerrainMinimum = 24;

    @Override
    public void validateMap(PlayerHalfMap halfMap) throws InvalidPlayerHalfMapException {
	int grassCount = 0;
	for (PlayerHalfMapNode node : halfMap.getMapNodes()) {
	    if (node.getTerrain() == ETerrain.Grass)
		grassCount++;
	}
	if (grassCount < grassTerrainMinimum)
	    throw new InvalidPlayerHalfMapException("InvalidHalfMap",
		    "Expected at least" + grassTerrainMinimum + " grass fields, but found " + grassCount);

    }

}
