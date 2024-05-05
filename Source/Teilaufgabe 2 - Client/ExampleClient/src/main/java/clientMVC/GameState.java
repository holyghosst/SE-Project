package clientMVC;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import map.ClientFullMap;
import network.ClientPlayerState;

public class GameState {
    private ClientPlayerState currentPlayerState;
    private ClientFullMap map;

    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
	changes.addPropertyChangeListener(listener);
    }

    public void setMap(ClientFullMap map) {
	ClientFullMap mapBeforeChange = this.map;
	this.map = map;
	changes.firePropertyChange("map", mapBeforeChange, map);
    }

    public void setCurrentPlayerState(ClientPlayerState playerState) {
	ClientPlayerState stateBeforeChange = this.currentPlayerState;
	this.currentPlayerState = playerState;
	changes.firePropertyChange("currentPlayerState", stateBeforeChange, playerState);
    }

}
