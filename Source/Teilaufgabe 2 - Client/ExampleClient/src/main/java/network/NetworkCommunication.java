package network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import map.ClientFullMap;
import map.HalfMap;
import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.GameState;
import pathgeneration.PlayerMoveDirection;
import reactor.core.publisher.Mono;

public class NetworkCommunication {
    private final static Logger logger = LoggerFactory.getLogger(NetworkCommunication.class);

    private String serverBaseURL;
    private UniquePlayerIdentifier playerID;
    private String gameID;
    private WebClient baseWebClient;
    private NetworkConverter converter;

    private static long lastRequestTime = 0;
    private static final long timeBetweenRequests = 400;

    public NetworkCommunication(String serverBaseURL, NetworkConverter converter) {
	super();
	this.serverBaseURL = serverBaseURL;
	this.converter = converter;
	this.baseWebClient = WebClient.builder().baseUrl(serverBaseURL + "/games")
		.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
		.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
    }

    public String getServerBaseURL() {
	return serverBaseURL;
    }

    public WebClient getBaseWebClient() {
	return baseWebClient;
    }

    public UniquePlayerIdentifier getPlayerID() {
	return playerID;
    }

    public String getStringPlayerID() {
	return playerID.getUniquePlayerID();
    }

    public void waitBeforeRequest() {
	long currentTime = System.currentTimeMillis();
	long differenceBetweenRequests = currentTime - lastRequestTime;
	if (differenceBetweenRequests < timeBetweenRequests) {
	    try {
		Thread.sleep(timeBetweenRequests - differenceBetweenRequests);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
	lastRequestTime = System.currentTimeMillis();
    }

    public void registerPlayer(String firstName, String lastName, String studentUAccount, String gameId)
	    throws NetworkCommunicationException {
	waitBeforeRequest();

	PlayerRegistration playerReg = new PlayerRegistration(firstName, lastName, studentUAccount);
	Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/players")
		.body(BodyInserters.fromValue(playerReg)).retrieve().bodyToMono(ResponseEnvelope.class);
	ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();

	if (resultReg.getState() == ERequestState.Error) {
	    logger.error("Error while registering player!");
	    throw new NetworkCommunicationException(resultReg.getExceptionMessage());
	} else {
	    UniquePlayerIdentifier uniqueID = resultReg.getData().get();
	    this.gameID = gameId;
	    this.playerID = uniqueID;
	    converter.setPlayerID(uniqueID);
	    logger.info("Player has been registered with ID: " + playerID.getUniquePlayerID());
	}
    }

    public void sendHalfMap(HalfMap halfMap) throws ConverterException, NetworkCommunicationException {
	waitBeforeRequest();

	PlayerHalfMap playerHalfMap = converter.convertHalfMap(halfMap);
	Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID + "/halfmaps")
		.body(BodyInserters.fromValue(playerHalfMap)).retrieve().bodyToMono(ResponseEnvelope.class);

	ResponseEnvelope requestResult = webAccess.block();
	if (requestResult.getState() == ERequestState.Error) {
	    throw new NetworkCommunicationException(requestResult.getExceptionMessage());
	}
	logger.info("HalfMap has been sent.");
    }

    public GameState getGameState() throws NetworkCommunicationException {
	waitBeforeRequest();

	WebClient baseWebClient = WebClient.builder().baseUrl(serverBaseURL + "/games")
		.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
		.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();

	Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
		.uri("/" + gameID + "/states/" + getStringPlayerID()).retrieve().bodyToMono(ResponseEnvelope.class);
	ResponseEnvelope<GameState> requestResult = webAccess.block();
	if (requestResult.getState() == ERequestState.Error) {
	    logger.error("Error while getting GameState");
	    throw new NetworkCommunicationException(requestResult.getExceptionMessage());
	}
	return requestResult.getData().get();

    }

    public void sendMove(PlayerMoveDirection moveDirection) throws ConverterException, NetworkCommunicationException {
	waitBeforeRequest();
	PlayerMove playerMove = converter.convertPlayerMoveDirection(moveDirection);

	Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID + "/moves")
		.header("accept", "application/xml").body(BodyInserters.fromValue(playerMove)).retrieve()
		.bodyToMono(ResponseEnvelope.class);

	ResponseEnvelope requestResult = webAccess.block();
	if (requestResult.getState() == ERequestState.Error) {
	    logger.error("Error while sending PlayerMove");
	    throw new NetworkCommunicationException(requestResult.getExceptionMessage());
	}
    }

    public ClientGameState getClientGameState() throws ConverterException, NetworkCommunicationException {
	return converter.convertGameState(getGameState());
    }

    public boolean isMyTurn() throws ConverterException, NetworkCommunicationException {
	ClientGameState gameState = getClientGameState();
	ClientPlayerState playerState = gameState.getPlayerState();
	boolean myTurn = playerState.getState() == ClientPlayerGameState.MustAct;

	return myTurn;
    }

    public boolean gameUndetermined() throws ConverterException, NetworkCommunicationException {
	ClientGameState gameState = getClientGameState();
	ClientPlayerState playerState = gameState.getPlayerState();
	boolean isUndetermined = (playerState.getState() == ClientPlayerGameState.MustAct
		|| playerState.getState() == ClientPlayerGameState.MustWait);

	return isUndetermined;
    }

    public ClientFullMap getFullMap() throws ConverterException, NetworkCommunicationException {
	ClientGameState gameState = getClientGameState();
	ClientFullMap map = gameState.getMap();
	return map;
    }

}
