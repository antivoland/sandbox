package antivoland.amahir.translit.ngram;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationSets {
    public static Map<String, String> ruLaKnown() throws Exception {
        return validationSet("validation/rula.known");
    }

    public static Map<String, String> ruLaUnknown() throws Exception {
        return validationSet("validation/rula.unknown");
    }

    public static Map<String, String> laRuKnown() throws Exception {
        return validationSet("validation/laru.known");
    }

    public static Map<String, String> laRuUnknown() throws Exception {
        return validationSet("validation/laru.unknown");
    }

    private static Map<String, String> validationSet(String fileName) throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource(fileName).toURI());
        try (Stream<String> stream = Files.lines(path)) {
            return stream.map(l -> l.split(","))
                    .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));
        }
    }
}
