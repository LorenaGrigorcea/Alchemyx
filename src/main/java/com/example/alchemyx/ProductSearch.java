package com.example.alchemyx;

public class ProductSearch {
    Integer IDItem;
    String itemName, itemType;
    Integer itemQuantity;
    String itemDescription;

    public ProductSearch(Integer IDItem, String itemName, String itemType, Integer itemQuantity, String itemDescription) {
        this.IDItem = IDItem;
        this.itemName = itemName;
        this.itemType = itemType;
        this.itemQuantity = itemQuantity;
        this.itemDescription = itemDescription;
    }

    public Integer getIDItem() {
        return IDItem;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setIDItem(Integer iDItem) {
        this.IDItem = iDItem;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
    public ProductSearch(String itemName, String itemType, Integer itemQuantity, String itemDescription) {
        this(0, itemName, itemType, itemQuantity, itemDescription);
    }

}
