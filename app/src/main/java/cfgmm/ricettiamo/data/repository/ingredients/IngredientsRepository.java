package cfgmm.ricettiamo.data.repository.ingredients;

import static cfgmm.ricettiamo.util.Constants.INGREDIENTS_API_TEST_JSON_FILE;

import android.app.Application;

import java.io.IOException;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.database.RecipesDao;
import cfgmm.ricettiamo.data.database.RecipesRoomDatabase;
import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.IngredientApiResponse;
import cfgmm.ricettiamo.util.JSONParserUtil;
import cfgmm.ricettiamo.util.ServiceLocator;

public class IngredientsRepository implements IIngredientsRepository{

    private final Application application;
    private final IngredientsResponseCallback ingredientsResponseCallback;
    private final RecipesDao recipesDao;
    private final JSONParserUtil.JsonParserType jsonParserType;

    public IngredientsRepository(Application application, IngredientsResponseCallback ingredientsResponseCallback,
                                 JSONParserUtil.JsonParserType jsonParserType) {
        this.application = application;
        this.ingredientsResponseCallback = ingredientsResponseCallback;
        RecipesRoomDatabase recipesRoomDatabase = ServiceLocator.getInstance().getRecipesDao(application);
        this.recipesDao = recipesRoomDatabase.recipesDao();;
        this.jsonParserType = jsonParserType;
    }public IngredientsRepository(Application application, IngredientsResponseCallback ingredientsResponseCallback) {
        this(application, ingredientsResponseCallback, null);
    }

    @Override
    public void getIngredientByName(String name) {
        IngredientApiResponse ingredientApiResponse = null;
        JSONParserUtil jsonParserUtil = new JSONParserUtil(application);

        switch (jsonParserType) {
            case GSON:
                try {
                    ingredientApiResponse = jsonParserUtil.parseJSONFileWithGSonIngredients(INGREDIENTS_API_TEST_JSON_FILE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case JSON_ERROR:
                ingredientsResponseCallback.onFailure(application.getString(R.string.error_retrieving_ingredient));
                break;
        }

        if (ingredientApiResponse != null) {
            saveDataInDatabase(ingredientApiResponse.getIngredients());
        } else {
            ingredientsResponseCallback.onFailure(application.getString(R.string.error_retrieving_ingredient));
        }
    }

    public void saveDataInDatabase(List<Ingredient> ingredientList) {
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

            ingredientsResponseCallback.onSuccess(ingredientList);
        });
    }

    /**
     * Gets the list of all ingredients from the local database.
     */
    @Override
    public void getAllIngredients() {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            ingredientsResponseCallback.onSuccess(recipesDao.getAllIngredients());
        });
    }

    @Override
    public void updateIngredient(Ingredient ingredient) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipesDao.updateIngredient(ingredient);
            ingredientsResponseCallback.onIngredientStatusChanged(ingredient);
        });
    }

    /**
     * Gets the list of shopping list ingredients from the local database.
     */
    @Override
    public void getShoppingListIngredients() {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            ingredientsResponseCallback.onSuccess(recipesDao.getShoppingListIngredients());
        });
    }

    /**
     * Gets the list of fridge list from the local database.
     */
    @Override
    public void getFridgeListIngredients() {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            ingredientsResponseCallback.onSuccess(recipesDao.getFridgeListIngredients());
        });
    }
}
