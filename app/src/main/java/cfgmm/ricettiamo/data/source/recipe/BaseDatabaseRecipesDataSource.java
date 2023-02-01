package cfgmm.ricettiamo.data.source.recipe;

import cfgmm.ricettiamo.data.repository.recipe.IRecipesResponseCallback;
import cfgmm.ricettiamo.model.Recipe;

public abstract class BaseDatabaseRecipesDataSource {

    IRecipesResponseCallback recipesResponseCallback;

    public void setCallBack(IRecipesResponseCallback recipesResponseCallback) {
        this.recipesResponseCallback = recipesResponseCallback;
    }

    public abstract void writeRecipe(Recipe recipe);

    public abstract void getFirstRecipe(String author);

    public abstract void getMostRecentRecipe(String author);

    public abstract void getTopTen(String author);

    /*public abstract void getFavoriteRecipesId(String user);

    public abstract void addFavoriteRecipe(String user, String recipe);

    public abstract void deleteFavoriteRecipe(String user, String recipe);

    public abstract void deleteAllFavoriteRecipes(String user);
     */
}
