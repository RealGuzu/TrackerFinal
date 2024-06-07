package com.example.tracker.Utilities;

import android.os.Parcel;
import android.os.Parcelable;

public class DataClass implements Parcelable {
    private String title;
    private String amount;
    private String category;
    private String paymentMethod;
    private String key;
    private String type;

    public DataClass(String title, String amount, String category, String paymentMethod, String type) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.type = type;
    }

    public DataClass() {
        // Default constructor
    }

    public DataClass(String title, String amount, String category, String method) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.paymentMethod = method;
    }

    // Other getters and setters...

    protected DataClass(Parcel in) {
        title = in.readString();
        amount = in.readString();
        category = in.readString();
        paymentMethod = in.readString();
        key = in.readString();
        type = in.readString();
    }

    public static final Creator<DataClass> CREATOR = new Creator<DataClass>() {
        @Override
        public DataClass createFromParcel(Parcel in) {
            return new DataClass(in);
        }

        @Override
        public DataClass[] newArray(int size) {
            return new DataClass[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(amount);
        dest.writeString(category);
        dest.writeString(paymentMethod);
        dest.writeString(key);
        dest.writeString(type);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(int anInt) {
    }

    public int getId() {
        return 0;
    }
}
