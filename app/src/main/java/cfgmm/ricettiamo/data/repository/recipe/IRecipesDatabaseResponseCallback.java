package cfgmm.ricettiamo.data.repository.recipe;

import java.util.List;

import cfgmm.ricettiamo.model.Recipe;

public interface IRecipesDatabaseResponseCallback {
    void onSuccessWriteDatabase(Recipe savedRecipe);

    void onFailureWriteDatabase(int writeDatabase_error);

    void onSuccessGetMyRecipes(List<Recipe> recipes);

    void onFailureGetMyRecipes(int writeDatabase_error);

    void onSuccessGetAllRecipes(List<Recipe> recipes);

    void onFailureGetAllRecipes(int writeDatabase_error);
}
