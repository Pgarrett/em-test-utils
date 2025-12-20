package dto;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Za2 {

    @JsonProperty("value")
    private Optional<String> value;

    public Optional<String> getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = Optional.ofNullable(value);
    }

}
