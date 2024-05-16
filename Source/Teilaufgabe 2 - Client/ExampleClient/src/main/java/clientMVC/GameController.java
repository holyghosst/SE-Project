package clientMVC;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import map.ClientFullMap;
import map.HalfMap;
import map.HalfMapGenerator;
import network.ClientGameState;
import network.ConverterException;
import network.NetworkCommunication;
import network.NetworkCommunicationException;
import pathgeneration.Pathfinder;
import pathgeneration.PlayerMoveDirection;

public class GameController {
    private final static Logger logger = LoggerFactory.getLogger(GameController.class);
    private GameState gameState;
    private NetworkCommunication network;
    private HalfMapGenerator halfMapGenerator = new HalfMapGenerator();
    private Pathfinder pathfinder = new Pathfinder();

    public GameController(NetworkCommunication network, GameState gameState) {
	this.network = network;
	this.gameState = gameState;
    }

    public void registerPlayer(String firstName, String lastName, String studentUAccount, String gameID)
	    throws NetworkCommunicationException {
	network.registerPlayer(firstName, lastName, studentUAccount, gameID);
    }

    public HalfMap generateHalfMap() {
	HalfMap halfMap = halfMapGenerator.generateHalfMap();
	gameState.setHalfMap(halfMap);
	return halfMap;
    }

    public void sendHalfMap() throws ConverterException, NetworkCommunicationException {
	network.sendHalfMap(generateHalfMap());
    }

    public boolean isMyTurn() throws ConverterException, NetworkCommunicationException {
	return network.isMyTurn();
    }

    public boolean gameUndetermined() throws ConverterException, NetworkCommunicationException {
	return network.gameUndetermined();
    }

    public boolean gameEnded() {
	return (gameState.getCurrentStateOfGame() == StateOfGame.ENDED);
    }

    public ClientFullMap getFullMap() throws ConverterException, NetworkCommunicationException {
	return network.getFullMap();
    }

    public void updateGameState() throws ConverterException, NetworkCommunicationException {
	ClientGameState clientGameState = network.getClientGameState();
	pathfinder.setFullMap(clientGameState.getMap());
	gameState.setMap(clientGameState.getMap());
	gameState.setCurrentPlayerState(clientGameState.getPlayerState());
	logger.info("Updated game state.");
    }

    public void sendMove(PlayerMoveDirection moveDirection) throws ConverterException, NetworkCommunicationException {
	network.sendMove(moveDirection);
    }

    public void findTreasure() {
	pathfinder.findTreasure(gameState.getMap().getCurrentPosition());
    }

    public void makeTreasureLookingMove() throws ConverterException, NetworkCommunicationException {
	if (!pathfinder.getTreasureFinderMovements().isEmpty()) {
	    logger.info("Moving " + pathfinder.getTreasureFinderMovements().peek());
	    sendMove(pathfinder.getTreasureFinderMovements().poll());
	}
    }

    public void findPathToEnemyPart() {
	pathfinder.findGoal(gameState.getMap().getCurrentPosition());
    }

    public void moveToEnemyPart() throws ConverterException, NetworkCommunicationException {
	if (!pathfinder.getMovements().isEmpty()) {
	    logger.info("Moving " + pathfinder.getMovements().peek());
	    sendMove(pathfinder.getMovements().poll());
	}
    }

    public void findFortress() {
	pathfinder.findFortress(gameState.getMap().getCurrentPosition());
    }

    public void makeFortressLookingMove() throws ConverterException, NetworkCommunicationException {
	if (!pathfinder.getFortressFinderMovements().isEmpty()) {
	    logger.info("Moving " + pathfinder.getFortressFinderMovements().peek());
	    sendMove(pathfinder.getFortressFinderMovements().poll());
	}
    }

    public boolean onEnemyTerritory() {
	return pathfinder.isOnEnemyTerritory();
    }

    public boolean foundTreasure() {
	return gameState.getCurrentPlayerState().hasCollectedTreasure();
    }

}
