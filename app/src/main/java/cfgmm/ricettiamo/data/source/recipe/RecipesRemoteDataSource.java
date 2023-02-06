package cfgmm.ricettiamo.data.source.recipe;

import static cfgmm.ricettiamo.util.Constants.ADD_RECIPE_INFORMATIONS;
import static cfgmm.ricettiamo.util.Constants.ADD_RECIPE_INGREDIENTS;
import static cfgmm.ricettiamo.util.Constants.API_KEY_ERROR;
import static cfgmm.ricettiamo.util.Constants.NUMBER_OF_ELEMENTS;
import static cfgmm.ricettiamo.util.Constants.RETROFIT_ERROR;

import androidx.annotation.NonNull;

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
    public void getRecipes(String user_input) {
        Call<RecipeApiResponse> recipeResponseCall = recipeApiService.getRecipesByName(user_input, NUMBER_OF_ELEMENTS,
                ADD_RECIPE_INFORMATIONS, ADD_RECIPE_INGREDIENTS, apiKey);

        recipeResponseCall.enqueue(new Callback<RecipeApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeApiResponse> call,
                                   @NonNull Response<RecipeApiResponse> response) {

                if (response.body() != null && response.isSuccessful()) {
                    recipesCallback.onSuccessFromRemote(response.body());

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
