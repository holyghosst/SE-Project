package server.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import server.exceptions.NotEnoughPlayersException;
import server.exceptions.PlayerIDNotFoundException;
import server.exceptions.TooManyHalfMapsException;
import server.exceptions.TooManyPlayersException;
import server.map.FortressState;
import server.map.HalfMap;
import server.map.MapNode;
import server.map.PlayerPositionState;
import server.map.ServerFullMap;
import server.map.TreasureState;
import server.network.PlayerGameState;
import server.network.ServerGameState;
import server.network.ServerPlayerState;
import server.network.ServerUniqueGameIdentifier;
import server.network.ServerUniquePlayerIdentifier;
import server.network.ServerUniquePlayerIdentifierGenerator;

public class Game {
    private final static int maxPlayerNumber = 2;
    private final static int maxTurnNumber = 320;
    private int turnNumber = 0;

    private static Random random = new Random();
    private ServerUniquePlayerIdentifier currentPlayerTurn;

    private String gameStateID = UUID.randomUUID().toString();
    private ServerUniquePlayerIdentifier firstToInitializeMap;

    private ServerUniqueGameIdentifier uniqueGameIdentifier;
    private Set<ServerPlayerState> players = new HashSet<>();
    private ServerFullMap map = new ServerFullMap();
    private List<HalfMap> halfMaps = new ArrayList<>();

    public ServerUniqueGameIdentifier getUniqueGameIdentifier() {
	return uniqueGameIdentifier;
    }

    public Set<ServerPlayerState> getPlayers() {
	return players;
    }

    public ServerFullMap getMap() {
	return map;
    }

    private void updateGameStateID() {
	this.gameStateID = UUID.randomUUID().toString();
    }

    public boolean twoPlayersRegistered() {
	return players.size() == maxPlayerNumber;
    }

    private Set<MapNode> placeFakeEnemyPosition(Set<MapNode> nodes) {
	MapNode fakeEnemyPosition = nodes.stream()
		.filter(node -> node.getPlayerPositionState() != PlayerPositionState.MY_PLAYER
			&& node.getFortressState() != FortressState.MY_FORT)
		.findAny().get();
	nodes.remove(fakeEnemyPosition);
	fakeEnemyPosition.setPlayerPositionState(PlayerPositionState.ENEMY_PLAYER);
	nodes.add(fakeEnemyPosition);
	return nodes;

    }

    public ServerFullMap getPlayerSpecificMap(ServerUniquePlayerIdentifier playerID) throws PlayerIDNotFoundException {
	boolean isFirstPlayer = playerID.getUniquePlayerID().equals(firstToInitializeMap.getUniquePlayerID());

	Set<MapNode> playerSpecificNodes = new HashSet<>();
	for (MapNode node : map.getNodes()) {

	    MapNode newNode = new MapNode(node.getX(), node.getY(), node.getTerrain(), node.getTreasureState(),
		    node.getFortressState(), node.getPlayerPositionState());

	    if (isFirstPlayer && node.getPlayerPositionState() == PlayerPositionState.SECOND_PLAYER) {
		newNode.setPlayerPositionState(PlayerPositionState.NO_PLAYER);
	    } else if (!isFirstPlayer && node.getPlayerPositionState() == PlayerPositionState.FIRST_PLAYER) {
		newNode.setPlayerPositionState(PlayerPositionState.NO_PLAYER);
	    }

	    if (isFirstPlayer) {
		if (node.getTreasureState() == TreasureState.SECOND_TREASURE) {
		    newNode.setTreasureState(TreasureState.NO_OR_UNKNOWN);
		} else if (node.getTreasureState() == TreasureState.FIRST_TREASURE) {
		    newNode.setTreasureState(TreasureState.NO_OR_UNKNOWN);
		}

	    } else {
		if (node.getTreasureState() == TreasureState.FIRST_TREASURE) {
		    newNode.setTreasureState(TreasureState.NO_OR_UNKNOWN);
		} else if (node.getTreasureState() == TreasureState.SECOND_TREASURE) {
		    newNode.setTreasureState(TreasureState.NO_OR_UNKNOWN);
		}
	    }

	    if (isFirstPlayer) {
		if (node.getFortressState() == FortressState.FIRST_FORT) {
		    newNode.setFortressState(FortressState.MY_FORT);
		    newNode.setPlayerPositionState(PlayerPositionState.MY_PLAYER);
		} else if (node.getFortressState() == FortressState.SECOND_FORT) {
		    newNode.setFortressState(FortressState.NO_OR_UNKNOWN);
		}
	    } else {
		if (node.getFortressState() == FortressState.SECOND_FORT) {
		    newNode.setFortressState(FortressState.MY_FORT);
		    newNode.setPlayerPositionState(PlayerPositionState.MY_PLAYER);
		} else if (node.getFortressState() == FortressState.FIRST_FORT) {
		    newNode.setFortressState(FortressState.NO_OR_UNKNOWN);
		}
	    }
	    playerSpecificNodes.add(newNode);
	}
	playerSpecificNodes = placeFakeEnemyPosition(playerSpecificNodes);
	return new ServerFullMap(playerSpecificNodes);
    }

    public ServerGameState getGameState(ServerUniquePlayerIdentifier playerID) throws PlayerIDNotFoundException {
	ServerFullMap fullMap = new ServerFullMap();
	if (map.isInitialized()) {
	    fullMap = getPlayerSpecificMap(playerID);
	}
	Set<ServerPlayerState> players = new HashSet<>();

	ServerPlayerState requestingPlayer = getPlayers().stream()
		.filter(player -> player.getUniquePlayerID().equals(playerID.getUniquePlayerID())).findFirst()
		.orElseThrow(() -> new PlayerIDNotFoundException("PlayerIDNotFound", "PlayerID "
			+ playerID.getUniquePlayerID() + " was not found when trying to retrieve GameState"));
	players.add(requestingPlayer);

	Optional<ServerPlayerState> enemyPlayerOptional = getPlayers().stream()
		.filter(player -> !player.getUniquePlayerID().equals(playerID.getUniquePlayerID())).findFirst();

	if (enemyPlayerOptional.isPresent()) {
	    ServerPlayerState enemyPlayer = enemyPlayerOptional.get();
	    enemyPlayer = new ServerPlayerState(enemyPlayer.getFirstName(), enemyPlayer.getLastName(),
		    enemyPlayer.getUaccount(), enemyPlayer.getPlayerGameState(),
		    ServerUniquePlayerIdentifierGenerator.generateServerUniquePlayerIdentifier().getUniquePlayerID(),
		    enemyPlayer.hasCollectedTreasure());
	    players.add(enemyPlayer);
	}
	return new ServerGameState(fullMap, players, gameStateID);
    }

    public Game(ServerUniqueGameIdentifier gameID) {
	this.uniqueGameIdentifier = gameID;
    }

    public void addPlayer(ServerUniquePlayerIdentifier playerID, String studentFirstName, String studentLastName,
	    String studentUAccount) throws TooManyPlayersException {
	if (twoPlayersRegistered()) {
	    throw new TooManyPlayersException("TooManyPlayersException",
		    "Tried to register player, but there are already 2 players!");
	}
	if (players.size() == 1) {
	    players.add(new ServerPlayerState(studentFirstName, studentLastName, studentUAccount,
		    PlayerGameState.MUST_WAIT, playerID.getUniquePlayerID(), false));

	    if (random.nextBoolean()) {
		ServerPlayerState playerToAct = players.stream()
			.filter(player -> player.getUniquePlayerID().equals(playerID.getUniquePlayerID())).findFirst()
			.get();
		players.remove(playerToAct);
		playerToAct.setPlayerGameState(PlayerGameState.MUST_ACT);
		players.add(playerToAct);
		currentPlayerTurn = playerToAct;
		return;
	    } else {
		ServerPlayerState playerToAct = players.stream()
			.filter(player -> !player.getUniquePlayerID().equals(playerID.getUniquePlayerID())).findFirst()
			.get();
		players.remove(playerToAct);
		playerToAct.setPlayerGameState(PlayerGameState.MUST_ACT);
		players.add(playerToAct);
		currentPlayerTurn = playerToAct;
		return;
	    }
	}
	players.add(new ServerPlayerState(studentFirstName, studentLastName, studentUAccount, PlayerGameState.MUST_WAIT,
		playerID.getUniquePlayerID(), false));
	updateGameStateID();
    }

    public void addHalfMap(HalfMap halfMap)
	    throws NotEnoughPlayersException, TooManyHalfMapsException, PlayerIDNotFoundException {
	if (players.size() < maxPlayerNumber) {
	    throw new NotEnoughPlayersException("NotEnoughPlayers", halfMap.getPlayerID().getUniquePlayerID()
		    + " tried to send halfmap, even though the second player is not registered yet!");
	} else {
	    halfMaps.add(halfMap);
	    if (halfMaps.size() == 1) {
		firstToInitializeMap = halfMap.getPlayerID();
	    }
	    if (halfMaps.size() > maxPlayerNumber) {
		throw new TooManyHalfMapsException("TooManyHalfMaps",
			"Too many halfmaps were sent! Responsible Player: "
				+ halfMap.getPlayerID().getUniquePlayerID());
	    }
	    map.addHalfMap(halfMap);
	    turn();
	    updateGameStateID();
	}
    }

    public boolean hasPlayer(ServerUniquePlayerIdentifier playerID) {
	return players.stream().anyMatch(player -> player.getUniquePlayerID().equals(playerID.getUniquePlayerID()));
    }

    public String toString() {
	return uniqueGameIdentifier.getUniqueGameID();
    }

    public void turn() {
	ServerPlayerState currentPlayer = players.stream()
		.filter(player -> player.getUniquePlayerID().equals(currentPlayerTurn.getUniquePlayerID())).findFirst()
		.get();

	ServerPlayerState nextPlayer = players.stream()
		.filter(player -> !player.getUniquePlayerID().equals(currentPlayerTurn.getUniquePlayerID())).findFirst()
		.get();

	players.remove(currentPlayer);
	currentPlayer.setPlayerGameState(PlayerGameState.MUST_WAIT);
	players.add(currentPlayer);

	players.remove(nextPlayer);
	nextPlayer.setPlayerGameState(PlayerGameState.MUST_ACT);
	players.add(nextPlayer);

	turnNumber++;
	currentPlayerTurn = nextPlayer;
    }

    public void setAsLooserAndAnotherAsWinner(ServerUniquePlayerIdentifier playerID) {
	ServerPlayerState looser = players.stream()
		.filter(player -> player.getUniquePlayerID().equals(playerID.getUniquePlayerID())).findFirst().get();
	ServerPlayerState winner = players.stream()
		.filter(player -> !player.getUniquePlayerID().equals(playerID.getUniquePlayerID())).findFirst().get();

	players.remove(looser);
	looser.setPlayerGameState(PlayerGameState.LOST);
	players.add(looser);

	players.remove(winner);
	winner.setPlayerGameState(PlayerGameState.WON);
	players.add(winner);
    }

}
