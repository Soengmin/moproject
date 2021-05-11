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
import project.sec.util.NaverMovieSearch;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class MovieUtil {

    private final EntityManager em;
    private final NaverMovieSearch nms;


    /**
     *     스케쥴러 - 영화 주기마다 업데이트 해주는데 사용
     *     cron = 초 분 시 일 월 요일 연도
     *     요일 : 0(일요일) ~ 6(토요일)
     */
/*    @Scheduled(cron = "0 * * * * *")
    public void update_movie() {
        load_movie(true);
    }*/

    public void load_movie(boolean is_scheduled) {
        // true : 당일 날짜 영화 순위만 크롤링, false : 기간 설정
        String url = "https://movie.naver.com/movie/sdb/rank/rmovie.nhn?sel=cnt&tg=0&date=";
        String date = "";
        int year = 2016;
        int month = 12;
        int day = 31;
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
                    String search = nms.search(movie.getTitle());
                    List<MovieForm> apiResult = nms.fromJsontoMovies(search);
                    for (MovieForm movieForm : apiResult) {
                        if (movie.getLink().equals(movieForm.getLink())) {
                            movie.setActor(movieForm.getActor());
                            movie.setDirector(movieForm.getDirector());
                        }
                    }

                    em.persist(movie);

                    // ========= 장르 추가 ===========
                    e2 = curDoc.select("a[href]");
                    HashSet<String> hashSet = new HashSet<>();
                    for (Element element1 : e2) {
                        String tmp_href = element1.attr("href");
                        String href = "";
                        for (int i = 0; i < tmp_href.length(); i++) {
                            if (!(tmp_href.charAt(i) >= '0' && tmp_href.charAt(i) <= '9'))
                                href += tmp_href.charAt(i);
                            else break;
                        }
                        if (href.equals("/movie/sdb/browsing/bmovie.nhn?genre=")) hashSet.add(element1.text());
                    }

                    for (String s : hashSet) {
                        Movie_Genre movie_genre = new Movie_Genre(movie);
                        List<Genre> find = em.createQuery("select g from Genre g where g.genre = :find", Genre.class)
                                .setParameter("find", s)
                                .getResultList();
                        if (find.size() == 1) {
                            movie_genre.setGenre_id(find.get(0));
                            // 현재 영화의 장르가 Genre 테이블에 이미 있으면 그냥 넣어줌
                        }
                        else if (find.size() == 0) {
                            Genre genre = new Genre(s);
                            em.persist(genre);
                            movie_genre.setGenre_id(genre);
                        }
                        em.persist(movie_genre);
                    }

                    System.out.println(element.text() + " " + date);
                }
            }

            if (month <= 11 || is_scheduled) {
                System.out.println("저장된 영화 = " + count + "개");
                break;
            }
            else {
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
