package network;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.PlayerRegistration;
import reactor.core.publisher.Mono;

public class NetworkCommunication {
	private String serverBaseURL;
	private WebClient baseWebClient;
	private String playerID;

	public String getServerBaseURL() {
		return serverBaseURL;
	}

	public WebClient getBaseWebClient() {
		return baseWebClient;
	}

	public String getPlayerID() {
		return playerID;
	}

	public void registerPlayer(PlayerRegistration playerReg, String gameId) {
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/players")
				.body(BodyInserters.fromValue(playerReg)) // specify the data which is sent to the server
				.retrieve().bodyToMono(ResponseEnvelope.class); // specify the object returned by the server

		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();
		if (resultReg.getState() == ERequestState.Error) {
			// typically happens if you forgot to create a new game before the client
			// execution or forgot to adapt the run configuration so that it supplies
			// the id of the new game to the client
			// open http://swe1.wst.univie.ac.at:18235/games in your browser to create a new
			// game and obtain its game id
			System.err.println("Client error, errormessage: " + resultReg.getExceptionMessage());
		} else {
			UniquePlayerIdentifier uniqueID = resultReg.getData().get();
			this.playerID = uniqueID.getUniquePlayerID();
			System.out.println("My Player ID: " + playerID);
		}
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
