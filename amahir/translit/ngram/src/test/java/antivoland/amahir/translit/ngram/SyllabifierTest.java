package antivoland.amahir.translit.ngram;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class SyllabifierTest {
    private static final Logger LOG = LoggerFactory.getLogger(SyllabifierTest.class);

    @Test
    public void testRu() throws Exception {
        LOG.info("Syllabifying russian names");
        Syllabifier ruSyllabifier = new Syllabifier(ruSyllables());
        testForks(ruSyllabifier.syllabify("александр"), 2);
        testForks(ruSyllabifier.syllabify("сергей"), 1);
        testForks(ruSyllabifier.syllabify("елена"), 1);
        testForks(ruSyllabifier.syllabify("андрей"), 1);
        testForks(ruSyllabifier.syllabify("алексей"), 2);
        testForks(ruSyllabifier.syllabify("ольга"), 1);
        testForks(ruSyllabifier.syllabify("дмитрий"), 2);
        testForks(ruSyllabifier.syllabify("татьяна"), 1);
        testForks(ruSyllabifier.syllabify("ирина"), 1);
        testForks(ruSyllabifier.syllabify("инесса"), 1);
    }

    private Set<String> ruSyllables() throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource("translit.txt").toURI());
        try (Stream<String> stream = Files.lines(path)) {
            return stream.map(l -> l.split("="))
                    .map(kv -> kv[0])
                    .collect(Collectors.toSet());
        }
    }

    @Test
    public void testEn() throws Exception {
        LOG.info("Syllabifying russian names");
        Syllabifier enSyllabifier = new Syllabifier(enSyllables());
        testForks(enSyllabifier.syllabify("alexandr"), 1);
        testForks(enSyllabifier.syllabify("sergey"), 1);
        testForks(enSyllabifier.syllabify("elena"), 1);
        testForks(enSyllabifier.syllabify("andrey"), 1);
        testForks(enSyllabifier.syllabify("alexey"), 1);
        testForks(enSyllabifier.syllabify("olga"), 1);
        testForks(enSyllabifier.syllabify("dmitrij"), 1);
        testForks(enSyllabifier.syllabify("tatyana"), 2);
        testForks(enSyllabifier.syllabify("irina"), 1);
        testForks(enSyllabifier.syllabify("inessa"), 4);
    }

    private Set<String> enSyllables() throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource("translit.txt").toURI());
        try (Stream<String> stream = Files.lines(path)) {
            return stream.map(l -> l.split("="))
                    .flatMap(kv -> asList(kv[1].split(",")).stream().map(s -> s))
                    .collect(Collectors.toSet());
        }
    }

    private void testForks(List<List<String>> forks, int count) {
        LOG.info("{} -> {}", String.join("", forks.get(0)), forks);
        Assert.assertEquals(count, forks.size());
    }
}
