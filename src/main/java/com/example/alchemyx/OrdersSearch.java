package com.example.alchemyx;

public class OrdersSearch {
    Integer id;
    String itemName, itemType;
    Integer quantity;
    String date;
    String status;

    public OrdersSearch(String itemName, String itemType, Integer quantity, String date, String status) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.quantity = quantity;
        this.date = date;
        this.status = status;
    }

    public OrdersSearch(String itemName, String itemType, Integer quantity, String status) {
        this.itemName = itemName;
        this.itemType = itemType;
        this.quantity = quantity;
        this.status = status;
    }

    public OrdersSearch(Integer id, String itemName, String itemType, Integer quantity, String date, String status) {
        this.id = id;
        this.itemName = itemName;
        this.itemType = itemType;
        this.quantity = quantity;
        this.date = date;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}
