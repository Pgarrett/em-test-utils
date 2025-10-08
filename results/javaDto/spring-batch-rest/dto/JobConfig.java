package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobConfig {

    @JsonProperty("name")
    private Optional<String> name;

    @JsonProperty("asynchronous")
    private Optional<Boolean> asynchronous;

    public Optional<String> getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = Optional.ofNullable(name);
    }

    public Optional<Boolean> getAsynchronous() {
        return asynchronous;
    }
    
    public void setAsynchronous(Boolean asynchronous) {
        this.asynchronous = Optional.ofNullable(asynchronous);
    }

}
