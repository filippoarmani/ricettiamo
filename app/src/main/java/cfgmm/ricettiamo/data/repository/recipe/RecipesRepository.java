package cfgmm.ricettiamo.data.repository.recipe;

import android.app.Application;
import androidx.annotation.NonNull;

import java.util.List;

import cfgmm.ricettiamo.data.database.RecipesDao;
import cfgmm.ricettiamo.data.database.RecipesRoomDatabase;
import cfgmm.ricettiamo.data.service.RecipeApiService;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.util.ServiceLocator;

/**
 * Repository to get the recipes using the API
 * provided by spoonacular.com (https://spoonacular.com).
 */
public class RecipesRepository implements IRecipesRepository{
    private static final String TAG = RecipesRepository.class.getSimpleName();

    private final Application application;
    private final RecipeApiService recipeApiService;
    private final RecipesDao recipesDao;
    private final RecipesResponseCallback recipesResponseCallback;

    public RecipesRepository(Application application, RecipesResponseCallback recipesResponseCallback) {
        this.application = application;
        this.recipeApiService = ServiceLocator.getInstance().getRecipeApiService();
        RecipesRoomDatabase newsRoomDatabase = ServiceLocator.getInstance().getRecipesDao(application);
        this.recipesDao = newsRoomDatabase.recipesDao();
        this.recipesResponseCallback = recipesResponseCallback;
    }

    @Override
    public void fetchRecipes() {

        /*long currentTime = System.currentTimeMillis();

        // It gets the news from the Web Service if the last download
        // of the news has been performed more than FRESH_TIMEOUT value ago
        if (currentTime - lastUpdate > FRESH_TIMEOUT) {
            Call<NewsApiResponse> newsResponseCall = newsApiService.getNews(country,
                    TOP_HEADLINES_PAGE_SIZE_VALUE, page, application.getString(R.string.news_api_key));

            newsResponseCall.enqueue(new Callback<NewsApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<NewsApiResponse> call,
                                       @NonNull Response<NewsApiResponse> response) {

                    if (response.body() != null && response.isSuccessful() &&
                            !response.body().getStatus().equals("error")) {
                        List<News> newsList = response.body().getNewsList();
                        saveDataInDatabase(newsList);
                    } else {
                        newsResponseCallback.onFailure(application.getString(R.string.error_retrieving_news));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NewsApiResponse> call, @NonNull Throwable t) {
                    newsResponseCallback.onFailure(t.getMessage());
                }
            });
        } else {
            Log.d(TAG, application.getString(R.string.data_read_from_local_database));
            readDataFromDatabase(lastUpdate);
        }*/
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
     * Saves the recipes in the local database.
     * The method is executed with an ExecutorService defined in NewsRoomDatabase class
     * because the database access cannot been executed in the main thread.
     * @param recipeList the list of recipes to be written in the local database.
     */
    private void saveDataInDatabase(List<Recipe> recipeList) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<Recipe> allRecipe = recipesDao.getAll();

            // Checks if the news just downloaded has already been downloaded earlier
            // in order to preserve the news status (marked as favorite or not)
            for (Recipe recipe : allRecipe) {
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
