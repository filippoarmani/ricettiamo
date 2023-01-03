package cfgmm.ricettiamo.data.source.recipe;

import static cfgmm.ricettiamo.util.Constants.API_KEY_ERROR;
import static cfgmm.ricettiamo.util.Constants.UNEXPECTED_ERROR;

import org.json.JSONException;

import java.io.IOException;

import cfgmm.ricettiamo.model.RecipeApiResponse;
import cfgmm.ricettiamo.util.JSONParserUtil;

/**
 * Class to get the recipes from a local JSON file to simulate the Web Service response.
 */
public class RecipesMockRemoteDataSource extends BaseRecipesRemoteDataSource{

    private final JSONParserUtil jsonParserUtil;
    private final JSONParserUtil.JsonParserType jsonParserType;

    public RecipesMockRemoteDataSource(JSONParserUtil jsonParserUtil,
                                    JSONParserUtil.JsonParserType jsonParserType) {

        this.jsonParserUtil = jsonParserUtil;
        this.jsonParserType = jsonParserType;
    }

    @Override
    public void getRecipes() {
        RecipeApiResponse recipeApiResponse = null;
//TODO
        /*switch (jsonParserType) {
            case JSON_READER:
                try {
                    recipeApiResponse =
                            jsonParserUtil.parseJSONFileWithJsonReader(NEWS_API_TEST_JSON_FILE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case JSON_OBJECT_ARRAY:
                try {
                    recipeApiResponse = jsonParserUtil.parseJSONFileWithJSONObjectArray(NEWS_API_TEST_JSON_FILE);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                break;
            case GSON:
                try {
                    recipeApiResponse = jsonParserUtil.parseJSONFileWithGSon(NEWS_API_TEST_JSON_FILE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case JSON_ERROR:
                recipesCallback.onFailureFromRemote(new Exception(UNEXPECTED_ERROR));
                break;
        }*/

        if (recipeApiResponse != null) {
            recipesCallback.onSuccessFromRemote(recipeApiResponse, System.currentTimeMillis());
        } else {
            recipesCallback.onFailureFromRemote(new Exception(API_KEY_ERROR));
        }
    }
}
