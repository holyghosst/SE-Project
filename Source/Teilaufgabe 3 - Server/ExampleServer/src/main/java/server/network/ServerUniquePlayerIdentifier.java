package server.network;

import java.util.Objects;

public class ServerUniquePlayerIdentifier {
    private String uniquePlayerID;

    public ServerUniquePlayerIdentifier(String uniquePlayerID) {
	this.uniquePlayerID = uniquePlayerID;
    }

    public String getUniquePlayerID() {
	return uniquePlayerID;
    }

    @Override
    public int hashCode() {
	return Objects.hash(uniquePlayerID);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	ServerUniquePlayerIdentifier other = (ServerUniquePlayerIdentifier) obj;
	return Objects.equals(uniquePlayerID, other.uniquePlayerID);
    }

    public String toString() {
	return getUniquePlayerID();
    }

}
