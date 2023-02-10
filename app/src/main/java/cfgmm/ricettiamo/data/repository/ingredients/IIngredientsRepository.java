package cfgmm.ricettiamo.data.repository.ingredients;


import java.util.List;

import cfgmm.ricettiamo.model.Ingredient;

public interface IIngredientsRepository {

    void getIngredientByName(String name);
    void getAllIngredients();

    void updateIngredient(Ingredient ingredient);

    void getShoppingListIngredients();
    void getFridgeListIngredients();
    void insertIngredient(Ingredient ingredient);
    void deleteIngredient(Ingredient ingredient);
}
