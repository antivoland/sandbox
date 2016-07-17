package antivoland.rtest.model;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class WalletService {
    private static final BigDecimal CONVERSION_FEE = BigDecimal.ZERO; // todo: move to config and replace with non-zero value
    private static final Map<String, Wallet> wallets = new HashMap<>();

    private final Converter converter;

    @Inject
    public WalletService(Converter converter) {
        this.converter = converter;
    }

    public void put(String userId, String currency, BigDecimal balance) throws WalletAlreadyExistsException {
        String walletId = Wallet.id(userId, currency);
        if (wallets.containsKey(walletId)) {
            throw new WalletAlreadyExistsException(userId, currency);
        }
        wallets.put(walletId, new Wallet(userId, currency, balance));
    }

    public Wallet get(String userId, String currency) throws WalletNotFoundException {
        Wallet wallet = wallets.get(Wallet.id(userId, currency));
        if (wallet == null) {
            throw new WalletNotFoundException(userId, currency);
        }
        return wallet;
    }

    private void withdraw(String userId, String currency, BigDecimal amount) throws WalletNotFoundException, WalletHasInsufficientFundsException {
        Wallet wallet = get(userId, currency);
        BigDecimal newBalance;
        if (wallet.currency.equals(currency)) {
            newBalance = wallet.balance.subtract(amount);
        } else {
            BigDecimal converted = converter.convert(currency, wallet.currency, amount, CONVERSION_FEE);
            newBalance = wallet.balance.subtract(converted);
        }
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new WalletHasInsufficientFundsException(userId, currency);
        }
        wallet.balance = newBalance;
    }

    private void charge(String userId, String currency, BigDecimal amount) throws WalletNotFoundException {
        Wallet wallet = get(userId, currency);
        BigDecimal newBalance;
        if (wallet.currency.equals(currency)) {
            newBalance = wallet.balance.add(amount);
        } else {
            BigDecimal converted = converter.convert(currency, wallet.currency, amount, CONVERSION_FEE);
            newBalance = wallet.balance.add(converted);
        }
        wallet.balance = newBalance;
    }
}
