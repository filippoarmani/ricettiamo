package cfgmm.ricettiamo.data.source.comment;

import static cfgmm.ricettiamo.util.Constants.FIREBASE_COMMENTS_COLLECTION;
import static cfgmm.ricettiamo.util.Constants.FIREBASE_REALTIME_DATABASE;
import static cfgmm.ricettiamo.util.Constants.FIREBASE_RECIPES_COLLECTION;
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
import cfgmm.ricettiamo.model.Recipe;
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
    public void updateStars(String id, int score, boolean type) {
        AtomicInteger oldStars = new AtomicInteger();
        String path = getPath(type);
        databaseReference.child(path).child(id).child("score").get()
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
        stars.put("score", oldStars.get() + score);
        databaseReference.child(path).child(id)
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
    public void exists(String id, boolean type) {
        try {
            String path = getPath(type);
            databaseReference.child(path).child(id).get()
                    .addOnSuccessListener(result -> {
                        if(result != null && getValue(result, type)) {
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
    
    public boolean getValue(DataSnapshot snapshot, boolean type) {
        if(type)
            return snapshot.getValue(User.class) != null;
        else
            return snapshot.getValue(Recipe.class) != null;
    }

    public String getPath(boolean type) {
        if(type) {
            return FIREBASE_USERS_COLLECTION;
        } else {
            return FIREBASE_RECIPES_COLLECTION;
        }
    }
}
