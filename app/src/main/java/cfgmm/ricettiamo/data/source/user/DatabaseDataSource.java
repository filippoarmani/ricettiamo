package cfgmm.ricettiamo.data.source.user;

import static cfgmm.ricettiamo.util.Constants.FIREBASE_REALTIME_DATABASE;
import static cfgmm.ricettiamo.util.Constants.FIREBASE_USERS_COLLECTION;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
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

    @Override
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

    @Override
    public void readUser(String id) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(id)
                .get()
                .addOnSuccessListener(task -> {
                    Log.d(TAG, "readUser: success");
                    userResponseCallBack.onSuccessReadDatabase(task.getValue(User.class));
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "readUser: failure");
                    userResponseCallBack.onFailureReadDatabase(R.string.retrievingDatabase_error);
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

    @Override
    public void getTopTen() {
        databaseReference.child(FIREBASE_USERS_COLLECTION).orderByChild("totalStars").get()
                .addOnSuccessListener(task -> {
                    List<User> topTen = new ArrayList<>();
                    for (DataSnapshot snapshot:  task.getChildren()) {
                        topTen.add(snapshot.getValue(User.class));

                        if(topTen.size() == 10) {
                            break;
                        }
                    }
                    userResponseCallBack.onSuccessGetTopTen(topTen);
                })
                .addOnFailureListener(error -> {
                   Log.d(TAG, "getTopTen: failure");
                   userResponseCallBack.onFailureGetTopTen(R.string.retrievingDatabase_error);
                });
    }

    @Override
    public void getPosition(String id) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).orderByChild("totalStars").get()
                .addOnSuccessListener(task -> {
                    int i = 1;
                    for (DataSnapshot snapshot:  task.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        assert user != null;
                        if(id.equals(user.getId())) {
                            break;
                        } else {
                            i++;
                        }
                    }
                    userResponseCallBack.onSuccessGetPosition(i);
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "getTopTen: failure");
                    userResponseCallBack.onFailureGetPosition(R.string.retrievingDatabase_error);
                });
    }

}
