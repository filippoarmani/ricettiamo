package cfgmm.ricettiamo.data.repository.recipe;

import androidx.lifecycle.MutableLiveData;

import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.Result;

/**
 * Interface for Repositories that manage News objects.
 */
public interface IRecipesRepository {
    /*enum JsonParserType {
        JSON_READER,
        JSON_OBJECT_ARRAY,
        GSON,
        JSON_ERROR
    };*/

    void getRecipes(String user_input);
    //void getRecipeIngredients(int idRecipe, Recipe recipe);

    void updateRecipes(Recipe recipe);

    void getFavoriteRecipes();

    void deleteFavoriteRecipes();

    //Community Recipes
    boolean writeRecipe(Recipe recipe);
    Result getFirstRecipe(String id);
    MutableLiveData<Result> getMostRecentRecipe(String id);
    MutableLiveData<Result> getMyRecipes(String id);
    MutableLiveData<Result> getAllRecipes();
}
