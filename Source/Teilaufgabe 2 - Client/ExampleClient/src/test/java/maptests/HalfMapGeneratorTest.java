package maptests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import map.HalfMap;
import map.HalfMapGenerator;
import map.Terrain;

class HalfMapGeneratorTest {
    private HalfMapGenerator halfMapGenerator;

    @BeforeEach
    public void setUp() throws Exception {
	halfMapGenerator = new HalfMapGenerator();
    }

    @Test
    public void NoHalfMap_GenerateHalfMap_GrassCountShouldBe33OrMore() {
	HalfMap map = halfMapGenerator.generateHalfMap();
	long grassCount = map.getFields().values().stream().filter(field -> field.getTerrainType() == Terrain.GRASS)
		.count();
	assertTrue(grassCount >= 33);
    }

    @Test
    public void NoHalfMap_GenerateHalfMap_WaterCountShouldBe7OrMore() {
	HalfMap map = halfMapGenerator.generateHalfMap();
	long waterCount = map.getFields().values().stream().filter(field -> field.getTerrainType() == Terrain.WATER)
		.count();
	assertTrue(waterCount >= 7);
    }

    @Test
    public void NoHalfMap_GenerateHalfMap_MountainCountShouldBe5OrMore() {
	HalfMap map = halfMapGenerator.generateHalfMap();
	long mountainCount = map.getFields().values().stream()
		.filter(field -> field.getTerrainType() == Terrain.MOUNTAIN).count();
	assertTrue(mountainCount >= 5);
    }

    @Test
    public void NoHalfMap_GenerateHalfMap_FortressCoordinatesNotNull() {
	HalfMap map = halfMapGenerator.generateHalfMap();
	assertNotNull(map.getStartPosition());
    }

    @Test
    public void NoHalfMap_GenerateHalfMap_HalfMapContainsExactly50Fields() {
	HalfMap map = halfMapGenerator.generateHalfMap();
	int fieldCount = map.getFields().size();
	assertThat(fieldCount, is(equalTo(50)));
    }

}
