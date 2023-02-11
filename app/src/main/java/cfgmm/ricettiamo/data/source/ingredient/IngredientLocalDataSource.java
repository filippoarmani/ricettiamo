package cfgmm.ricettiamo.data.source.ingredient;

import android.util.Log;

import java.util.List;

import cfgmm.ricettiamo.data.database.RecipesDao;
import cfgmm.ricettiamo.data.database.RecipesRoomDatabase;
import cfgmm.ricettiamo.model.Ingredient;

public class IngredientLocalDataSource extends BaseIngredientLocalDataSource {

    private static final String TAG = IngredientLocalDataSource.class.getSimpleName();

    private final RecipesDao recipesDao;

    public IngredientLocalDataSource(RecipesRoomDatabase recipesRoomDatabase) {
        recipesDao = recipesRoomDatabase.recipesDao();
    }

    @Override
    public void insertIngredient(Ingredient ingredient) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipesDao.insertIngredient(ingredient);
            Log.d(TAG, "insertIngredient");
            ingredientsCallback.onIngredientStatusChanged(ingredient, false, true);
        });
    }

    @Override
    public void deleteIngredient(Ingredient ingredient) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipesDao.deleteIngredient(ingredient);
            Log.d(TAG, "deleteIngredient");
            ingredientsCallback.onIngredientStatusChanged(ingredient, true, false);
        });
    }

    @Override
    protected void saveDataInDatabase(List<Ingredient> ingredientList) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Ingredient> allIngredients = recipesDao.getAllIngredients();

            for (Ingredient ingredient : allIngredients) {
                if (ingredientList.contains(ingredient)) {
                    ingredientList.set(ingredientList.indexOf(ingredient), ingredient);
                }
            }

            List<Long> insertedIngredientsIds = recipesDao.insertIngredientList(ingredientList);
            for (int i = 0; i < ingredientList.size(); i++) {
                ingredientList.get(i).setId(insertedIngredientsIds.get(i));
            }
            Log.d(TAG, "saveDataInDatabase");
            ingredientsCallback.onSuccessFromLocal(ingredientList);
        });
    }

    /**
     * Gets the list of all ingredients from the local database.
     */
    @Override
    public void getAllIngredients() {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            Log.d(TAG, "getAllIngredients");
            ingredientsCallback.onSuccessFromLocal(recipesDao.getAllIngredients());
        });
    }

    @Override
    public void updateIngredient(Ingredient ingredient) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipesDao.updateIngredient(ingredient);
            Log.d(TAG, "updateIngredient");
            ingredientsCallback.onIngredientStatusChanged(ingredient, false, false);
        });
    }

    /**
     * Gets the list of shopping list ingredients from the local database.
     */
    @Override
    public void getShoppingListIngredients() {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            Log.d(TAG, "getShoppingListIngredients");
            ingredientsCallback.onSuccessFromLocal(recipesDao.getShoppingListIngredients());
        });
    }

    /**
     * Gets the list of fridge list from the local database.
     */
    @Override
    public void getFridgeListIngredients() {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            Log.d(TAG, "getFridgeListIngredients");
            ingredientsCallback.onSuccessFromLocal(recipesDao.getFridgeListIngredients());
        });
    }
}
