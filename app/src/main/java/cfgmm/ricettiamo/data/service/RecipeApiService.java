package cfgmm.ricettiamo.data.service;

import cfgmm.ricettiamo.model.RecipeApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Interface for Service to get recipes from the Web Service.
 */
public interface RecipeApiService {
    @GET(/*TOP_HEADLINES_ENDPOINT*/)
    Call<RecipeApiResponse> getRecipes(
            @Header("Authentication") String apiKey);
}
