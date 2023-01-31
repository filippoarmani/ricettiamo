package cfgmm.ricettiamo.data.repository.recipe;

import cfgmm.ricettiamo.model.Recipe;

/**
 * Interface for Repositories that manage News objects.
 */
public interface IRecipesRepository {
    /*enum JsonParserType {
        JSON_READER,
        JSON_OBJECT_ARRAY,
        GSON,
        JSON_ERROR
    };*/

    void getRecipes(String user_input);

    void updateRecipes(Recipe recipe);

    void getFavoriteRecipes();

    void deleteFavoriteRecipes();
}
