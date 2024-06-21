package server.exceptions;

public class PlayerIDNotFoundException extends GenericExampleException {

    public PlayerIDNotFoundException(String errorName, String errorMessage) {
	super(errorName, errorMessage);
    }

}
