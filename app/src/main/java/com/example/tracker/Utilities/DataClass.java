package com.example.tracker.Utilities;

public class DataClass {
    private String title;
    private String amount;
    private String category;
    private String paymentMethod;
    private String key;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public DataClass(String title, String amount, String category, String paymentMethod) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.paymentMethod = paymentMethod;
    }

    public DataClass() {

    }

    public String getTitle() {
        return title;
    }

    public String getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getId() {
        return getId();
    }

    public void setId(int anInt) {
    }
}
