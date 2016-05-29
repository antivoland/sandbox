package antivoland.oftest.model;

public class Tile {
    public static int coordinate(double pointCoordinate) {
        return (int) pointCoordinate;
    }

    public static int[] bounds(int tileCoordinate) {
        if (tileCoordinate > 0) {
            return new int[]{tileCoordinate, tileCoordinate + 1};
        } else if (tileCoordinate < 0) {
            return new int[]{tileCoordinate - 1, tileCoordinate};
        } else {
            return new int[]{-1, 1};
        }
    }
}
