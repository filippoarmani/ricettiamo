package cfgmm.ricettiamo.data.repository.recipe;

import java.util.List;

import cfgmm.ricettiamo.model.Recipe;

/**
 * Interface to send data from Repositories that implement
 * IRecipesRepository interface to Activity/Fragment.
 */
public interface RecipesResponseCallback {
    void onSuccess(List<Recipe> recipesList);
    void onFailure(String errorMessage);
    void onRecipesFavoriteStatusChanged(Recipe recipe);
}
