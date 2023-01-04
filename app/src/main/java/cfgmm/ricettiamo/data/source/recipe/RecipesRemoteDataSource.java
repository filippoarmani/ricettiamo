package cfgmm.ricettiamo.data.source.recipe;

import static cfgmm.ricettiamo.util.Constants.API_KEY_ERROR;
import static cfgmm.ricettiamo.util.Constants.FIREBASE_FAVORITE_RECIPES_COLLECTION;
import static cfgmm.ricettiamo.util.Constants.FIREBASE_REALTIME_DATABASE;
import static cfgmm.ricettiamo.util.Constants.FIREBASE_USERS_COLLECTION;
import static cfgmm.ricettiamo.util.Constants.RETROFIT_ERROR;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.data.service.RecipeApiService;
import cfgmm.ricettiamo.model.RecipeApiResponse;
import cfgmm.ricettiamo.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class to get the user favorite recipes using Firebase Realtime Database.
 */
public class RecipesRemoteDataSource extends BaseRecipesRemoteDataSource{

    private final RecipeApiService recipeApiService;
    private final String apiKey;

    public RecipesRemoteDataSource(String apiKey) {
        this.apiKey = apiKey;
        this.recipeApiService = ServiceLocator.getInstance().getRecipeApiService();
    }

    @Override
    public void getRecipes() {
        Call<RecipeApiResponse> recipeResponseCall = recipeApiService.getRecipes(apiKey);

        recipeResponseCall.enqueue(new Callback<RecipeApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeApiResponse> call,
                                   @NonNull Response<RecipeApiResponse> response) {

                if (response.body() != null && response.isSuccessful() &&
                        !response.body().getStatus().equals("error")) {
                    recipesCallback.onSuccessFromRemote(response.body(), System.currentTimeMillis());

                } else {
                    recipesCallback.onFailureFromRemote(new Exception(API_KEY_ERROR));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeApiResponse> call, @NonNull Throwable t) {
                recipesCallback.onFailureFromRemote(new Exception(RETROFIT_ERROR));
            }
        });
    }
}