package dk.au.mad22spring.janesbuns.models;

public class CreamBun {
    public String name;
    public Integer amount;
    public Double price;

    public CreamBun(String name, Integer amount, Double price) {
        this.name = name;
        this.amount = amount;
        this.price = price;
    }

    public CreamBun() {
        this.name = "";
        this.amount = 0;
        this.price = .0;
    }
}
