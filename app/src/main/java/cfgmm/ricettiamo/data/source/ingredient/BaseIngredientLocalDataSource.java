package cfgmm.ricettiamo.data.source.ingredient;

import java.util.List;

import cfgmm.ricettiamo.data.repository.ingredients.IIngredientsCallback;
import cfgmm.ricettiamo.model.Ingredient;

public abstract class BaseIngredientLocalDataSource {

    protected IIngredientsCallback ingredientsCallback;

    public void setIngredientsCallback(IIngredientsCallback ingredientsCallback) {
        this.ingredientsCallback = ingredientsCallback;
    }

    public abstract void insertIngredient(Ingredient ingredient);

    public abstract void deleteIngredient(Ingredient ingredient);

    protected abstract void saveDataInDatabase(List<Ingredient> ingredientList);

    public abstract void getAllIngredients();

    public abstract void updateIngredient(Ingredient ingredient);

    public abstract void getShoppingListIngredients();

    public abstract void getFridgeListIngredients();
}
