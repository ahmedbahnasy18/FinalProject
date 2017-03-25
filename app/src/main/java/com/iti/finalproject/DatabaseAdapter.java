package com.iti.finalproject;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Mostafa on 03/21/2017.
 */

public class DatabaseAdapter {

    private FirebaseDatabase mDatabase;
    private static DatabaseAdapter mInstance;

    public FirebaseDatabase getDatabase(){
        if (mDatabase == null){
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    public static DatabaseAdapter getInstance(){
        if (mInstance == null)
            mInstance = new DatabaseAdapter();
        return mInstance;
    }

    private DatabaseAdapter(){

    }

    public void submitOrder (Order order){
        getDatabase().getReference("Orders").child(order.getId()).setValue(order);
    }

    public void addUserInfo(String UID, String phone, List<String> addresses){
        DatabaseReference user = getDatabase().getReference("Users").child(UID);
        user.child("Phone").setValue(phone);
        user.child("Addresses").setValue(addresses);
    }

    public void updateUserAddresses(String UID, List<String> addresses){
        getDatabase().getReference("Users").child(UID).child("Addresses").setValue(addresses);
    }

    public void addChiefInfo(Chief chief){
        getDatabase().getReference("Chiefs").child(chief.getId()).setValue(chief);
    }

    public void addChiefRating(String ChiefID, int Rating){
        if (Rating > 5 || Rating < 0)
            throw new IllegalArgumentException();
        getDatabase().getReference("Chiefs").child(ChiefID).child("rating").setValue(Rating);
    }

}
