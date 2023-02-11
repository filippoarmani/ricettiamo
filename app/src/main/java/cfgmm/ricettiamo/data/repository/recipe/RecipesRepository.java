package cfgmm.ricettiamo.data.repository.recipe;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import cfgmm.ricettiamo.data.source.recipe.BaseDatabaseRecipesDataSource;
import cfgmm.ricettiamo.data.source.recipe.BasePhotoStorageDataSource;
import cfgmm.ricettiamo.data.source.recipe.BaseRecipesLocalDataSource;
import cfgmm.ricettiamo.data.source.recipe.BaseRecipesRemoteDataSource;
import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.Result;

/**
 * Repository to get the recipes using the API
 * provided by spoonacular.com (https://spoonacular.com).
 */
public class RecipesRepository implements IRecipesRepository, IRecipesDatabaseResponseCallback, IPhotoResponseCallback, RecipesResponseCallback{
    private static final String TAG = RecipesRepository.class.getSimpleName();

    BaseDatabaseRecipesDataSource databaseRecipesDataSource;
    BasePhotoStorageDataSource photoStorageDataSource;
    BaseRecipesLocalDataSource recipesLocalDataSource;
    BaseRecipesRemoteDataSource recipesRemoteDataSource;

    private MutableLiveData<Result> myRecipes;
    private MutableLiveData<Result> allRecipes;

    private Result searchRecipe;
    private MutableLiveData<Result> favoriteRecipe;

    private Result savedRecipe;
    private Result photo;

    public RecipesRepository(BaseDatabaseRecipesDataSource databaseRecipesDataSource,
                             BasePhotoStorageDataSource photoStorageDataSource,
                             BaseRecipesLocalDataSource recipesLocalDataSource,
                             BaseRecipesRemoteDataSource recipesRemoteDataSource) {
        this.databaseRecipesDataSource = databaseRecipesDataSource;
        this.photoStorageDataSource = photoStorageDataSource;
        this.recipesLocalDataSource = recipesLocalDataSource;
        this.recipesRemoteDataSource = recipesRemoteDataSource;

        databaseRecipesDataSource.setCallBack(this);
        photoStorageDataSource.setCallBack(this);
        recipesLocalDataSource.setCallBack(this);
        recipesRemoteDataSource.setCallBack(this);

        this.myRecipes = new MutableLiveData<>();
        this.allRecipes = new MutableLiveData<>();
        this.favoriteRecipe = new MutableLiveData<>();
    }

    @Override
    public Result getRecipes(String user_input) {
        recipesRemoteDataSource.getRecipes(user_input);
        return searchRecipe;
    }

    /**
     * Marks the favorite recipes as not favorite.
     */
    @Override
    public void deleteFavoriteRecipes() {
        recipesLocalDataSource.deleteFavoriteRecipes();
    }

    /**
     * Update the recipes changing the status of "favorite"
     * in the local database.
     * @param recipe The recipe to be updated.
     */
    @Override
    public MutableLiveData<Result> updateRecipes(Recipe recipe) {
        recipesLocalDataSource.updateRecipes(recipe);
        return favoriteRecipe;
    }

    /**
     * Gets the list of favorite recipes from the local database.
     */
    @Override
    public MutableLiveData<Result> getFavoriteRecipes() {
        recipesLocalDataSource.getFavoriteRecipes();

        return favoriteRecipe;
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

    @Override
    public void onSuccessFavorite(List<Recipe> recipesList) {
        favoriteRecipe.postValue(new Result.ListRecipeResponseSuccess(recipesList));
    }

    @Override
    public void onFailure(int error) {
        searchRecipe = new Result.Error(error);
    }

    @Override
    public void onRecipesFavoriteStatusChanged(Recipe recipe) {
        Result result = favoriteRecipe.getValue();
        if(result != null && result.isSuccess()) {
            List<Recipe> favRecipe = ((Result.ListRecipeResponseSuccess) result).getData();

            if (recipe.isFavorite()) {
                favRecipe.add(recipe);
            } else {
                favRecipe.remove(recipe);
            }

            result = new Result.ListRecipeResponseSuccess(favRecipe);
        }

        favoriteRecipe.postValue(result);
    }

    @Override
    public void onSuccessFromRemote(List<Recipe> recipesList, List<Ingredient> ingredientList) {
        recipesLocalDataSource.saveDataInDatabase(recipesList, ingredientList);
    }

    @Override
    public void onFailureFromRemote(int error) {
        searchRecipe = new Result.Error(error);
    }

    @Override
    public void onSuccessFromDatabase(List<Recipe> all) {
        searchRecipe = new Result.ListRecipeResponseSuccess(all);
    }
}
