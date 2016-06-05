package antivoland.perfectsquare;

import org.junit.Assert;
import org.junit.Test;

public class PerfectSquareTest {
    @Test
    public void testSomeNumbers() {
        Assert.assertTrue(PerfectSquare.check(0));
        Assert.assertTrue(PerfectSquare.check(1));
        Assert.assertTrue(PerfectSquare.check(4));
        Assert.assertTrue(PerfectSquare.check(9));
        Assert.assertTrue(PerfectSquare.check(16));
        Assert.assertTrue(PerfectSquare.check(25));
        Assert.assertTrue(PerfectSquare.check(36));
        Assert.assertTrue(PerfectSquare.check(49));
        Assert.assertTrue(PerfectSquare.check(64));
        Assert.assertTrue(PerfectSquare.check(81));
    }
}
