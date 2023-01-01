package cfgmm.ricettiamo.data.source.user;

import static cfgmm.ricettiamo.util.Constants.FIREBASE_REALTIME_DATABASE;
import static cfgmm.ricettiamo.util.Constants.FIREBASE_USERS_COLLECTION;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cfgmm.ricettiamo.model.User;

public class DatabaseDataSource extends BaseDatabaseDataSource {

    private static final String TAG = DatabaseDataSource.class.getSimpleName();

    private final DatabaseReference databaseReference;

    public DatabaseDataSource() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE);
        databaseReference = firebaseDatabase.getReference().getRef();
    }

    public void writeUser(User user) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(user.getId())
                .setValue(user.toMap())
                .addOnSuccessListener(task -> {
                    Log.d(TAG, "writeUser: success");
                    userResponseCallBack.onSuccessWriteDatabase();
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "writeUser: failure");
                    userResponseCallBack.onFailureWriteDatabase(error.getLocalizedMessage());
                });
    }

    public void readUser(String id) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(id)
                .get()
                .addOnSuccessListener(task -> {
                    Log.d(TAG, "readUser: success");
                    userResponseCallBack.onSuccessReadDatabase(task.getValue(User.class));
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "readUser: failure");
                    userResponseCallBack.onFailureReadDatabase(error.getLocalizedMessage());
                });
    }

}