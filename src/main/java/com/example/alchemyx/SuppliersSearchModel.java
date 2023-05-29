package com.example.alchemyx;

public class SuppliersSearchModel {
    Integer id;
    String supplierName;
    String category;
    String contactPerson;
    String City;
    String Country;

    public SuppliersSearchModel(Integer id, String supplierName, String category, String contactPerson, String city, String country) {
        this.id = id;
        this.supplierName = supplierName;
        this.category = category;
        this.contactPerson = contactPerson;
        City = city;
        Country = country;
    }

    public Integer getId() {
        return id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getCategory() {
        return category;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getCity() {
        return City;
    }

    public String getCountry() {
        return Country;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setCountry(String country) {
        Country = country;
    }
}
