package client.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientMVC.GameController;
import clientMVC.GameState;
import clientMVC.GameStateView;
import network.NetworkCommunication;
import network.NetworkConverter;

public class MainClient {
    private final static Logger logger = LoggerFactory.getLogger(MainClient.class);

    public static void main(String[] args) {

	String serverBaseUrl = args[1];
	String gameId = args[2];

	NetworkConverter networkConverter = new NetworkConverter();
	NetworkCommunication networkCommunication = new NetworkCommunication(serverBaseUrl, networkConverter);

	GameState gameState = new GameState();
	GameStateView gameStateView = new GameStateView(gameState);
	GameController gameController = new GameController(networkCommunication, gameState);

	try {
	    gameController.registerPlayer("Oleksandr", "Lymarenko", "lymarenkoo02", gameId);

	    while (!gameController.isMyTurn())
		;
	    logger.info("Both players have been registered.");

	    gameController.sendHalfMap();

	    while (!gameController.isMyTurn())
		;
	    logger.info("Both clients have sent the map.");

	    while (gameController.gameUndetermined()) {
		while (!gameController.isMyTurn() && gameController.gameUndetermined())
		    ;

		gameController.updateGameState();
		if (gameController.gameEnded()) {
		    break;
		}

		if (!gameController.foundTreasure()) {
		    gameController.findTreasure();
		    gameController.makeTreasureLookingMove();
		} else {
		    if (!gameController.onEnemyTerritory()) {
			gameController.findPathToEnemyPart();
			gameController.moveToEnemyPart();
		    } else {
			gameController.findFortress();
			gameController.makeFortressLookingMove();
		    }

		}

	    }
	    logger.info("Game ended.");
	    System.exit(0);

	} catch (Exception e) {
	    logger.error("Fatal error during game execution!");
	    e.printStackTrace();
	    System.exit(-1);
	}
    }
}
