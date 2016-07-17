package antivoland.rtest.model;

import java.math.BigDecimal;

public abstract class Converter {
    public final BigDecimal convert(String from, String to, BigDecimal amount, BigDecimal fee) {
        return amount.multiply(rate(from, to)).multiply(fee.add(BigDecimal.ONE));
    }

    protected abstract BigDecimal rate(String from, String to);
}
