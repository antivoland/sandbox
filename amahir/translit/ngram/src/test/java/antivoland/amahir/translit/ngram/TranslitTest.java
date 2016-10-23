package antivoland.amahir.translit.ngram;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TranslitTest {
    private static final Logger LOG = LoggerFactory.getLogger(TranslitTest.class);

//    @Test
//    public void test() throws Exception {
//        Syllabifier ruSyllabifier = Syllabifiers.ru();
//        Syllabifier enSyllabifier = Syllabifiers.en();
//
//        Map<String, List<String>> ruEnDictionary = Dictionaries.ruEn();
//
//        int N = 3;
//        Map<Ngram, Double> ruNgramProbabilities = ruNgramProbabilities(N);
//
//        List<List<String>> forks = ruSyllabifier.syllabify("инесса");
//        for (List<String> fork : forks) {
//            System.out.println(fork +" -> "+forkProbability(fork, ruNgramProbabilities, N));
//        }
//
//    }
//
//    @Test
//    public void testen() throws Exception {
//        Syllabifier ruSyllabifier = Syllabifiers.ru();
//        Syllabifier enSyllabifier = Syllabifiers.en();
//
//        Map<String, List<String>> ruEnDictionary = Dictionaries.ruEn();
//
//        int N = 3;
//        Map<Ngram, Double> enNgramProbabilities = enNgramProbabilities(N);
//
//        List<List<String>> forks = enSyllabifier.syllabify("inessa");
//        for (List<String> fork : forks) {
//            System.out.println(fork +" -> "+forkProbability(fork, enNgramProbabilities, N));
//        }
//
//    }
//
////    public List<List<String>> translit(List<List<String>> forks) {
////        for (List<String> fork : forks) {
////
////        }
////    }
//
//    private Double forkProbability(List<String> fork, Map<Ngram, Double> ngramProbabilities, int N) {
//        Double probability = 1.0;
//        for (int i = 0; i < fork.size(); ++i) {
//            Ngram ngram = new Ngram(fork.subList(Math.max(0, i - N), i));
//            Double ngramProbability = ngramProbabilities.get(ngram);
//            if (ngramProbability != null) {
//                probability *= ngramProbability;
//            }
//        }
//        return probability;
//    }



}
