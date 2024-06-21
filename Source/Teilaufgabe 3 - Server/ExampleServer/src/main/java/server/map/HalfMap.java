package server.map;

import java.util.Set;

import server.network.ServerUniquePlayerIdentifier;

public class HalfMap {
    private Set<HalfMapNode> nodes;
    private ServerUniquePlayerIdentifier playerID;
    private final static int maximalX = 9;
    private final static int maximalY = 4;

    public ServerUniquePlayerIdentifier getPlayerID() {
	return playerID;
    }

    public static int getMaximalX() {
	return maximalX;
    }

    public static int getMaximalY() {
	return maximalY;
    }

    public Set<HalfMapNode> getNodes() {
	return nodes;
    }

    public HalfMap(Set<HalfMapNode> nodes, ServerUniquePlayerIdentifier playerID) {
	this.nodes = nodes;
	this.playerID = playerID;
    }
}
