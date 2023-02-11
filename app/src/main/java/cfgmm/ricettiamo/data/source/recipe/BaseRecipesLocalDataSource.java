package cfgmm.ricettiamo.data.source.recipe;

import java.util.List;

import cfgmm.ricettiamo.data.repository.recipe.RecipesResponseCallback;
import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.Recipe;

public abstract class BaseRecipesLocalDataSource {

    RecipesResponseCallback recipesCallback;

    public void setCallBack(RecipesResponseCallback recipesCallback) {
        this.recipesCallback = recipesCallback;
    }

    public abstract void deleteFavoriteRecipes();

    public abstract void updateRecipes(Recipe recipe);

    public abstract void getFavoriteRecipes();

    public abstract void saveDataInDatabase(List<Recipe> recipeList, List<Ingredient> ingredientList);
}
