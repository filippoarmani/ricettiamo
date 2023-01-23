package cfgmm.ricettiamo.data.service;

import static cfgmm.ricettiamo.util.Constants.RECIPE_PARAMETER;
import static cfgmm.ricettiamo.util.Constants.SEARCH_RECIPES;

import cfgmm.ricettiamo.model.RecipeApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Interface for Service to get recipes from the Web Service.
 */
public interface    RecipeApiService {
    @GET(SEARCH_RECIPES)
    Call<RecipeApiResponse> getRecipes(
            @Query(RECIPE_PARAMETER) String name,
            @Header("Authentication") String apiKey);
}
