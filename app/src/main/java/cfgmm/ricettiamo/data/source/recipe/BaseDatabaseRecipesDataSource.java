package cfgmm.ricettiamo.data.source.recipe;

import cfgmm.ricettiamo.data.repository.recipe.IRecipesDatabaseResponseCallback;
import cfgmm.ricettiamo.model.Recipe;

public abstract class BaseDatabaseRecipesDataSource {

    IRecipesDatabaseResponseCallback recipesResponseCallback;

    public void setCallBack(IRecipesDatabaseResponseCallback recipesResponseCallback) {
        this.recipesResponseCallback = recipesResponseCallback;
    }

    public abstract void writeRecipe(Recipe recipe);

    public abstract void getAllRecipes();

    public abstract void getMyRecipes(String author);

}
