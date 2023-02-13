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

    private MutableLiveData<Result> searchRecipe;
    private MutableLiveData<Result> favoriteRecipe;

    private MutableLiveData<Result> savedRecipe;
    private MutableLiveData<Result> photo;

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

        this.searchRecipe = new MutableLiveData<>();
        this.myRecipes = new MutableLiveData<>();
        this.allRecipes = new MutableLiveData<>();
        this.favoriteRecipe = new MutableLiveData<>();
        this.savedRecipe = new MutableLiveData<>();
        this.photo = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<Result> getRecipes(String user_input) {
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

    @Override
    public MutableLiveData<Result> insertRecipe(Recipe recipe) {
        recipesLocalDataSource.insertRecipe(recipe);

        return allRecipes;
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
    public MutableLiveData<Result> writeRecipe(Recipe recipe) {
        databaseRecipesDataSource.writeRecipe(recipe);
        return savedRecipe;
    }

    @Override
    public MutableLiveData<Result> uploadPhoto(Uri photo) {
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
        this.savedRecipe.postValue(new Result.RecipeDatabaseResponseSuccess(savedRecipe));
    }

    @Override
    public void onFailureWriteDatabase(int writeDatabase_error) {
        this.savedRecipe.postValue(new Result.Error(writeDatabase_error));
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
        photo.postValue(new Result.PhotoResponseSuccess(uri));
    }

    @Override
    public void onFailureUploadPhoto() {
        photo.postValue(new Result.Error(1));
    }

    @Override
    public void onSuccessFavorite(List<Recipe> recipesList) {
        favoriteRecipe.postValue(new Result.ListRecipeResponseSuccess(recipesList));
    }

    @Override
    public void onFailure(int error) {
        searchRecipe.postValue(new Result.Error(error));
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
        searchRecipe.postValue(new Result.Error(error));
    }

    @Override
    public void onSuccessFromDatabase(List<Recipe> all) {
        searchRecipe.postValue(new Result.ListRecipeResponseSuccess(all));
    }
}
