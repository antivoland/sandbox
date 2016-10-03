package antivoland.amahir.intersection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Intersection {
    public static int[] intersection(int[] a, int[] b) {
        List<Integer> intersection = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < a.length && j < b.length) {
            if (a[i] > b[j]) {
                j = indexOfClosestGreaterOrEqual(b, j, a[i]);
            } else if (a[i] < b[j]) {
                i = indexOfClosestGreaterOrEqual(a, i, b[j]);
            } else {
                intersection.add(a[i]);
                ++i;
                ++j;
            }
        }
        return intersection.stream().mapToInt(e -> e).toArray();
    }

    static int indexOfClosestGreaterOrEqual(int[] values, int from, int value) {
        int index = Arrays.binarySearch(values, from, values.length, value);
        if (index < 0) {
            index = -1 * (index + 1);
        }
        return index > from ? index : values.length;
    }
}
