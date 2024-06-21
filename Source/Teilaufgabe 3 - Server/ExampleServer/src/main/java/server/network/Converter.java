package server.network;

import java.util.HashSet;
import java.util.Set;

import messagesbase.UniqueGameIdentifier;
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
import server.exceptions.ConverterException;
import server.map.FortressState;
import server.map.HalfMap;
import server.map.HalfMapNode;
import server.map.MapNode;
import server.map.PlayerPositionState;
import server.map.ServerFullMap;
import server.map.Terrain;
import server.map.TreasureState;

public class Converter {
    public UniqueGameIdentifier convertServerUniqueGameIdentifierToUniqueGameIdentifier(
	    ServerUniqueGameIdentifier serverUniqueGameIdentifier) {
	return UniqueGameIdentifier.of(serverUniqueGameIdentifier.getUniqueGameID());
    }

    public ServerUniqueGameIdentifier convertUniqueGameIdentifierToServerUniqueGameIdentifier(
	    UniqueGameIdentifier gameID) {
	return new ServerUniqueGameIdentifier(gameID.getUniqueGameID());

    }

    public UniquePlayerIdentifier convertServerUniquePlayerIdentifierToUniquePlayerIdentifier(
	    ServerUniquePlayerIdentifier playerID) {
	return UniquePlayerIdentifier.of(playerID.getUniquePlayerID());
    }

    public ServerUniquePlayerIdentifier convertUniquePlayerIdentifierToServerUniquePlayerIdentifier(
	    UniquePlayerIdentifier playerID) {
	return new ServerUniquePlayerIdentifier(playerID.getUniquePlayerID());
    }

    public EPlayerGameState convertPlayerGameState(PlayerGameState playerGameState) throws ConverterException {
	switch (playerGameState) {
	case LOST:
	    return EPlayerGameState.Lost;
	case MUST_ACT:
	    return EPlayerGameState.MustAct;
	case MUST_WAIT:
	    return EPlayerGameState.MustWait;
	case WON:
	    return EPlayerGameState.Won;
	default:
	    throw new ConverterException("ConverterException", "Failed to convert PlayerGameState!");
	}
    }

    public EPlayerPositionState convertPlayerPositionState(PlayerPositionState state) throws ConverterException {
	switch (state) {
	case BOTH:
	    return EPlayerPositionState.BothPlayerPosition;
	case ENEMY_PLAYER:
	    return EPlayerPositionState.EnemyPlayerPosition;
	case MY_PLAYER:
	    return EPlayerPositionState.MyPlayerPosition;
	case NO_PLAYER:
	    return EPlayerPositionState.NoPlayerPresent;
	default:
	    throw new ConverterException("ConverterException", "Failed to convert PlayerPositionState!");
	}
    }

    public ETreasureState convertTreasureState(TreasureState state) throws ConverterException {
	switch (state) {
	case MY_TREASURE:
	    return ETreasureState.MyTreasureIsPresent;
	case NO_OR_UNKNOWN:
	    return ETreasureState.NoOrUnknownTreasureState;
	default:
	    throw new ConverterException("ConverterException", "Failed to convert TreasureState!");
	}
    }

    public EFortState convertFortressState(FortressState state) throws ConverterException {
	switch (state) {
	case ENEMY_FORT:
	    return EFortState.EnemyFortPresent;
	case NO_OR_UNKNOWN:
	    return EFortState.NoOrUnknownFortState;
	case MY_FORT:
	    return EFortState.MyFortPresent;
	default:
	    throw new ConverterException("ConverterException", "Failed to convert FortressState!");
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
	    throw new ConverterException("ConverterException", "Failed to convert ETerrain!");
	}
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
	    throw new ConverterException("ConverterException", "Failed to convert Terrain!");
	}
    }

    public PlayerState convertServerPlayerState(ServerPlayerState state) throws ConverterException {
	String firstName = state.getFirstName();
	String lastName = state.getLastName();
	String uAccount = state.getUaccount();
	EPlayerGameState playerGameState = convertPlayerGameState(state.getPlayerGameState());
	UniquePlayerIdentifier playerID = new UniquePlayerIdentifier(state.getUniquePlayerID());
	boolean collectedTreasure = state.hasCollectedTreasure();

	return new PlayerState(firstName, lastName, uAccount, playerGameState, playerID, collectedTreasure);
    }

    public GameState convertServerGameState(ServerGameState gameState) throws ConverterException {
	FullMap map = convertServerFullMap(gameState.getMap());
	Set<PlayerState> players = new HashSet<>();
	for (ServerPlayerState player : gameState.getPlayers()) {
	    players.add(convertServerPlayerState(player));
	}
	return new GameState(map, players, gameState.getGameStateID());
    }

    public FullMapNode convertMapNode(MapNode node) throws ConverterException {
	ETerrain terrain = convertTerrain(node.getTerrain());
	EPlayerPositionState playerPositionState = convertPlayerPositionState(node.getPlayerPositionState());
	ETreasureState treasureState = convertTreasureState(node.getTreasureState());
	EFortState fortState = convertFortressState(node.getFortressState());

	return new FullMapNode(terrain, playerPositionState, treasureState, fortState, node.getX(), node.getY());
    }

    public FullMap convertServerFullMap(ServerFullMap map) throws ConverterException {
	Set<FullMapNode> nodes = new HashSet<FullMapNode>();
	for (MapNode node : map.getNodes()) {
	    nodes.add(convertMapNode(node));
	}
	return new FullMap(nodes);
    }

    public HalfMapNode convertPlayerHalfMapNode(PlayerHalfMapNode node) throws ConverterException {
	ETerrain terrain = node.getTerrain();
	return new HalfMapNode(node.getX(), node.getY(), node.isFortPresent(), convertETerrain(terrain));
    }

    public HalfMap convertPlayerHalfMap(PlayerHalfMap halfMap) throws ConverterException {
	Set<HalfMapNode> nodes = new HashSet<>();
	for (PlayerHalfMapNode node : halfMap.getMapNodes()) {
	    HalfMapNode newNode = convertPlayerHalfMapNode(node);
	    nodes.add(newNode);
	}
	return new HalfMap(nodes, new ServerUniquePlayerIdentifier(halfMap.getUniquePlayerID()));
    }

}
