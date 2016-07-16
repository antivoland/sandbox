package antivoland.rtest.model;

import java.math.BigDecimal;

public class Wallet {
    public final String id;
    public final String userId;
    public final String currency;
    public BigDecimal balance;

    public Wallet(String userId, String currency, BigDecimal balance) {
        this.id = id(userId, currency);
        this.userId = userId;
        this.currency = currency;
        this.balance = balance;
    }

    public static String id(String userId, String currency) {
        return userId + ":" + currency;
    }
}
