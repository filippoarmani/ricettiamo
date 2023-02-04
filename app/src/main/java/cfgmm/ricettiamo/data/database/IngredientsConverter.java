package cfgmm.ricettiamo.data.database;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cfgmm.ricettiamo.model.Ingredient;

public class IngredientsConverter {
    @TypeConverter
    public List<Ingredient> IngredientToList(String value) {
        List<Ingredient> ingredientList = new ArrayList<>();
        String name = "";
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == ',') {
                i += 1;
                Ingredient ingredient = new Ingredient(name, 0, null);
                ingredientList.add(ingredient);
            } else {
                name += value.charAt(i);
            }
        }

        return ingredientList;
    }

    @TypeConverter
    public String IngredientToString(List<Ingredient> value) {
        String ingredientsString = "";

        if (value != null) {
            for (int i = 0; i < value.size(); i++) {
                ingredientsString += value.get(i).getName() + ", ";
            }
        }

        return ingredientsString;

    }
}
