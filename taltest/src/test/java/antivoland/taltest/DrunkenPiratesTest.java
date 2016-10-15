package antivoland.taltest;

import antivoland.taltest.api.dev.Employees;
import antivoland.taltest.api.dev.Managers;
import antivoland.taltest.api.dev.domain.EmployeeDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Taltest.class)
public class DrunkenPiratesTest {
    private static final Logger LOG = LoggerFactory.getLogger(DrunkenPiratesTest.class);
    private static final String CAPTAIN = "captain";
    private static final String SAILOR_PREFIX = "sailor";
    private static final int SAILORS = 999;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(SAILORS);

    @Autowired
    Employees employees;
    @Autowired
    Managers managers;

    private MockMvc employeesMock;
    private MockMvc managersMock;

    @Before
    public void setup() throws Exception {
        employeesMock = standaloneSetup(employees).build();
        managersMock = standaloneSetup(managers).build();
    }

    @Test
    public void test() throws Exception {
        addSailor(CAPTAIN, CAPTAIN);
        Assert.assertEquals(0, captainPeople());

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(SAILORS);
        for (int no = 1; no <= SAILORS; ++no) {
            THREAD_POOL.submit(takeOnBoard(start, finish, no));
        }
        start.countDown();
        finish.await();

        LOG.info("What'll we do with a {} drunken sailors,", SAILORS);
        Assert.assertEquals(SAILORS + 1, crewSize());
        LOG.info("Early in the morning.");
        Assert.assertEquals(SAILORS, captainPeople());

        start = new CountDownLatch(1);
        finish = new CountDownLatch(SAILORS);
        for (int no = 1; no <= SAILORS; ++no) {
            THREAD_POOL.submit(throwOverboard(start, finish, no));
        }
        start.countDown();
        finish.await();

        LOG.info("Stuff {} sailors in a sacks and throw them over,", SAILORS);
        Assert.assertEquals(1, crewSize());
        LOG.info("Early in the morning.");
        Assert.assertEquals(0, captainPeople());

        removeSailor(CAPTAIN);
        Assert.assertEquals(0, crewSize());
    }

    private Runnable takeOnBoard(CountDownLatch start, CountDownLatch finish, int no) {
        return () -> {
            try {
                start.await();
                LOG.debug("What'll we do with a drunken sailor No {},", no);
                comeOnBoard(no);
            } catch (Exception e) {
                throw new Error(e);
            } finally {
                finish.countDown();
            }
        };
    }

    private Runnable throwOverboard(CountDownLatch start, CountDownLatch finish, int no) {
        return () -> {
            try {
                start.await();
                LOG.debug("Stuff sailor No {} in a sack and throw him over,", no);
                fallOverboard(no);
            } catch (Exception e) {
                throw new Error(e);
            } finally {
                finish.countDown();
            }
        };
    }

    private void comeOnBoard(int sailorNo) throws Exception {
        String sailorId = sailorId(sailorNo);
        addSailor(sailorId, SAILOR_PREFIX + " #" + sailorNo);
        swearAllegianceToTheCaptain(sailorId);
    }

    private void fallOverboard(int sailorNo) throws Exception {
        removeSailor(sailorId(sailorNo));
    }

    private void addSailor(String id, String name) throws Exception {
        MockHttpServletRequestBuilder request = put("/api/dev/employees/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(MAPPER.writeValueAsString(new EmployeeDetails(name)));
        employeesMock.perform(request).andExpect(status().isOk());
    }

    private void swearAllegianceToTheCaptain(String sailorId) throws Exception {
        MockHttpServletRequestBuilder request = put("/api/dev/managers/" + CAPTAIN + "/employees/" + sailorId);
        managersMock.perform(request).andExpect(status().isOk());
    }

    private void removeSailor(String id) throws Exception {
        MockHttpServletRequestBuilder request = delete("/api/dev/employees/" + id);
        employeesMock.perform(request).andExpect(status().isOk());
    }

    private static String sailorId(int no) {
        return SAILOR_PREFIX + no;
    }

    private int crewSize() throws Exception {
        MockHttpServletRequestBuilder request = get("/api/dev/employees");
        String response = employeesMock.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();
        Map<String, EmployeeDetails> employees = MAPPER.readValue(response, MAPPER.getTypeFactory().constructMapType(Map.class, String.class, EmployeeDetails.class));
        return (int) employees.keySet().stream().filter(k -> k.startsWith(SAILOR_PREFIX) || k.equals(CAPTAIN)).count();
    }

    private int captainPeople() throws Exception {
        MockHttpServletRequestBuilder request = get("/api/dev/managers");
        String response = managersMock.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn().getResponse().getContentAsString();
        Map<String, String[]> managers = MAPPER.readValue(response, MAPPER.getTypeFactory().constructMapType(Map.class, String.class, String[].class));
        return managers.containsKey(CAPTAIN) ? managers.get(CAPTAIN).length : 0;
    }
}
