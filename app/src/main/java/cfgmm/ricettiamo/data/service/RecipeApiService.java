package cfgmm.ricettiamo.data.service;

import static cfgmm.ricettiamo.util.Constants.INGREDIENTS_LIST;
import static cfgmm.ricettiamo.util.Constants.RECIPE_PARAMETER;
import static cfgmm.ricettiamo.util.Constants.SEARCH_INGREDIENT;
import static cfgmm.ricettiamo.util.Constants.SEARCH_RECIPES;
import static cfgmm.ricettiamo.util.Constants.SEARCH_RECIPES_BY_INGREDIENT;

import cfgmm.ricettiamo.model.RecipeApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface for Service to get recipes from the Web Service.
 */
public interface    RecipeApiService {

    //search recipes by name
    @GET(SEARCH_RECIPES)
    Call<RecipeApiResponse> getRecipesByName(
            @Query(RECIPE_PARAMETER) String name,
            @Query("number") int number,
            @Header("x-api-key") String apiKey);

    //search recipes by ingredient
    @GET(SEARCH_RECIPES_BY_INGREDIENT)
    Call<RecipeApiResponse> getRecipesByIngredient(
            //A comma-separated list of ingredients that the recipes should contain.
            //example: apples,flour,sugar
            @Query(INGREDIENTS_LIST) String listOfIngredients,
            @Query("number") int number,
            @Header("x-api-key") String apiKey);

    //search ingredient
    @GET(SEARCH_INGREDIENT)
    Call<RecipeApiResponse> getIngredientByName(
            @Query(RECIPE_PARAMETER) String name,
            @Header("x-api-key") String apiKey);

    //get ingredient informations
    @GET("food/ingredients/{id}/informations")
    Call<RecipeApiResponse> getIngredientById(
            @Path("id") int id,
            @Header("x-api-key") String apiKey);

    //get recipe steps
    @GET("recipes/{id}/analyzedInstructions")
    Call<RecipeApiResponse> getRecipeSteps(
            @Path("id") int id,
            @Header("x-api-key") String apiKey);
}
