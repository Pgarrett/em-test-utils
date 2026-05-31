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
public class POST__additional_properties_no_root {

    @JsonIgnore
    private Map<String, POST__additional_properties_no_root_ap> additionalProperties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, POST__additional_properties_no_root_ap> getAdditionalProperties() {
        return additionalProperties;
    }
    
    @JsonAnySetter
    public void addAdditionalProperty(String name, POST__additional_properties_no_root_ap value) {
        this.additionalProperties.put(name, value);
    }

}
