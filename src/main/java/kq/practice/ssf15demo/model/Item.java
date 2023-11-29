package kq.practice.ssf15demo.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class Item {

    @NotEmpty(message="Please enter item name")
    private String name;

    @Min(value=1, message="You must order at least 1 item")
    @Max(value=10, message="You cannot order more than 10 items")
    private Integer quantity;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Item [name=" + name + ", quantity=" + quantity + "]";
    }
    
    public Item() {
    }
    
    public Item(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }
}
