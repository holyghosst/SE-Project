package server.validation;

import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import server.exceptions.InvalidPlayerHalfMapException;

public class PlayerHalfMapDimensionsRule implements Rule {
    private final int xMax = 9;
    private final int yMax = 4;

    @Override
    public void validateMap(PlayerHalfMap halfMap) throws InvalidPlayerHalfMapException {
	int foundXMax = 0;
	int foundYMax = 0;
	for (PlayerHalfMapNode node : halfMap.getMapNodes()) {
	    if (node.getX() > foundXMax)
		foundXMax = node.getX();
	    if (node.getY() > foundYMax)
		foundYMax = node.getY();
	}
	if (foundXMax != xMax || foundYMax != yMax) {
	    throw new InvalidPlayerHalfMapException("InvalidHalfMap", "Dimensions " + xMax + "x" + yMax
		    + " were expected, but were found to be " + foundXMax + "x" + foundYMax);
	}
    }

}
