package antivoland.amahir.translit;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TranslitTest {
    private static final String RU_EN_REGEX = "[a-zA-Zа-яА-Я]+\\t[0-9]+";
    private static final String STATS_SEPARATOR = "\t";

    @Test
    public void testShort() throws Exception {
        dictionary().entrySet().forEach(System.out::println);
        wordFrequency("names.stats.short").entrySet().forEach(System.out::println);
    }

    private Map<String, Set<String>> dictionary() throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource("translit.txt").toURI());
        try (Stream<String> stream = Files.lines(path)) {
            return stream.map(l -> l.split("=")).collect(Collectors.toMap(kv -> kv[0], kv -> new HashSet<>(Arrays.asList(kv[1].split(",")))));
        }
    }

    private Map<String, Integer> wordFrequency(String fileName) throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource(fileName).toURI());
        try (Stream<String> stream = Files.lines(path)) {
            return stream.filter(l -> l.matches(RU_EN_REGEX)).map(l -> l.split(STATS_SEPARATOR)).collect(Collectors.toMap(kv -> kv[0], kv -> Integer.parseInt(kv[1])));
        }
    }
}
