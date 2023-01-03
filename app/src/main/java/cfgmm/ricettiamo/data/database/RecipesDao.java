package cfgmm.ricettiamo.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import cfgmm.ricettiamo.model.Recipe;

@Dao
public interface RecipesDao {
    @Query("SELECT * FROM recipe")
    List<Recipe> getAll();

    @Query("SELECT * FROM recipe WHERE id = :id")
    Recipe getRecipes(long id);

    @Query("SELECT * FROM recipe WHERE isFavorite = 1")
    List<Recipe> getFavoriteRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertRecipeList(List<Recipe> recipeList);

    @Update
    int updateSingleFavoriteRecipes(Recipe recipe);

    @Update
    int updateListFavoriteRecipes(List<Recipe> recipe);

    @Query("DELETE FROM recipe")
    int deleteAll();
}
