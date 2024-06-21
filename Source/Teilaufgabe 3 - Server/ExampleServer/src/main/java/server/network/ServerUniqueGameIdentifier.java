package server.network;

import java.util.Objects;

public class ServerUniqueGameIdentifier {
    private String uniqueGameID;

    ServerUniqueGameIdentifier(String stringGameIdentifier) {
	this.uniqueGameID = stringGameIdentifier;
    }

    public String getUniqueGameID() {
	return uniqueGameID;
    }

    @Override
    public int hashCode() {
	return Objects.hash(uniqueGameID);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ServerUniqueGameIdentifier other = (ServerUniqueGameIdentifier) obj;
	return Objects.equals(uniqueGameID, other.uniqueGameID);
    }

}
