package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class POST__products__productName__features__featureName_ {

    @JsonProperty("description")
    private Optional<String> description;

    public Optional<String> getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = Optional.ofNullable(description);
    }

}
