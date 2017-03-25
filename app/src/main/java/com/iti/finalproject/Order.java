package com.iti.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 20/03/2017.
 */

public class Order implements Parcelable {

    public static final int DELIVERY_METHOD_PICKUP = 0;
    public static final int DELIVERY_METHOD_DELIVERY = 1;

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_CONFIRMED = 1;
    public static final int STATUS_CANCELED = 2;
    public static final int STATUS_DELIVERED = 3;
    public static final int STATUS_NOT_DELIVERED = 4;

    private String id;
    private List<Item> items;
    private int deliveryMethod;
    private int status;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String date;
    private boolean rated;
    private String ChiefID;
    private String UserID;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getChiefID() {
        return ChiefID;
    }

    public void setChiefID(String chiefID) {
        ChiefID = chiefID;
    }

    public Order(){
        this.id = "";
        this.items = new ArrayList<>();
        this.deliveryMethod = DELIVERY_METHOD_PICKUP;
        this.status = STATUS_PENDING;
        this.date = "";
        this.address = "";
        this.rated = false;
        this.ChiefID = "";
    }

    public Order(String id, List<Item> items, int deliveryMethod, int status, String date, String address, boolean rated, String ChiefID, String UserID) {
        if (status < 0 || status > 4){
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.items = items;
        this.deliveryMethod = deliveryMethod;
        this.status = status;
        this.date = date;
        this.address = address;
        this.rated = rated;
        this.ChiefID = ChiefID;
        this.UserID = UserID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(int deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        if (status < 0 || status > 4){
            throw new IllegalArgumentException();
        }
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeTypedList(this.items);
        dest.writeInt(this.deliveryMethod);
        dest.writeInt(this.status);
        dest.writeString(this.date);
        dest.writeByte(this.rated ? (byte) 1 : (byte) 0);
        dest.writeString(this.ChiefID);
        dest.writeString(this.UserID);
    }

    protected Order(Parcel in) {
        this.id = in.readString();
        this.items = in.createTypedArrayList(Item.CREATOR);
        this.deliveryMethod = in.readInt();
        this.status = in.readInt();
        this.date = in.readString();
        this.rated = in.readByte() != 0;
        this.ChiefID = in.readString();
        this.UserID = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
