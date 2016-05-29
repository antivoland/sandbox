package antivoland.oftest.api.dev;

import antivoland.oftest.Oftest;
import antivoland.oftest.api.dev.domain.Point;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Oftest.class)
@WebIntegrationTest
public class TenLittleNiggersTest {
    private static final Logger LOG = LoggerFactory.getLogger(TenLittleNiggersTest.class);
    private static final String USERS_URL = "/api/dev/users/%s";
    private static final String NEIGHBORS_URL = "/api/dev/points/%s:%s/neighbors";
    private static final String DISTANCE_URL = "/api/dev/points/%s:%s/distance-to/%s";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    Users users;
    @Autowired
    Points points;

    private MockMvc usersMock;
    private MockMvc pointsMock;
    private final Deque<Integer> boys = new ArrayDeque<>();

    @Before
    public void setup() throws Exception {
        usersMock = standaloneSetup(users).build();
        pointsMock = standaloneSetup(points).build();
    }

    @Test
    public void test() throws Exception {
        Point domodedovo = new Point(55.408611, 37.906111);
        placeBoysAt(domodedovo);

        LOG.info("Ten little nigger boys went out to dine");
        Assert.assertEquals(10, countBoys());
        Point mcdonalds = new Point(55.764726, 37.603052);
        moveBoysTo(mcdonalds);
        LOG.info("One choked his little self, and then there were nine");
        removeOneBoy();

        LOG.info("Nine little nigger boys sat up very late");
        Assert.assertEquals(9, countBoys());
        LOG.info("One overslept himself, and then there were eight");
        removeOneBoy();

        LOG.info("Eight little nigger boys travelling in Devon");
        Assert.assertEquals(8, countBoys());
        Point mollyGwynns = new Point(55.741069, 37.628205);
        moveBoysTo(mollyGwynns);
        LOG.info("One said he´d stay there, and then there were seven");
        removeOneBoy();

        LOG.info("Seven little nigger boys chopping up sticks");
        Assert.assertEquals(7, countBoys());
        Point botanicalGarden = new Point(55.778600, 37.634998);
        moveBoysTo(botanicalGarden);
        LOG.info("One chopped himself in half, and then there were six");
        removeOneBoy();

        LOG.info("Six little nigger boys playing with a hive");
        Assert.assertEquals(6, countBoys());
        LOG.info("A bumble-bee stung one, and then there were five");
        removeOneBoy();

        LOG.info("Five little nigger boys going in for law");
        Assert.assertEquals(5, countBoys());
        LOG.info("One got in chancery, and then there were four");
        removeOneBoy();

        LOG.info("Four little nigger boys going out to sea");
        Assert.assertEquals(4, countBoys());
        LOG.info("A red herring swallowed one, and then there were three");
        removeOneBoy();

        LOG.info("Three little nigger boys walking in the Zoo");
        Assert.assertEquals(3, countBoys());
        Point moscowZoo = new Point(55.761944, 37.577222);
        moveBoysTo(moscowZoo);
        LOG.info("A big bear hugged one, and then there were two");
        removeOneBoy();

        LOG.info("Two little nigger boys sitting in the sun");
        Assert.assertEquals(2, countBoys());
        LOG.info("One got frizzled up, and then there was one");
        removeOneBoy();

        LOG.info("One little nigger boy left all alone");
        Assert.assertEquals(1, countBoys());
        LOG.info("One got frizzled up, and then there was one");
        removeOneBoy();

        Assert.assertEquals(0, countBoys());
    }

    private void placeBoysAt(Point point) throws Exception {
        int boy = 0;
        while (boys.size() < 10) {
            boys.push(++boy);
            createUser(boy, point);
        }
    }

    private int countBoys() throws Exception {
        return population(55, 37);
    }

    private void moveBoysTo(Point point) throws Exception {
        for (int boy : boys) {
            trackUser(boy, point);
        }
    }

    private void removeOneBoy() throws Exception {
        int boy = boys.pop();
        removeUser(boy);
    }

    private void createUser(int userId, Point point) throws Exception {
        MockHttpServletRequestBuilder request = put(String.format(USERS_URL, userId))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(MAPPER.writeValueAsString(point));

        usersMock.perform(request).andExpect(status().isOk());
    }

    private void trackUser(int userId, Point point) throws Exception {
        MockHttpServletRequestBuilder request = post(String.format(USERS_URL, userId))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(MAPPER.writeValueAsString(point));

        usersMock.perform(request).andExpect(status().isOk());
    }

    private void removeUser(int userId) throws Exception {
        MockHttpServletRequestBuilder request = delete(String.format(USERS_URL, userId))
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        usersMock.perform(request).andExpect(status().isOk());
    }

    private int population(int tileY, int tileX) throws Exception {
        MockHttpServletRequestBuilder request = get(String.format(NEIGHBORS_URL, tileY, tileX));

        String response = pointsMock.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        return MAPPER.readValue(response, Integer.class);
    }
}
