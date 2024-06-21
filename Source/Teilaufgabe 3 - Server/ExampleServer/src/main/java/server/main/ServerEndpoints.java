package server.main;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.GameState;
import server.control.ServerControl;
import server.exceptions.ConverterException;
import server.exceptions.GameIDNotFoundException;
import server.exceptions.GenericExampleException;
import server.exceptions.InvalidPlayerHalfMapException;
import server.exceptions.NotEnoughPlayersException;
import server.exceptions.PlayerIDNotFoundException;
import server.exceptions.TooManyHalfMapsException;
import server.network.Converter;
import server.network.ServerUniqueGameIdentifier;
import server.network.ServerUniquePlayerIdentifier;

@RestController
@RequestMapping(value = "/games")
public class ServerEndpoints {
    private ServerControl serverControl = new ServerControl();
    private Converter converter = new Converter();

    // ADDITIONAL TIPS ON THIS MATTER ARE GIVEN THROUGHOUT THE TUTORIAL SESSION!
    // Note, the same network messages which you have used for the Client (along
    // with its documentation) apply to the Server too.

    /*
     * Please do NOT add all the necessary code in the methods provided below. When
     * following the single responsibility principle, those methods should only
     * contain the bare minimum related to network handling. Such as calls to
     * converters which convert the objects from/to internal data objects to/from
     * messages. Include the other logic (e.g., new game creation and game id
     * handling) by means of composition and related object hierarchies (i.e., other
     * classes should provide it).
     */

    // below you can find two example endpoints (i.e., one GET and one POST based
    // endpoint, which are all endpoint types that you need),
    // Hence, all the other endpoints can be defined similarly.

    // example for a GET endpoint based on /games
    // similar to the client, the HTTP method and the expected data types are
    // specified at the server-side too
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody UniqueGameIdentifier newGame(
	    @RequestParam(required = false, defaultValue = "false", value = "enableDebugMode") boolean enableDebugMode,
	    @RequestParam(required = false, defaultValue = "false", value = "enableDummyCompetition") boolean enableDummyCompetition)
	    throws GenericExampleException {

	boolean showExceptionHandling = false;
	if (showExceptionHandling) {
	    throw new GenericExampleException("Name: Something", "Message: went totally wrong");
	}

	return converter.convertServerUniqueGameIdentifierToUniqueGameIdentifier(serverControl.addNewGame());
    }

    @RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(
	    @Validated @PathVariable UniqueGameIdentifier gameID,
	    @Validated @RequestBody PlayerRegistration playerRegistration) throws GenericExampleException {
	ServerUniqueGameIdentifier convertedGameId = converter
		.convertUniqueGameIdentifierToServerUniqueGameIdentifier(gameID);
	String firstName = playerRegistration.getStudentFirstName();
	String lastName = playerRegistration.getStudentLastName();
	String uAccount = playerRegistration.getStudentUAccount();

	ServerUniquePlayerIdentifier playerID = serverControl.registerPlayer(convertedGameId, firstName, lastName,
		uAccount);

	ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage = new ResponseEnvelope<>(
		converter.convertServerUniquePlayerIdentifierToUniquePlayerIdentifier(playerID));
	return playerIDMessage;
    }

    @RequestMapping(value = "/{gameID}/halfmaps", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody ResponseEnvelope<GameState> receiveHalfMap(
	    @Validated @PathVariable UniqueGameIdentifier gameID, @Validated @RequestBody PlayerHalfMap map)
	    throws NotEnoughPlayersException, ConverterException, InvalidPlayerHalfMapException,
	    GameIDNotFoundException, PlayerIDNotFoundException, TooManyHalfMapsException {

	ServerUniqueGameIdentifier convertedGameId = converter
		.convertUniqueGameIdentifierToServerUniqueGameIdentifier(gameID);
	if (!serverControl.twoPlayersRegistered(convertedGameId)) {
	    throw new NotEnoughPlayersException("NotEnoughPlayers",
		    "Player tried to send halfmap, even though the second player is not registered yet!");
	}

	GameState gameState = converter.convertServerGameState(serverControl.receiveHalfMap(convertedGameId, map));

	ResponseEnvelope<GameState> message = new ResponseEnvelope<GameState>(gameState);
	return message;
    }

    @RequestMapping(value = "/{gameID}/states/{playerID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody ResponseEnvelope<GameState> returnGameState(
	    @Validated @PathVariable UniqueGameIdentifier gameID,
	    @Validated @PathVariable UniquePlayerIdentifier playerID) throws GenericExampleException {

	ServerUniqueGameIdentifier id = converter.convertUniqueGameIdentifierToServerUniqueGameIdentifier(gameID);
	ServerUniquePlayerIdentifier convertedPlayerID = converter
		.convertUniquePlayerIdentifierToServerUniquePlayerIdentifier(playerID);

	GameState gameState = converter.convertServerGameState(serverControl.returnGameState(id, convertedPlayerID));

	return new ResponseEnvelope<GameState>(gameState);
    }

    /*
     * Note, this is only the most basic way of handling exceptions in Spring (but
     * sufficient for our task) it would, for example struggle if you use multiple
     * controllers. Add the exception types to the @ExceptionHandler which your
     * exception handling should support the superclass catches subclasses aspect of
     * try/catch also applies here. Hence, we recommend to simply extend your own
     * Exceptions from the GenericExampleException. For larger projects, one would
     * most likely want to use the HandlerExceptionResolver; see here
     * https://www.baeldung.com/exception-handling-for-rest-with-spring
     * 
     * Ask yourself: Why is handling the exceptions in a different method than the
     * endpoint methods a good solution? This applies a principle from Block 4,
     * which one?
     */
    @ExceptionHandler({ GenericExampleException.class })
    public @ResponseBody ResponseEnvelope<?> handleException(GenericExampleException ex, HttpServletResponse response) {
	ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());
	response.setStatus(HttpServletResponse.SC_OK);
	return result;
    }
}
