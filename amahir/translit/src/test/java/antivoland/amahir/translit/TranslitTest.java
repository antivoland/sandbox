package antivoland.amahir.translit;

import io.github.adrianulbona.hmm.Emission;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class TranslitTest {
    private static final String RU_REGEX = "[а-яА-Я]+\\t[0-9]+";
    private static final String EN_REGEX = "[a-zA-Z']+\\t[0-9]+";

    @Test
    public void testEnRuShort() throws Exception {
        List<Emission<TranslitState, TranslitObservation>> ruEnEmissions = ruEnEmissions();
        List<Emission<TranslitState, TranslitObservation>> enRuEmissions = enRuEmissions();
        TranslitModel model = new TranslitModel(
                ruEnEmissions,
                ruWordFrequency("names.stats"),
                new TranslitSequencer(ruEnEmissions),
                new TranslitSequencer(enRuEmissions));
        String translit = model.translit("valdemar");
        System.out.println("valdemar -> " + translit);
    }

    @Test
    public void testRuEnShort() throws Exception {
        List<Emission<TranslitState, TranslitObservation>> ruEnEmissions = ruEnEmissions();
        List<Emission<TranslitState, TranslitObservation>> enRuEmissions = enRuEmissions();
        TranslitModel model = new TranslitModel(
                enRuEmissions,
                enWordFrequency("names.stats"),
                new TranslitSequencer(enRuEmissions),
                new TranslitSequencer(ruEnEmissions));
        String translit = model.translit("вальдемар");
        System.out.println("вальдемар -> " + translit);
    }

    private List<Emission<TranslitState, TranslitObservation>> ruEnEmissions() throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource("translit.txt").toURI());
        try (Stream<String> stream = Files.lines(path)) {
            return stream.map(l -> l.split("="))
                    .flatMap(kv -> asList(kv[1].split(",")).stream().map(o -> emission(kv[0], o)))
                    .collect(Collectors.toList());
        }
    }

    private List<Emission<TranslitState, TranslitObservation>> enRuEmissions() throws Exception {
        return ruEnEmissions().stream()
                .map(e -> emission(e.getObservation().chars, e.getState().chars))
                .collect(Collectors.toList());
    }

    private static Emission<TranslitState, TranslitObservation> emission(String state, String observation) {
        return new Emission<>(new TranslitState(state), new TranslitObservation(observation));
    }

    private Map<String, Integer> ruWordFrequency(String fileName) throws Exception {
        return wordFrequency(fileName, RU_REGEX);
    }

    private Map<String, Integer> enWordFrequency(String fileName) throws Exception {
        return wordFrequency(fileName, EN_REGEX);
    }

    private Map<String, Integer> wordFrequency(String fileName, String regex) throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource(fileName).toURI());
        try (Stream<String> stream = Files.lines(path)) {
            return stream.filter(l -> l.matches(regex)).map(l -> l.split("\t")).collect(Collectors.toMap(kv -> kv[0], kv -> Integer.parseInt(kv[1])));
        }
    }
}
