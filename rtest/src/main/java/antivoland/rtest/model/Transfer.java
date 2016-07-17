package antivoland.rtest.model;

import java.math.BigDecimal;
import java.time.Instant;

public class Transfer implements Versioned {
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
    public long version;

    public Transfer(String id, String from, String to, String currency, BigDecimal amount) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.currency = currency;
        this.amount = amount;
        this.state = State.create;
        this.status = Status.pending;
        this.created = Instant.now();
        this.version = Versioned.SINCE;
    }

    public Transfer(Transfer transfer) {
        this.id = transfer.id;
        this.from = transfer.from;
        this.to = transfer.to;
        this.currency = transfer.currency;
        this.amount = transfer.amount;
        this.state = transfer.state;
        this.status = transfer.status;
        this.created = transfer.created;
        this.completed = transfer.completed;
        this.version = transfer.version;
    }

    public void complete(Status status) {
        this.status = status;
        this.completed = Instant.now();
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
