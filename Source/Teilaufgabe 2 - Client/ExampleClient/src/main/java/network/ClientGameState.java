package network;

import map.ClientFullMap;

public class ClientGameState {
    private String gameStateID;
    private ClientPlayerState playerState;
    private ClientFullMap map;

    public ClientGameState(String gameStateID, ClientPlayerState playerState, ClientFullMap map) {
	super();
	this.gameStateID = gameStateID;
	this.playerState = playerState;
	this.map = map;
    }

    public String getGameStateID() {
	return gameStateID;
    }

    public ClientPlayerState getPlayerState() {
	return playerState;
    }

    public ClientFullMap getMap() {
	return map;
    }

    @Override
    public String toString() {
	return "ClientGameState [gameStateID=" + gameStateID + ", playerState=" + playerState + "]";
    }

}
