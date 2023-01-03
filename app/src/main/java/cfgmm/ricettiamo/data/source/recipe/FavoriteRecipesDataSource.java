package cfgmm.ricettiamo.data.source.recipe;

import static cfgmm.ricettiamo.util.Constants.FIREBASE_FAVORITE_RECIPES_COLLECTION;
import static cfgmm.ricettiamo.util.Constants.FIREBASE_REALTIME_DATABASE;
import static cfgmm.ricettiamo.util.Constants.FIREBASE_USERS_COLLECTION;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.model.Recipe;

/**
 * Class to get the user favorite recipes using Firebase Realtime Database.
 */
public class FavoriteRecipesDataSource extends BaseFavoriteRecipesDataSource {

    private static final String TAG = FavoriteRecipesDataSource.class.getSimpleName();

    private final DatabaseReference databaseReference;
    private final String idToken;

    public FavoriteRecipesDataSource(String idToken) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(   FIREBASE_REALTIME_DATABASE);
        databaseReference = firebaseDatabase.getReference().getRef();
        this.idToken = idToken;
    }

    @Override
    public void getFavoriteRecipes() {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(FIREBASE_FAVORITE_RECIPES_COLLECTION).get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "Error getting data", task.getException());
                    }
                    else {
                        Log.d(TAG, "Successful read: " + task.getResult().getValue());

                        List<Recipe> newsList = new ArrayList<>();
                        for(DataSnapshot ds : task.getResult().getChildren()) {
                            Recipe news = ds.getValue(Recipe.class);
                            newsList.add(news);
                        }

                        recipesCallback.onSuccessFromCloudReading(newsList);
                    }
                });
    }

    @Override
    public void addFavoriteRecipes(Recipe recipe) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(FIREBASE_FAVORITE_RECIPES_COLLECTION).child(String.valueOf(recipe.hashCode())).setValue(recipe)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        recipesCallback.onSuccessFromCloudWriting(recipe);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        recipesCallback.onFailureFromCloud(e);
                    }
                });
    }

    @Override
    public void deleteFavoriteRecipes(Recipe recipe) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(FIREBASE_FAVORITE_RECIPES_COLLECTION).child(String.valueOf(recipe.hashCode())).
                removeValue().addOnSuccessListener(aVoid -> {
                    recipesCallback.onSuccessFromCloudWriting(recipe);
                }).addOnFailureListener(e -> {
                    recipesCallback.onFailureFromCloud(e);
                });
    }

    @Override
    public void deleteAllFavoriteRecipes() {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(FIREBASE_FAVORITE_RECIPES_COLLECTION).removeValue().addOnSuccessListener(aVoid -> {
                    recipesCallback.onSuccessFromCloudWriting(null);
                }).addOnFailureListener(e -> {
                    recipesCallback.onFailureFromCloud(e);
                });
    }
}
