package antivoland.rtest.model;

import java.math.BigDecimal;
import java.time.Instant;

public class Transfer {
    public enum State {create, withdraw, charge, finalize}

    public enum Status {pending, succeeded, failed}

    public final String id;
    public final String from;
    public final String to;
    public final String currency;
    public final BigDecimal amount;
    public State state;
    public Status status;
    public final Instant created;
    public Instant completed;

    public Transfer(String id, String from, String to, String currency, BigDecimal amount) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.currency = currency;
        this.amount = amount;
        this.state = State.create;
        this.status = Status.pending;
        this.created = Instant.now();
    }

    public void complete(Status status) {
        this.status = status;
        this.completed = Instant.now();
    }
}
