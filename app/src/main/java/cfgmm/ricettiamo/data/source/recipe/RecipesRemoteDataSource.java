package cfgmm.ricettiamo.data.source.recipe;

import static cfgmm.ricettiamo.util.Constants.ADD_RECIPE_INFORMATIONS;
import static cfgmm.ricettiamo.util.Constants.ADD_RECIPE_INGREDIENTS;
import static cfgmm.ricettiamo.util.Constants.NUMBER_OF_ELEMENTS;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.service.RecipeApiService;
import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.RecipeApiResponse;
import cfgmm.ricettiamo.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesRemoteDataSource extends BaseRecipesRemoteDataSource {

    private final RecipeApiService recipeApiService;
    private final String apiKey;


    public RecipesRemoteDataSource(String apiKey) {
        this.apiKey = apiKey;
        this.recipeApiService = ServiceLocator.getInstance().getRecipeApiService();
    }

    @Override
    public void getRecipes(String user_input) {

        // It gets the recipies from the Web Service
        Call<RecipeApiResponse> recipeResponseCall = recipeApiService.getRecipesByName(user_input, NUMBER_OF_ELEMENTS,
                ADD_RECIPE_INFORMATIONS, ADD_RECIPE_INGREDIENTS, apiKey);

        recipeResponseCall.enqueue(new Callback<RecipeApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeApiResponse> call,
                                   @NonNull Response<RecipeApiResponse> response) {

                if (response.body() != null && response.isSuccessful()) {
                    List<Recipe> recipesList = response.body().getListRecipes();
                    List<Ingredient> ingredientList = new ArrayList<>();
                    for (int i = 0; i < recipesList.size(); i++) {
                        Recipe recipetemp = recipesList.get(i);
                        for (int j = 0; j < recipetemp.getIngredientsList().size(); j++) {
                            Ingredient ingredient = new Ingredient(recipetemp.getIngredientsList().get(j).getName(),
                                    recipetemp.getIngredientsList().get(j).getQta(),
                                    recipetemp.getIngredientsList().get(j).getSize());
                            ingredientList.add(ingredient);
                        }
                    }
                    recipesCallback.onSuccessFromRemote(recipesList, ingredientList);
                } else {
                    recipesCallback.onFailureFromRemote(R.string.error_retrieving_recipe);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeApiResponse> call, @NonNull Throwable t) {
                recipesCallback.onFailureFromRemote(R.string.error_retrieving_recipe);
            }
        });
    }
}
