package antivoland.amahir.translit.ngram;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SyllabifierTest {
    private static final Logger LOG = LoggerFactory.getLogger(SyllabifierTest.class);

    @Test
    public void testRu() throws Exception {
        LOG.info("Syllabifying russian names");
        Syllabifier ruSyllabifier = Syllabifiers.ru();
        testForks("александр", ruSyllabifier, 2);
        testForks("сергей", ruSyllabifier, 1);
        testForks("елена", ruSyllabifier, 1);
        testForks("андрей", ruSyllabifier, 1);
        testForks("алексей", ruSyllabifier, 2);
        testForks("ольга", ruSyllabifier, 1);
        testForks("дмитрий", ruSyllabifier, 2);
        testForks("татьяна", ruSyllabifier, 1);
        testForks("ирина", ruSyllabifier, 1);
        testForks("инесса", ruSyllabifier, 1);
    }

    @Test
    public void testEn() throws Exception {
        LOG.info("Syllabifying transliterated names");
        Syllabifier enSyllabifier = Syllabifiers.en();
        testForks("alexandr", enSyllabifier, 1);
        testForks("sergey", enSyllabifier, 1);
        testForks("elena", enSyllabifier, 1);
        testForks("andrey", enSyllabifier, 1);
        testForks("alexey", enSyllabifier, 1);
        testForks("olga", enSyllabifier, 1);
        testForks("dmitrij", enSyllabifier, 1);
        testForks("tatyana", enSyllabifier, 2);
        testForks("irina", enSyllabifier, 1);
        testForks("inessa", enSyllabifier, 4);
    }

    private void testForks(String name, Syllabifier syllabifier, int expected) {
        List<List<String>> forks = syllabifier.syllabify(name);
        LOG.info("{} -> {}", name, forks);
        Assert.assertEquals(expected, forks.size());
    }
}
