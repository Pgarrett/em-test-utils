package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {
    
    @JsonProperty("name")
    private Optional<String> name;

    @JsonProperty("age")
    private Optional<Integer> age;

    @JsonProperty("address")
    private Optional<Address> address;

    public Optional<String> getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = Optional.ofNullable(name);
    }

    public Optional<Integer> getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = Optional.ofNullable(age);
    }

    public Optional<Address> getAddress() {
        return address;
    }
    
    public void setAddress(Address address) {
        this.address = Optional.ofNullable(address);
    }

}
