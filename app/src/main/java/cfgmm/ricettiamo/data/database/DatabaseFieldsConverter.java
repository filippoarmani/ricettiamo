package cfgmm.ricettiamo.data.database;

import androidx.room.TypeConverter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.Step;
import cfgmm.ricettiamo.model.StepsAnalyze;

public class DatabaseFieldsConverter {
    @TypeConverter
    public List<Ingredient> IngredientToList(String value) {
        /*Type Ingredient = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, Ingredient);*/

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
        /*Gson gson = new Gson();
        Ingredient json = gson.toJson(value);
        return json;*/

        String ingredientsString = "";
        if (value != null) {
            for (int i = 0; i < value.size(); i++) {
                ingredientsString += value.get(i).getName() + ", ";
            }
        }
        return ingredientsString;

    }

    @TypeConverter
    public List<StepsAnalyze> StepToList(String value) {
        /*Type StepsAnalyze = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, StepsAnalyze);*/

        List<StepsAnalyze> stepsAnalyzes = new ArrayList<>();
        List<Step> steps = new ArrayList<>();
        String description = "";
        int number = 0;
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == ';') {
                i += 1;
                Step step = new Step(number, description, null, null);
                steps.add(step);
                StepsAnalyze analyzeStep = new StepsAnalyze("", steps);
                stepsAnalyzes.add(analyzeStep);
            } else if (value.charAt(i) == '.'){
                i += 1;
                number += 1;
            } else {
                description += value.charAt(i);
            }
        }
        return stepsAnalyzes;
    }

    @TypeConverter
    public String StepToString(List<StepsAnalyze> value) {
        /*Gson gson = new Gson();
        StepsAnalyze json = gson.toJson(value);
        return json;*/

        String stepString = "";
        if (value != null) {
            for (int i = 0; i < value.size(); i++) {
                if (value.get(i).getName().equals("")) {
                    for (int j = 0; j < value.get(i).getSteps().size(); j++) {
                        stepString += value.get(i).getSteps().get(j).getNumber() + ". " +
                                value.get(i).getSteps().get(j).getDescription() + "; ";
                    }
                }
            }
        }
        return stepString;

    }

    @TypeConverter
    public List<String> DishtypesToList(String value) {
        /*Type String = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, String);*/

        List<String> dishTypesList = new ArrayList<>();
        String string = "";
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == ',') {
                i += 1;
                dishTypesList.add(string);
            } else {
                string += value.charAt(i);
            }
        }
        return dishTypesList;
    }

    @TypeConverter
    public String DishTypesToString(List<String> value) {
        /*Gson gson = new Gson();
        String json = gson.toJson(value);
        return json;*/

        String dishTypesString = "";
        if (value != null) {
            for (int i = 0; i < value.size(); i++) {
                dishTypesString += value.get(i) + ", ";
            }
        }
        return dishTypesString;
    }
}
