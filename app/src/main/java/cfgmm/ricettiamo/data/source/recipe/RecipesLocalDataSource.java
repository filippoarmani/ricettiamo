package cfgmm.ricettiamo.data.source.recipe;

import java.util.List;

import cfgmm.ricettiamo.data.database.RecipesDao;
import cfgmm.ricettiamo.data.database.RecipesRoomDatabase;
import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.Recipe;

public class RecipesLocalDataSource extends BaseRecipesLocalDataSource{

    private final RecipesDao recipesDao;

    public RecipesLocalDataSource(RecipesRoomDatabase recipesRoomDatabase) {
        recipesDao = recipesRoomDatabase.recipesDao();
    }

    /**
     * Marks the favorite recipes as not favorite.
     */
    @Override
    public void deleteFavoriteRecipes() {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Recipe> favoriteRecipes = recipesDao.getFavoriteRecipes();
            for (Recipe recipe : favoriteRecipes) {
                recipe.setIsFavorite(false);
            }
            recipesDao.updateListFavoriteRecipes(favoriteRecipes);
            recipesCallback.onSuccessFavorite(recipesDao.getFavoriteRecipes());
        });
    }

    /**
     * Update the recipes changing the status of "favorite"
     * in the local database.
     * @param recipe The recipe to be updated.
     */
    @Override
    public void updateRecipes(Recipe recipe) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipesDao.updateSingleFavoriteRecipes(recipe);
            recipesCallback.onRecipesFavoriteStatusChanged(recipe);
        });
    }

    /**
     * Gets the list of favorite recipes from the local database.
     */
    @Override
    public void getFavoriteRecipes() {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipesCallback.onSuccessFavorite(recipesDao.getFavoriteRecipes());
        });
    }

    /**
     * Saves the recipes in the local database.
     * The method is executed with an ExecutorService defined in RecipesRoomDatabase class
     * because the database access cannot been executed in the main thread.
     * @param recipeList the list of recipes to be written in the local database.
     */
    @Override
    public void saveDataInDatabase(List<Recipe> recipeList, List<Ingredient> ingredientList) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Recipe> allRecipe = recipesDao.getAll();
            List<Ingredient> allIngredients = recipesDao.getAllIngredients();

            for (Recipe recipe : allRecipe) {
                if (recipeList.contains(recipe)) {
                    recipeList.set(recipeList.indexOf(recipe), recipe);
                }
            }
            for (Ingredient ingredient : allIngredients) {
                if (ingredientList.contains(ingredient)) {
                    ingredientList.set(ingredientList.indexOf(ingredient), ingredient);
                }
            }

            List<Long> insertedRecipeIds = recipesDao.insertRecipeList(recipeList);
            for (int i = 0; i < recipeList.size(); i++) {
                recipeList.get(i).setId(insertedRecipeIds.get(i));
            }
            List<Long> insertedIngredientsIds = recipesDao.insertIngredientList(ingredientList);
            for (int i = 0; i < ingredientList.size(); i++) {
                ingredientList.get(i).setId(insertedIngredientsIds.get(i));
            }

            recipesCallback.onSuccessFromDatabase(recipeList);
        });
    }

    /**
     * Gets the recipes from the local database.
     * The method is executed with an ExecutorService defined in RecipesRoomDatabase class
     * because the database access cannot been executed in the main thread.
     */
    private void readDataFromDatabase() {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipesCallback.onSuccessFromDatabase(recipesDao.getAll());
        });
    }
}
