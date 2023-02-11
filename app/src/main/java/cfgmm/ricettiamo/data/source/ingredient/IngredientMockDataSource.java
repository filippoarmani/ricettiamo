package cfgmm.ricettiamo.data.source.ingredient;

import static cfgmm.ricettiamo.util.Constants.INGREDIENTS_API_TEST_JSON_FILE;

import java.io.IOException;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.IngredientApiResponse;
import cfgmm.ricettiamo.util.JSONParserUtil;

public class IngredientMockDataSource extends BaseIngredientMockDataSource{

    private final JSONParserUtil jsonParserUtil;
    private final JSONParserUtil.JsonParserType jsonParserType;

    public IngredientMockDataSource(JSONParserUtil jsonParserUtil,
                                    JSONParserUtil.JsonParserType jsonParserType) {

        this.jsonParserUtil = jsonParserUtil;
        this.jsonParserType = jsonParserType;
    }

    @Override
    public void getIngredientByName(String name) {
        IngredientApiResponse ingredientApiResponse = null;

        switch (jsonParserType) {
            case GSON:
                try {
                    ingredientApiResponse = jsonParserUtil.parseJSONFileWithGSonIngredients(INGREDIENTS_API_TEST_JSON_FILE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case JSON_ERROR:
                ingredientsCallback.onFailureFromRemote(R.string.error_retrieving_ingredient);
                break;
        }

        if (ingredientApiResponse != null) {
            //saveDataInDatabase(ingredientApiResponse.getIngredients());
            ingredientsCallback.onSuccessFromRemote(ingredientApiResponse);
        } else {
            ingredientsCallback.onFailureFromRemote(R.string.error_retrieving_ingredient);
        }
    }
}
