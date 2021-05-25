package game;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class Score {

    @JsonProperty("name")
    private String name;
    @JsonProperty("steps")
    private String steps;

    public Score(@JsonProperty("name") String name, @JsonProperty("steps")String steps) {

        this.name = name;
        this.steps = steps;
    }
}
