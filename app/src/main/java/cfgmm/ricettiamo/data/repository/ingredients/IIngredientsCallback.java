package cfgmm.ricettiamo.data.repository.ingredients;

import java.util.List;

import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.IngredientApiResponse;

public interface IIngredientsCallback {
    void onSuccessFromLocal(List<Ingredient> ingredientList);
    void onFailureFromLocal(int error);
    void onIngredientStatusChanged(Ingredient ingredient, boolean delete, boolean insert);

    void onSuccessFromRemote(IngredientApiResponse ingredientApiResponse);
    void onFailureFromRemote(int error);
}
