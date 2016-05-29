package antivoland.oftest.model;

import org.junit.Assert;
import org.junit.Test;

public class TileTest {
    @Test
    public void testPositiveCoordinate() {
        Assert.assertEquals(1, Tile.coordinate(1.618034));
    }

    @Test
    public void testNegativeCoordinate() {
        Assert.assertEquals(-1, Tile.coordinate(-1.618034));
    }

    @Test
    public void testZeroCoordinate() {
        Assert.assertEquals(0, Tile.coordinate(-0.577216));
        Assert.assertEquals(0, Tile.coordinate(0.577216));
    }

    @Test
    public void testPositiveBounds() {
        Assert.assertArrayEquals(new int[]{1, 2}, Tile.bounds(1));
    }

    @Test
    public void testNegativeBounds() {
        Assert.assertArrayEquals(new int[]{-2, -1}, Tile.bounds(-1));
    }

    @Test
    public void testZeroBounds() {
        Assert.assertArrayEquals(new int[]{-1, 1}, Tile.bounds(0));
    }
}
