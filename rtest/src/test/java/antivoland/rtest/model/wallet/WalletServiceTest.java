package antivoland.rtest.model.wallet;

import antivoland.rtest.storage.WalletDummyStorage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WalletServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(WalletServiceTest.class);
    private static final String WALL = "wall";
    private static final String BOTTLE = "BOTTLE";
    private static final int BOTTLES = 999;

    private final ExecutorService threadPool = Executors.newFixedThreadPool(BOTTLES);
    private WalletService walletService;

    @Before
    public void setup() {
        walletService = new WalletService(new WalletDummyStorage(), null, null);
    }

    @After
    public void destroy() {
        threadPool.shutdownNow();
    }

    @Test
    public void test() throws Exception {
        walletService.put(WALL, BOTTLE, BigDecimal.ZERO);

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(BOTTLES);
        for (int no = 1; no <= BOTTLES; ++no) {
            threadPool.submit(staff(start, finish, no));
        }
        start.countDown();
        finish.await();

        Wallet wall = walletService.get(WALL);
        LOG.info(wall.balance + " bottles of beer on the wall...");
        Assert.assertTrue(new BigDecimal(String.valueOf(BOTTLES)).compareTo(wall.balance) == 0);
        Assert.assertEquals(BOTTLES, wall.version);

        LOG.info("Bar is open!");

        start = new CountDownLatch(1);
        finish = new CountDownLatch(BOTTLES);
        for (int no = 1; no <= BOTTLES; ++no) {
            threadPool.submit(visitor(start, finish, no));
        }
        start.countDown();
        finish.await();

        wall = walletService.get(WALL);
        LOG.info(wall.balance + " bottles of beer on the wall...");
        Assert.assertTrue(BigDecimal.ZERO.compareTo(wall.balance) == 0);
        Assert.assertEquals(BOTTLES * 2, wall.version);
    }

    private Runnable staff(CountDownLatch start, CountDownLatch finish, int no) {
        return () -> {
            try {
                start.await();
                LOG.info("Putting bottle No " + no);
                walletService.charge(WALL, BOTTLE, BigDecimal.ONE);
            } catch (Exception e) {
                throw new Error(e);
            } finally {
                finish.countDown();
            }
        };
    }

    private Runnable visitor(CountDownLatch start, CountDownLatch finish, int no) {
        return () -> {
            try {
                start.await();
                LOG.info("Drinking bottle No " + no);
                walletService.withdraw(WALL, BOTTLE, BigDecimal.ONE);
            } catch (Exception e) {
                throw new Error(e);
            } finally {
                finish.countDown();
            }
        };
    }
}
