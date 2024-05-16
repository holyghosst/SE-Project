package clientMVC;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import map.ClientFullMap;
import map.HalfMap;
import network.ClientPlayerState;

public class GameStateView implements PropertyChangeListener {

    public GameStateView(GameState gameState) {
	gameState.addPropertyChangeListener(this);
    }

    public void printOutHalfMap(HalfMap halfMap) {
	System.out.println();
	StringBuilder stringBuilder = new StringBuilder();
	for (int y = 0; y < 5; y++) {
	    stringBuilder.setLength(0);
	    for (int x = 0; x < 10; x++) {
		stringBuilder.append(halfMap.getField(x, y).printOut());
	    }
	    System.out.println(stringBuilder.toString());
	}
    }

    public void printOutFullMap(ClientFullMap map) {
	System.out.println();
	StringBuilder stringBuilder = new StringBuilder();
	for (int y = 0; y <= map.getMaximalY(); y++) {
	    stringBuilder.setLength(0);
	    for (int x = 0; x <= map.getMaximalX(); x++) {
		stringBuilder.append(map.getField(x, y).printOut());
	    }
	    System.out.println(stringBuilder.toString());
	}

    }

    public String printOutGameStatus(ClientPlayerState playerState) {
	switch (playerState.getState()) {
	case Lost:
	    return "client has lost.";
	case Won:
	    return "client has won.";
	case MustAct:
	    return "client must act.";
	case MustWait:
	    return "client must wait.";
	default:
	    return "Unknown.";
	}
    }

    public void printOutPlayerState(ClientPlayerState playerState) {
	if (playerState.hasCollectedTreasure()) {
	    System.out.print("Treasure collected, ");
	} else {
	    System.out.print("Treasure NOT collected, ");
	}
	System.out.print(printOutGameStatus(playerState));
	System.out.println();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals("map")) {
	    if (evt.getNewValue() instanceof ClientFullMap) {
		ClientFullMap map = (ClientFullMap) evt.getNewValue();
		printOutFullMap(map);
	    }
	}
	if (evt.getPropertyName().equals("currentPlayerState")) {
	    if (evt.getNewValue() instanceof ClientPlayerState) {
		ClientPlayerState playerState = (ClientPlayerState) evt.getNewValue();
		printOutPlayerState(playerState);
	    }
	}
	if (evt.getPropertyName().equals("halfmap")) {
	    if (evt.getNewValue() instanceof HalfMap) {
		HalfMap halfMap = (HalfMap) evt.getNewValue();
		printOutHalfMap(halfMap);
	    }
	}
    }

}
