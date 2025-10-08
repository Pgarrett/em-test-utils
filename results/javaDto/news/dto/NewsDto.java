package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewsDto {

    @JsonProperty("authorId")
    private Optional<String> authorId;

    @JsonProperty("country")
    private Optional<String> country;

    @JsonProperty("creationTime")
    private Optional<String> creationTime;

    @JsonProperty("id")
    private Optional<String> id;

    @JsonProperty("newsId")
    private Optional<String> newsId;

    @JsonProperty("text")
    private Optional<String> text;

    public Optional<String> getAuthorid() {
        return authorId;
    }
    
    public void setAuthorid(String authorId) {
        this.authorId = Optional.ofNullable(authorId);
    }

    public Optional<String> getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = Optional.ofNullable(country);
    }

    public Optional<String> getCreationtime() {
        return creationTime;
    }
    
    public void setCreationtime(String creationTime) {
        this.creationTime = Optional.ofNullable(creationTime);
    }

    public Optional<String> getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = Optional.ofNullable(id);
    }

    public Optional<String> getNewsid() {
        return newsId;
    }
    
    public void setNewsid(String newsId) {
        this.newsId = Optional.ofNullable(newsId);
    }

    public Optional<String> getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = Optional.ofNullable(text);
    }

}
