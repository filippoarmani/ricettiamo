package cfgmm.ricettiamo.data.source.recipe;

import static cfgmm.ricettiamo.util.Constants.FIREBASE_REALTIME_DATABASE;
import static cfgmm.ricettiamo.util.Constants.FIREBASE_RECIPES_COLLECTION;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Recipe;

public class DatabaseRecipesDataSource extends BaseDatabaseRecipesDataSource {

    private static final String TAG = DatabaseRecipesDataSource.class.getSimpleName();

    private final DatabaseReference databaseReference;

    public DatabaseRecipesDataSource() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE);
        databaseReference = firebaseDatabase.getReference().getRef();
    }

   /* @Override
    public void writeRecipe(Recipe recipe) {
        String id = "" + recipe.getId();
        databaseReference.child(FIREBASE_RECIPES_COLLECTION).child(id)
                .setValue(recipe.toMap())
                .addOnSuccessListener(task -> {
                    Log.d(TAG, "writeRecipe: success");
                    recipesResponseCallback.onSuccessWriteDatabase();
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "writeRecipe: failure");
                    recipesResponseCallback.onFailureWriteDatabase(R.string.writeDatabase_error);
                });
    }*/

    @Override
    public void getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        Query userRecipe = databaseReference.child(FIREBASE_RECIPES_COLLECTION);
        userRecipe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:  dataSnapshot.getChildren()) {
                    recipes.add(snapshot.getValue(Recipe.class));
                }
                Log.d(TAG, "getAllRecipes: success");
                recipesResponseCallback.onSuccessGetAllRecipes(recipes);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "getAllRecipes: failure");
                recipesResponseCallback.onFailureGetAllRecipes(R.string.writeDatabase_error);
            }
        });
    }

    @Override
    public void getFirstRecipe(String author) {
        Query firstRecipe = databaseReference.child(FIREBASE_RECIPES_COLLECTION).equalTo(author).orderByChild("date").limitToFirst(1);
        firstRecipe.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Recipe firstRecipe = dataSnapshot.getValue(Recipe.class);
                Log.d(TAG, "getFirstRecipe: success");
                recipesResponseCallback.onSuccessGetFirstRecipe(firstRecipe);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "getFirstRecipe: failure");
                recipesResponseCallback.onFailureGetFirstRecipe(R.string.writeDatabase_error);
            }
        });
    }

    @Override
    public void getMostRecentRecipe(String author) {
        Query mostRecent = databaseReference.child(FIREBASE_RECIPES_COLLECTION).equalTo(author).orderByChild("date").limitToLast(1);
        mostRecent.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Recipe mostRecentRecipe = dataSnapshot.getValue(Recipe.class);
                Log.d(TAG, "getMostRecentRecipe: success");
                recipesResponseCallback.onSuccessGetMostRecentRecipe(mostRecentRecipe);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "getMostRecentRecipe: failure");
                recipesResponseCallback.onFailureGetMostRecentRecipe(R.string.writeDatabase_error);
            }
        });
    }

    @Override
    public void getMyRecipes(String author) {
        List<Recipe> recipes = new ArrayList<>();
        Query userRecipe = databaseReference.child(FIREBASE_RECIPES_COLLECTION).equalTo(author).orderByChild("score");
        userRecipe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:  dataSnapshot.getChildren()) {
                    recipes.add(snapshot.getValue(Recipe.class));
                }
                Log.d(TAG, "getMyRecipes: success");
                recipesResponseCallback.onSuccessGetMyRecipes(recipes);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "getMyRecipes: failure");
                recipesResponseCallback.onFailureGetMyRecipes(R.string.writeDatabase_error);
            }
        });
    }

    /*@Override
    public void getFavoriteRecipesId(String user) {
        List<String> favoriteRecipes = new ArrayList<>();
        Query userRecipe = databaseReference.child(FIREBASE_FAVORITE_RECIPES_COLLECTION).child(user);
        userRecipe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:  dataSnapshot.getChildren()) {
                    favoriteRecipes.add(snapshot.getValue(String.class));
                }
                Log.d(TAG, "getFavoriteRecipesId: success");
                recipesResponseCallback.onSuccessGetFavoriteRecipesId(favoriteRecipes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "getFavoriteRecipesId: failure");
                recipesResponseCallback.onFailureGetFavoriteRecipesId(R.string.writeDatabase_error);
            }
        });
    }

    @Override
    public void addFavoriteRecipe(String user, String recipe) {
        databaseReference.child(FIREBASE_FAVORITE_RECIPES_COLLECTION).child(user)
                .child(recipe).setValue(recipe)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "addFavoriteRecipe: success");
                    recipesResponseCallback.onSuccessAddFavoriteRecipe(null);
                }).addOnFailureListener(e -> {
                    Log.d(TAG, "addFavoriteRecipe: failure");
                    recipesResponseCallback.onFailureAddFavoriteRecipe(R.string.writeDatabase_error);
                });
    }

    @Override
    public void deleteFavoriteRecipe(String user, String recipe) {
        databaseReference.child(FIREBASE_FAVORITE_RECIPES_COLLECTION).child(user)
                .child(recipe).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "deleteFavoriteRecipe: success");
                    recipesResponseCallback.onSuccessDeleteFavoriteRecipe(null);
                }).addOnFailureListener(e -> {
                    Log.d(TAG, "deleteFavoriteRecipe: failure");
                    recipesResponseCallback.onFailureDeleteFavoriteRecipe(R.string.writeDatabase_error);
                });
    }

    @Override
    public void deleteAllFavoriteRecipes(String user) {
        databaseReference.child(FIREBASE_FAVORITE_RECIPES_COLLECTION).child(user).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "deleteAllFavoriteRecipes: success");
                    recipesResponseCallback.onSuccessDeleteAllFavoriteRecipes(null);
                }).addOnFailureListener(e -> {
                    Log.d(TAG, "deleteAllFavoriteRecipes: failure");
                    recipesResponseCallback.onFailureDeleteAllFavoriteRecipes(R.string.writeDatabase_error);
                });
    }

     */
}
