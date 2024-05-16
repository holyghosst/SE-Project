package pathgenerationtests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import map.ClientFullMap;
import map.Coordinates;
import map.FieldObject;
import map.MapField;
import map.PartialMap;
import map.Terrain;
import pathgeneration.Pathfinder;

class PathfinderTest {
    Pathfinder pathFinder;

    @BeforeEach
    public void setUp() throws Exception {
	pathFinder = new Pathfinder();

    }

    @Test
    public void SearchingForEnemyPart_CheckIfFieldIsPassable_ShouldBeTrue() {
	ClientFullMap map = Mockito.mock(ClientFullMap.class);
	when(map.getMaximalX()).thenReturn(19);
	when(map.getMaximalY()).thenReturn(5);
	when(map.getField(any(Coordinates.class))).thenReturn(new MapField(Terrain.GRASS, FieldObject.EMPTY));
	pathFinder.setFullMap(map);

	assertTrue(pathFinder.isPassable(0, 0));
    }

    @Test
    public void SearchingForEnemyPart_FindGoal_MovementsAreNotBeEmpty() {
	ClientFullMap map = Mockito.mock(ClientFullMap.class);
	when(map.getMaximalX()).thenReturn(19);
	when(map.getMaximalY()).thenReturn(5);
	when(map.getField(any(Coordinates.class))).thenReturn(new MapField(Terrain.GRASS, FieldObject.EMPTY));
	pathFinder.setFullMap(map);

	pathFinder.findGoal(new Coordinates(0, 0));
	assertThat(pathFinder.getMovements().size(), is(not(equalTo(0))));
    }

    @Test
    public void SearchingForTreasure_FindGoal_TreasureFinderMovementsAreNotEmpty() {
	ClientFullMap map = Mockito.mock(ClientFullMap.class);
	when(map.getMyHalf()).thenReturn(new PartialMap(0, 9, 4, 9));
	pathFinder.setFullMap(map);

	when(map.getField(any(Coordinates.class))).thenReturn(new MapField(Terrain.GRASS, FieldObject.EMPTY));
	pathFinder.findTreasure(new Coordinates(4, 7));
	assertThat(pathFinder.getTreasureFinderMovements().size(), is(not(equalTo(0))));
    }

    @Test
    public void SearchingForFortress_FindGoal_FortressFinderMovementsAreNotEmpty() {
	ClientFullMap map = Mockito.mock(ClientFullMap.class);
	when(map.getEnemyHalf()).thenReturn(new PartialMap(0, 9, 0, 4));
	pathFinder.setFullMap(map);

	when(map.getField(any(Coordinates.class))).thenReturn(new MapField(Terrain.GRASS, FieldObject.EMPTY));
	pathFinder.findFortress(new Coordinates(1, 2));
	assertThat(pathFinder.getFortressFinderMovements().size(), is(not(equalTo(0))));
    }

}
