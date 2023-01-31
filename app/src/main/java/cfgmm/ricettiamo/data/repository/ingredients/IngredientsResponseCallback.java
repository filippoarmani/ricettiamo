package cfgmm.ricettiamo.data.repository.ingredients;

import java.util.List;

import cfgmm.ricettiamo.model.Ingredient;

public interface IngredientsResponseCallback {

    void onSuccess(List<Ingredient> ingredientList);
    void onFailure(String errorMessage);
}
