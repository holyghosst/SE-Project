package maptests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import map.ClientFullMap;
import map.Coordinates;
import map.FieldObject;
import map.MapField;
import map.Terrain;

class ClientFullMapTest {
    ClientFullMap map;

    @BeforeEach
    void setUp() throws Exception {
	map = new ClientFullMap();
    }

    @Test
    void EmptyFullMap_AddField_MapShouldNotBeEmpty() {
	Coordinates coordinates = new Coordinates(0, 0);
	MapField field = new MapField(Terrain.MOUNTAIN, FieldObject.EMPTY);
	map.addField(coordinates, field);
	assertThat(map.getFields().size(), is(equalTo(1)));
    }

    @Test
    public void FullMap_FindMapHalves_HalvesAreCorrectlyFound() {
	Coordinates coordinates = new Coordinates(0, 0);
	MapField field = new MapField(Terrain.GRASS, FieldObject.PLAYER_FORTRESS);

	map.addField(coordinates, field);
	map.addField(new Coordinates(9, 0), new MapField(Terrain.GRASS, FieldObject.EMPTY));
	map.addField(new Coordinates(0, 9), new MapField(Terrain.GRASS, FieldObject.EMPTY));
	map.findMapHalves();

	assertThat(map.getMyHalf().getxMax(), is(equalTo(9)));
	assertThat(map.getMyHalf().getxMin(), is(equalTo(0)));

	assertThat(map.getMyHalf().getyMax(), is(equalTo(4)));
	assertThat(map.getMyHalf().getxMin(), is(equalTo(0)));

	assertThat(map.getEnemyHalf().getxMax(), is(equalTo(9)));
	assertThat(map.getEnemyHalf().getxMin(), is(equalTo(0)));

	assertThat(map.getEnemyHalf().getyMax(), is(equalTo(9)));
	assertThat(map.getEnemyHalf().getyMin(), is(equalTo(5)));
    }

}
