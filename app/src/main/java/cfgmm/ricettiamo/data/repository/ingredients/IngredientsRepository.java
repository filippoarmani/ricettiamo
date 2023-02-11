package cfgmm.ricettiamo.data.repository.ingredients;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import cfgmm.ricettiamo.data.source.ingredient.BaseIngredientLocalDataSource;
import cfgmm.ricettiamo.data.source.ingredient.BaseIngredientMockDataSource;
import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.IngredientApiResponse;
import cfgmm.ricettiamo.model.Result;

public class IngredientsRepository implements IIngredientsRepository, IIngredientsCallback {

    private final BaseIngredientLocalDataSource baseIngredientLocalDataSource;
    private final BaseIngredientMockDataSource baseIngredientMockDataSource;

    private MutableLiveData<Result> ingredientList;

    public IngredientsRepository(BaseIngredientMockDataSource baseIngredientMockDataSource,
                                 BaseIngredientLocalDataSource baseIngredientLocalDataSource) {

        this.ingredientList = new MutableLiveData<>();

        this.baseIngredientMockDataSource = baseIngredientMockDataSource;
        this.baseIngredientLocalDataSource = baseIngredientLocalDataSource;
        baseIngredientLocalDataSource.setIngredientsCallback(this);
        baseIngredientMockDataSource.setIngredientsCallback(this);
    }

    @Override
    public MutableLiveData<Result> insertIngredient(Ingredient ingredient) {
        baseIngredientLocalDataSource.insertIngredient(ingredient);

        return ingredientList;
    }

    @Override
    public MutableLiveData<Result> deleteIngredient(Ingredient ingredient) {
        baseIngredientLocalDataSource.deleteIngredient(ingredient);

        return ingredientList;
    }

    /**
     * Gets the list of all ingredients from the local database.
     */
    @Override
    public MutableLiveData<Result> getAllIngredients() {
        baseIngredientLocalDataSource.getAllIngredients();
        return ingredientList;
    }

    @Override
    public MutableLiveData<Result> updateIngredient(Ingredient ingredient) {
        baseIngredientLocalDataSource.updateIngredient(ingredient);
        return ingredientList;
    }

    /**
     * Gets the list of shopping list ingredients from the local database.
     */
    @Override
    public MutableLiveData<Result> getShoppingListIngredients() {
        baseIngredientLocalDataSource.getShoppingListIngredients();
        return ingredientList;
    }

    /**
     * Gets the list of fridge list from the local database.
     */
    @Override
    public MutableLiveData<Result> getFridgeListIngredients() {
        baseIngredientLocalDataSource.getFridgeListIngredients();
        return ingredientList;
    }

    //Callback
    @Override
    public void onSuccessFromLocal(List<Ingredient> ingredientList) {
        this.ingredientList.postValue(new Result.ListIngredientResponseSuccess(ingredientList));
    }

    @Override
    public void onFailureFromLocal(int errorMessage) {
        this.ingredientList.postValue(new Result.Error(errorMessage));
    }

    @Override
    public void onIngredientStatusChanged(Ingredient ingredient, boolean delete, boolean insert) {
        Result result = ingredientList.getValue();
        if(result != null && result.isSuccess()) {
            List<Ingredient> list = ((Result.ListIngredientResponseSuccess) result).getData();

            if(delete) {
                list.remove(ingredient);
            }

            if(insert) {
                list.add(ingredient);
            }

            result = new Result.ListIngredientResponseSuccess(list);
        }
        ingredientList.postValue(result);
    }

    @Override
    public void onSuccessFromRemote(IngredientApiResponse ingredientApiResponse) {
        ingredientList.postValue(new Result.ListIngredientResponseSuccess(ingredientApiResponse.getIngredients()));
    }

    @Override
    public void onFailureFromRemote(int error) {
        ingredientList.postValue(new Result.Error(error));
    }
}
