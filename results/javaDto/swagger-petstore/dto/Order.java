package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

    @JsonProperty("shipDate")
    private Optional<String> shipDate;

    @JsonProperty("complete")
    private Optional<Boolean> complete;

    public Optional<String> getShipdate() {
        return shipDate;
    }
    
    public void setShipdate(String shipDate) {
        this.shipDate = Optional.ofNullable(shipDate);
    }

    public Optional<Boolean> getComplete() {
        return complete;
    }
    
    public void setComplete(Boolean complete) {
        this.complete = Optional.ofNullable(complete);
    }

}
