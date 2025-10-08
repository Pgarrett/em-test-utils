package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HospitalDTO {

    @JsonProperty("address")
    private Optional<String> address;

    @JsonProperty("availableBeds")
    private Optional<Integer> availableBeds;

    @JsonProperty("beds")
    private Optional<Integer> beds;

    @JsonProperty("id")
    private Optional<String> id;

    @JsonProperty("latitude")
    private Optional<String> latitude;

    @JsonProperty("longitude")
    private Optional<String> longitude;

    @JsonProperty("name")
    private Optional<String> name;

    public Optional<String> getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = Optional.ofNullable(address);
    }

    public Optional<Integer> getAvailablebeds() {
        return availableBeds;
    }
    
    public void setAvailablebeds(Integer availableBeds) {
        this.availableBeds = Optional.ofNullable(availableBeds);
    }

    public Optional<Integer> getBeds() {
        return beds;
    }
    
    public void setBeds(Integer beds) {
        this.beds = Optional.ofNullable(beds);
    }

    public Optional<String> getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = Optional.ofNullable(id);
    }

    public Optional<String> getLatitude() {
        return latitude;
    }
    
    public void setLatitude(String latitude) {
        this.latitude = Optional.ofNullable(latitude);
    }

    public Optional<String> getLongitude() {
        return longitude;
    }
    
    public void setLongitude(String longitude) {
        this.longitude = Optional.ofNullable(longitude);
    }

    public Optional<String> getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = Optional.ofNullable(name);
    }

}
