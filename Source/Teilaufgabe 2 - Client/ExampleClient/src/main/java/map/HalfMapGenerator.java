package map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HalfMapGenerator {
    private final static Logger logger = LoggerFactory.getLogger(HalfMapGenerator.class);
    private Random random = new Random();
    private ArrayList<Terrain> terrainTypes = new ArrayList<Terrain>(
	    List.of(Terrain.GRASS, Terrain.WATER, Terrain.MOUNTAIN));
    private HalfMapValidator validator = new HalfMapValidator();

    private MapField generateSingleRandomMapField() {
	Terrain randomTerrain = terrainTypes.get(random.nextInt(terrainTypes.size()));
	return new MapField(randomTerrain, FieldObject.EMPTY);
    }

    private List<Coordinates> generateMapCoordinates() {
	List<Coordinates> coordinates = new ArrayList<Coordinates>();

	for (int xCoordinate = 0; xCoordinate < 10; xCoordinate++) {
	    for (int yCoordinate = 0; yCoordinate < 5; yCoordinate++) {
		coordinates.add(new Coordinates(xCoordinate, yCoordinate));
	    }
	}
	return coordinates;
    }

    private Map<Coordinates, MapField> fillWithTerrain(List<Coordinates> coordinates) {
	Map<Coordinates, MapField> fields = new HashMap<Coordinates, MapField>();
	int grassFieldsLeast = 33;
	int waterFieldsLeast = 7;
	int mountainFieldsLeast = 5;

	for (; grassFieldsLeast > 0; grassFieldsLeast--) {
	    int randomCoordinates = random.nextInt(coordinates.size());
	    fields.put(coordinates.get(randomCoordinates), new MapField(Terrain.GRASS, FieldObject.EMPTY));
	    coordinates.remove(randomCoordinates);
	}

	for (; waterFieldsLeast > 0; waterFieldsLeast--) {
	    int randomCoordinates = random.nextInt(coordinates.size());
	    fields.put(coordinates.get(randomCoordinates), new MapField(Terrain.WATER, FieldObject.EMPTY));
	    coordinates.remove(randomCoordinates);
	}

	for (; mountainFieldsLeast > 0; mountainFieldsLeast--) {
	    int randomCoordinates = random.nextInt(coordinates.size());
	    fields.put(coordinates.get(randomCoordinates), new MapField(Terrain.MOUNTAIN, FieldObject.EMPTY));
	    coordinates.remove(randomCoordinates);
	}

	for (Iterator<Coordinates> iter = coordinates.iterator(); iter.hasNext();) {
	    fields.put(iter.next(), generateSingleRandomMapField());
	    iter.remove();
	}
	return fields;

    }

    private Coordinates fortressCoordinates(Map<Coordinates, MapField> fields) {
	Coordinates c = new Coordinates(0, 0);
	for (Entry<Coordinates, MapField> entry : fields.entrySet()) {
	    if (entry.getValue().getTerrainType() == Terrain.GRASS) {
		c = entry.getKey();
	    }
	}
	return c;
    }

    public HalfMap generateHalfMap() {
	List<Coordinates> coordinates = generateMapCoordinates();
	Map<Coordinates, MapField> fields = fillWithTerrain(coordinates);
	Coordinates fortressCoordinates = fortressCoordinates(fields);
	while (!validator.validateNoIslands(fields, fortressCoordinates) || !validator.validateMapEdges(fields)) {
	    coordinates = generateMapCoordinates();
	    fields = fillWithTerrain(coordinates);
	    fortressCoordinates = fortressCoordinates(fields);
	}
	logger.info("HalfMap has been successfuly generated and validated.");
	return new HalfMap(fields, fortressCoordinates);

    }

}
