package cfgmm.ricettiamo.data.source.recipe;

import java.util.List;

import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.RecipeApiResponse;

/**
 * Base class to get recipes from a local source.
 */
public abstract class BaseRecipesLocalDataSource {

    protected RecipesCallback recipesCallback;

    public void setRecipesCallback(RecipesCallback recipesCallback) {
        this.recipesCallback= recipesCallback;
    }

    public abstract void getRecipes();
    public abstract void getFavoriteRecipes();
    public abstract void updateRecipes(Recipe recipe);
    public abstract void deleteFavoriteRecipes();
    public abstract void insertRecipes(RecipeApiResponse newsApiResponse);
    public abstract void insertRecipes(List<Recipe> recipeList);
    public abstract void deleteAll();
}
