package project.sec.util;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.sec.controller.MovieForm;
import project.sec.domain.Genre;
import project.sec.domain.Movie;
import project.sec.domain.Movie_Genre;
import project.sec.repository.NewestMovieRepository;
import project.sec.service.DictionaryService;
import project.sec.util.NaverMovieSearch;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class MovieUtil {

    private final EntityManager em;
    private final NaverMovieSearch nms;
    private final DictionaryService dictionaryService;
    private final NewestMovieRepository newestMovieRepository;


    /**
     * 스케쥴러 - 영화 주기마다 업데이트 해주는데 사용
     * cron = 초 분 시 일 월 요일 연도
     * 요일 : 0(일요일) ~ 6(토요일)
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void update_movie() {
        newestMovieRepository.delete_all();
        load_movie(true);
    }
    @Transactional
    public void load_movie(boolean is_scheduled) {
        // true : 당일 날짜 영화 순위만 크롤링, false : 기간 설정
        String url = "https://movie.naver.com/movie/sdb/rank/rmovie.nhn?sel=cnt&tg=0&date=";
        String date = "";
        int year = 2021;
        int month = 06;
        int day = 03;
        int count = 0;

        for (; ; ) {
            Document doc = null;
            Document curDoc = null;
            if (!is_scheduled) {
                if (month >= 10) date = "" + year + month;
                else date = "" + year + "0" + month;
                if (day >= 10) date = date + "" + day;
                else date = date + "0" + day;
            }
            try {
                doc = Jsoup.connect(url + date + "").get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements elements = doc.select("div.tit3");

            for (Element element : elements) {
                List<Movie> findMovie = em.createQuery("select m from Movie m where m.title = :title", Movie.class)
                        .setParameter("title", element.text())
                        .getResultList();

                if (findMovie.isEmpty()) {
                    ++count;
                    Movie movie = new Movie(element.text(),
                            "https://movie.naver.com" +
                                    element.getElementsByAttribute("href").attr("href"));
                    try {
                        // tmp : 영화 링크
                        String tmp = "https://movie.naver.com" + element.getElementsByAttribute("href").attr("href");
                        curDoc = Jsoup.connect(tmp).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Elements e2 = curDoc.select("meta[property]");
                    for (Element element1 : e2) {
                        if (element1.attr("property").equals("og:image")) {
                            movie.setImg_link(element1.attr("content"));
                            // 표지 이미지
                        }
                    }

                    e2 = curDoc.select("div.story_area").select("p.con_tx");
                    movie.setOutline(e2.text());

                    // ====== 감독, 배우
                    try {
                        String search = nms.search(movie.getTitle());
                        List<MovieForm> apiResult = nms.fromJsontoMovies(search);
                        for (MovieForm movieForm : apiResult) {
                            if (movie.getLink().equals(movieForm.getLink())) {
                                movie.setActor(movieForm.getActor().replace("<b>", "")
                                        .replace("</b>", ""));
                                movie.setDirector(movieForm.getDirector().replace("<b>", "")
                                        .replace("</b>", ""));
                            }
                        }
                    } catch (Exception e){
                    }

                    em.persist(movie);

                    // ========= 장르, 최초 개봉일, 국가 추출해서 해시셋에 저장
                    e2 = curDoc.select("a[href]");
                    HashSet<String> GenreHash = new HashSet<>();
                    HashSet<String> CountryHash = new HashSet<>();
                    HashSet<Integer> OpeningDateSet = new HashSet<>();
                    for (Element element1 : e2) {
                        // 영화 장르 ====================
                        String tmp_href = element1.attr("href");
                        String href = "";
                        for (int i = 0; i < tmp_href.length(); i++) {
                            if (!(tmp_href.charAt(i) >= '0' && tmp_href.charAt(i) <= '9'))
                                href += tmp_href.charAt(i);
                            else break;
                        }
                        if (href.equals("/movie/sdb/browsing/bmovie.nhn?genre=")) GenreHash.add(element1.text());

                        // 최초 개봉일
                        if (tmp_href.length() > 8 &&
                                tmp_href.substring(0, tmp_href.length() - 8).equals("/movie/sdb/browsing/bmovie.nhn?open=")) {
                            OpeningDateSet.add(Integer.parseInt(tmp_href.substring(tmp_href.length() - 8)));
                        }

                        // 국가
                        if (tmp_href.length() > 2 &&
                                tmp_href.substring(0, tmp_href.length() - 2).equals("/movie/sdb/browsing/bmovie.nhn?nation=")) {
                            CountryHash.add(element1.text());
                        }
                    }

                    // 국가 저장 여러개면 쉼표로 구분해서 String으로 저장
                    if (CountryHash.size() == 1) {
                        movie.setCountry(CountryHash.iterator().next());
                    } else if (CountryHash.size() > 1) {
                        String tmp = "";
                        for (String countryHash : CountryHash) {
                            tmp += countryHash + ",";
                        }
                        tmp = tmp.substring(0, tmp.length() - 1);
                        movie.setCountry(tmp);
                    }

                    // 개봉일 저장 =======
                    List<Integer> tmp_list = new ArrayList<>(OpeningDateSet);
                    Collections.sort(tmp_list);
                    if (!tmp_list.isEmpty()) movie.setOpeningDate(tmp_list.get(0));

                    // 장르 저장 ==================
                    for (String s : GenreHash) {
                        Movie_Genre movie_genre = new Movie_Genre(movie);
                        List<Genre> find = em.createQuery("select g from Genre g where g.genre = :find", Genre.class)
                                .setParameter("find", s)
                                .getResultList();
                        if (find.size() == 1) {
                            movie_genre.setGenre_id(find.get(0));
                            // 현재 영화의 장르가 Genre 테이블에 이미 있으면 그냥 넣어줌
                        } else if (find.size() == 0) {
                            Genre genre = new Genre(s);
                            em.persist(genre);
                            movie_genre.setGenre_id(genre);
                        }
                        em.persist(movie_genre);
                        movie.getGenreList().add(movie_genre);
                    }

                    dictionaryService.make_dictionary(movie);
                    if (is_scheduled) newestMovieRepository.save(movie); // 최신영화 테이블에 저장
                    System.out.println(element.text() + " " + date);
                } else {
                    /**
                     * movie 테이블에 이미 영화가 있는 경우에도 최신영화 테이블엔 영화 저장
                     */
                    if (is_scheduled) newestMovieRepository.save(findMovie.get(0));
                }
            }

            if (year <= 2017 || is_scheduled) {
                System.out.println("저장된 영화 = " + count + "개");
                break;
            } else {
                if (day == 1) {
                    if (month == 1) {
                        --year;
                        month = 12;
                    } else month--;
                    day = 32;
                }
            }
            day--;
        }
    }
}
