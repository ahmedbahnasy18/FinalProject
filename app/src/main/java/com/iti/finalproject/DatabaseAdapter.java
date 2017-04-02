package com.iti.finalproject;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public void addUserInfo(String UID, String name, String phone, List<String> addresses){
        DatabaseReference user = getDatabase().getReference("Users").child(UID);
        user.child("Phone").setValue(phone);
        user.child("Addresses").setValue(addresses);
        user.child("Name").setValue(name);
    }

    public void addChiefRating(final String ChiefID, String OrderID, final float Rating){
        if (Rating > 5 || Rating < 0)
            throw new IllegalArgumentException();
        getDatabase().getReference("Chiefs").child(ChiefID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                float previousRating = 0;
                float ratingCount = 0;

                if (dataSnapshot.child("rating").getValue(Float.class) != null){
                    previousRating = dataSnapshot.child("rating").getValue(Float.class);
                }

                if (dataSnapshot.child("ratingCount").getValue(Integer.class) != null){
                    ratingCount = dataSnapshot.child("ratingCount").getValue(Integer.class);
                }

                float currentRating = ((ratingCount * previousRating) + Rating) / (ratingCount + 1);

                getDatabase().getReference("Chiefs").child(ChiefID).child("rating").setValue(currentRating);
                getDatabase().getReference("Chiefs").child(ChiefID).child("ratingCount").setValue(ratingCount + 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        getDatabase().getReference("Orders").child(OrderID).child("rated").setValue(true);
    }

}
