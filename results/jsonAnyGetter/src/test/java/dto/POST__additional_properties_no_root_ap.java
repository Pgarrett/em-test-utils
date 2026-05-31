package dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class POST__additional_properties_no_root_ap {

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
