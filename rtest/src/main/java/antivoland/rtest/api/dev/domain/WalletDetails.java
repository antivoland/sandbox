package antivoland.rtest.api.dev.domain;

import org.codehaus.jackson.annotate.JsonProperty;

import java.math.BigDecimal;

public class WalletDetails {
    public final BigDecimal balance;

    public WalletDetails(@JsonProperty("balance") BigDecimal balance) {
        this.balance = balance;
    }
}
