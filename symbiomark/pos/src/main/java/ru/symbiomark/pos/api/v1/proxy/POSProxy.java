package ru.symbiomark.pos.api.v1.proxy;

import org.springframework.stereotype.Component;
import ru.symbiomark.pos.api.v1.domain.PointOfSale;

import java.util.Collection;

@Component
public class POSProxy {
    public Collection<PointOfSale> list() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
