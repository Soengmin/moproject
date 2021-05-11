package project.sec.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import project.sec.controller.MovieForm;

import java.util.ArrayList;
import java.util.List;

@Component
public class NaverMovieSearch {

    public String search(String query){
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", "EYcL3tQQ1ZzNq2tMKvoq");
        headers.add("X-Naver-Client-Secret", "9wphWJDuhv");
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = rest.exchange("https://openapi.naver.com/v1/search/movie.json?query="+query+"&display=100", HttpMethod.GET, requestEntity, String.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        int status = httpStatus.value();
        String response = responseEntity.getBody();
//        System.out.println("Response status: " + status);
        //System.out.println(response);

        return response;
    }

    public List<MovieForm> fromJsontoMovies(String result){
        JSONObject rjson = new JSONObject(result);
        JSONArray movies = rjson.getJSONArray("items");
        List<MovieForm> ret = new ArrayList<>();
        for(int i = 0; i<movies.length();i++){
            JSONObject itemJson = (JSONObject) movies.get(i);
            MovieForm movieForm = new MovieForm(itemJson);
            ret.add(movieForm);
        }
        return ret;
    }
}
