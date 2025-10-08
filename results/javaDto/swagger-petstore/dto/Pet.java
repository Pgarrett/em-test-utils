package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pet {

    @JsonProperty("category")
    private Optional<Category> category;

    @JsonProperty("photoUrls")
    private Optional<List<String>> photoUrls;

    @JsonProperty("tags")
    private Optional<List<Tag>> tags;

    public Optional<Category> getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = Optional.ofNullable(category);
    }

    public Optional<List<String>> getPhotourls() {
        return photoUrls;
    }
    
    public void setPhotourls(List<String> photoUrls) {
        this.photoUrls = Optional.ofNullable(photoUrls);
    }

    public Optional<List<Tag>> getTags() {
        return tags;
    }
    
    public void setTags(List<Tag> tags) {
        this.tags = Optional.ofNullable(tags);
    }

}
