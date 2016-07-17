package antivoland.rtest.model;

import java.math.BigDecimal;

public abstract class Converter {
    public final BigDecimal convert(String from, String to, BigDecimal amount, BigDecimal fee) {
        BigDecimal converted = amount.multiply(rate(from, to));
        return BigDecimal.ONE.subtract(fee).multiply(converted);
    }

    protected abstract BigDecimal rate(String from, String to);
}
