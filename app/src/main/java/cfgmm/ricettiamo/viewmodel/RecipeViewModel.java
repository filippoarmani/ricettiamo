package cfgmm.ricettiamo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import cfgmm.ricettiamo.data.repository.recipe.IRecipesRepository;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.Result;

public class RecipeViewModel extends ViewModel {
    private static final String TAG = RecipeViewModel.class.getSimpleName();
    private final IRecipesRepository recipesRepository;
    private boolean isLoading;
    private boolean firstLoading;
    private List<Recipe> recipesList;
    private List<Recipe> favoriteRecipes;

    public RecipeViewModel(IRecipesRepository iRecipesRepository) {
        this.recipesRepository = iRecipesRepository;
        this.firstLoading = true;
    }

    /**
     * Returns the list associated with the
     * recipes list to the Fragment/Activity.
     * @return The list associated with the recipes.
     */
    public List<Recipe> getRecipes(String user_input) {
        if (recipesList == null) {
            getRecipes(user_input);
        }
        return recipesList;
    }

    /**
     * Returns the list associated with the
     * favourite recipes list to the Fragment/Activity.
     * @return The list associated with the recipes.
     */
    public List<Recipe> getFavoriteRecipes(boolean isFirstLoading) {
        if (favoriteRecipes == null) {
            getFavoriteRecipes(isFirstLoading);
        }
        return favoriteRecipes;
    }

    /**
     * Updates the recipes status.
     * @param recipe The recipes to be updated.
     */
    public void updateRecipes(Recipe recipe) {
        recipesRepository.updateRecipes(recipe);
    }

    /**
     * Removes the recipes from the list of favorite recipes.
     * @param recipe The recipe to be removed from the list of favorite recipes.
     */
    public void removeFromFavorite(Recipe recipe) {
        recipesRepository.updateRecipes(recipe);
    }

    /**
     * Clears the list of favorite recipes.
     */
    public void deleteAllFavoriteNews() {
        recipesRepository.deleteFavoriteRecipes();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isFirstLoading() {
        return firstLoading;
    }

    public void setFirstLoading(boolean firstLoading) {
        this.firstLoading = firstLoading;
    }

    /*public MutableLiveData<Result> getRecipesResponseLiveData() {
        return recipesList;
    }*/
}