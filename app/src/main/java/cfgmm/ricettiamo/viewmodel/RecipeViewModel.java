package cfgmm.ricettiamo.viewmodel;

import androidx.lifecycle.MutableLiveData;

import cfgmm.ricettiamo.data.repository.recipe.IRecipesRepository;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.Result;

public class RecipeViewModel {
    private static final String TAG = RecipeViewModel.class.getSimpleName();

    private final IRecipesRepository recipesRepository;
    private boolean isLoading;
    private boolean firstLoading;
    private MutableLiveData<Result> recipesList;
    private MutableLiveData<Result> favoriteRecipes;

    public RecipeViewModel(IRecipesRepository iRecipesRepository) {
        this.recipesRepository = iRecipesRepository;
        this.firstLoading = true;
    }

    /**
     * Returns the LiveData object associated with the
     * recipes list to the Fragment/Activity.
     * @return The LiveData object associated with the news recipes.
     */
    public MutableLiveData<Result> getRecipes(String user_input) {
        if (recipesList == null) {
            fetchRecipes(user_input);
        }
        return recipesList;
    }

    /**
     * Returns the LiveData object associated with the
     * list of favorite recipes to the Fragment/Activity.
     * @return The LiveData object associated with the list of favorite recipes.
     */
    public MutableLiveData<Result> getFavoriteRecipes(boolean isFirstLoading) {
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

    public void fetchRecipes(String user_input) {
        recipesRepository.fetchRecipes(user_input);
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

    public MutableLiveData<Result> getNewsResponseLiveData() {
        return recipesList;
    }
}
