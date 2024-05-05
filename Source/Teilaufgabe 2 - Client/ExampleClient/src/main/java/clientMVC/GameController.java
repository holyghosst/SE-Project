package clientMVC;

import map.ClientFullMap;
import map.HalfMapGenerator;
import network.ClientGameState;
import network.ConverterException;
import network.NetworkCommunication;

public class GameController {
    private GameState gameState;
    private NetworkCommunication network;
    private HalfMapGenerator halfMapGenerator = new HalfMapGenerator();

    public GameController(NetworkCommunication network, GameState gameState) {
	this.network = network;
	this.gameState = gameState;
    }

    public void registerPlayer(String firstName, String lastName, String studentUAccount, String gameID) {
	network.registerPlayer(firstName, lastName, studentUAccount, gameID);
    }

    public void sendHalfMap() throws ConverterException {
	network.sendHalfMap(halfMapGenerator.generateHalfMap());
    }

    public boolean isMyTurn() throws ConverterException {
	return network.isMyTurn();
    }

    public boolean gameUndetermined() throws ConverterException {
	return network.gameUndetermined();
    }

    public ClientFullMap getFullMap() throws ConverterException {
	return network.getFullMap();
    }

    public void updateGameState() throws ConverterException {
	ClientGameState clientGameState = network.getClientGameState();
	gameState.setMap(clientGameState.getMap());
	gameState.setCurrentPlayerState(clientGameState.getPlayerState());
    }

    public void sendMove() {
	network.sendMove();
    }

}
