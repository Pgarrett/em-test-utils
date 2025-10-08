package dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import shaded.com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {

    @JsonProperty("description")
    private Optional<String> description;

    @JsonProperty("id")
    private Optional<String> id;

    @JsonProperty("name")
    private Optional<String> name;

    @JsonProperty("productName")
    private Optional<String> productName;

    @JsonProperty("quantity")
    private Optional<Integer> quantity;

    public Optional<String> getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = Optional.ofNullable(description);
    }

    public Optional<String> getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = Optional.ofNullable(id);
    }

    public Optional<String> getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = Optional.ofNullable(name);
    }

    public Optional<String> getProductname() {
        return productName;
    }
    
    public void setProductname(String productName) {
        this.productName = Optional.ofNullable(productName);
    }

    public Optional<Integer> getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = Optional.ofNullable(quantity);
    }

}
