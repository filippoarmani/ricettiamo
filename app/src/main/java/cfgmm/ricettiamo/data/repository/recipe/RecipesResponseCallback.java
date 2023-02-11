package cfgmm.ricettiamo.data.repository.recipe;

import java.util.List;

import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.Recipe;

/**
 * Interface to send data from Repositories that implement
 * IRecipesRepository interface to Activity/Fragment.
 */
public interface RecipesResponseCallback {
    void onSuccessFavorite(List<Recipe> recipesList);
    void onFailure(int errorMessage);
    void onRecipesFavoriteStatusChanged(Recipe recipe);

    void onSuccessFromRemote(List<Recipe> recipesList, List<Ingredient> ingredientList);
    void onFailureFromRemote(int error);

    void onSuccessFromDatabase(List<Recipe> all);
}