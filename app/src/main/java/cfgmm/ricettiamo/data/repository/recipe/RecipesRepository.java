package cfgmm.ricettiamo.data.repository.recipe;

import static cfgmm.ricettiamo.util.Constants.ADD_RECIPE_INFORMATIONS;
import static cfgmm.ricettiamo.util.Constants.ADD_RECIPE_INGREDIENTS;
import static cfgmm.ricettiamo.util.Constants.NUMBER_OF_ELEMENTS;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.database.RecipesDao;
import cfgmm.ricettiamo.data.database.RecipesRoomDatabase;
import cfgmm.ricettiamo.data.service.RecipeApiService;
import cfgmm.ricettiamo.data.source.recipe.BaseDatabaseRecipesDataSource;
import cfgmm.ricettiamo.data.source.recipe.BasePhotoStorageDataSource;
import cfgmm.ricettiamo.data.source.recipe.DatabaseRecipesDataSource;
import cfgmm.ricettiamo.data.source.recipe.PhotoStorageDataSource;
import cfgmm.ricettiamo.model.Ingredient;
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
public class RecipesRepository implements IRecipesRepository, IRecipesDatabaseResponseCallback, IPhotoResponseCallback{
    private static final String TAG = RecipesRepository.class.getSimpleName();

    private final Application application;
    private final RecipeApiService recipeApiService;
    private final RecipesDao recipesDao;
    private final RecipesResponseCallback recipesResponseCallback;
    BaseDatabaseRecipesDataSource databaseRecipesDataSource;
    BasePhotoStorageDataSource photoStorageDataSource;

    private MutableLiveData<Result> myRecipes;
    private MutableLiveData<Result> allRecipes;

    private Result savedRecipe;
    private Result photo;

    public RecipesRepository(Application application, RecipesResponseCallback recipesResponseCallback) {
        this.application = application;
        this.recipeApiService = ServiceLocator.getInstance().getRecipeApiService();
        RecipesRoomDatabase recipesRoomDatabase = ServiceLocator.getInstance().getRecipesDao(application);
        this.recipesDao = recipesRoomDatabase.recipesDao();
        this.recipesResponseCallback = recipesResponseCallback;

        //Community Recipes
        this.databaseRecipesDataSource = new DatabaseRecipesDataSource();
        this.photoStorageDataSource = new PhotoStorageDataSource();
        databaseRecipesDataSource.setCallBack(this);
        photoStorageDataSource.setCallBack(this);
        this.myRecipes = new MutableLiveData<>();
        this.allRecipes = new MutableLiveData<>();
    }

    @Override
    public void getRecipes(String user_input) {

        // It gets the recipies from the Web Service
        Call<RecipeApiResponse> recipeResponseCall = recipeApiService.getRecipesByName(user_input, NUMBER_OF_ELEMENTS,
                ADD_RECIPE_INFORMATIONS, ADD_RECIPE_INGREDIENTS, application.getString(R.string.recipes_api_key));

        recipeResponseCall.enqueue(new Callback<RecipeApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<RecipeApiResponse> call,
                                   @NonNull Response<RecipeApiResponse> response) {

                if (response.body() != null && response.isSuccessful()) {
                    List<Recipe> recipesList = response.body().getListRecipes();
                    List<Ingredient> ingredientList = new ArrayList<>();
                    for (int i = 0; i < recipesList.size(); i++) {
                        Recipe recipetemp = recipesList.get(i);
                        for (int j = 0; j < recipetemp.getIngredientsList().size(); j++) {
                            Ingredient ingredient = new Ingredient(recipetemp.getIngredientsList().get(j).getName(),
                                    recipetemp.getIngredientsList().get(j).getQta(),
                                    recipetemp.getIngredientsList().get(j).getSize());
                            ingredientList.add(ingredient);
                        }
                    }
                    saveDataInDatabase(recipesList, ingredientList);
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
    private void saveDataInDatabase(List<Recipe> recipeList, List<Ingredient> ingredientList) {
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
    public Result writeRecipe(Recipe recipe) {
        databaseRecipesDataSource.writeRecipe(recipe);
        return savedRecipe;
    }

    @Override
    public Result uploadPhoto(Uri photo) {
        photoStorageDataSource.uploadFile(photo);
        return this.photo;
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
    public void onSuccessWriteDatabase(Recipe savedRecipe) {
        this.savedRecipe = new Result.RecipeDatabaseResponseSuccess(savedRecipe);
    }

    @Override
    public void onFailureWriteDatabase(int writeDatabase_error) {
        this.savedRecipe = new Result.Error(writeDatabase_error);
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

    @Override
    public void onSuccessUploadPhoto(Uri uri) {
        photo = new Result.PhotoResponseSuccess(uri);
    }

    @Override
    public void onFailureUploadPhoto() {
        photo = new Result.Error(1);
    }
}
