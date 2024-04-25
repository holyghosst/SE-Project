package network;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import map.Coordinates;
import map.HalfMap;
import map.MapField;
import map.Terrain;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;

public class NetworkConverter {
    private NetworkCommunication networkSocket;

    public ETerrain convertTerrain(Terrain terrain) {
	ETerrain returnValue = null;
	if (terrain == Terrain.WATER) {
	    returnValue = ETerrain.Water;
	} else if (terrain == Terrain.GRASS) {
	    returnValue = ETerrain.Grass;
	}
	if (terrain == Terrain.MOUNTAIN) {
	    returnValue = ETerrain.Mountain;
	}
	return returnValue;
    }

    public PlayerHalfMap convertHalfMap(String playerId, HalfMap halfMap) {
	Collection<PlayerHalfMapNode> entries = new HashSet<>();

	for (Map.Entry<Coordinates, MapField> field : halfMap.getFields().entrySet()) {
	    int xCoordinate = field.getKey().getX();
	    int yCoordinate = field.getKey().getY();
	    boolean fortPresent = field.getValue().hasFortress();
	    Terrain fieldTerrain = field.getValue().getTerrainType();

	    entries.add(new PlayerHalfMapNode(xCoordinate, yCoordinate, fortPresent, convertTerrain(fieldTerrain)));
	}
	return new PlayerHalfMap(playerId, entries);
    }

    public String getPlayerID() {
	return networkSocket.getPlayerID().getUniquePlayerID();
    }

    public void registerPlayer(String firstName, String lastName, String studentUAccount, String gameID) {
	PlayerRegistration playerReg = new PlayerRegistration(firstName, lastName, studentUAccount);
	networkSocket.registerPlayer(playerReg, gameID);
    }

    public void sendHalfMap(HalfMap halfMap) {
	networkSocket.sendPlayerHalfMap(convertHalfMap(getPlayerID(), halfMap));
    }

    public NetworkConverter(String serverBaseURL) {
	this.networkSocket = new NetworkCommunication(serverBaseURL);
    }

    public boolean isMyTurn() {
	GameState gameState = networkSocket.getGameState();
	PlayerState playerState = gameState.getPlayers().stream()
		.filter(player -> player.equals(networkSocket.getPlayerID())).findFirst().get();
	boolean state = playerState.getState() == EPlayerGameState.MustAct;
	return state;
    }

}
