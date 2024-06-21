package server.validation;

import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import server.exceptions.InvalidPlayerHalfMapException;

public class FortressCountRule implements Rule {
    private final int fortressNumber = 1;

    @Override
    public void validateMap(PlayerHalfMap halfMap) throws InvalidPlayerHalfMapException {
	int fortressCount = 0;
	for (PlayerHalfMapNode node : halfMap.getMapNodes()) {
	    if (node.isFortPresent())
		fortressCount++;
	}
	if (fortressCount < fortressNumber)
	    throw new InvalidPlayerHalfMapException("InvalidHalfMap",
		    "Expected " + fortressNumber + "fortresses, but found " + fortressCount);

    }

}
