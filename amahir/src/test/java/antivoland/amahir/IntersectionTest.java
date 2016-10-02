package antivoland.amahir;

import org.junit.Assert;
import org.junit.Test;

public class IntersectionTest {
    private static final int RARE_GROUPS = 5;

    @Test
    public void testEmpty() {
        int[] intersection = Intersection.intersection(new int[0], new int[0]);
        Assert.assertArrayEquals(new int[0], intersection);
    }

    @Test
    public void testRegular() {
        int[] a = new int[]{1, 2, 4, 6, 8, 10};
        int[] b = new int[]{3, 4, 5, 8, 11, 12};
        int[] intersection = Intersection.intersection(a, b);
        Assert.assertArrayEquals(new int[]{4, 8}, intersection);
    }

    @Test
    public void testRare() {
        int[] a = new int[RARE_GROUPS * 101];
        int[] b = new int[RARE_GROUPS * 101];
        for (int i = 0; i < RARE_GROUPS; ++i) {
            for (int j = 0; j <= 100; ++j) {
                a[101 * i + j] = 100 * 2 * i + j;
                b[101 * i + j] = 100 * (2 * i + 1) + j;
            }
        }
        int[] expected = new int[RARE_GROUPS * 2 - 1];
        for (int i = 0; i < RARE_GROUPS * 2 - 1; ++i) {
            expected[i] = 100 * (i + 1);
        }

        int[] intersection = Intersection.intersection(a, b);
        Assert.assertArrayEquals(expected, intersection);
    }
}
