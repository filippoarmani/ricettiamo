package cfgmm.ricettiamo.data.repository.recipe;

import java.util.List;

import cfgmm.ricettiamo.model.Recipe;

public interface IRecipesResponseCallback {
    void onSuccessWriteDatabase();
    void onFailureWriteDatabase(int writeDatabase_error);


    void onSuccessGetFirstRecipe(Recipe firstRecipe);

    void onFailureGetFirstRecipe(int writeDatabase_error);

    void onSuccessGetMostRecentRecipe(Recipe mostRecentRecipe);

    void onFailureGetMostRecentRecipe(int writeDatabase_error);

    void onSuccessGetTopTen(List<Recipe> recipes);

    void onFailureGetTopTen(int writeDatabase_error);

    void onSuccessGetFavoriteRecipesId(List<String> favoriteRecipes);

    void onFailureGetFavoriteRecipesId(int writeDatabase_error);

    void onSuccessAddFavoriteRecipe(Object o);

    void onFailureAddFavoriteRecipe(int writeDatabase_error);

    void onSuccessDeleteFavoriteRecipe(Object o);

    void onFailureDeleteFavoriteRecipe(int writeDatabase_error);

    void onSuccessDeleteAllFavoriteRecipes(Object o);

    void onFailureDeleteAllFavoriteRecipes(int writeDatabase_error);
}
