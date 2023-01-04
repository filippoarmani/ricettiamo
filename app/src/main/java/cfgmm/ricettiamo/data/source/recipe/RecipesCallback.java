package cfgmm.ricettiamo.data.source.recipe;

import java.util.List;

import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.RecipeApiResponse;
//TODO
/**
 * Interface to send data from DataSource to Repositories
 * that implement IRecipesRepositoryWithLiveData interface.
 */
public interface RecipesCallback {
    void onSuccessFromRemote(RecipeApiResponse recipeApiResponse, long lastUpdate);
    void onFailureFromRemote(Exception exception);
    void onSuccessFromLocal(RecipeApiResponse recipeApiResponse);
    void onFailureFromLocal(Exception exception);
    void onNewsFavoriteStatusChanged(Recipe recipe, List<Recipe> favoriteRecipes);
    void onNewsFavoriteStatusChanged(List<Recipe> recipes);
    void onDeleteFavoriteNewsSuccess(List<Recipe> favoriteRecipes);
    void onSuccessFromCloudReading(List<Recipe> recipeList);
    void onSuccessFromCloudWriting(Recipe recipe);
    void onFailureFromCloud(Exception exception);
    void onSuccessSynchronization();
    void onSuccessDeletion();
}