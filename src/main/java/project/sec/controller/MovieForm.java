package project.sec.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

@Getter
@Setter
@NoArgsConstructor
public class MovieForm {
    private String title;
    private String link;
    private String image;
    private String director;
    private String actor;

    public MovieForm(JSONObject itemJson){
        this.title = itemJson.getString("title");
        this.link = itemJson.getString("link");
        this.image = itemJson.getString("image");
        this.director = itemJson.getString("director");
        this.actor = itemJson.getString("actor");
    }
}
