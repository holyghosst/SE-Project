package server.validation;

import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import server.exceptions.InvalidPlayerHalfMapException;

public class MountainCountRule implements Rule {
    private final int mountainTerrainMinimum = 5;

    @Override
    public void validateMap(PlayerHalfMap halfMap) throws InvalidPlayerHalfMapException {
	int mountainCount = 0;
	for (PlayerHalfMapNode node : halfMap.getMapNodes()) {
	    if (node.getTerrain() == ETerrain.Mountain)
		mountainCount++;
	}
	if (mountainCount < mountainTerrainMinimum)
	    throw new InvalidPlayerHalfMapException("InvalidHalfMap",
		    "Expected at least" + mountainTerrainMinimum + " mountain fields, but found " + mountainCount);

    }

}
