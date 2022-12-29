package cfgmm.ricettiamo.data.repository.recipe;

import android.app.Application;

import java.util.List;

import cfgmm.ricettiamo.data.service.RecipeApiService;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.util.ServiceLocator;

/**
 * Repository to get the news using the API
 * provided by NewsApi.org (https://newsapi.org).
 */
public class RecipesRepository implements IRecipesRepository{
    private static final String TAG = RecipesRepository.class.getSimpleName();

    private final Application application;
    private final RecipeApiService recipeApiService;
    //private final NewsDao newsDao;
    private final RecipesResponseCallback recipesResponseCallback;

    public RecipesRepository(Application application, RecipesResponseCallback recipesResponseCallback) {
        this.application = application;
        this.recipeApiService = ServiceLocator.getInstance().getRecipeApiService();
        //NewsRoomDatabase newsRoomDatabase = ServiceLocator.getInstance().getNewsDao(application);
        //this.newsDao = newsRoomDatabase.newsDao();
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
     * Marks the favorite news as not favorite.
     */
    @Override
    public void deleteFavoriteRecipes() {
        /*NewsRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Recipe> favoriteNews = newsDao.getFavoriteRecipes();
            for (Recipe recipe : favoriteNews) {
                recipe.setFavorite(false);
            }
            newsDao.updateListFavoriteRecpes(favoriteNews);
            newsResponseCallback.onSuccess(newsDao.getFavoriteNews(), System.currentTimeMillis());
        });*/
    }

    /**
     * Update the news changing the status of "favorite"
     * in the local database.
     * @param recipe The news to be updated.
     */
    @Override
    public void updateRecipes(Recipe recipe) {
        /*NewsRoomDatabase.databaseWriteExecutor.execute(() -> {
            newsDao.updateSingleFavoriteNews(news);
            newsResponseCallback.onNewsFavoriteStatusChanged(news);
        });*/
    }

    /**
     * Gets the list of favorite news from the local database.
     */
    @Override
    public void getFavoriteRecipes() {
        /*NewsRoomDatabase.databaseWriteExecutor.execute(() -> {
            newsResponseCallback.onSuccess(newsDao.getFavoriteNews(), System.currentTimeMillis());
        });*/
    }

    /**
     * Saves the news in the local database.
     * The method is executed with an ExecutorService defined in NewsRoomDatabase class
     * because the database access cannot been executed in the main thread.
     * @param newsList the list of news to be written in the local database.
     */
    private void saveDataInDatabase(List<Recipe> newsList) {
        /*NewsRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<News> allNews = newsDao.getAll();

            // Checks if the news just downloaded has already been downloaded earlier
            // in order to preserve the news status (marked as favorite or not)
            for (News news : allNews) {
                // This check works because News and NewsSource classes have their own
                // implementation of equals(Object) and hashCode() methods
                if (newsList.contains(news)) {
                    // The primary key and the favorite status is contained only in the News objects
                    // retrieved from the database, and not in the News objects downloaded from the
                    // Web Service. If the same news was already downloaded earlier, the following
                    // line of code replaces the the News object in newsList with the corresponding
                    // News object saved in the database, so that it has the primary key and the
                    // favorite status.
                    newsList.set(newsList.indexOf(news), news);
                }
            }

            // Writes the news in the database and gets the associated primary keys
            List<Long> insertedNewsIds = newsDao.insertNewsList(newsList);
            for (int i = 0; i < newsList.size(); i++) {
                // Adds the primary key to the corresponding object News just downloaded so that
                // if the user marks the news as favorite (and vice-versa), we can use its id
                // to know which news in the database must be marked as favorite/not favorite
                newsList.get(i).setId(insertedNewsIds.get(i));
            }

            newsResponseCallback.onSuccess(newsList, System.currentTimeMillis());
        });*/
    }

    /**
     * Gets the news from the local database.
     * The method is executed with an ExecutorService defined in NewsRoomDatabase class
     * because the database access cannot been executed in the main thread.
     */
    private void readDataFromDatabase(long lastUpdate) {
        /*NewsRoomDatabase.databaseWriteExecutor.execute(() -> {
            newsResponseCallback.onSuccess(newsDao.getAll(), lastUpdate);
        });*/
    }
}
