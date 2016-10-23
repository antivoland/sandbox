package antivoland.amahir.translit.ngram;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class Syllabifiers {
    private static final String FILE_NAME = "translit.txt";

    public static Syllabifier ru() throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource(FILE_NAME).toURI());
        try (Stream<String> stream = Files.lines(path)) {
            return new Syllabifier(stream.map(l -> l.split("="))
                    .map(kv -> kv[0])
                    .collect(Collectors.toSet()));
        }
    }

    public static Syllabifier en() throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource(FILE_NAME).toURI());
        try (Stream<String> stream = Files.lines(path)) {
            return new Syllabifier(stream.map(l -> l.split("="))
                    .flatMap(kv -> asList(kv[1].split(",")).stream().map(s -> s))
                    .collect(Collectors.toSet()));
        }
    }

}
