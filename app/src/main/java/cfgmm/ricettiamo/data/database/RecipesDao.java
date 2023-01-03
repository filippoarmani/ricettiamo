package cfgmm.ricettiamo.data.database;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import cfgmm.ricettiamo.model.Recipe;

@Dao
public interface RecipesDao {

    /*@Query("SELECT * FROM recipes)
    List<Recipe> getAll();

    @Query("SELECT * FROM news WHERE id = :id")
    Recipe getNews(long id);

    @Query("SELECT * FROM news WHERE is_favorite = 1)
    List<Recipe> getFavoriteNews();*/
}
