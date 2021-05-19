package project.sec.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.openkoreantext.processor.KoreanTokenJava;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.sec.domain.Dictionary;
import project.sec.domain.Movie;
import project.sec.domain.Movie_Genre;
import project.sec.repository.DictionaryRepository;
import scala.collection.Seq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
@Getter
@RequiredArgsConstructor
public class DictionaryService {
    private final DictionaryRepository dictionaryRepository;

    public void save(Dictionary dictionary) {
        String word = dictionary.getWord();
        List<Dictionary> find = dictionaryRepository.findByWord(word);
        if (find.size() == 0) {
            dictionaryRepository.save(dictionary);
        }
    }

    public List<Dictionary> findByWord(String word) {
        return dictionaryRepository.findByWord(word);
    }

    @Transactional
    public void make_dictionary(Movie movie) {
        HashMap<String, Integer> ret_map = new HashMap<>();
        HashSet<String> dic_set = new HashSet<>();

        String[] tmp = {};
        try {
            tmp = movie.getActor().split("\\|");
            for (String s : tmp) {
                dic_set.add(s);
            }
        } catch (NullPointerException e) {
        }

        try {
            for (String s : movie.getCountry().split(",")) {
                dic_set.add(s);
            }
        } catch (NullPointerException e) {
        }

        try {
            for (String s : movie.getDirector().split("\\|")) {
                dic_set.add(s);
            }
        } catch (NullPointerException e) {
        }

        List<Movie_Genre> genreList = movie.getGenreList();
        for (Movie_Genre movie_genre : genreList) {
            dic_set.add(movie_genre.getGenre().getGenre());
        }

        if (movie.getOutline().length() >= 1) {
            CharSequence normalized = OpenKoreanTextProcessorJava.normalize(movie.getOutline());
            Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);
            List<KoreanTokenJava> strings = OpenKoreanTextProcessorJava.tokensToJavaKoreanTokenList(tokens);
            for (KoreanTokenJava s : strings) {
                if (s.getPos().toString().equals("Noun") ||
                        s.getPos().toString().equals("Alpha")) {
                    dic_set.add(s.getText());
                }
            }
        }

        for (String s : dic_set) {
            Dictionary dictionary = new Dictionary(s);
            save(dictionary);
        }
    }
}
