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

    void onSuccessGetMyRecipesScore(List<Recipe> recipes);

    void onFailureGetMyRecipesScore(int writeDatabase_error);

    /*void onSuccessGetFavoriteRecipesId(List<String> favoriteRecipes);

    void onFailureGetFavoriteRecipesId(int writeDatabase_error);

    void onSuccessAddFavoriteRecipe(Object o);

    void onFailureAddFavoriteRecipe(int writeDatabase_error);

    void onSuccessDeleteFavoriteRecipe(Object o);

    void onFailureDeleteFavoriteRecipe(int writeDatabase_error);

    void onSuccessDeleteAllFavoriteRecipes(Object o);

    void onFailureDeleteAllFavoriteRecipes(int writeDatabase_error);
    */
}
