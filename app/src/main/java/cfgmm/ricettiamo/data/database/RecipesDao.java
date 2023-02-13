package cfgmm.ricettiamo.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.Recipe;

@Dao
public interface RecipesDao {
    @Query("SELECT * FROM recipe")
    List<Recipe> getAll();

    @Query("SELECT * FROM recipe WHERE id = :id")
    Recipe getRecipe(long id);

    @Query("SELECT * FROM recipe WHERE is_favorite = 1")
    List<Recipe> getFavoriteRecipes();

    @Query("SELECT * FROM ingredient")
    List<Ingredient> getAllIngredients();
    @Query("SELECT * FROM ingredient WHERE shoppingList = 1")
    List<Ingredient> getShoppingListIngredients();
    @Query("SELECT * FROM ingredient WHERE fridgeList = 1")
    List<Ingredient> getFridgeListIngredients();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertRecipeList(List<Recipe> recipeList);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertIngredientList(List<Ingredient> ingredientList);
    @Insert
    void insertIngredient(Ingredient ingredient);
    @Insert
    void insertRecipe(Recipe recipe);
    @Delete
    void deleteIngredient(Ingredient ingredient);

    @Update
    int updateSingleFavoriteRecipes(Recipe recipe);
    @Update
    int updateIngredient(Ingredient ingredient);

    @Update
    int updateListFavoriteRecipes(List<Recipe> recipe);

    @Query("DELETE FROM recipe")
    int deleteAll();

}
