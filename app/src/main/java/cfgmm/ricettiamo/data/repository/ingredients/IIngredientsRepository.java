package cfgmm.ricettiamo.data.repository.ingredients;


import androidx.lifecycle.MutableLiveData;

import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.Result;

public interface IIngredientsRepository {

    MutableLiveData<Result> getAllIngredients();

    MutableLiveData<Result> updateIngredient(Ingredient ingredient);

    MutableLiveData<Result> getShoppingListIngredients();
    MutableLiveData<Result> getFridgeListIngredients();
    MutableLiveData<Result> insertIngredient(Ingredient ingredient);
    MutableLiveData<Result> deleteIngredient(Ingredient ingredient);
}
