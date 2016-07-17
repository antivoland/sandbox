package antivoland.rtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

public class RtestConfig {
    public int port;
    public BigDecimal conversionFee;

    public static RtestConfig get() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        InputStream in = RtestConfig.class.getClassLoader().getResourceAsStream("rtest.yml"); // todo: refer to external resource
        try {
            return mapper.readValue(in, RtestConfig.class);
        } catch (IOException e) {
            throw new Error(e);
        }
    }
}
