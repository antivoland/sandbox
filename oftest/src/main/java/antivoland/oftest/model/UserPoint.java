package antivoland.oftest.model;

public class UserPoint {
    public final int userId;
    public final double lat;
    public final double lon;

    public UserPoint(int userId, double lat, double lon) {
        this.userId = userId;
        this.lat = lat;
        this.lon = lon;
    }
}
