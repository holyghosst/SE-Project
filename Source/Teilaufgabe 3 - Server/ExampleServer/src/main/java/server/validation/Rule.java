package server.validation;

import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.InvalidPlayerHalfMapException;

public interface Rule {
    public void validateMap(PlayerHalfMap halfMap) throws InvalidPlayerHalfMapException;
}
