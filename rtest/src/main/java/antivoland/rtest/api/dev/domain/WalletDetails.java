package antivoland.rtest.api.dev.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class WalletDetails {
    public final String currency;
    public final BigDecimal balance;

    public WalletDetails(
            @JsonProperty("currency") String currency,
            @JsonProperty("balance") BigDecimal balance) {
        this.currency = currency;
        this.balance = balance;
    }
}
