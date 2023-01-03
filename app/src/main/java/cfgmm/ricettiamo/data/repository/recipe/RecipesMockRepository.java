package cfgmm.ricettiamo.data.repository.recipe;

import android.app.Application;

import java.util.List;

import cfgmm.ricettiamo.data.database.RecipesDao;
import cfgmm.ricettiamo.data.database.RecipesRoomDatabase;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.util.JSONParserUtil;
import cfgmm.ricettiamo.util.ServiceLocator;

/* to implement*/
/**
 * Mock Repository that gets the news from the local JSON file recipesapi-test.json,
 * that is saved in "assets" folder.
 */
public class RecipesMockRepository implements IRecipesRepository {
    private final Application application;
    private final RecipesResponseCallback recipesResponseCallback;
    private final RecipesDao recipesDao;
    private final JSONParserUtil.JsonParserType jsonParserType;

    public RecipesMockRepository(Application application, RecipesResponseCallback recipesResponseCallback,
                                 RecipesResponseCallback recpiesResponseCallback, JSONParserUtil.JsonParserType jsonParserType) {
        this.application = application;
        this.recipesResponseCallback = recpiesResponseCallback;
        RecipesRoomDatabase newsRoomDatabase = ServiceLocator.getInstance().getRecipesDao(application);
        this.recipesDao = newsRoomDatabase.recipesDao();
        this.jsonParserType = jsonParserType;
    }

    @Override
    public void fetchRecipes() {

        /*NewsApiResponse newsApiResponse = null;
        JSONParserUtil jsonParserUtil = new JSONParserUtil(application);

        switch (jsonParserType) {
            case JSON_READER:
                try {
                    newsApiResponse = jsonParserUtil.parseJSONFileWithJsonReader(NEWS_API_TEST_JSON_FILE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case JSON_OBJECT_ARRAY:
                try {
                    newsApiResponse = jsonParserUtil.parseJSONFileWithJSONObjectArray(NEWS_API_TEST_JSON_FILE);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                break;
            case GSON:
                try {
                    newsApiResponse = jsonParserUtil.parseJSONFileWithGSon(NEWS_API_TEST_JSON_FILE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case JSON_ERROR:
                newsResponseCallback.onFailure(application.getString(R.string.error_retrieving_news));
                break;
        }

        if (newsApiResponse != null) {
            saveDataInDatabase(newsApiResponse.getNewsList());
        } else {
            newsResponseCallback.onFailure(application.getString(R.string.error_retrieving_recipes));
        }*/
    }

    /**
     * Update the recipes changing the status of "favorite"
     * in the local database.
     * @param recipe The news to be updated.
     */
    @Override
    public void updateRecipes(Recipe recipe) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipesDao.updateSingleFavoriteRecipes(recipe);
            recipesResponseCallback.onNewsFavoriteStatusChanged(recipe);
        });
    }

    /**
     * Gets the list of favorite recipes from the local database.
     */
    @Override
    public void getFavoriteRecipes() {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipesResponseCallback.onSuccess(recipesDao.getFavoriteRecipes(), System.currentTimeMillis());
        });
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
            recipesResponseCallback.onSuccess(recipesDao.getFavoriteRecipes(), System.currentTimeMillis());
        });
    }

    /**
     * Saves the recipes in the local database.
     * The method is executed with an ExecutorService defined in NewsRoomDatabase class
     * because the database access cannot been executed in the main thread.
     * @param recipeList the list of news to be written in the local database.
     */
    private void saveDataInDatabase(List<Recipe> recipeList) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<Recipe> allRecipes = recipesDao.getAll();

            // Checks if the news just downloaded has already been downloaded earlier
            // in order to preserve the news status (marked as favorite or not)
            for (Recipe recipe : allRecipes) {
                // This check works because Recipe and NewsSource classes have their own
                // implementation of equals(Object) and hashCode() methods
                if (recipeList.contains(recipe)) {
                    // The primary key and the favorite status is contained only in the News objects
                    // retrieved from the database, and not in the News objects downloaded from the
                    // Web Service. If the same news was already downloaded earlier, the following
                    // line of code replaces the the News object in newsList with the corresponding
                    // News object saved in the database, so that it has the primary key and the
                    // favorite status.
                    recipeList.set(recipeList.indexOf(recipe), recipe);
                }
            }

            // Writes the news in the database and gets the associated primary keys
            List<Long> insertedNewsIds = recipesDao.insertRecipeList(recipeList);
            for (int i = 0; i < recipeList.size(); i++) {
                // Adds the primary key to the corresponding object News just downloaded so that
                // if the user marks the news as favorite (and vice-versa), we can use its id
                // to know which news in the database must be marked as favorite/not favorite
                recipeList.get(i).setId(insertedNewsIds.get(i));
            }

            recipesResponseCallback.onSuccess(recipeList, System.currentTimeMillis());
        });
    }

    /**
     * Gets the recipes from the local database.
     * The method is executed with an ExecutorService defined in NewsRoomDatabase class
     * because the database access cannot been executed in the main thread.
     */
    private void readDataFromDatabase(long lastUpdate) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipesResponseCallback.onSuccess(recipesDao.getAll(), lastUpdate);
        });
    }
}
