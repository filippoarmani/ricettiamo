package cfgmm.ricettiamo.data.source.recipe;

import static cfgmm.ricettiamo.util.Constants.UNEXPECTED_ERROR;

import java.util.List;

import cfgmm.ricettiamo.data.database.RecipesDao;
import cfgmm.ricettiamo.data.database.RecipesRoomDatabase;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.RecipeApiResponse;
//TODO
/**
 * Class to get reciepes from local database using Room.
 */
public class RecipesLocalDataSource extends BaseRecipesLocalDataSource {

    private final RecipesDao recipesDao;
    /*private final SharedPreferencesUtil sharedPreferencesUtil;
    private final DataEncryptionUtil dataEncryptionUtil;*/

    public RecipesLocalDataSource(RecipesRoomDatabase recipesRoomDatabase/*,
                                  SharedPreferencesUtil sharedPreferencesUtil,
                                  DataEncryptionUtil dataEncryptionUtil*/
    ) {
        this.recipesDao = recipesRoomDatabase.recipesDao();
        /*this.sharedPreferencesUtil = sharedPreferencesUtil;
        this.dataEncryptionUtil = dataEncryptionUtil;*/
    }

    /**
     * Gets the news from the local database.
     * The method is executed with an ExecutorService defined in NewsRoomDatabase class
     * because the database access cannot been executed in the main thread.
     */
    @Override
    public void getRecipes() {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            //TODO Fix this instruction
            RecipeApiResponse recipeApiResponse = new RecipeApiResponse();
            recipeApiResponse.setListRecipes(recipesDao.getAll());
            recipesCallback.onSuccessFromLocal(recipeApiResponse);
        });
    }

    @Override
    public void getFavoriteRecipes() {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Recipe> favoriteRecipes = recipesDao.getFavoriteRecipes();
            recipesCallback.onRecipesFavoriteStatusChanged(favoriteRecipes);
        });
    }

    @Override
    public void updateRecipes(Recipe recipe) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            if (recipe != null) {
                int rowUpdatedCounter = recipesDao.updateSingleFavoriteRecipes(recipe);
                // It means that the update succeeded because only one row had to be updated
                if (rowUpdatedCounter == 1) {
                    Recipe updatedNews = recipesDao.getRecipes(recipe.getId());
                    recipesCallback.onRecipesFavoriteStatusChanged(updatedNews, recipesDao.getFavoriteRecipes());
                } else {
                    recipesCallback.onFailureFromLocal(new Exception(UNEXPECTED_ERROR));
                }
            } else {
                // When the user deleted all favorite news from remote
                //TODO Check if it works fine and there are not drawbacks
                List<Recipe> allNews = recipesDao.getAll();
                for (Recipe n : allNews) {
                    recipesDao.updateSingleFavoriteRecipes(n);
                }
            }
        });
    }

    @Override
    public void deleteFavoriteRecipes() {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Recipe> favoriteNews = recipesDao.getFavoriteRecipes();
            for (Recipe news : favoriteNews) {
                news.setIsFavorite(false);
            }
            int updatedRowsNumber = recipesDao.updateListFavoriteRecipes(favoriteNews);

            // It means that the update succeeded because the number of updated rows is
            // equal to the number of the original favorite news
            if (updatedRowsNumber == favoriteNews.size()) {
                recipesCallback.onDeleteFavoriteNewsSuccess(favoriteNews);
            } else {
                recipesCallback.onFailureFromLocal(new Exception(UNEXPECTED_ERROR));
            }
        });
    }

    /**
     * Saves the news in the local database.
     * The method is executed with an ExecutorService defined in NewsRoomDatabase class
     * because the database access cannot been executed in the main thread.
     * @param recipeApiResponse the list of news to be written in the local database.
     */
    @Override
    public void insertRecipes(RecipeApiResponse recipeApiResponse) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            // Reads the news from the database
            List<Recipe> allNews = recipesDao.getAll();
            List<Recipe> newsList = recipeApiResponse.getListRecipes();

            if (newsList != null) {

                // Checks if the news just downloaded has already been downloaded earlier
                // in order to preserve the news status (marked as favorite or not)
                for (Recipe news : allNews) {
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
                List<Long> insertedNewsIds = recipesDao.insertRecipeList(newsList);
                for (int i = 0; i < newsList.size(); i++) {
                    // Adds the primary key to the corresponding object News just downloaded so that
                    // if the user marks the news as favorite (and vice-versa), we can use its id
                    // to know which news in the database must be marked as favorite/not favorite
                    newsList.get(i).setId(insertedNewsIds.get(i));
                }

                /*sharedPreferencesUtil.writeStringData(SHARED_PREFERENCES_FILE_NAME,
                        LAST_UPDATE, String.valueOf(System.currentTimeMillis()));*/

                recipesCallback.onSuccessFromLocal(recipeApiResponse);
            }
        });
    }

    /**
     * Saves the news in the local database.
     * The method is executed with an ExecutorService defined in NewsRoomDatabase class
     * because the database access cannot been executed in the main thread.
     * @param recipeList the list of news to be written in the local database.
     */
    @Override
    public void insertRecipes(List<Recipe> recipeList) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            if (recipeList != null) {

                // Reads the news from the database
                List<Recipe> allNews = recipesDao.getAll();

                // Checks if the news just downloaded has already been downloaded earlier
                // in order to preserve the news status (marked as favorite or not)
                for (Recipe news : allNews) {
                    // This check works because News and NewsSource classes have their own
                    // implementation of equals(Object) and hashCode() methods
                    if (recipeList.contains(news)) {
                        // The primary key and the favorite status is contained only in the News objects
                        // retrieved from the database, and not in the News objects downloaded from the
                        // Web Service. If the same news was already downloaded earlier, the following
                        // line of code replaces the the News object in newsList with the corresponding
                        // News object saved in the database, so that it has the primary key and the
                        // favorite status.
                        recipeList.set(recipeList.indexOf(news), news);
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

                RecipeApiResponse newsApiResponse = new RecipeApiResponse();
                newsApiResponse.setListRecipes(recipeList);
                recipesCallback.onSuccessSynchronization();
            }
        });
    }

    @Override
    public void deleteAll() {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            int newsCounter = recipesDao.getAll().size();
            int newsDeletedNews = recipesDao.deleteAll();

            // It means that everything has been deleted
            if (newsCounter == newsDeletedNews) {
                /*sharedPreferencesUtil.deleteAll(SHARED_PREFERENCES_FILE_NAME);
                dataEncryptionUtil.deleteAll(ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ENCRYPTED_DATA_FILE_NAME);*/
                recipesCallback.onSuccessDeletion();
            }
        });
    }
}
