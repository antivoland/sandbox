package antivoland.rtest.model;

import java.math.BigDecimal;
import java.time.Instant;

public class Transfer {
    public enum Status {pending, succeeded, failed}

    public final Wallet from;
    public final Wallet to;
    public final BigDecimal amount;
    public Status status;
    public final Instant created;
    public Instant completed;

    public Transfer(Wallet from, Wallet to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.status = Status.pending;
        this.created = Instant.now();
    }

    public void complete(Status status) {
        this.status = status;
        this.completed = Instant.now();
    }
}
