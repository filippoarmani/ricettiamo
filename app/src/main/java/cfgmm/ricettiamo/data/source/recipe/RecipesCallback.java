package cfgmm.ricettiamo.data.source.recipe;

import java.util.List;

import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.RecipeApiResponse;
/**
 * Interface to send data from DataSource to Repositories
 * that implement IRecipesRepositoryWithLiveData interface.
 */
public interface RecipesCallback {
    void onSuccessFromRemote(RecipeApiResponse recipeApiResponse);
    void onFailureFromRemote(Exception exception);
    void onSuccessFromLocal(RecipeApiResponse recipeApiResponse);
    void onFailureFromLocal(Exception exception);
    void onRecipesFavoriteStatusChanged(Recipe recipe, List<Recipe> favoriteRecipes);
    void onRecipesFavoriteStatusChanged(List<Recipe> recipes);
    void onDeleteFavoriteRecipeSuccess(List<Recipe> favoriteRecipes);
    void onSuccessFromCloudReading(List<Recipe> recipeList);
    void onSuccessFromCloudWriting(Recipe recipe);
    void onFailureFromCloud(Exception exception);
    void onSuccessSynchronization();
    void onSuccessDeletion();
}
