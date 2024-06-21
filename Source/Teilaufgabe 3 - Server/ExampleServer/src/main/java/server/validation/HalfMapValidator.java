package server.validation;

import java.util.ArrayList;
import java.util.List;

import messagesbase.messagesfromclient.PlayerHalfMap;
import server.exceptions.InvalidPlayerHalfMapException;

public class HalfMapValidator {
    private List<Rule> halfMapRules = new ArrayList<Rule>();

    public HalfMapValidator() {
	halfMapRules.add(new PlayerHalfMapSizeRule());
	halfMapRules.add(new FortressCountRule());
	halfMapRules.add(new GrassCountRule());
	halfMapRules.add(new WaterCountRule());
	halfMapRules.add(new MountainCountRule());
	halfMapRules.add(new PlayerHalfMapDimensionsRule());
    }

    public void validatePlayerHalfMap(PlayerHalfMap halfMap) throws InvalidPlayerHalfMapException {
	for (Rule rule : halfMapRules) {
	    rule.validateMap(halfMap);
	}
    }

}
