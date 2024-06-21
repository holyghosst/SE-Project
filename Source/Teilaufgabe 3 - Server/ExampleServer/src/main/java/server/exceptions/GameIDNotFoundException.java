package server.exceptions;

public class GameIDNotFoundException extends GenericExampleException {

    public GameIDNotFoundException(String errorName, String errorMessage) {
	super(errorName, errorMessage);
    }

}
