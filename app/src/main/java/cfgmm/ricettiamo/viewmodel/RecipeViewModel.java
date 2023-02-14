package cfgmm.ricettiamo.viewmodel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import cfgmm.ricettiamo.data.repository.recipe.IRecipesRepository;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.Result;

public class RecipeViewModel extends ViewModel {
    private static final String TAG = RecipeViewModel.class.getSimpleName();
    private final IRecipesRepository recipesRepository;

    private MutableLiveData<Result> favoriteRecipes;
    private MutableLiveData<Result> myRecipes;
    private MutableLiveData<Result> allRecipes;

    public RecipeViewModel(IRecipesRepository iRecipesRepository) {
        this.recipesRepository = iRecipesRepository;
    }

    /**
     * Returns the list associated with the
     * recipes list to the Fragment/Activity.
     * @return The list associated with the recipes.
     */
    public MutableLiveData<Result> getSearchRecipes(String user_input) {
        return recipesRepository.getRecipes(user_input);
    }

    /**
     * Returns the list associated with the
     * favourite recipes list to the Fragment/Activity.
     * @return The list associated with the recipes.
     */
    public MutableLiveData<Result> getFavoriteRecipes() {
        if (favoriteRecipes == null) {
            favoriteRecipes = recipesRepository.getFavoriteRecipes();
        }
        return favoriteRecipes;
    }

    public void insertRecipe(Recipe recipe) {
        allRecipes = recipesRepository.insertRecipe(recipe);
    }

    /**
     * Updates the recipes status.
     * @param recipe The recipes to be updated.
     */
    public void updateRecipes(Recipe recipe) {
        favoriteRecipes = recipesRepository.updateRecipes(recipe);
    }

    //Community Recipes
    public MutableLiveData<Result> writeRecipe(Recipe recipe) {
        return recipesRepository.writeRecipe(recipe);
    }

    public MutableLiveData<Result> getMyRecipes(String id) {
        if(myRecipes == null)
            myRecipes = recipesRepository.getMyRecipes(id);

        return myRecipes;
    }

    public MutableLiveData<Result> uploadPhoto(Uri uri) {
        return recipesRepository.uploadPhoto(uri);
    }

    public MutableLiveData<Result> getAllRecipes() {
        if(this.allRecipes == null)
            this.allRecipes = recipesRepository.getAllRecipes();

        return allRecipes;
    }
}
