package ru.itis.maletskov.internship.util.youtube;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoId {
    @JsonProperty("kind")
    private String kind;
    @JsonProperty("videoId")
    private String videoId;
}
