package antivoland.amahir.translit.ngram;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class Dictionaries {
    public static Map<String, List<String>> ruEn() throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource("translit.txt").toURI());
        try (Stream<String> stream = Files.lines(path)) {
            return stream.map(l -> l.split("="))
                    .collect(Collectors.toMap(kv -> kv[0], kv -> asList(kv[1].split(","))));
        }
    }

    public static Map<String, List<String>> enRu() throws Exception {
        return ruEn().entrySet().stream()
                .flatMap(kv -> kv.getValue().stream().map(v -> new String[]{v, kv.getKey()}))
                .collect(Collectors.groupingBy(kv -> kv[0], Collectors.toList())).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(kv -> kv[1]).collect(Collectors.toList())));
    }
}
