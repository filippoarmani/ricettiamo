package cfgmm.ricettiamo.data.repository.ingredients;

import java.util.List;

import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.Recipe;

public interface IngredientsResponseCallback {

    void onSuccess(List<Ingredient> ingredientList);
    void onFailure(String errorMessage);
    void onIngredientStatusChanged(Ingredient ingredient, boolean delete, boolean insert);
}
