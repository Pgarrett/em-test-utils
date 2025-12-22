package dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class POST__additionalProperties {

    @JsonProperty("stringProp")
    private Optional<String> stringProp;

    @JsonIgnore
    private Map<String, POST__additionalProperties_ap> additionalProperties = new HashMap<>();

    public Optional<String> getStringProp() {
        return stringProp;
    }

    public void setStringProp(String stringProp) {
        this.stringProp = Optional.ofNullable(stringProp);
    }

    @JsonAnyGetter
    public Map<String, POST__additionalProperties_ap> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void addAdditionalProperty(String name, POST__additionalProperties_ap value) {
        this.additionalProperties.put(name, value);
    }

}
