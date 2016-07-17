package antivoland.rtest.model;

import antivoland.rtest.RtestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;

public class WalletService {
    private static final Logger LOG = LoggerFactory.getLogger(WalletService.class);
    private static final String WITHDRAW = "Withdrawing %s %S from '%s'";
    private static final String CHARGE = "Charging %s %S to '%s'";

    private final WalletStorage walletStorage;
    private final Converter converter;
    private final RtestConfig config;

    @Inject
    public WalletService(WalletStorage walletStorage, Converter converter, RtestConfig config) {
        this.walletStorage = walletStorage;
        this.converter = converter;
        this.config = config;
    }

    public void put(String id, String currency, BigDecimal balance) throws WalletAlreadyExistsException {
        Wallet wallet = new Wallet(id, currency, balance);
        if (!walletStorage.insert(wallet.id, wallet)) {
            throw new WalletAlreadyExistsException(wallet.id);
        }
    }

    public Wallet get(String id) throws WalletNotFoundException {
        return walletStorage.get(id);
    }

    public void withdraw(String id, String currency, BigDecimal amount) throws WalletNotFoundException, WalletHasInsufficientFundsException {
        LOG.info(String.format(WITHDRAW, amount, currency, id));
        Wallet wallet;
        do {
            wallet = get(id);
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
        } while (!walletStorage.update(wallet.id, wallet));
    }

    public void charge(String id, String currency, BigDecimal amount) throws WalletNotFoundException {
        LOG.info(String.format(CHARGE, amount, currency, id));
        Wallet wallet;
        do {
            wallet = get(id);
            BigDecimal newBalance;
            if (wallet.currency.equals(currency)) {
                newBalance = wallet.balance.add(amount);
            } else {
                BigDecimal converted = converter.convert(currency, wallet.currency, amount, config.conversionFee);
                newBalance = wallet.balance.add(converted);
            }
            wallet.balance = newBalance;
        } while (!walletStorage.update(wallet.id, wallet));
    }
}
