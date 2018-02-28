package application.mechanics.utils;

import application.mechanics.base.geometry.Coordinates;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SuppressWarnings("MagicNumber")
@RunWith(SpringRunner.class)
@SpringBootTest
public class MapHelperTest {
    @Test
    public void testSomeDefaultIndexes() {
        assertEquals(new Index(5, 5), MapHelper.getIndex(new Coordinates(0, 50, 0)));
        assertEquals(new Index(5, 5), MapHelper.getIndex(new Coordinates(-100, 50, -100)));
        assertEquals(new Index(7, 6), MapHelper.getIndex(new Coordinates(500, 50, 250)));
        assertEquals(new Index(2, 3), MapHelper.getIndex(new Coordinates(-750, 50, -500)));
    }

    @Test
    public void testSomeNonDefaultIndexes() {
        assertEquals(new Index(5, 5), MapHelper.getIndex(new Coordinates(-124, 50, 120)));
        assertEquals(new Index(4, 3), MapHelper.getIndex(new Coordinates(-268, 50, -480)));
    }

    @Test
    public void testMapEdges() {
        assertEquals(new Index(0, 0), MapHelper.getIndex(new Coordinates(-1375, 50, -1375)));
        assertEquals(new Index(0, 9), MapHelper.getIndex(new Coordinates(-1375, 50, 1125)));
        assertEquals(new Index(9, 0), MapHelper.getIndex(new Coordinates(1125, 50, -1375)));
        assertEquals(new Index(9, 9), MapHelper.getIndex(new Coordinates(1125, 50, 1125)));
    }

    @Test
    public void testOutOfBounds() {
        assertEquals(new Index(0, 0), MapHelper.getIndex(new Coordinates(-1500, 50, -1700)));
        assertEquals(new Index(9, 9), MapHelper.getIndex(new Coordinates(1126, 50, 1400)));
    }
}
