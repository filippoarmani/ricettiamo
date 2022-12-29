package cfgmm.ricettiamo.data.repository.recipe;

import cfgmm.ricettiamo.model.Recipe;

/**
 * Interface for Repositories that manage News objects.
 */
public interface IRecipesRepository {

    void fetchRecipes();

    void updateRecipes(Recipe recipe);

    void getFavoriteRecipes();

    void deleteFavoriteRecipes();
}
