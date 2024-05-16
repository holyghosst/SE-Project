package maptests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import map.HalfMap;
import map.HalfMapGenerator;
import map.MapField;

class HalfMapTest {
    HalfMapGenerator halfMapGenerator = new HalfMapGenerator();
    private HalfMap halfMap;

    @BeforeEach
    public void setUp() throws Exception {
	halfMap = halfMapGenerator.generateHalfMap();
    }

    @Test
    public void HalfMap_AccessNonExistentField_ShouldReturnNull() {
	MapField field = halfMap.getField(100, 100);
	assertNull(field);
    }

    @Test
    public void HalfMap_AccessExistentField_ShouldReturnNotNull() {
	MapField field = halfMap.getField(0, 0);
	assertNotNull(field);
    }

}
