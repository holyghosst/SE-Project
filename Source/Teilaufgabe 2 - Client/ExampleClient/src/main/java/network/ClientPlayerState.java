package network;

public class ClientPlayerState {
    private boolean collectedTreasure;
    private ClientPlayerGameState state;

    public ClientPlayerState(boolean collectedTreasure, ClientPlayerGameState state) {
	super();
	this.collectedTreasure = collectedTreasure;
	this.state = state;
    }

    public boolean hasCollectedTreasure() {
	return collectedTreasure;
    }

    public ClientPlayerGameState getState() {
	return state;
    }

    @Override
    public String toString() {
	return "ClientPlayerState [collectedTreasure=" + collectedTreasure + ", state=" + state + "]";
    }

    public void printOut() {
	if (collectedTreasure) {
	    System.out.print("Treasure collected, ");
	} else {
	    System.out.print("Treasure NOT collected, ");
	}
	System.out.print(state);
	System.out.println();
    }

}
