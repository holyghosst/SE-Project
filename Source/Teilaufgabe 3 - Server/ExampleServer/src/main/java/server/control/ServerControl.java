package server.control;

import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.ConverterException;
import server.exceptions.GameIDNotFoundException;
import server.exceptions.InvalidPlayerHalfMapException;
import server.exceptions.NotEnoughPlayersException;
import server.exceptions.PlayerIDNotFoundException;
import server.exceptions.TooManyHalfMapsException;
import server.exceptions.TooManyPlayersException;
import server.game.Game;
import server.map.HalfMap;
import server.network.Converter;
import server.network.ServerGameState;
import server.network.ServerUniqueGameIdentifier;
import server.network.ServerUniqueGameIdentifierGenerator;
import server.network.ServerUniquePlayerIdentifier;
import server.network.ServerUniquePlayerIdentifierGenerator;
import server.validation.HalfMapValidator;

public class ServerControl {

    // TAKEN FROM 1
    // Quelle: https://stackoverflow.com/questions/10752753/java-set-retain-order
    // Ich hab nach einer Collection gesucht, die die Einfügeordnung beibehält. Aus
    // dieser Diskussion auf stackoverflow hab ich dann über
    // LinkedHashSet erfahren.

    private final Converter converter = new Converter();
    private final static Logger logger = LoggerFactory.getLogger(ServerControl.class);

    private final static int maxNumberOfGames = 99;

    // TAKEN FROM START 1
    private Set<Game> activeGames = new LinkedHashSet<Game>();
    // TAKEN FROM END 1

    public Set<Game> getActiveGames() {
	return activeGames;
    }

    public ServerUniqueGameIdentifier addNewGame() {
	if (activeGames.size() == maxNumberOfGames) {
	    activeGames.remove(activeGames.iterator().next());
	}
	ServerUniqueGameIdentifier gameID = ServerUniqueGameIdentifierGenerator.generateServerUniqueGameIdentifier();
	activeGames.add(new Game(gameID));
	logger.info("Added game with ID:" + gameID.getUniqueGameID());
	return gameID;
    }

    public void addPlayer(ServerUniqueGameIdentifier gameID, ServerUniquePlayerIdentifier playerID,
	    String studentFirstName, String studentLastName, String studentUAccount) throws TooManyPlayersException {
	Game game = activeGames.stream().filter(g -> g.getUniqueGameIdentifier().equals(gameID)).findFirst()
		.orElseThrow();

	activeGames.remove(game);
	game.addPlayer(playerID, studentFirstName, studentLastName, studentUAccount);
	activeGames.add(game);
	logger.info("Added player with ID " + playerID.getUniquePlayerID() + " gameID: " + gameID.getUniqueGameID());
    }

    public void addHalfMap(ServerUniqueGameIdentifier gameID, HalfMap halfMap)
	    throws NotEnoughPlayersException, TooManyHalfMapsException, PlayerIDNotFoundException {
	Game game = activeGames.stream().filter(g -> g.getUniqueGameIdentifier().equals(gameID)).findFirst()
		.orElseThrow();

	activeGames.remove(game);
	game.addHalfMap(halfMap);
	activeGames.add(game);
	logger.info("HalfMap from player " + halfMap.getPlayerID().getUniquePlayerID() + "added to game "
		+ gameID.getUniqueGameID());
    }

    public ServerUniquePlayerIdentifier registerPlayer(ServerUniqueGameIdentifier gameID, String studentFirstName,
	    String studentLastName, String studentUAccount) throws GameIDNotFoundException, TooManyPlayersException {
	if (!doesGameExist(gameID)) {
	    throw new GameIDNotFoundException("NonExistentGameID",
		    "GameID " + gameID.getUniqueGameID() + " does not exist!");
	}

	ServerUniquePlayerIdentifier playerIdentifier = ServerUniquePlayerIdentifierGenerator
		.generateServerUniquePlayerIdentifier();

	addPlayer(gameID, playerIdentifier, studentFirstName, studentLastName, studentUAccount);
	return playerIdentifier;

    }

    public ServerGameState receiveHalfMap(ServerUniqueGameIdentifier gameID, PlayerHalfMap halfMap)
	    throws InvalidPlayerHalfMapException, GameIDNotFoundException, PlayerIDNotFoundException,
	    NotEnoughPlayersException, TooManyHalfMapsException, ConverterException {
	ServerUniquePlayerIdentifier playerID = new ServerUniquePlayerIdentifier(halfMap.getUniquePlayerID());

	if (!doesGameExist(gameID)) {
	    throw new GameIDNotFoundException("NonExistentGameID",
		    "GameID " + gameID.getUniqueGameID() + " does not exist!");
	}

	if (!doesPlayerExist(playerID)) {
	    throw new PlayerIDNotFoundException("NotValidPlayerID",
		    "Player ID " + playerID.getUniquePlayerID() + " is not valid.");
	}

	HalfMapValidator halfMapValidator = new HalfMapValidator();
	try {
	    halfMapValidator.validatePlayerHalfMap(halfMap);
	} catch (InvalidPlayerHalfMapException e) {
	    setLooser(gameID, playerID);
	    throw e;
	}

	addHalfMap(gameID, converter.convertPlayerHalfMap(halfMap));
	Game game = activeGames.stream().filter(g -> g.getUniqueGameIdentifier().equals(gameID)).findFirst().get();
	return game.getGameState(playerID);
    }

    public ServerGameState returnGameState(ServerUniqueGameIdentifier gameID, ServerUniquePlayerIdentifier playerID)
	    throws GameIDNotFoundException, PlayerIDNotFoundException {
	if (!doesGameExist(gameID)) {
	    throw new GameIDNotFoundException("NonExistentGameID",
		    "GameID " + gameID.getUniqueGameID() + " does not exist!");
	}
	if (!doesPlayerExist(playerID)) {
	    throw new PlayerIDNotFoundException("NotValidPlayerID",
		    "Player ID " + playerID.getUniquePlayerID() + " is not valid.");
	}

	Game game = activeGames.stream().filter(g -> g.getUniqueGameIdentifier().equals(gameID)).findFirst()
		.orElseThrow();

	return game.getGameState(playerID);
    }

    public boolean doesGameExist(ServerUniqueGameIdentifier gameID) {
	return activeGames.stream().anyMatch(game -> game.getUniqueGameIdentifier().equals(gameID));
    }

    public boolean doesPlayerExist(ServerUniquePlayerIdentifier playerID) {
	return activeGames.stream().anyMatch(game -> game.hasPlayer(playerID));
    }

    public boolean twoPlayersRegistered(ServerUniqueGameIdentifier gameID) throws GameIDNotFoundException {
	if (!doesGameExist(gameID)) {
	    throw new GameIDNotFoundException("NonExistentGameID",
		    "GameID " + gameID.getUniqueGameID() + " does not exist!");
	}
	Game game = activeGames.stream().filter(g -> g.getUniqueGameIdentifier().equals(gameID)).findFirst().get();
	return game.twoPlayersRegistered();
    }

    public void setLooser(ServerUniqueGameIdentifier gameID, ServerUniquePlayerIdentifier playerID) {
	Game game = activeGames.stream().filter(g -> g.getUniqueGameIdentifier().equals(gameID)).findFirst().get();
	game.setAsLooserAndAnotherAsWinner(playerID);
    }

}
