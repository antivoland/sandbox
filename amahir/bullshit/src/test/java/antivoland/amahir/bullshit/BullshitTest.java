package antivoland.amahir.bullshit;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BullshitTest {
    @Test
    public void testBullshit() throws Exception {
        String phrase = "Quick win by Search Engine Optimization Policy";
        Assert.assertEquals(2, Bullshit.nonbullshitability(phrase, dictionary()));
    }

    private Bullshit.Dictionary dictionary() throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource("bullshit.txt").toURI());
        try (Stream<String> stream = Files.lines(path)) {
            return new Bullshit.Dictionary(stream.collect(Collectors.toSet()));
        }
    }
}
