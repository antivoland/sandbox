package antivoland.amahir.translit;

import io.github.adrianulbona.hmm.Emission;
import io.github.adrianulbona.hmm.Transition;
import io.github.adrianulbona.hmm.solver.MostProbableStateSequenceFinder;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class TranslitTest {
    private static final String RU_EN_REGEX = "[a-zA-Zа-яА-Я]+\\t[0-9]+";
    private static final String RU_REGEX = "[а-яА-Я]+\\t[0-9]+";
    private static final String STATS_SEPARATOR = "\t";


    @Test
    public void testShort() throws Exception {
//        dictionary().entrySet().forEach(System.out::println);
//        wordFrequency("names.stats.short").entrySet().forEach(System.out::println);

        Map<String, Set<String>> dictionary = dictionary();
        TranslitModel model = new TranslitModel(dictionary, ruWordFrequency("names.stats.short"));
        List<TranslitObservation> obs = TranslitModel.parse("алексей", dictionary);
        List<TranslitState> translitStates = new MostProbableStateSequenceFinder<>(model).basedOn(obs);
    }
//    private static


    private Map<String, Set<String>> dictionary() throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource("translit.txt").toURI());
        try (Stream<String> stream = Files.lines(path)) {
            return stream.map(l -> l.split("=")).collect(Collectors.toMap(kv -> kv[0], kv -> new HashSet<>(asList(kv[1].split(",")))));
        }
    }

    private Map<String, Integer> ruWordFrequency(String fileName) throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource(fileName).toURI());
        try (Stream<String> stream = Files.lines(path)) {
            return stream.filter(l -> l.matches(RU_REGEX)).map(l -> l.split(STATS_SEPARATOR)).collect(Collectors.toMap(kv -> kv[0], kv -> Integer.parseInt(kv[1])));
        }
    }

    private Map<String, Integer> wordFrequency(String fileName) throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource(fileName).toURI());
        try (Stream<String> stream = Files.lines(path)) {
            return stream.filter(l -> l.matches(RU_EN_REGEX)).map(l -> l.split(STATS_SEPARATOR)).collect(Collectors.toMap(kv -> kv[0], kv -> Integer.parseInt(kv[1])));
        }
    }
}
