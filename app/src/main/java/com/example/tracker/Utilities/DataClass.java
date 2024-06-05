package com.example.tracker.Utilities;

public class DataClass {
    private String title;
    private String amount;
    private String category;
    private String paymentMethod;
    private String key;

    public DataClass(String title, String amount, String category, String paymentMethod) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.paymentMethod = paymentMethod;
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
}
