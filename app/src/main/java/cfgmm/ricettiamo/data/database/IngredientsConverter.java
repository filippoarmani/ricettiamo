package cfgmm.ricettiamo.data.database;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

import cfgmm.ricettiamo.model.Ingredient;

public class IngredientsConverter {
    /*@TypeConverter
    public CountryLangs storedStringToLanguages(String value) {
        List<Ingredient> langs = Arrays.asList(value.split("\\s*,\\s*"));
        return new CountryLangs(langs);
    }

    @TypeConverter
    public String languagesToStoredString(CountryLangs cl) {
        String value = "";

        for (String lang : cl.getCountryLangs())
            value += lang + ",";

        return value;

    }*/
}
