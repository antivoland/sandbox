package antivoland.oftest.api.dev;

import antivoland.oftest.Oftest;
import antivoland.oftest.api.dev.domain.Distance;
import antivoland.oftest.api.dev.domain.Point;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Для этого теста я решил замерить длину конька крыши здания Манежа и длину
 * её диагонали по картам Яндекса, это 176 и 183 метра соответственно. 180
 * метров я выставил как погрешность определения расстояний внутри ячейки
 * с координатами (55, 37).
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Oftest.class)
@WebIntegrationTest
public class ManegeMeasurementsTest {
    private static final String USERS_URL = "/api/dev/users/%s";
    private static final String DISTANCE_URL = "/api/dev/points/%s:%s/distance-to/%s";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final int MANEGE = 1;
    private static final Point NORTH_RIDGE_POINT = new Point(55.754207, 37.612793);
    private static final Point SOUTH_RIDGE_POINT = new Point(55.752687, 37.612028);
    private static final Point NORTH_EAST_CORNER = new Point(55.754146, 37.613168);
    private static final Point SOUTH_WEST_CORNER = new Point(55.752757, 37.611585);

    @Autowired
    Users users;
    @Autowired
    Points points;

    private MockMvc usersMock;
    private MockMvc pointsMock;

    @Before
    public void setup() throws Exception {
        usersMock = standaloneSetup(users).build();
        pointsMock = standaloneSetup(points).build();
    }

    @Test
    public void test() throws Exception {
        startAt(NORTH_RIDGE_POINT);
        Assert.assertEquals(Distance.CLOSE, distanceTo(SOUTH_RIDGE_POINT));

        moveTo(NORTH_EAST_CORNER);
        Assert.assertEquals(Distance.FAR, distanceTo(SOUTH_WEST_CORNER));

        cleanUp();
    }

    private void startAt(Point point) throws Exception {
        MockHttpServletRequestBuilder request = put(String.format(USERS_URL, MANEGE))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(MAPPER.writeValueAsString(point));

        usersMock.perform(request).andExpect(status().isOk());
    }

    private void moveTo(Point point) throws Exception {
        MockHttpServletRequestBuilder request = post(String.format(USERS_URL, MANEGE))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(MAPPER.writeValueAsString(point));

        usersMock.perform(request).andExpect(status().isOk());
    }

    private void cleanUp() throws Exception {
        MockHttpServletRequestBuilder request = delete(String.format(USERS_URL, MANEGE))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        usersMock.perform(request).andExpect(status().isOk());
    }

    private Distance distanceTo(Point point) throws Exception {
        MockHttpServletRequestBuilder request = get(String.format(DISTANCE_URL, point.lat, point.lon, MANEGE));

        String response = pointsMock.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        return MAPPER.readValue(response, Distance.class);
    }
}
