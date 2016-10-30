package antivoland.amahir.translit.ngram;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

class Words implements AutoCloseable {
    private static final String RU_REGEX = "[а-яА-Я]+\\t[0-9]+";
    private static final String EN_REGEX = "[a-zA-Z']+\\t[0-9]+";
    private static final String FILE_NAME = "names.stats.medium";
    private static final int FREQUENCY_POWER = 2; // todo: нужна автоматическая подборка параметра

    public final Stream<WordFrequency> stream;

    public Words(String fileName, String regex) throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource(fileName).toURI());
        this.stream = Files.lines(path)
                .filter(l -> l.matches(regex)).map(l -> l.split("\t"))
                .map(kv -> new WordFrequency(kv[0], Integer.valueOf(kv[1]), FREQUENCY_POWER));
    }

    @Override
    public void close() throws Exception {
        stream.close();
    }

    public static Words ruWordFrequencies() throws Exception {
        return new Words(FILE_NAME, RU_REGEX);
    }

    public static Words laWordFrequencies() throws Exception {
        return new Words(FILE_NAME, EN_REGEX);
    }
}
