package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Patient {

    @JsonProperty("active")
    private Optional<Boolean> active;

    @JsonProperty("birthDate")
    private Optional<String> birthDate;

    @JsonProperty("cpf")
    private Optional<String> cpf;

    @JsonProperty("entryDate")
    private Optional<String> entryDate;

    @JsonProperty("exitDate")
    private Optional<String> exitDate;

    @JsonProperty("gender")
    private Optional<String> gender;

    @JsonProperty("id")
    private Optional<String> id;

    @JsonProperty("location")
    private Optional<Location> location;

    @JsonProperty("name")
    private Optional<String> name;

    public Optional<Boolean> getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = Optional.ofNullable(active);
    }

    public Optional<String> getBirthdate() {
        return birthDate;
    }
    
    public void setBirthdate(String birthDate) {
        this.birthDate = Optional.ofNullable(birthDate);
    }

    public Optional<String> getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = Optional.ofNullable(cpf);
    }

    public Optional<String> getEntrydate() {
        return entryDate;
    }
    
    public void setEntrydate(String entryDate) {
        this.entryDate = Optional.ofNullable(entryDate);
    }

    public Optional<String> getExitdate() {
        return exitDate;
    }
    
    public void setExitdate(String exitDate) {
        this.exitDate = Optional.ofNullable(exitDate);
    }

    public Optional<String> getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = Optional.ofNullable(gender);
    }

    public Optional<String> getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = Optional.ofNullable(id);
    }

    public Optional<Location> getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = Optional.ofNullable(location);
    }

    public Optional<String> getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = Optional.ofNullable(name);
    }

}
