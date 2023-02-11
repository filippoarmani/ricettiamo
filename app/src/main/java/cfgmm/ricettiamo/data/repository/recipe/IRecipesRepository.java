package cfgmm.ricettiamo.data.repository.recipe;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.Result;

/**
 * Interface for Repositories that manage News objects.
 */
public interface IRecipesRepository {

    Result getRecipes(String user_input);

    MutableLiveData<Result> updateRecipes(Recipe recipe);

    MutableLiveData<Result> getFavoriteRecipes();

    void deleteFavoriteRecipes();

    //Community Recipes
    Result writeRecipe(Recipe recipe);
    Result uploadPhoto(Uri photo);
    MutableLiveData<Result> getMyRecipes(String id);
    MutableLiveData<Result> getAllRecipes();
}
