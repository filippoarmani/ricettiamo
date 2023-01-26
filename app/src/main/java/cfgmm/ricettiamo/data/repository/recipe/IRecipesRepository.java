package cfgmm.ricettiamo.data.repository.recipe;

import cfgmm.ricettiamo.model.Recipe;

/**
 * Interface for Repositories that manage News objects.
 */
public interface IRecipesRepository {

    void getRecipes(String user_input);

    void updateRecipes(Recipe recipe);

    void getFavoriteRecipes(Boolean firstLoading);

    void deleteFavoriteRecipes();
}
