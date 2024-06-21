package server.network;

import java.util.Random;

public class ServerUniqueGameIdentifierGenerator {
    private static final int identifierSize = 5;
    private static Random random = new Random();
    private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static ServerUniqueGameIdentifier generateServerUniqueGameIdentifier() {
	String gameIdentifierString = "";

	for (int i = 0; i < identifierSize; i++) {
	    gameIdentifierString += alphabet.charAt(random.nextInt(alphabet.length()));
	}
	ServerUniqueGameIdentifier serverUniqueGameID = new ServerUniqueGameIdentifier(gameIdentifierString);
	return serverUniqueGameID;

    }

}
