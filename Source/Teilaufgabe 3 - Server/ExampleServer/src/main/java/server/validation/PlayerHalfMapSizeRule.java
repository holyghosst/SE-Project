package server.validation;

import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.InvalidPlayerHalfMapException;

public class PlayerHalfMapSizeRule implements Rule {
    private final int playerHalfMapSize = 50;

    @Override
    public void validateMap(PlayerHalfMap halfMap) throws InvalidPlayerHalfMapException {
	if (halfMap.getMapNodes().size() != playerHalfMapSize)
	    throw new InvalidPlayerHalfMapException("InvalidHalfMap",
		    "Size of the halfmap was " + halfMap.getMapNodes().size() + ", expected " + playerHalfMapSize);

    }

}
