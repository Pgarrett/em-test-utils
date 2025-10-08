package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tag {

    @JsonProperty("id")
    private Optional<Long> id;

    @JsonProperty("name")
    private Optional<String> name;

    public Optional<Long> getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = Optional.ofNullable(id);
    }

    public Optional<String> getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = Optional.ofNullable(name);
    }

}
