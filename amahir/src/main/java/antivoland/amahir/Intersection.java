package antivoland.amahir;

import java.util.ArrayList;
import java.util.List;

public class Intersection {
    public static int[] intersection(int[] a, int[] b) {
        List<Integer> intersection = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < a.length && j < b.length) {
            if (a[i] > b[j]) {
                ++j;
            } else if (a[i] < b[j]) {
                ++i;
            } else {
                intersection.add(a[i]);
                ++i;
                ++j;
            }
        }
        return intersection.stream().mapToInt(e -> e).toArray();
    }
}
