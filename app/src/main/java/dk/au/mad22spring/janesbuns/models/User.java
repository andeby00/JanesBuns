package dk.au.mad22spring.janesbuns.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String fullName, phone, email, address, city;
    public List<Order> orders;
    public int postalCode;
    public boolean isAdmin;

    public User() {
        orders = new ArrayList<>();
    }

    public User(String fullName, String phone, String email, String address, String city, int postalCode, boolean isAdmin) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.isAdmin = isAdmin;
        this.orders = new ArrayList<>();
    }
}
