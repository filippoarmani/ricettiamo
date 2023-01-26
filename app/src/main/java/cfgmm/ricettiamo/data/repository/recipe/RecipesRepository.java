package cfgmm.ricettiamo.data.repository.recipe;

import android.app.Application;
import androidx.annotation.NonNull;

import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.database.RecipesDao;
import cfgmm.ricettiamo.data.database.RecipesRoomDatabase;
import cfgmm.ricettiamo.data.service.RecipeApiService;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.RecipeApiResponse;
import cfgmm.ricettiamo.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void getRecipes(String user_input) {

        // It gets the recipies from the Web Service
        Call<RecipeApiResponse> recipeResponseCall = recipeApiService.getRecipes(user_input,
                application.getString(R.string.recipes_api_key));

        recipeResponseCall.enqueue(new Callback<RecipeApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeApiResponse> call,
                                   @NonNull Response<RecipeApiResponse> response) {

                if (response.body() != null && response.isSuccessful() &&
                        !response.body().getStatus().equals("error")) {
                    List<Recipe> recipesList = response.body().getListRecipes();
                    saveDataInDatabase(recipesList);
                } else {
                    recipesResponseCallback.onFailure(application.getString(R.string.error_retrieving_recipe));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecipeApiResponse> call, @NonNull Throwable t) {
                recipesResponseCallback.onFailure(t.getMessage());
            }
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
     * Update the recipes changing the status of "favorite"
     * in the local database.
     * @param recipe The news to be updated.
     */
    @Override
    public void updateRecipes(Recipe recipe) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipesDao.updateSingleFavoriteRecipes(recipe);
            recipesResponseCallback.onRecipesFavoriteStatusChanged(recipe);
        });
    }

    /**
     * Gets the list of favorite recipes from the local database.
     */
    @Override
    public void getFavoriteRecipes(Boolean firstLoading) {
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
