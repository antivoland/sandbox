package antivoland.oftest.model;

import org.junit.Assert;
import org.junit.Test;

public class TileTest {
    @Test
    public void testPositiveCoordinate() {
        Assert.assertEquals(Tile.coordinate(1.618034), 1);
    }

    @Test
    public void testNegativeCoordinate() {
        Assert.assertEquals(Tile.coordinate(-1.618034), -1);
    }

    @Test
    public void testZeroCoordinate() {
        Assert.assertEquals(Tile.coordinate(-0.577216), 0);
        Assert.assertEquals(Tile.coordinate(0.577216), 0);
    }

    @Test
    public void testPositiveBounds() {
        Assert.assertArrayEquals(Tile.bounds(1), new int[]{1, 2});
    }

    @Test
    public void testNegativeBounds() {
        Assert.assertArrayEquals(Tile.bounds(-1), new int[]{-2, -1});
    }

    @Test
    public void testZeroBounds() {
        Assert.assertArrayEquals(Tile.bounds(0), new int[]{-1, 1});
    }
}
