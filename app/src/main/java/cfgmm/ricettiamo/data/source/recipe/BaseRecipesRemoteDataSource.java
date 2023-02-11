package cfgmm.ricettiamo.data.source.recipe;

import cfgmm.ricettiamo.data.repository.recipe.RecipesResponseCallback;

public abstract class BaseRecipesRemoteDataSource {

    RecipesResponseCallback recipesCallback;

    public void setCallBack(RecipesResponseCallback recipesCallback) {
        this.recipesCallback = recipesCallback;
    }

    public abstract void getRecipes(String user_input);
}
