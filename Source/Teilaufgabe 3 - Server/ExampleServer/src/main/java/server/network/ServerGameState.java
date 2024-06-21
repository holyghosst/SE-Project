package server.network;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import server.map.ServerFullMap;

public class ServerGameState {
    private ServerFullMap map;
    private Set<ServerPlayerState> players;

    private String gameStateID;

    public ServerFullMap getMap() {
	return map;
    }

    public Collection<ServerPlayerState> getPlayers() {
	return players;
    }

    public String getGameStateID() {
	return gameStateID;
    }

    public ServerGameState(ServerFullMap map, Set<ServerPlayerState> players, String gameStateID) {
	this.map = map;
	this.players = players;
	this.gameStateID = gameStateID;
    }

    public ServerGameState() {
	this.gameStateID = UUID.randomUUID().toString();
    }
}
