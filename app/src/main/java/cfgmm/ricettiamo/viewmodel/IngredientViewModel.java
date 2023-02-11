package cfgmm.ricettiamo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import cfgmm.ricettiamo.data.repository.ingredients.IIngredientsRepository;
import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.Result;

public class IngredientViewModel extends ViewModel {

    private IIngredientsRepository ingredientsRepository;
    private MutableLiveData<Result> ingredientList;
    private MutableLiveData<Result> fridgeList;
    private MutableLiveData<Result> shoppingList;

    public IngredientViewModel(IIngredientsRepository ingredientsRepository) {
        this.ingredientsRepository = ingredientsRepository;
    }

    public void insertIngredient(Ingredient ingredient) {
        ingredientList = ingredientsRepository.insertIngredient(ingredient);
    }

    public void deleteIngredient(Ingredient ingredient) {
        ingredientList = ingredientsRepository.deleteIngredient(ingredient);
    }

    public MutableLiveData<Result> getAllIngredients() {
        if(ingredientList == null) {
            ingredientList = ingredientsRepository.getAllIngredients();
        }

        return ingredientList;
    }

    public void updateIngredient(Ingredient ingredient){
        ingredientList = ingredientsRepository.updateIngredient(ingredient);
    }

    public MutableLiveData<Result> getShoppingListIngredients(){
        if(ingredientList == null) {
            ingredientList = ingredientsRepository.getShoppingListIngredients();
        }

        return ingredientList;
    }

    public MutableLiveData<Result> getFridgeListIngredients() {
        if(ingredientList == null) {
            ingredientList = ingredientsRepository.getFridgeListIngredients();
        }

        return ingredientList;
    }
}
