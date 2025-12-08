package com.mbj.gestionproduit;

public class Product {
    private String code;
    private String label;
    private int quantity;
    private double price;

    public Product(String code, String label, double price, int quantity) {
        this.code = code;
        this.label = label;
        this.price = price;
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" + "code='" + code + '\'' + ", label='" + label + '\'' + ", quantity=" + quantity + ", price=" + price + '}';
    }
}