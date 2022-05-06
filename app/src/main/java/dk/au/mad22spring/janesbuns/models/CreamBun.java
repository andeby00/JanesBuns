package dk.au.mad22spring.janesbuns.models;

public class CreamBun {
    public String name;
    public Integer amount;
    public Double price;
    public String uri;

    public CreamBun(String name, Integer amount, Double price, String uri) {
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.uri = uri;
    }

    public CreamBun(String uri) {
        this.name = "";
        this.amount = 0;
        this.price = .0;
        this.uri = uri;
    }

    public CreamBun() {
        this.name = "";
        this.amount = 0;
        this.price = .0;
        this.uri = "";
    }
}
