package cfgmm.ricettiamo.data.repository.recipe;

import static cfgmm.ricettiamo.util.Constants.ADD_RECIPE_INFORMATIONS;
import static cfgmm.ricettiamo.util.Constants.NUMBER_OF_ELEMENTS;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.database.RecipesDao;
import cfgmm.ricettiamo.data.database.RecipesRoomDatabase;
import cfgmm.ricettiamo.data.service.RecipeApiService;
import cfgmm.ricettiamo.data.source.recipe.BaseDatabaseRecipesDataSource;
import cfgmm.ricettiamo.data.source.recipe.DatabaseRecipesDataSource;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.RecipeApiResponse;
import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository to get the recipes using the API
 * provided by spoonacular.com (https://spoonacular.com).
 */
public class RecipesRepository implements IRecipesRepository, IRecipesDatabaseResponseCallback{
    private static final String TAG = RecipesRepository.class.getSimpleName();

    private final Application application;
    private final RecipeApiService recipeApiService;
    private final RecipesDao recipesDao;
    private final RecipesResponseCallback recipesResponseCallback;
    BaseDatabaseRecipesDataSource databaseRecipesDataSource;

    private Result firstRecipe;
    private MutableLiveData<Result> mostRecentRecipe;
    private MutableLiveData<Result> myRecipes;
    private MutableLiveData<Result> allRecipes;
    private boolean saveSucess;

    public RecipesRepository(Application application, RecipesResponseCallback recipesResponseCallback) {
        this.application = application;
        this.recipeApiService = ServiceLocator.getInstance().getRecipeApiService();
        RecipesRoomDatabase recipesRoomDatabase = ServiceLocator.getInstance().getRecipesDao(application);
        this.recipesDao = recipesRoomDatabase.recipesDao();
        this.recipesResponseCallback = recipesResponseCallback;

        //Community Recipes
        this.databaseRecipesDataSource = new DatabaseRecipesDataSource();
        databaseRecipesDataSource.setCallBack(this);
        this.mostRecentRecipe = new MutableLiveData<>();
        this.myRecipes = new MutableLiveData<>();
        this.allRecipes = new MutableLiveData<>();
        saveSucess = false;
    }

    @Override
    public void getRecipes(String user_input) {

        // It gets the recipies from the Web Service
        Call<RecipeApiResponse> recipeResponseCall = recipeApiService.getRecipesByName(user_input, NUMBER_OF_ELEMENTS,
                ADD_RECIPE_INFORMATIONS, application.getString(R.string.recipes_api_key));

        recipeResponseCall.enqueue(new Callback<RecipeApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeApiResponse> call,
                                   @NonNull Response<RecipeApiResponse> response) {

                if (response.body() != null && response.isSuccessful()) {
                    List<Recipe> recipesList = response.body().getListRecipes();
                    /*for (int i = 0; i < recipesList.size(); i++) {
                        String ingredientsNames = "";
                        for (int j = 0; j < recipesList.get(i).getIngredientsList().size(); j++)
                            ingredientsNames += recipesList.get(i).getIngredientsList().get(j).getName() + ", ";
                        recipesList.get(i).setIngredientsList(null);
                    }*/
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
            recipesResponseCallback.onSuccess(recipesDao.getFavoriteRecipes());
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
            recipesResponseCallback.onRecipesFavoriteStatusChanged(recipe);
        });
    }

    /**
     * Gets the list of favorite recipes from the local database.
     */
    @Override
    public void getFavoriteRecipes() {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipesResponseCallback.onSuccess(recipesDao.getFavoriteRecipes());
        });
    }

    /**
     * Saves the recipes in the local database.
     * The method is executed with an ExecutorService defined in RecipesRoomDatabase class
     * because the database access cannot been executed in the main thread.
     * @param recipeList the list of recipes to be written in the local database.
     */
    private void saveDataInDatabase(List<Recipe> recipeList) {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Recipe> allRecipe = recipesDao.getAll();

            for (Recipe recipe : allRecipe) {
                if (recipeList.contains(recipe)) {
                    recipeList.set(recipeList.indexOf(recipe), recipe);
                }
            }

            List<Long> insertedRecipeIds = recipesDao.insertRecipeList(recipeList);
            for (int i = 0; i < recipeList.size(); i++) {
                recipeList.get(i).setId(insertedRecipeIds.get(i));
            }

            recipesResponseCallback.onSuccess(recipeList);
        });
    }

    /**
     * Gets the recipes from the local database.
     * The method is executed with an ExecutorService defined in RecipesRoomDatabase class
     * because the database access cannot been executed in the main thread.
     */
    private void readDataFromDatabase() {
        RecipesRoomDatabase.databaseWriteExecutor.execute(() -> {
            recipesResponseCallback.onSuccess(recipesDao.getAll());
        });
    }

    //Community Recipes
    @Override
    public boolean writeRecipe(Recipe recipe) {
        databaseRecipesDataSource.writeRecipe(recipe);
        return saveSucess;
    }

    @Override
    public Result getFirstRecipe(String id) {
        databaseRecipesDataSource.getFirstRecipe(id);
        return firstRecipe;
    }

    @Override
    public MutableLiveData<Result> getMostRecentRecipe(String id) {
        databaseRecipesDataSource.getMostRecentRecipe(id);
        return mostRecentRecipe;
    }

    @Override
    public MutableLiveData<Result> getMyRecipes(String id) {
        databaseRecipesDataSource.getMyRecipes(id);
        return myRecipes;
    }

    @Override
    public MutableLiveData<Result> getAllRecipes() {
        databaseRecipesDataSource.getAllRecipes();
        return allRecipes;
    }


    @Override
    public void onSuccessWriteDatabase() {
        saveSucess = true;
    }

    @Override
    public void onFailureWriteDatabase(int writeDatabase_error) {
        saveSucess = false;
    }

    @Override
    public void onSuccessGetFirstRecipe(Recipe firstRecipe) {
        this.firstRecipe = new Result.RecipeDatabaseResponseSuccess(firstRecipe);
    }

    @Override
    public void onFailureGetFirstRecipe(int writeDatabase_error) {
        this.firstRecipe = new Result.RecipeDatabaseResponseSuccess(null);
    }

    @Override
    public void onSuccessGetMostRecentRecipe(Recipe mostRecentRecipe) {
        this.mostRecentRecipe.postValue(new Result.RecipeDatabaseResponseSuccess(mostRecentRecipe));
    }

    @Override
    public void onFailureGetMostRecentRecipe(int writeDatabase_error) {
        this.mostRecentRecipe.postValue(new Result.Error(writeDatabase_error));
    }

    @Override
    public void onSuccessGetMyRecipes(List<Recipe> recipes) {
        this.myRecipes.postValue(new Result.ListRecipeResponseSuccess(recipes));
    }

    @Override
    public void onFailureGetMyRecipes(int writeDatabase_error) {
        this.myRecipes.postValue(new Result.Error(writeDatabase_error));
    }

    @Override
    public void onSuccessGetAllRecipes(List<Recipe> recipes) {
        this.allRecipes.postValue(new Result.ListRecipeResponseSuccess(recipes));
    }

    @Override
    public void onFailureGetAllRecipes(int writeDatabase_error) {
        this.allRecipes.postValue(new Result.Error(writeDatabase_error));
    }
}
