package cfgmm.ricettiamo.viewmodel;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import cfgmm.ricettiamo.R;
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

    private MutableLiveData<Result> myRecipes;
    private MutableLiveData<Result> allRecipes;
    private MutableLiveData<Result> mostRecentRecipe;
    private Result firstRecipe;

    public RecipeViewModel(IRecipesRepository iRecipesRepository) {
        this.recipesRepository = iRecipesRepository;
        this.firstLoading = true;

        firstRecipe = new Result.RecipeDatabaseResponseSuccess(null);
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
    public void deleteAllFavoriteRecipes() {
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

    //Community Recipes
    public Result writeRecipe(Recipe recipe) {
        return recipesRepository.writeRecipe(recipe);
    }

    public MutableLiveData<Result> getMyRecipes(String id) {
        if(myRecipes == null)
            myRecipes = recipesRepository.getMyRecipes(id);

        return myRecipes;
    }

    public Result uploadPhoto(Uri uri) {
        return recipesRepository.uploadPhoto(uri);
    }

    public MutableLiveData<Result> getAllRecipes() {
        if(allRecipes == null)
            allRecipes = recipesRepository.getAllRecipes();

        return allRecipes;
    }

    public MutableLiveData<Result> getMostRecentRecipe(String id) {
        if(mostRecentRecipe == null)
            mostRecentRecipe = recipesRepository.getMostRecentRecipe(id);

        return mostRecentRecipe;
    }

    public Result getFirstRecipe(String id) {
        if(!firstRecipe.isSuccess() || !(((Result.RecipeDatabaseResponseSuccess) firstRecipe).getData() == null)) {
            firstRecipe = recipesRepository.getFirstRecipe(id);
        }

        return firstRecipe;
    }
}
