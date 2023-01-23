package cfgmm.ricettiamo.data.source.recipe;

/**
 * Base class to get recipes from a remote source.
 */
public abstract class BaseRecipesRemoteDataSource {
    protected RecipesCallback recipesCallback;

    public void setRecipesCallback(RecipesCallback recipesCallback) {
        this.recipesCallback = recipesCallback;
    }

    public abstract void getRecipes(String user_input);
}
