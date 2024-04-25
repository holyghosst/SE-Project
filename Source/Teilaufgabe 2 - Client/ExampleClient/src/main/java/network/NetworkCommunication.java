package network;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.GameState;
import reactor.core.publisher.Mono;

public class NetworkCommunication {
    private String serverBaseURL;
    private WebClient baseWebClient;
    private UniquePlayerIdentifier playerID;
    private String gameID;

    public String getServerBaseURL() {
	return serverBaseURL;
    }

    public WebClient getBaseWebClient() {
	return baseWebClient;
    }

    public UniquePlayerIdentifier getPlayerID() {
	return playerID;
    }

    public void registerPlayer(PlayerRegistration playerReg, String gameId) {
	Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/players")
		.body(BodyInserters.fromValue(playerReg)) // specify the data which is sent to the server
		.retrieve().bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

	ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();
	if (resultReg.getState() == ERequestState.Error) {
	    System.err.println("Client error, errormessage: " + resultReg.getExceptionMessage());
	} else {
	    UniquePlayerIdentifier uniqueID = resultReg.getData().get();
	    this.gameID = gameId;
	    this.playerID = uniqueID;
	}
    }

    public void sendPlayerHalfMap(PlayerHalfMap halfMap) {
	Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameID + "/halfmaps")
		.body(BodyInserters.fromValue(halfMap)).retrieve().bodyToMono(ResponseEnvelope.class);
	ResponseEnvelope requestResult = webAccess.block();
	if (requestResult.getState() == ERequestState.Error) {
	    System.err.println("Client error, errormessage: " + requestResult.getExceptionMessage());
	}
    }

    public GameState getGameState() {
	WebClient baseWebClient = WebClient.builder().baseUrl(serverBaseURL + "/games")
		.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) // the network protocol uses
											  // XML
		.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();

	Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
		.uri("/" + gameID + "/states/" + playerID.getUniquePlayerID()).retrieve()
		.bodyToMono(ResponseEnvelope.class);

	ResponseEnvelope<GameState> requestResult = webAccess.block();
	if (requestResult.getState() == ERequestState.Error) {
	    System.err.println("Client error, errormessage: " + requestResult.getExceptionMessage());
	}
	return requestResult.getData().get();

    }

    public NetworkCommunication(String serverBaseURL) {
	super();
	this.serverBaseURL = serverBaseURL;
	this.baseWebClient = WebClient.builder().baseUrl(serverBaseURL + "/games")
		.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) // the network protocol uses
											  // XML
		.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
    }

}
