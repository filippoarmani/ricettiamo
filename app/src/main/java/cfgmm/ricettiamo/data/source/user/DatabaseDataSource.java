package cfgmm.ricettiamo.data.source.user;

import static cfgmm.ricettiamo.util.Constants.FIREBASE_REALTIME_DATABASE;
import static cfgmm.ricettiamo.util.Constants.FIREBASE_USERS_COLLECTION;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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
        Query user = databaseReference.child(FIREBASE_USERS_COLLECTION).child(id);

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "readUser: success");
                userResponseCallBack.onSuccessReadDatabase(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "readUser: failure");
                userResponseCallBack.onFailureReadDatabase(R.string.retrievingDatabase_error);
            }
        });

    }

    @Override
    public void updateData(Map<String, Object> newInfo, String id) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(id)
                .updateChildren(newInfo)
                .addOnSuccessListener(task -> {
                    Log.d(TAG, "updateUser: success");
                    userResponseCallBack.onSuccessUpdateDatabase();
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "updateUser: failure");
                    userResponseCallBack.onFailureUpdateDatabase(R.string.updateData_error);
                });
    }

    @Override
    public void getTopTen() {
        Query topTen = databaseReference.child(FIREBASE_USERS_COLLECTION).orderByChild("score").limitToFirst(10);
        topTen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User[] topTen = new User[10];
                int i = (int) dataSnapshot.getChildrenCount() - 1;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    topTen[i] = postSnapshot.getValue(User.class);
                    i--;
                }
                Log.d(TAG, "getTopTen: success");
                userResponseCallBack.onSuccessGetTopTen(topTen);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "getTopTen: failure");
                userResponseCallBack.onFailureGetTopTen(R.string.retrievingDatabase_error);
            }
        });
    }

    @Override
    public void getPosition(String id) {
        Query topTen = databaseReference.child(FIREBASE_USERS_COLLECTION).orderByChild("score");
        topTen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot:  dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    if(id.equals(user.getId())) {
                        break;
                    } else {
                        i++;
                    }
                }
                i = (int) dataSnapshot.getChildrenCount() - i;
                Log.d(TAG, "getPosition: success");
                userResponseCallBack.onSuccessGetPosition(i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "getPosition: failure");
                userResponseCallBack.onFailureGetPosition(R.string.retrievingDatabase_error);
            }
        });
    }

    @Override
    public boolean alreadyExist(User user) {
        AtomicBoolean exist = new AtomicBoolean(false);
        databaseReference.child(FIREBASE_USERS_COLLECTION).equalTo(user.getEmail()).get()
                .addOnSuccessListener(dataSnapshot -> {
                    for (DataSnapshot snapshot:  dataSnapshot.getChildren()) {
                        User dUser = snapshot.getValue(User.class);
                        assert dUser != null;
                        if(dUser.equals(user)) {
                            exist.set(true);
                        }
                    }
                })
                .addOnFailureListener(error -> {
                   exist.set(false);
                });

        return exist.get();
    }

    @Override
    public User getUserById(String id) {
        return databaseReference.child(FIREBASE_USERS_COLLECTION).child(id).get().getResult().getValue(User.class);
    }

}
