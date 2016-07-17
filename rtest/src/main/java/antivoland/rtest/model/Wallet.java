package antivoland.rtest.model;

import java.math.BigDecimal;

public class Wallet {
    public final String id;
    public final String currency;
    public BigDecimal balance;

    public Wallet(String id, String currency, BigDecimal balance) {
        this.id = id;
        this.currency = currency;
        this.balance = balance;
    }
}
