package cfgmm.ricettiamo.data.source.recipe;

/**
 * Base class to get recipes from a remote source.
 */
public abstract class BaseRecipesRemoteDataSource {
    protected RecipesCallback recipesCallback;

    public void setNewsCallback(RecipesCallback recipesCallback) {
        this.recipesCallback = recipesCallback;
    }

    public abstract void getRecipes();
}
