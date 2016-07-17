package antivoland.rtest.api.dev.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class TransferDetails {
    public final String from;
    public final String to;
    public final String currency;
    public final BigDecimal amount;

    public TransferDetails(
            @JsonProperty("from") String from,
            @JsonProperty("to") String to,
            @JsonProperty("currency") String currency,
            @JsonProperty("amount") BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.currency = currency;
        this.amount = amount;
    }
}
