package cfgmm.ricettiamo.data.database;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.Step;
import cfgmm.ricettiamo.model.StepsAnalyze;

public class DatabaseFieldsConverter {
    @TypeConverter
    public List<Ingredient> IngredientToList(String value) {

        List<Ingredient> ingredientList = new ArrayList<>();
        String temp = "";
        //int id = 0;
        String name = "";
        float qta = 0;
        int parameter = 1; //1 --> name, 2 --> qta, 3 --> size
        String size = "";
        for (int i = 0; i < value.length(); i++) {
            while (value.charAt(i) != ';' && value.charAt(i) != ',') {
                temp += value.charAt(i);
                i++;
            }
            if (value.charAt(i) == ';') {
                i += 1;
                Ingredient ingredient = new Ingredient(name, qta, size);
                ingredientList.add(ingredient);
                temp = "";
                parameter = 1;
            /*} if (value.charAt(i) == '.') {
                i += 1;
                id = Integer.parseInt(temp);
                temp = "";*/
            } if (value.charAt(i) == ',') {
                if (parameter == 1) {
                    name = temp;
                    temp = "";
                    parameter++;
                }
                else if (parameter == 2) {
                    qta = Float.parseFloat(temp);
                    temp = "";
                    parameter++;
                } else {
                    size = temp;
                    temp = "";
                }
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
                ingredientsString += /*value.get(i).getId() + ". " + */value.get(i).getName() + ", " +
                value.get(i).getQta() + ", ";
                if (value.get(i).getSize().equals("")) {
                    ingredientsString += "unit" + "; ";
                } else ingredientsString += value.get(i).getSize() + "; ";
            }
        }
        return ingredientsString;

    }

    @TypeConverter
    public List<StepsAnalyze> StepToList(String value) {

        List<StepsAnalyze> stepsAnalyzes = new ArrayList<>();
        List<Step> steps = new ArrayList<>();
        String description = "";
        int number = 0;
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == ';') {
                i += 1;
                Step step = new Step(number, description);
                steps.add(step);
                StepsAnalyze analyzeStep = new StepsAnalyze("", steps);
                stepsAnalyzes.add(analyzeStep);
            } else if (value.charAt(i) == '.'){
                i += 1;
                number += 1;
            } else {
                description = "";
            }
        }
        return stepsAnalyzes;
    }

    @TypeConverter
    public String StepToString(List<StepsAnalyze> value) {

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

        List<String> dishTypesList = new ArrayList<>();
        String string = "";
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == ',') {
                i += 1;
                dishTypesList.add(string);
            } else {
                string = "";
            }
        }
        return dishTypesList;
    }

    @TypeConverter
    public String DishTypesToString(List<String> value) {
        String dishTypesString = "";
        if (value != null) {
            for (int i = 0; i < value.size(); i++) {
                dishTypesString += value.get(i) + ", ";
            }
        }
        return dishTypesString;
    }
}
