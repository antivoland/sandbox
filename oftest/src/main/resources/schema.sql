CREATE TABLE user_point
(
  user_id INTEGER PRIMARY KEY   NOT NULL,
  lat     DOUBLE                NOT NULL,
  lon     DOUBLE                NOT NULL
);

CREATE INDEX idx_user_points ON user_point (lat, lon);

CREATE TABLE tile
(
  tile_y         INTEGER NOT NULL,
  tile_x         INTEGER NOT NULL,
  distance_error DOUBLE  NOT NULL,
  PRIMARY KEY (tile_y, tile_x)
);
