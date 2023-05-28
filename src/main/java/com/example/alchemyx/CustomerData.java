package com.example.alchemyx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CustomerData {
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty email;
    private SimpleStringProperty address;
    private SimpleStringProperty city;
    private SimpleStringProperty country;

    public CustomerData() {

    }

    public StringProperty nameProperty(){
        return name;
    }

    public CustomerData(String name, String email, String address, String city, String country) {
        this.setName(new SimpleStringProperty(name));
        this.setEmail(new SimpleStringProperty(email));
        this.setAddress(new SimpleStringProperty(address));
        this.setCity(new SimpleStringProperty(city));
        this.setCountry(new SimpleStringProperty(country));
    }

    public String getName() {
        return name.get();
    }

    public void setName(SimpleStringProperty name) {
        this.name = name;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(SimpleStringProperty email) {
        this.email = email;
    }

    public String  getAddress() {
        return address.get();
    }

    public void setAddress(SimpleStringProperty address) {
        this.address = address;
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(SimpleStringProperty city) {
        this.city = city;
    }

    public String getCountry() {
        return country.get();
    }

    public void setCountry(SimpleStringProperty country) {
        this.country = country;
    }
}