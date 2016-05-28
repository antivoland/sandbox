package antivoland.oftest.api.dev.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Point {
    public final double lat;
    public final double lon;

    public Point(@JsonProperty("lat") double lat, @JsonProperty("lon") double lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
