package network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import client.main.MainClient;
import map.ClientFullMap;
import map.HalfMap;
import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.GameState;
import reactor.core.publisher.Mono;

public class NetworkCommunication {
    private final static Logger logger = LoggerFactory.getLogger(MainClient.class);

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
		.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) // the network protocol uses
											  // XML
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

    public void registerPlayer(String firstName, String lastName, String studentUAccount, String gameId) {
	waitBeforeRequest();

	PlayerRegistration playerReg = new PlayerRegistration(firstName, lastName, studentUAccount);
	Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/players")
		.body(BodyInserters.fromValue(playerReg)).retrieve().bodyToMono(ResponseEnvelope.class);
	ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();

	if (resultReg.getState() == ERequestState.Error) {
	    System.err.println("Client error, errormessage: " + resultReg.getExceptionMessage());
	} else {
	    UniquePlayerIdentifier uniqueID = resultReg.getData().get();
	    this.gameID = gameId;
	    this.playerID = uniqueID;
	    converter.setPlayerID(uniqueID);
	    logger.info("Player has been registered with ID: " + playerID.getUniquePlayerID());
	}
    }

    public void sendHalfMap(HalfMap halfMap) throws ConverterException {
	waitBeforeRequest();

	PlayerHalfMap playerHalfMap = converter.convertHalfMap(halfMap);
	Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID + "/halfmaps")
		.body(BodyInserters.fromValue(playerHalfMap)).retrieve().bodyToMono(ResponseEnvelope.class);

	ResponseEnvelope requestResult = webAccess.block();
	if (requestResult.getState() == ERequestState.Error) {
	    System.err.println("Client error, errormessage: " + requestResult.getExceptionMessage());
	}
	logger.info("HalfMap has been sent.");
    }

    public GameState getGameState() {
	waitBeforeRequest();

	WebClient baseWebClient = WebClient.builder().baseUrl(serverBaseURL + "/games")
		.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
		.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();

	Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
		.uri("/" + gameID + "/states/" + getStringPlayerID()).retrieve().bodyToMono(ResponseEnvelope.class);
	ResponseEnvelope<GameState> requestResult = webAccess.block();
	if (requestResult.getState() == ERequestState.Error) {
	    System.err.println("Client error, errormessage: " + requestResult.getExceptionMessage());
	}
	return requestResult.getData().get();

    }

    public void sendMove() {
	waitBeforeRequest();
	PlayerMove move = PlayerMove.of(playerID, EMove.Left);

	Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID + "/moves")
		.header("accept", "application/xml").body(BodyInserters.fromValue(move)).retrieve()
		.bodyToMono(ResponseEnvelope.class);

	ResponseEnvelope requestResult = webAccess.block();
	if (requestResult.getState() == ERequestState.Error) {
	    System.err.println("Client error, errormessage: " + requestResult.getExceptionMessage());
	}
    }

    public ClientGameState getClientGameState() throws ConverterException {
	return converter.convertGameState(getGameState());
    }

    public boolean isMyTurn() throws ConverterException {
	ClientGameState gameState = getClientGameState();
	ClientPlayerState playerState = gameState.getPlayerState();
	boolean myTurn = playerState.getState() == ClientPlayerGameState.MustAct;

	return myTurn;
    }

    public boolean gameUndetermined() throws ConverterException {
	ClientGameState gameState = getClientGameState();
	ClientPlayerState playerState = gameState.getPlayerState();
	boolean undetermined = (playerState.getState() == ClientPlayerGameState.MustAct
		|| playerState.getState() == ClientPlayerGameState.MustWait);

	return undetermined;
    }

    public ClientFullMap getFullMap() throws ConverterException {
	ClientGameState gameState = getClientGameState();
	ClientFullMap map = gameState.getMap();
	return map;
    }

}
