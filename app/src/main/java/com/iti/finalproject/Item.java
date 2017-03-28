package com.iti.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ahmed on 20/03/2017.
 */

public class Item implements Parcelable {
	
    private int id;
    private String name;
    private float price;
    private String image;
    private String description;

    public Item(){
		this.id = 0;
        this.name = "";
        this.price = 0.0f;
        this.image = "";
        this.description = "";
	}

    public Item(int id, String name, float price, String image, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeFloat(this.price);
        dest.writeString(this.image);
        dest.writeString(this.description);
    }

    protected Item(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.price = in.readFloat();
        this.image = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
