package clientMVC;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import map.ClientFullMap;
import network.ClientPlayerState;

public class GameStateView implements PropertyChangeListener {

    public GameStateView(GameState gameState) {
	gameState.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals("map")) {
	    if (evt.getNewValue() instanceof ClientFullMap) {
		ClientFullMap map = (ClientFullMap) evt.getNewValue();
		map.printOut();
	    }
	}
	if (evt.getPropertyName().equals("currentPlayerState")) {
	    if (evt.getNewValue() instanceof ClientPlayerState) {
		ClientPlayerState playerState = (ClientPlayerState) evt.getNewValue();
		playerState.printOut();
	    }
	}
    }

}
