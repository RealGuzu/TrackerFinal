package com.example.tracker.Utilities;

public class DataClass {
    private String title;
    private String amount;
    private String category;
    private String paymentMethod;
    private String key;
    private String type;



    public DataClass(String title, String amount, String category, String paymentMethod, String type) {
    }

    public DataClass() {

    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataClass(String title, String amount, String category, String paymentMethod, String key, String type) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.key = key;
        this.type = type;
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
