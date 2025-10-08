package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeoJsonPoint {

    @JsonProperty("coordinates")
    private Optional<List<Double>> coordinates;

    @JsonProperty("type")
    private Optional<String> type;

    @JsonProperty("x")
    private Optional<Double> x;

    @JsonProperty("y")
    private Optional<Double> y;

    public Optional<List<Double>> getCoordinates() {
        return coordinates;
    }
    
    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = Optional.ofNullable(coordinates);
    }

    public Optional<String> getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = Optional.ofNullable(type);
    }

    public Optional<Double> getX() {
        return x;
    }
    
    public void setX(Double x) {
        this.x = Optional.ofNullable(x);
    }

    public Optional<Double> getY() {
        return y;
    }
    
    public void setY(Double y) {
        this.y = Optional.ofNullable(y);
    }

}
