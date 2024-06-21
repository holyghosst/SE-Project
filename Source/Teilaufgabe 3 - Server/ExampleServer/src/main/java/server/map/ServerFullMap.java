package server.map;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ServerFullMap {
    private Set<MapNode> mapNodes = new HashSet<>();
    private static Random random = new Random();

    private boolean horizontalMap = random.nextBoolean();
    private boolean firstMapAtStart = random.nextBoolean();
    private int xOffset = horizontalMap ? HalfMap.getMaximalX() + 1 : 0;
    private int yOffset = horizontalMap ? 0 : HalfMap.getMaximalY() + 1;

    private boolean initialized = false;

    public ServerFullMap(Set<MapNode> nodes) {
	this.mapNodes = nodes;
    }

    public ServerFullMap() {

    }

    public boolean isInitialized() {
	return initialized;
    }

    public Set<MapNode> getNodes() {
	return Collections.unmodifiableSet(mapNodes);
    }

    public void addHalfMap(HalfMap halfMap) {
	if (!initialized) {
	    initializeMap(halfMap);
	    initialized = true;
	} else {
	    addSecondHalfMap(halfMap);
	}
    }

    private void initializeMap(HalfMap halfMap) {
	for (HalfMapNode node : halfMap.getNodes()) {
	    int x = node.getX();
	    int y = node.getY();

	    if (!firstMapAtStart) {
		x += xOffset;
		y += yOffset;
	    }
	    MapNode newNode = new MapNode(x, y, node.getTerrain(), TreasureState.NO_OR_UNKNOWN,
		    FortressState.NO_OR_UNKNOWN, PlayerPositionState.NO_PLAYER);
	    if (node.fortressPresent()) {
		newNode.setFortressState(FortressState.FIRST_FORT);
		newNode.setPlayerPositionState(PlayerPositionState.FIRST_PLAYER);
	    }
	    mapNodes.add(newNode);
	}

	MapNode treasureNode = mapNodes.stream()
		.filter(n -> n.getTerrain() == Terrain.GRASS && n.getFortressState() == FortressState.NO_OR_UNKNOWN)
		.findAny().get();
	mapNodes.remove(treasureNode);
	treasureNode.setTreasureState(TreasureState.FIRST_TREASURE);
	mapNodes.add(treasureNode);
    }

    private void addSecondHalfMap(HalfMap halfMap) {
	Set<MapNode> secondHalfNodes = new HashSet<MapNode>();

	for (HalfMapNode node : halfMap.getNodes()) {
	    int x = node.getX();
	    int y = node.getY();
	    if (firstMapAtStart) {
		x += xOffset;
		y += yOffset;
	    }
	    MapNode newNode = new MapNode(x, y, node.getTerrain(), TreasureState.NO_OR_UNKNOWN,
		    FortressState.NO_OR_UNKNOWN, PlayerPositionState.NO_PLAYER);
	    if (node.fortressPresent()) {
		newNode.setFortressState(FortressState.SECOND_FORT);
		newNode.setPlayerPositionState(PlayerPositionState.SECOND_PLAYER);
	    }
	    secondHalfNodes.add(newNode);
	}

	MapNode treasureNode = secondHalfNodes.stream()
		.filter(n -> n.getTerrain() == Terrain.GRASS && n.getFortressState() == FortressState.NO_OR_UNKNOWN)
		.findAny().get();

	secondHalfNodes.remove(treasureNode);
	treasureNode.setTreasureState(TreasureState.SECOND_TREASURE);
	secondHalfNodes.add(treasureNode);
	mapNodes.addAll(secondHalfNodes);
    }

}
