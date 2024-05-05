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
	    logger.info("Sent the map");

	    while (!gameController.isMyTurn())
		;
	    logger.info("Both clients have sent the map.");

	    while (gameController.gameUndetermined()) {
		while (!gameController.isMyTurn())
		    ;
		gameController.updateGameState();
		gameController.sendMove();
		logger.info("Moved left!");
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
	/*
	 * TIP: Check out the network protocol documentation. It shows you with a nice
	 * sequence diagram all the steps which are required to be executed by your
	 * client along with a general overview on the required behavior (e.g., when it
	 * is necessary to repeatedly ask the server for its state to determine if
	 * actions can be sent or not). When the client will need to wait for the other
	 * client and when your client should stop sending any more messages to the
	 * server.
	 */

	/*
	 * IMPORTANT: Parsing/Handling of starting parameters.
	 * 
	 * args[0] = Game Mode, you Can use this to know that your code is running on
	 * the evaluation server (if this is the case args[0] = TR). If this is the
	 * case, only a command line interface must be displayed. Also, no JavaFX and
	 * Swing UI components and classes must be used/executed by your Client in any
	 * way IF args[0] = TR.
	 * 
	 * args[1] = Server URL, will hold the server URL your Client should use. Note,
	 * only use the server URL supplied here as the URL used by you during the
	 * development and by the evaluation server (for grading) is NOT the same!
	 * args[1] enables your Client always to get the correct one.
	 * 
	 * args[2] = Holds the game ID which your Client should use. For testing
	 * purposes, you can create a new one by accessing
	 * http://swe1.wst.univie.ac.at:18235/games with your web browser. IMPORTANT: If
	 * a value is stored in args[2], you MUST use it! DO NOT create new games in
	 * your code in such a case!
	 * 
	 * DON'T FORGET TO EVALUATE YOUR FINAL IMPLEMENTATION WITH OUR TEST SERVER. THIS
	 * IS ALSO THE BASE FOR GRADING. THE TEST SERVER CAN BE FOUND AT:
	 * http://swe1.wst.univie.ac.at/ :18235
	 * 
	 * HINT: The assignment section in Moodle also explains all the important
	 * aspects about the start parameters/arguments. Use the Run Configurations (as
	 * shown during the IDE Screencast) in Eclipse to simulate the starting of an
	 * application with start parameters or implement your argument parsing code to
	 * become more flexible (e.g., to mix hardcoded and supplied parameters whenever
	 * the one or the other is available).
	 */
    }
}
