package com.example.shopapp.Utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtils {
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference databaseReference;

    private static FirebaseStorage firebaseStorage;
    private static StorageReference storageReference;

    public static DatabaseReference getChildRef(String childName) {
        if (databaseReference == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
        }
        return databaseReference.child(childName);
    }

    public static StorageReference getChildStorageRef(String childName){
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        return storageReference.child(childName);
    }
}
