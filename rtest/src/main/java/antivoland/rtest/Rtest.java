package antivoland.rtest;

import antivoland.rtest.api.dev.Transfers;
import antivoland.rtest.api.dev.Wallets;
import antivoland.rtest.model.Converter;
import antivoland.rtest.model.TransferService;
import antivoland.rtest.model.WalletService;
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

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumSet;

public class Rtest {
    private static final Logger LOG = LoggerFactory.getLogger(Rtest.class);
    private static final int PORT = 10111; // todo: move to config

    public static void main(String[] args) throws Exception {
        // replace jersey logger with slf4j
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        Guice.createInjector(Stage.PRODUCTION, new ServletModule() {
            @Override
            protected void configureServlets() {
                serve("/*").with(GuiceContainer.class, Collections.singletonMap(JSONConfiguration.FEATURE_POJO_MAPPING, "true"));

                bind(WalletService.class).in(Scopes.SINGLETON);
                bind(TransferService.class).in(Scopes.SINGLETON);

                bind(Converter.class).to(DummyConverter.class);

                bind(Wallets.class).in(Scopes.SINGLETON);
                bind(Transfers.class).in(Scopes.SINGLETON);

                bind(Dummy.class).in(Scopes.SINGLETON);
            }
        }).getInstance(Dummy.class).test();

        Server server = new Server(PORT);
        ServletContextHandler handler = new ServletContextHandler(server, "/");
        handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));
        server.start();
        server.join();
    }

    public static class Dummy {
        private final WalletService walletService;
        private final TransferService transferService;

        @Inject
        public Dummy(WalletService walletService, TransferService transferService) {
            this.walletService = walletService;
            this.transferService = transferService;
        }

        public void test() throws Exception {
            walletService.put("merlin:gbp", "GBP", BigDecimal.TEN);
            walletService.put("alice:gbp", "GBP", BigDecimal.ZERO);
            walletService.put("bob:sos", "SOS", BigDecimal.ZERO);

            transferService.put("1", "merlin:gbp", "alice:gbp", "GBP", BigDecimal.ONE);
            transferService.put("2", "merlin:gbp", "bob:sos", "GBP", BigDecimal.ONE);
            transferService.put("3", "merlin:gbp", "bob:sos", "SOS", new BigDecimal("745.404"));

            transferService.execute("1");
            transferService.execute("2");
            transferService.execute("3");

            LOG.info("Merlin GBP balance: " + walletService.get("merlin:gbp").balance + " (expecting 7)");
            LOG.info("Alice GBP balance: " + walletService.get("alice:gbp").balance + " (expecting 1)");
            LOG.info("Bob SOS balance: " + walletService.get("bob:sos").balance + " (expecting 1490.808)");
        }
    }

    public static class DummyConverter extends Converter {
        @Override
        protected BigDecimal rate(String from, String to) {
            if ("GBP".equals(from) && "SOS".equals(to)) {
                return new BigDecimal("745.404");
            } else if ("SOS".equals(from) && "GBP".equals(to)) {
                return new BigDecimal("0.00134155");
            } else {
                throw new UnsupportedOperationException("Not implemented yet");
            }
        }
    }
}
