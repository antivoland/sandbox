package antivoland.amahir.translit.ngram;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Words {
    private static final String RU_REGEX = "[а-яА-Я]+\\t[0-9]+";
    private static final String EN_REGEX = "[a-zA-Z']+\\t[0-9]+";
    private static final String FILE_NAME = "names.stats.short";


    public static Map<String, Integer> ruWordFrequencies() throws Exception {
        return wordFrequencies(FILE_NAME, RU_REGEX);
    }

    public static Map<String, Integer> enWordFrequencies() throws Exception {
        return wordFrequencies(FILE_NAME, EN_REGEX);
    }

    private static Map<String, Integer> wordFrequencies(String fileName, String regex) throws Exception {
        Path path = Paths.get(ClassLoader.getSystemResource(fileName).toURI());
        try (Stream<String> stream = Files.lines(path)) {
            return stream.filter(l -> l.matches(regex)).map(l -> l.split("\t")).collect(Collectors.toMap(kv -> kv[0], kv -> Integer.parseInt(kv[1])));
        }
    }
}
