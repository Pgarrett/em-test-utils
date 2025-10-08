package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class POST__products__productName__constraints_requires {

    @JsonProperty("sourceFeature")
    private Optional<String> sourceFeature;

    @JsonProperty("requiredFeature")
    private Optional<String> requiredFeature;

    public Optional<String> getSourcefeature() {
        return sourceFeature;
    }
    
    public void setSourcefeature(String sourceFeature) {
        this.sourceFeature = Optional.ofNullable(sourceFeature);
    }

    public Optional<String> getRequiredfeature() {
        return requiredFeature;
    }
    
    public void setRequiredfeature(String requiredFeature) {
        this.requiredFeature = Optional.ofNullable(requiredFeature);
    }

}
