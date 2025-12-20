package dto;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class POST__additionalProperties {

    @JsonProperty("stringProp")
    private Optional<String> stringProp;

    @JsonProperty("nWciqLcIw_A")
    private Optional<Nwciqlciw_a> nWciqLcIw_A;

    @JsonProperty("zA2")
    private Optional<Za2> zA2;

    @JsonProperty("1IeiPHNZ_YI8EK")
    private Optional<1ieiphnz_yi8ek> 1IeiPHNZ_YI8EK;

    public Optional<String> getStringprop() {
        return stringProp;
    }
    
    public void setStringprop(String stringProp) {
        this.stringProp = Optional.ofNullable(stringProp);
    }

    public Optional<Nwciqlciw_a> getNwciqlciw_a() {
        return nWciqLcIw_A;
    }
    
    public void setNwciqlciw_a(Nwciqlciw_a nWciqLcIw_A) {
        this.nWciqLcIw_A = Optional.ofNullable(nWciqLcIw_A);
    }

    public Optional<Za2> getZa2() {
        return zA2;
    }
    
    public void setZa2(Za2 zA2) {
        this.zA2 = Optional.ofNullable(zA2);
    }

    public Optional<1ieiphnz_yi8ek> get1ieiphnz_yi8ek() {
        return 1IeiPHNZ_YI8EK;
    }
    
    public void set1ieiphnz_yi8ek(1ieiphnz_yi8ek 1IeiPHNZ_YI8EK) {
        this.1IeiPHNZ_YI8EK = Optional.ofNullable(1IeiPHNZ_YI8EK);
    }

}
