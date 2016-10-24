package antivoland.amahir.translit.hmm;

import io.github.adrianulbona.hmm.Emission;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class HmmTranslitTest {
    private static final Logger LOG = LoggerFactory.getLogger(HmmTranslitTest.class);
    private static final String RU_REGEX = "[а-яА-Я]+\\t[0-9]+";
    private static final String EN_REGEX = "[a-zA-Z']+\\t[0-9]+";

    @Test
    public void testEnRu() throws Exception {
        List<Emission<HmmTranslitState, HmmTranslitObservation>> ruEnEmissions = ruEnEmissions();
        List<Emission<HmmTranslitState, HmmTranslitObservation>> enRuEmissions = enRuEmissions();
        HmmTranslitModel model = new HmmTranslitModel(
                ruEnEmissions,
                ruWordFrequency("names.stats"),
                new HmmTranslitSequencer(ruEnEmissions),
                new HmmTranslitSequencer(enRuEmissions));
        testEnRu("avundiy", model);
        testEnRu("plakilla", model);
        testEnRu("usfazan", model);
        testEnRu("pheognia", model);
        testEnRu("epiktet", model);
        testEnRu("inessa", model);
        testEnRu("petrov", model);
        testEnRu("petroff", model);
        testEnRu("jurij", model);
    }

    private void testEnRu(String enName, HmmTranslitModel model) throws Exception {
        String translit = model.translit(enName);
        LOG.info("{} -> {}", enName, translit);
    }

    @Test
    public void testRuEn() throws Exception {
        List<Emission<HmmTranslitState, HmmTranslitObservation>> ruEnEmissions = ruEnEmissions();
        List<Emission<HmmTranslitState, HmmTranslitObservation>> enRuEmissions = enRuEmissions();
        HmmTranslitModel model = new HmmTranslitModel(
                enRuEmissions,
                enWordFrequency("names.stats"),
                new HmmTranslitSequencer(enRuEmissions),
                new HmmTranslitSequencer(ruEnEmissions));
        testRuEn("авундий", model);
        testRuEn("плакилла", model);
        testRuEn("усфазан", model);
        testRuEn("феогния", model);
        testRuEn("эпиктет", model);
        testRuEn("инесса", model);
        testRuEn("петров", model);
        testRuEn("юрий", model);
    }

    private void testRuEn(String ruName, HmmTranslitModel model) throws Exception {
        String translit = model.translit(ruName);
        LOG.info("{} -> {}", ruName, translit);
    }

    private List<Emission<HmmTranslitState, HmmTranslitObservation>> ruEnEmissions() throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource("translit.txt").toURI());
        try (Stream<String> stream = Files.lines(path)) {
            return stream.map(l -> l.split("="))
                    .flatMap(kv -> asList(kv[1].split(",")).stream().map(o -> emission(kv[0], o)))
                    .collect(Collectors.toList());
        }
    }

    private List<Emission<HmmTranslitState, HmmTranslitObservation>> enRuEmissions() throws Exception {
        return ruEnEmissions().stream()
                .map(e -> emission(e.getObservation().chars, e.getState().chars))
                .collect(Collectors.toList());
    }

    private static Emission<HmmTranslitState, HmmTranslitObservation> emission(String state, String observation) {
        return new Emission<>(new HmmTranslitState(state), new HmmTranslitObservation(observation));
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
