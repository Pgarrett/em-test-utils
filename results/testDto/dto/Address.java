package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {

    @JsonProperty("street")
    private Optional<String> street;

    @JsonProperty("country")
    private Optional<String> country;

    @JsonProperty("voted")
    private Optional<Boolean> voted;

    public Optional<String> getStreet() {
        return street;
    }
    
    public void setStreet(String street) {
        this.street = Optional.ofNullable(street);
    }

    public Optional<String> getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = Optional.ofNullable(country);
    }

    public Optional<Boolean> getVoted() {
        return voted;
    }
    
    public void setVoted(Boolean voted) {
        this.voted = Optional.ofNullable(voted);
    }

}
