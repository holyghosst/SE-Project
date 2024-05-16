package clientMVC;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import map.ClientFullMap;
import map.HalfMap;
import network.ClientPlayerGameState;
import network.ClientPlayerState;

public class GameState {
    private HalfMap halfMap;
    private ClientPlayerState currentPlayerState;
    private ClientFullMap map;
    private StateOfGame currentStateOfGame;

    public GameState() {
	currentStateOfGame = StateOfGame.UNDETERMINED;
    }

    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
	changes.addPropertyChangeListener(listener);
    }

    public void setHalfMap(HalfMap halfMap) {
	HalfMap halfMapBeforeChange = this.halfMap;
	this.halfMap = halfMap;
	changes.firePropertyChange("halfmap", halfMapBeforeChange, halfMap);
    }

    public void setMap(ClientFullMap map) {
	ClientFullMap mapBeforeChange = this.map;
	this.map = map;
	changes.firePropertyChange("map", mapBeforeChange, map);
    }

    private void setCurrentStateOfGame(ClientPlayerState currentPlayerState) {
	if (currentPlayerState.getState() == ClientPlayerGameState.MustAct
		|| currentPlayerState.getState() == ClientPlayerGameState.MustWait) {
	    currentStateOfGame = StateOfGame.UNDETERMINED;
	} else {
	    currentStateOfGame = StateOfGame.ENDED;
	}
    }

    public StateOfGame getCurrentStateOfGame() {
	return currentStateOfGame;
    }

    public void setCurrentPlayerState(ClientPlayerState playerState) {
	ClientPlayerState stateBeforeChange = this.currentPlayerState;
	this.currentPlayerState = playerState;
	setCurrentStateOfGame(playerState);
	changes.firePropertyChange("currentPlayerState", stateBeforeChange, playerState);
    }

    public ClientPlayerState getCurrentPlayerState() {
	return currentPlayerState;
    }

    public ClientFullMap getMap() {
	return map;
    }

}
