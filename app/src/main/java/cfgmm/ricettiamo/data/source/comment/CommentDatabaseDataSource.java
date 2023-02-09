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
    public void writeComment(Comment comment) {
        databaseReference.child(FIREBASE_COMMENTS_COLLECTION).child(comment.getIdRecipe()).child(comment.getIdComment())
                .setValue(comment.toMap())
                .addOnSuccessListener(task -> {
                    Log.d(TAG, "writeComment: success");
                    commentResponseCallBack.onSuccessWriteComment(comment);
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
    public void increaseScore(String id, int score, int oldScore) {
        Map<String, Object> data = new HashMap<>();
        data.put("score", score + oldScore);

        databaseReference.child(FIREBASE_USERS_COLLECTION).child(id).updateChildren(data)
                .addOnSuccessListener(result -> {
                    Log.d(TAG, "increaseScore: success");
                    commentResponseCallBack.onSuccessUpdateStars();
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "increaseScore: failure");
                    commentResponseCallBack.onFailureUpdateStars(1);
                });
    }

    @Override
    public void updateScore(String id, int score) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(id).get()
                .addOnSuccessListener(result -> {
                    User user = result.getValue(User.class);
                    if(user != null && user.getId().equals(id)) {
                        increaseScore(id, score, user.getScore());
                    } else {
                        Log.d(TAG, "increaseScore: skipped");
                    }
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "increaseScore: failure");
                    commentResponseCallBack.onFailureUpdateStars(1);
                });
    }

}
