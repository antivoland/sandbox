package antivoland.oftest.model.service;

public class TileNotFoundException extends Exception {
    private static final String MESSAGE = "Tile with coordinates (%s, %s) not found";

    public TileNotFoundException(int y, int x) {
        super(String.format(MESSAGE, y, x));
    }
}
