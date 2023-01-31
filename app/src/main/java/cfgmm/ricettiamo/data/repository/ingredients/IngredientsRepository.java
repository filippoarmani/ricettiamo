package cfgmm.ricettiamo.data.repository.ingredients;

import static cfgmm.ricettiamo.util.Constants.INGREDIENTS_API_TEST_JSON_FILE;

import android.app.Application;

import org.json.JSONException;

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
                                 RecipesDao recipesDao, JSONParserUtil.JsonParserType jsonParserType) {
        this.application = application;
        this.ingredientsResponseCallback = ingredientsResponseCallback;
        RecipesRoomDatabase newsRoomDatabase = ServiceLocator.getInstance().getRecipesDao(application);
        this.recipesDao = recipesDao;
        this.jsonParserType = jsonParserType;
    }

    @Override
    public void getIngredientByName(String name) {
        IngredientApiResponse ingredientApiResponse = null;
        JSONParserUtil jsonParserUtil = new JSONParserUtil(application);

        switch (jsonParserType) {
            case JSON_READER:
                try {
                    ingredientApiResponse = jsonParserUtil.parseJSONFileWithJsonReaderIngredients(INGREDIENTS_API_TEST_JSON_FILE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case JSON_OBJECT_ARRAY:
                try {
                    ingredientApiResponse = jsonParserUtil.parseJSONFileWithJSONObjectArrayIngredients(INGREDIENTS_API_TEST_JSON_FILE);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                break;
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
            saveDataInDatabase(ingredientApiResponse.getArticles());
        } else {
            ingredientsResponseCallback.onFailure(application.getString(R.string.error_retrieving_ingredient));
        }
    }

    @Override
    public void getIngredientById(int id) {

    }

    private void saveDataInDatabase(List<Ingredient> ingredientList) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<Ingredient> allIngredients = recipesDao.getAllIngredients();

            // Checks if the news just downloaded has already been downloaded earlier
            // in order to preserve the news status (marked as favorite or not)
            for (Ingredient ingredient : allIngredients) {
                // This check works because News and NewsSource classes have their own
                // implementation of equals(Object) and hashCode() methods
                if (ingredientList.contains(ingredient)) {
                    // The primary key and the favorite status is contained only in the News objects
                    // retrieved from the database, and not in the News objects downloaded from the
                    // Web Service. If the same news was already downloaded earlier, the following
                    // line of code replaces the the News object in newsList with the corresponding
                    // News object saved in the database, so that it has the primary key and the
                    // favorite status.
                    ingredientList.set(ingredientList.indexOf(ingredient), ingredient);
                }
            }

            // Writes the news in the database and gets the associated primary keys
            List<Long> insertedNewsIds = recipesDao.insertIngredientList(ingredientList);
            for (int i = 0; i < ingredientList.size(); i++) {
                // Adds the primary key to the corresponding object News just downloaded so that
                // if the user marks the news as favorite (and vice-versa), we can use its id
                // to know which news in the database must be marked as favorite/not favorite
                ingredientList.get(i).setId(insertedNewsIds.get(i));
            }

            ingredientsResponseCallback.onSuccess(ingredientList);
        });
    }
}
