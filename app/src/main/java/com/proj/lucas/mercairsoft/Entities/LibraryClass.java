package com.proj.lucas.mercairsoft.Entities;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by arturgirao on 25/09/2017.
 */

public final class LibraryClass {

    private static DatabaseReference firebase;
    private LibraryClass(){}
    public static DatabaseReference getFirebase(){
        if( firebase == null ){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return( firebase );
    }
}
