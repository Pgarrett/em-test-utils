package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class POST__products__productName__constraints_excludes {

    @JsonProperty("sourceFeature")
    private Optional<String> sourceFeature;

    @JsonProperty("excludedFeature")
    private Optional<String> excludedFeature;

    public Optional<String> getSourcefeature() {
        return sourceFeature;
    }
    
    public void setSourcefeature(String sourceFeature) {
        this.sourceFeature = Optional.ofNullable(sourceFeature);
    }

    public Optional<String> getExcludedfeature() {
        return excludedFeature;
    }
    
    public void setExcludedfeature(String excludedFeature) {
        this.excludedFeature = Optional.ofNullable(excludedFeature);
    }

}
