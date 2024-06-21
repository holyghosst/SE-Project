package server.network;

import java.util.UUID;

public class ServerUniquePlayerIdentifierGenerator {
    public static ServerUniquePlayerIdentifier generateServerUniquePlayerIdentifier() {
	return new ServerUniquePlayerIdentifier(UUID.randomUUID().toString());
    }

}
