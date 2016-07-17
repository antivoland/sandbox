package antivoland.rtest.model;

import java.math.BigDecimal;

public class Wallet implements Versioned {
    public final String id;
    public final String currency;
    public BigDecimal balance;
    public long version;

    public Wallet(String id, String currency, BigDecimal balance) {
        this.id = id;
        this.currency = currency;
        this.balance = balance;
        this.version = Versioned.SINCE;
    }

    public Wallet(Wallet wallet) {
        this.id = wallet.id;
        this.currency = wallet.currency;
        this.balance = wallet.balance;
        this.version = wallet.version;
    }

    @Override
    public long version() {
        return version;
    }

    @Override
    public void increaseVersion() {
        version++;
    }
}
