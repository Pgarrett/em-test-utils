package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location {

    @JsonProperty("id")
    private Optional<String> id;

    @JsonProperty("location")
    private Optional<GeoJsonPoint> location;

    @JsonProperty("name")
    private Optional<String> name;

    @JsonProperty("position")
    private Optional<GeoJsonPoint> position;

    @JsonProperty("referenceId")
    private Optional<String> referenceId;

    public Optional<String> getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = Optional.ofNullable(id);
    }

    public Optional<GeoJsonPoint> getLocation() {
        return location;
    }
    
    public void setLocation(GeoJsonPoint location) {
        this.location = Optional.ofNullable(location);
    }

    public Optional<String> getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = Optional.ofNullable(name);
    }

    public Optional<GeoJsonPoint> getPosition() {
        return position;
    }
    
    public void setPosition(GeoJsonPoint position) {
        this.position = Optional.ofNullable(position);
    }

    public Optional<String> getReferenceid() {
        return referenceId;
    }
    
    public void setReferenceid(String referenceId) {
        this.referenceId = Optional.ofNullable(referenceId);
    }

}
