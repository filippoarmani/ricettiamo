package cfgmm.ricettiamo.data.source.comment;

import static cfgmm.ricettiamo.util.Constants.FIREBASE_COMMENTS_COLLECTION;
import static cfgmm.ricettiamo.util.Constants.FIREBASE_REALTIME_DATABASE;
import static cfgmm.ricettiamo.util.Constants.FIREBASE_USERS_COLLECTION;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Comment;
import cfgmm.ricettiamo.model.User;

public class CommentDatabaseDataSource extends BaseCommentDatabaseDataSource {

    private static final String TAG = CommentDatabaseDataSource.class.getSimpleName();

    private final DatabaseReference databaseReference;

    public CommentDatabaseDataSource() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE);
        databaseReference = firebaseDatabase.getReference().getRef();
    }

    @Override
    public void writeComment(Comment comment, String authorId) {
        databaseReference.child(FIREBASE_COMMENTS_COLLECTION).child(comment.getIdRecipe()).child(comment.getIdComment())
                .setValue(comment.toMap())
                .addOnSuccessListener(task -> {
                    Log.d(TAG, "writeComment: success");
                    commentResponseCallBack.onSuccessWriteComment(comment, authorId);
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "writeComment: failure");
                    commentResponseCallBack.onFailureWriteComment(R.string.writeDatabase_error);
                });
    }

    @Override
    public void readComments(String idRecipe) {
        Query commentD = databaseReference.child(FIREBASE_COMMENTS_COLLECTION).child(idRecipe);
        commentD.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Comment> commentList = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    commentList.add(postSnapshot.getValue(Comment.class));
                }
                Log.d(TAG, "readComments: success");
                commentResponseCallBack.onSuccessReadComment(commentList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "readComments: failure");
                commentResponseCallBack.onFailureReadComment(R.string.retrievingDatabase_error);
            }
        });
    }

    @Override
    public void updateStars(String idUser, int score) {
        AtomicInteger oldStars = new AtomicInteger();
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idUser).child("totalStars").get()
                .addOnSuccessListener(result -> {
                    try {
                        Integer read = result.getValue(Integer.class);
                        oldStars.set(read);
                    } catch (Exception e) {
                        Log.d(TAG, "updateStars: failure");
                        commentResponseCallBack.onFailureUpdateStars(R.string.updateData_error);
                    }
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "updateStars: failure");
                    commentResponseCallBack.onFailureUpdateStars(R.string.updateData_error);
                });

        Map<String, Object> stars = new HashMap<>();
        stars.put("totalStars", oldStars.get() + score);
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idUser)
                .updateChildren(stars)
                .addOnSuccessListener(task -> {
                    Log.d(TAG, "updateStars: success");
                    commentResponseCallBack.onSuccessUpdateStars();
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "updateStars: failure");
                    commentResponseCallBack.onFailureUpdateStars(R.string.updateData_error);
                });
    }

    @Override
    public void exists(String idUser) {
        try {
            databaseReference.child(FIREBASE_USERS_COLLECTION).child(idUser).get()
                    .addOnSuccessListener(user -> {
                        if(user != null && user.getValue(User.class) != null) {
                            Log.d(TAG, "exists: false");
                            commentResponseCallBack.setTrue();
                        } else {
                            Log.d(TAG, "exists: false");
                            commentResponseCallBack.setFalse();
                        }
                    })
                    .addOnFailureListener(error -> {
                        Log.d(TAG, "exists: false");
                        commentResponseCallBack.setFalse();
                    });
        } catch(Exception e) {
            Log.d(TAG, "exists: false");
            commentResponseCallBack.setFalse();
        }
    }
}
