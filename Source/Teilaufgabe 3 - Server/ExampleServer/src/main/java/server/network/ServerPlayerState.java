package server.network;

public class ServerPlayerState extends ServerUniquePlayerIdentifier {
    private String firstName;
    private String lastName;
    private String uaccount;
    private PlayerGameState playerGameState;
    private boolean collectedTreasure;

    public String getFirstName() {
	return firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public String getUaccount() {
	return uaccount;
    }

    public PlayerGameState getPlayerGameState() {
	return playerGameState;
    }

    public void setPlayerGameState(PlayerGameState state) {
	this.playerGameState = state;
    }

    public boolean hasCollectedTreasure() {
	return collectedTreasure;
    }

    public ServerPlayerState(String firstName, String lastName, String uaccount, PlayerGameState playerGameState,
	    String uniquePlayerID, boolean collectedTreasure) {
	super(uniquePlayerID);
	this.firstName = firstName;
	this.lastName = lastName;
	this.uaccount = uaccount;
	this.playerGameState = playerGameState;
	this.collectedTreasure = collectedTreasure;
    }

    public ServerPlayerState(String uniquePlayerID) {
	super(uniquePlayerID);
    }

}
