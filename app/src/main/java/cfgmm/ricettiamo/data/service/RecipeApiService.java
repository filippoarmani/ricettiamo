package cfgmm.ricettiamo.data.service;

import cfgmm.ricettiamo.model.RecipeApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Interface for Service to get news from the Web Service.
 */
public interface RecipeApiService {
    /*@GET(TOP_HEADLINES_ENDPOINT)
    Call<NewsApiResponse> getNews(
            @Query(TOP_HEADLINES_COUNTRY_PARAMETER) String country,
            @Query(TOP_HEADLINES_PAGE_SIZE_PARAMETER) int pageSize,
            @Query(TOP_HEADLINES_PAGE_PARAMETER) int page,
            @Header("Authorization") String apiKey);*/
}
