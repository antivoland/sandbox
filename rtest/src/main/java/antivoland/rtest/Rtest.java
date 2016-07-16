package antivoland.rtest;

import antivoland.rtest.api.dev.Transfers;
import antivoland.rtest.api.dev.Users;
import antivoland.rtest.api.dev.Wallets;
import antivoland.rtest.model.Transfer;
import antivoland.rtest.model.TransferException;
import antivoland.rtest.model.User;
import antivoland.rtest.model.Wallet;
import com.google.inject.Guice;
import com.google.inject.Scopes;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.servlet.DispatcherType;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class Rtest {
    private static final Logger LOG = LoggerFactory.getLogger(Rtest.class);
    private static final int PORT = 10111; // todo: move to config
    private final static Map<String, User> users = new HashMap<>();
    private final static Map<String, Wallet> wallets = new HashMap<>();

    public static void main(String[] args) throws Exception {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        putUser("merlin", User.Gender.male);
        putWallet("merlin", "gbp", BigDecimal.TEN);

        putUser("alice", User.Gender.female);
        putWallet("alice", "gbp", BigDecimal.ZERO);

        putUser("bob", User.Gender.male);
        putWallet("bob", "sos", BigDecimal.ZERO);

        transfer(Wallet.id("merlin", "gbp"), Wallet.id("alice", "gbp"), BigDecimal.ONE);
        transfer(Wallet.id("merlin", "gbp"), Wallet.id("bob", "sos"), BigDecimal.ONE);

        LOG.info("Merlin GBP balance: " + wallets.get(Wallet.id("merlin", "gbp")).balance + " (expecting 8)");
        LOG.info("Alice GBP balance: " + wallets.get(Wallet.id("alice", "gbp")).balance + " (expecting 1)");
        LOG.info("Bob SOS balance: " + wallets.get(Wallet.id("bob", "sos")).balance + " (expecting 745.404)");

        Guice.createInjector(Stage.PRODUCTION,
                new ServletModule() {
                    @Override
                    protected void configureServlets() {
                        serve("/*").with(GuiceContainer.class, Collections.singletonMap(JSONConfiguration.FEATURE_POJO_MAPPING, "true"));

                        bind(Users.class).in(Scopes.SINGLETON);
                        bind(Wallets.class).in(Scopes.SINGLETON);
                        bind(Transfers.class).in(Scopes.SINGLETON);
                    }
                });

        Server server = new Server(PORT);
        ServletContextHandler handler = new ServletContextHandler(server, "/");
        handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
        server.start();
        server.join();
    }

    private static void putUser(String id, User.Gender gender) {
        User user = new User(id, gender);
        users.put(user.id, user);
    }

    private static void putWallet(String userId, String currency, BigDecimal balance) {
        Wallet wallet = new Wallet(userId, currency, balance);
        wallets.put(wallet.id, wallet);
    }

    private static Transfer transfer(String fromId, String toId, BigDecimal amount) throws TransferException {
        Wallet from = wallets.get(fromId);
        if (from.balance.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new TransferException("Not enough funds");
        }

        Wallet to = wallets.get(toId);
        Transfer transfer = new Transfer(from, to, amount);

        from.balance = from.balance.subtract(amount);
        if (from.currency.equals(to.currency)) {
            to.balance = to.balance.add(amount);
        } else {
            to.balance = to.balance.add(convert(from.currency, to.currency, amount));
        }
        transfer.complete(Transfer.Status.succeeded);
        return transfer;
    }

    private static BigDecimal convert(String from, String to, BigDecimal amount) {
        if ("gbp".equals(from) && "sos".equals(to)) {
            return amount.multiply(new BigDecimal("745.404"));
        } else {
            throw new UnsupportedOperationException("Not implemented yet");
        }
    }
}
