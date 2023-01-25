package cfgmm.ricettiamo.data.source.user;

import static cfgmm.ricettiamo.util.Constants.FIREBASE_REALTIME_DATABASE;
import static cfgmm.ricettiamo.util.Constants.FIREBASE_USERS_COLLECTION;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import cfgmm.ricettiamo.R;
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
                    userResponseCallBack.onFailureWriteDatabase(R.string.writeDatabase_error);
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
                    userResponseCallBack.onFailureReadDatabase(R.string.readDatabase_error);
                });
    }

    @Override
    public void updateData(Map<String, Object> newInfo, String id) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(id)
                .updateChildren(newInfo)
                .addOnSuccessListener(task -> {
                    Log.d(TAG, "updateUser: success");
                    userResponseCallBack.onSuccessWriteDatabase();
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "updateUser: failure");
                    userResponseCallBack.onFailureWriteDatabase(R.string.updateData_error);
                });
    }

}
