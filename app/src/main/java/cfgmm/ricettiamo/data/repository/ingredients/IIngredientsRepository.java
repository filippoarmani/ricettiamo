package cfgmm.ricettiamo.data.repository.ingredients;


public interface IIngredientsRepository {
    /*enum JsonParserType {
        JSON_READER,
        JSON_OBJECT_ARRAY,
        GSON,
        JSON_ERROR
    };*/

    void getIngredientByName(String name);
    void getIngredientById(int id);
}
