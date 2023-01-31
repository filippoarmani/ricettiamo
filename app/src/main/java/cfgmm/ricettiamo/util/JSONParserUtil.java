package cfgmm.ricettiamo.util;

import android.app.Application;
import android.util.JsonReader;
import android.util.JsonToken;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.IngredientApiResponse;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.RecipeApiResponse;

//TODO
public class JSONParserUtil {
    private static final String TAG = JSONParserUtil.class.getSimpleName();

    public enum JsonParserType {
        JSON_READER,
        JSON_OBJECT_ARRAY,
        GSON,
        JSON_ERROR
    };
    private final Application application;

    private final String resultsParameter = "results";
    private final String totalResultsParameter = "totalResults";
    private final String authorParameter = "author";
    private final String descriptionParameter = "description";
    private final String urlParameter = "url";
    private final String urlToImageParameter = "urlToImage";
    private final String nameParameter = "name";

    public JSONParserUtil(Application application) {
        this.application = application;
    }

    /**
     * Returns a list of News from a JSON file parsed using JsonReader class.
     * Doc can be read here: https://developer.android.com/reference/android/util/JsonReader
     * @param fileName The JSON file to be parsed.
     * @return The NewsApiResponse object associated with the JSON file content.
     * @throws IOException
     */
    public RecipeApiResponse parseJSONFileWithJsonReaderRecipe(String fileName) throws IOException {
        InputStream inputStream = application.getAssets().open(fileName);
        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
        RecipeApiResponse recipeApiResponse = new RecipeApiResponse();
        List<Recipe> recipeList = null;

        jsonReader.beginObject(); // Beginning of JSON root

        while (jsonReader.hasNext()) {
            String rootJSONParam = jsonReader.nextName();
            if (rootJSONParam.equals(resultsParameter)) {
                jsonReader.beginArray(); // Beginning of recipes array
                recipeList = new ArrayList<>();
                while (jsonReader.hasNext()) {
                    jsonReader.beginObject(); // Beginning of article object
                    Recipe recipe = new Recipe();
                    while (jsonReader.hasNext()) {
                        String resultsJSONParam = jsonReader.nextName();
                        if (jsonReader.peek() != JsonToken.NULL &&
                                resultsJSONParam.equals(authorParameter)) {
                            String author = jsonReader.nextString();
                            recipe.setAuthor(author);
                        } else if (jsonReader.peek() != JsonToken.NULL &&
                                resultsJSONParam.equals(nameParameter)) {
                            String name = jsonReader.nextString();
                            recipe.setName(name);
                        } else if (jsonReader.peek() != JsonToken.NULL &&
                                resultsJSONParam.equals(descriptionParameter)) {
                            String description = jsonReader.nextString();
                            recipe.setDescription(description);
                        } else if (jsonReader.peek() != JsonToken.NULL &&
                                resultsJSONParam.equals(urlParameter)) {
                            String url = jsonReader.nextString();
                            recipe.setUrl(url);
                        } else if (jsonReader.peek() != JsonToken.NULL &&
                                resultsJSONParam.equals(urlToImageParameter)) {
                            String urlToImage = jsonReader.nextString();
                            recipe.setUrlToImage(urlToImage);
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject(); // End of article object
                    recipeList.add(recipe);
                }
                jsonReader.endArray(); // End of articles array
            }
        }
        jsonReader.endObject(); // End of JSON object

        recipeApiResponse.setListRecipes(recipeList);

        return recipeApiResponse;
    }

    /**
     * Returns a list of Recipes from a JSON file parsed using JSONObject and JSONReader classes.
     * Doc of JSONObject: https://developer.android.com/reference/org/json/JSONObject
     * Doc of JSONArray: https://developer.android.com/reference/org/json/JSONArray
     * @param fileName The JSON file to be parsed.
     * @return The RecipeApiResponse object associated with the JSON file content.
     * @throws IOException
     * @throws JSONException
     */
    public RecipeApiResponse parseJSONFileWithJSONObjectArrayRecipe(String fileName)
            throws IOException, JSONException {

        InputStream inputStream = application.getAssets().open(fileName);
        String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        JSONObject rootJSONObject = new JSONObject(content);

        RecipeApiResponse recipeApiResponse = new RecipeApiResponse();
        recipeApiResponse.setStatus(rootJSONObject.getString(nameParameter));
        recipeApiResponse.setTotalResults(rootJSONObject.getInt(totalResultsParameter));

        JSONArray resultsJSONArray = rootJSONObject.getJSONArray(resultsParameter);

        List<Recipe> recipeList = null;
        int articlesCount = resultsJSONArray.length();

        if (articlesCount > 0) {
            recipeList = new ArrayList<>();
            Recipe recipe;
            for (int i = 0; i < articlesCount; i++) {
                JSONObject resultsJSONObject = resultsJSONArray.getJSONObject(i);
                recipe = new Recipe();
                recipe.setAuthor(resultsJSONObject.getString(authorParameter));
                recipe.setName(resultsJSONObject.getString(nameParameter));
                recipe.setDescription(resultsJSONObject.getString(descriptionParameter));
                recipe.setUrl(resultsJSONObject.getString(urlParameter));
                recipe.setUrlToImage(resultsJSONObject.getString(urlToImageParameter));
                recipeList.add(recipe);
            }
        }
        recipeApiResponse.setListRecipes(recipeList);

        return recipeApiResponse;
    }

    /**
     * Returns a list of Ingredients from a JSON file parsed using Gson.
     * Doc can be read here: https://github.com/google/gson
     * @param fileName The JSON file to be parsed.
     * @return The IngredientApiResponse object associated with the JSON file content.
     * @throws IOException
     */
    public RecipeApiResponse parseJSONFileWithGSonRecipe(String fileName) throws IOException {
        InputStream inputStream = application.getAssets().open(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return new Gson().fromJson(bufferedReader, RecipeApiResponse.class);
    }


    public IngredientApiResponse parseJSONFileWithJsonReaderIngredients(String fileName) throws IOException {
        InputStream inputStream = application.getAssets().open(fileName);
        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
        IngredientApiResponse ingredientApiResponse = new IngredientApiResponse();
        List<Ingredient> ingredientList = null;

        jsonReader.beginObject(); // Beginning of JSON root

        while (jsonReader.hasNext()) {
            String rootJSONParam = jsonReader.nextName();
            if (rootJSONParam.equals(resultsParameter)) {
                jsonReader.beginArray(); // Beginning of recipes array
                ingredientList = new ArrayList<>();
                while (jsonReader.hasNext()) {
                    jsonReader.beginObject(); // Beginning of article object
                    Ingredient ingredient = new Ingredient();
                    while (jsonReader.hasNext()) {
                        String resultsJSONParam = jsonReader.nextName();
                        if (jsonReader.peek() != JsonToken.NULL &&
                                resultsJSONParam.equals(nameParameter)) {
                            String name = jsonReader.nextString();
                            ingredient.setName(name);
                        } /*else if (jsonReader.peek() != JsonToken.NULL &&
                                resultsJSONParam.equals(urlParameter)) {
                            String url = jsonReader.nextString();
                            ingredient.setUrl(url);
                        }todo*/ else if (jsonReader.peek() != JsonToken.NULL &&
                                resultsJSONParam.equals(urlToImageParameter)) {
                            String urlToImage = jsonReader.nextString();
                            ingredient.setUrlToImage(urlToImage);
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject(); // End of article object
                    ingredientList.add(ingredient);
                }
                jsonReader.endArray(); // End of articles array
            }
        }
        jsonReader.endObject(); // End of JSON object

        ingredientApiResponse.setArticles(ingredientList);

        return ingredientApiResponse;
    }

    /**
     * Returns a list of Ingredients from a JSON file parsed using JSONObject and JSONReader classes.
     * Doc of JSONObject: https://developer.android.com/reference/org/json/JSONObject
     * Doc of JSONArray: https://developer.android.com/reference/org/json/JSONArray
     * @param fileName The JSON file to be parsed.
     * @return The IngredientApiResponse object associated with the JSON file content.
     * @throws IOException
     * @throws JSONException
     */
    public IngredientApiResponse parseJSONFileWithJSONObjectArrayIngredients(String fileName)
            throws IOException, JSONException {

        InputStream inputStream = application.getAssets().open(fileName);
        String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        JSONObject rootJSONObject = new JSONObject(content);

        IngredientApiResponse ingredientApiResponse = new IngredientApiResponse();
        ingredientApiResponse.setStatus(rootJSONObject.getString(nameParameter));
        ingredientApiResponse.setTotalResults(rootJSONObject.getInt(totalResultsParameter));

        JSONArray articlesJSONArray = rootJSONObject.getJSONArray(resultsParameter);

        List<Ingredient> ingredientList = null;
        int articlesCount = articlesJSONArray.length();

        if (articlesCount > 0) {
            ingredientList = new ArrayList<>();
            Ingredient ingredient;
            for (int i = 0; i < articlesCount; i++) {
                JSONObject resultsJSONObject = articlesJSONArray.getJSONObject(i);
                ingredient = new Ingredient();
                ingredient.setName(resultsJSONObject.getString(nameParameter));
                //ingredient.setUrl(resultsJSONObject.getString(urlParameter)); todo
                ingredient.setUrlToImage(resultsJSONObject.getString(urlToImageParameter));
                ingredientList.add(ingredient);
            }
        }
        ingredientApiResponse.setArticles(ingredientList);

        return ingredientApiResponse;
    }

    /**
     * Returns a list of Ingredients from a JSON file parsed using Gson.
     * Doc can be read here: https://github.com/google/gson
     * @param fileName The JSON file to be parsed.
     * @return The IngredientApiResponse object associated with the JSON file content.
     * @throws IOException
     */
    public IngredientApiResponse parseJSONFileWithGSonIngredients(String fileName) throws IOException {
        InputStream inputStream = application.getAssets().open(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        return new Gson().fromJson(bufferedReader, IngredientApiResponse.class);
    }
}
