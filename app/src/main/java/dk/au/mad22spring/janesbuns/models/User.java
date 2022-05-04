package dk.au.mad22spring.janesbuns.models;

public class User {
    public String fullName, phone, email, address, city;
    public int postalCode;
    public boolean isAdmin;

    public User() {}

    public User(String fullName, String phone, String email, String address, String city, int postalCode, boolean isAdmin) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.isAdmin = isAdmin;
    }
}
