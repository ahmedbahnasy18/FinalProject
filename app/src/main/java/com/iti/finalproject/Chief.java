package com.iti.finalproject;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 20/03/2017.
 */

public class Chief implements Parcelable {
	
    private String id;
    private String name;
    private int rating;
    private int ratingCount;
    private String image;
    private String openHours;
    private boolean pickUp;
    private boolean delivery;
    private String deliverIn;
    private String phone;
    private List<Item> menuItems;

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Chief(){
		this.id = "";
        this.name = "";
        this.rating = 0;
        this.image = "";
        this.phone = "";
        this.openHours = "";
        this.pickUp = false;
        this.delivery = false;
        this.deliverIn = "";
        this.menuItems = new ArrayList<>();
	}

    public Chief(String id, String name, int rating, String image, String phone, String openHours, boolean pickUp, boolean delivery, String deliverIn, ArrayList<Item> menu) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.image = image;
        this.phone = phone;
        this.openHours = openHours;
        this.pickUp = pickUp;
        this.delivery = delivery;
        this.deliverIn = deliverIn;
        this.menuItems = menu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = Math.round(rating);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public boolean isPickUp() {
        return pickUp;
    }

    public void setPickUp(boolean pickUp) {
        this.pickUp = pickUp;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public String getDeliverIn() {
        return deliverIn;
    }

    public void setDeliverIn(String deliverIn) {
        this.deliverIn = deliverIn;
    }

    public List<Item> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(ArrayList<Item> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.rating);
        dest.writeString(this.image);
        dest.writeString(this.openHours);
        dest.writeByte(this.pickUp ? (byte) 1 : (byte) 0);
        dest.writeByte(this.delivery ? (byte) 1 : (byte) 0);
        dest.writeString(this.deliverIn);
        dest.writeTypedList(this.menuItems);
    }

    protected Chief(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.rating = in.readInt();
        this.image = in.readString();
        this.openHours = in.readString();
        this.pickUp = in.readByte() != 0;
        this.delivery = in.readByte() != 0;
        this.deliverIn = in.readString();
        this.menuItems = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<Chief> CREATOR = new Creator<Chief>() {
        @Override
        public Chief createFromParcel(Parcel source) {
            return new Chief(source);
        }

        @Override
        public Chief[] newArray(int size) {
            return new Chief[size];
        }
    };
}
