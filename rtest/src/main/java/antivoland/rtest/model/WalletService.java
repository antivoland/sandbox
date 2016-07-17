package antivoland.rtest.model;

import antivoland.rtest.RtestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class WalletService {
    private static final Logger LOG = LoggerFactory.getLogger(WalletService.class);
    private static final String WITHDRAW = "Withdrawing %s %S from '%s'";
    private static final String CHARGE = "Charging %s %S to '%s'";

    private final Map<String, Wallet> wallets = new HashMap<>();
    private final Converter converter;
    private final RtestConfig config;

    @Inject
    public WalletService(Converter converter, RtestConfig config) {
        this.converter = converter;
        this.config = config;
    }

    public void put(String id, String currency, BigDecimal balance) throws WalletAlreadyExistsException {
        if (wallets.containsKey(id)) {
            throw new WalletAlreadyExistsException(id);
        }
        wallets.put(id, new Wallet(id, currency, balance));
    }

    public Wallet get(String id) throws WalletNotFoundException {
        Wallet wallet = wallets.get(id);
        if (wallet == null) {
            throw new WalletNotFoundException(id);
        }
        return wallet;
    }

    public void withdraw(String id, String currency, BigDecimal amount) throws WalletNotFoundException, WalletHasInsufficientFundsException {
        LOG.info(String.format(WITHDRAW, amount, currency, id));
        Wallet wallet = get(id);
        BigDecimal newBalance;
        if (wallet.currency.equals(currency)) {
            newBalance = wallet.balance.subtract(amount);
        } else {
            BigDecimal converted = converter.convert(currency, wallet.currency, amount, config.conversionFee);
            newBalance = wallet.balance.subtract(converted);
        }
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new WalletHasInsufficientFundsException(wallet.id);
        }
        wallet.balance = newBalance;
    }

    public void charge(String id, String currency, BigDecimal amount) throws WalletNotFoundException {
        LOG.info(String.format(CHARGE, amount, currency, id));
        Wallet wallet = get(id);
        BigDecimal newBalance;
        if (wallet.currency.equals(currency)) {
            newBalance = wallet.balance.add(amount);
        } else {
            BigDecimal converted = converter.convert(currency, wallet.currency, amount, config.conversionFee);
            newBalance = wallet.balance.add(converted);
        }
        wallet.balance = newBalance;
    }
}
