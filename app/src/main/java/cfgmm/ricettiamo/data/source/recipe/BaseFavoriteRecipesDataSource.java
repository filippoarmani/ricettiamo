package cfgmm.ricettiamo.data.source.recipe;

import cfgmm.ricettiamo.model.Recipe;

/**
 * Base class to get the user favorite recipes from a remote source.
 */
public abstract class BaseFavoriteRecipesDataSource {

    protected RecipesCallback recipesCallback;

    public void setNewsCallback(RecipesCallback recipesCallback) {
        this.recipesCallback = recipesCallback;
    }

    public abstract void getFavoriteRecipes();
    public abstract void addFavoriteRecipes(Recipe news);
    public abstract void deleteFavoriteRecipes(Recipe news);
    public abstract void deleteAllFavoriteRecipes();
}
