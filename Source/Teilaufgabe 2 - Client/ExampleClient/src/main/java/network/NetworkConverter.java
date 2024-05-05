package network;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import map.ClientFullMap;
import map.Coordinates;
import map.FieldObject;
import map.HalfMap;
import map.MapField;
import map.PlayerPosition;
import map.Terrain;
import map.TreasureState;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.FullMapNode;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;

public class NetworkConverter {

    private UniquePlayerIdentifier playerID;

    public void setPlayerID(UniquePlayerIdentifier playerID) {
	this.playerID = playerID;
    }

    public ETerrain convertTerrain(Terrain terrain) throws ConverterException {
	switch (terrain) {
	case GRASS:
	    return ETerrain.Grass;
	case MOUNTAIN:
	    return ETerrain.Mountain;
	case WATER:
	    return ETerrain.Water;
	default:
	    throw new ConverterException("Failed to convert Terrain.");
	}
    }

    public Terrain convertETerrain(ETerrain terrain) throws ConverterException {
	switch (terrain) {
	case Grass:
	    return Terrain.GRASS;
	case Mountain:
	    return Terrain.MOUNTAIN;
	case Water:
	    return Terrain.WATER;
	default:
	    throw new ConverterException("Failed to convert ETerrain. ");
	}
    }

    public ClientPlayerGameState convertEPlayerGameState(EPlayerGameState playerGameState) throws ConverterException {
	switch (playerGameState) {
	case Won:
	    return ClientPlayerGameState.Won;
	case Lost:
	    return ClientPlayerGameState.Lost;
	case MustAct:
	    return ClientPlayerGameState.MustAct;
	case MustWait:
	    return ClientPlayerGameState.MustWait;
	default:
	    throw new ConverterException("Failed to convert EPlayerGameState.");
	}

    }

    public PlayerPosition convertEPlayerPositionState(EPlayerPositionState playerPositionState)
	    throws ConverterException {
	switch (playerPositionState) {
	case BothPlayerPosition:
	    return PlayerPosition.BOTH_PRESENT;
	case EnemyPlayerPosition:
	    return PlayerPosition.ENEMY_PRESENT;
	case MyPlayerPosition:
	    return PlayerPosition.PRESENT;
	case NoPlayerPresent:
	    return PlayerPosition.NOT_PRESENT;
	default:
	    throw new ConverterException("Failed to convert EPlayerPositionState");
	}
    }

    public FieldObject convertEFortState(EFortState fortState) throws ConverterException {
	switch (fortState) {
	case EnemyFortPresent:
	    return FieldObject.ENEMY_FORTRESS;
	case MyFortPresent:
	    return FieldObject.PLAYER_FORTRESS;
	case NoOrUnknownFortState:
	    return FieldObject.UNKNOWN;
	default:
	    throw new ConverterException("Failed to convert EFortState");

	}
    }

    public TreasureState convertETreasureState(ETreasureState treasureState) throws ConverterException {
	switch (treasureState) {
	case MyTreasureIsPresent:
	    return TreasureState.PLAYER_TREASURE;
	case NoOrUnknownTreasureState:
	    return TreasureState.NO_UNKNOWN;
	default:
	    throw new ConverterException("Failed to convert ETreasureState");
	}
    }

    public PlayerHalfMap convertHalfMap(HalfMap halfMap) throws ConverterException {
	Collection<PlayerHalfMapNode> entries = new HashSet<>();

	for (Map.Entry<Coordinates, MapField> field : halfMap.getFields().entrySet()) {
	    int xCoordinate = field.getKey().getX();
	    int yCoordinate = field.getKey().getY();
	    boolean fortPresent = field.getValue().hasFortress();
	    Terrain fieldTerrain = field.getValue().getTerrainType();

	    entries.add(new PlayerHalfMapNode(xCoordinate, yCoordinate, fortPresent, convertTerrain(fieldTerrain)));
	}
	return new PlayerHalfMap(playerID, entries);
    }

    public MapField convertFullMapNode(FullMapNode fullMapNode) throws ConverterException {
	Terrain terrain = convertETerrain(fullMapNode.getTerrain());
	FieldObject fieldObject = convertEFortState(fullMapNode.getFortState());
	TreasureState treasureState = convertETreasureState(fullMapNode.getTreasureState());
	PlayerPosition playerPosition = convertEPlayerPositionState(fullMapNode.getPlayerPositionState());

	return new MapField(terrain, fieldObject, playerPosition, treasureState);

    }

    public Coordinates convertFullMapNodeCoordinates(FullMapNode fullMapNode) {
	Coordinates coordinates = new Coordinates(fullMapNode.getX(), fullMapNode.getY());
	return coordinates;

    }

    public ClientFullMap convertFullMap(FullMap fullMap) throws ConverterException {
	ClientFullMap clientFullMap = new ClientFullMap();
	Collection<FullMapNode> nodes = fullMap.getMapNodes();
	for (FullMapNode node : nodes) {
	    clientFullMap.addField(convertFullMapNodeCoordinates(node), convertFullMapNode(node));
	}

	return clientFullMap;
    }

    public ClientPlayerState convertPlayerState(PlayerState playerState) throws ConverterException {
	boolean collectedTreasure = playerState.hasCollectedTreasure();
	ClientPlayerGameState clientPlayerGameState = convertEPlayerGameState(playerState.getState());

	return new ClientPlayerState(collectedTreasure, clientPlayerGameState);
    }

    public ClientGameState convertGameState(GameState gameState) throws ConverterException {
	String gameStateID = gameState.getGameStateId();
	PlayerState playerState = gameState.getPlayers().stream().filter(player -> player.equals(playerID)).findFirst()
		.get();
	ClientPlayerState convertedPlayerState = convertPlayerState(playerState);
	ClientFullMap map = convertFullMap(gameState.getMap());

	return new ClientGameState(gameStateID, convertedPlayerState, map);

    }

}
