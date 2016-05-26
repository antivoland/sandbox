package antivoland.oftest.api.dev.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Point {
    public final double lon;
    public final double lat;

    public Point(@JsonProperty("lon") double lon, @JsonProperty("lat") double lat) {
        this.lon = lon;
        this.lat = lat;
    }
}
