package dto;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class POST__additionalProperties_ap {

    @JsonProperty("value")
    private Optional<String> value;

    @JsonProperty("source")
    private Optional<String> source;

    public Optional<String> getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = Optional.ofNullable(value);
    }

    public Optional<String> getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = Optional.ofNullable(source);
    }

}
