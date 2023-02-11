package cfgmm.ricettiamo.data.source.ingredient;

import cfgmm.ricettiamo.data.repository.ingredients.IIngredientsCallback;

public abstract class BaseIngredientMockDataSource {
    protected IIngredientsCallback ingredientsCallback;

    public void setIngredientsCallback(IIngredientsCallback ingredientsCallback) {
        this.ingredientsCallback = ingredientsCallback;
    }

    public abstract void getIngredientByName(String name);
}
