package cfgmm.ricettiamo.util;

import android.app.Application;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cfgmm.ricettiamo.model.IngredientApiResponse;
import cfgmm.ricettiamo.model.RecipeApiResponse;

public class JSONParserUtil {
    private static final String TAG = JSONParserUtil.class.getSimpleName();

    public enum JsonParserType {
        GSON,
        JSON_ERROR
    }
    private final Application application;

    public JSONParserUtil(Application application) {
        this.application = application;
    }

    /**
     * Returns a list of Recipes from a JSON file parsed using Gson.
     * Doc can be read here: https://github.com/google/gson
     * @param fileName The JSON file to be parsed.
     * @return The IngredientApiResponse object associated with the JSON file content.
     * @throws IOException exception
     */
    public RecipeApiResponse parseJSONFileWithGSonRecipe(String fileName) throws IOException {
        InputStream inputStream = application.getAssets().open(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return new Gson().fromJson(bufferedReader, RecipeApiResponse.class);
    }

    /**
     * Returns a list of Ingredients from a JSON file parsed using Gson.
     * Doc can be read here: https://github.com/google/gson
     * @param fileName The JSON file to be parsed.
     * @return The IngredientApiResponse object associated with the JSON file content.
     * @throws IOException esxeption
     */
    public IngredientApiResponse parseJSONFileWithGSonIngredients(String fileName) throws IOException {
        InputStream inputStream = application.getAssets().open(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return new Gson().fromJson(bufferedReader, IngredientApiResponse.class);
    }
}
