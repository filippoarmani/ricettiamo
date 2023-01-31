package cfgmm.ricettiamo.data.database;

import static cfgmm.ricettiamo.util.Constants.DATABASE_VERSION;
import static cfgmm.ricettiamo.util.Constants.RECIPES_DATABASE_NAME;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.Ingredient;

/**
 * Main access point for the underlying connection to the local database.
 * https://developer.android.com/reference/kotlin/androidx/room/Database
 */
@Database(entities = {Recipe.class, Ingredient.class}, version = DATABASE_VERSION)
public abstract class RecipesRoomDatabase extends RoomDatabase {
    public abstract RecipesDao recipesDao();

    private static volatile RecipesRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static RecipesRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecipesRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecipesRoomDatabase.class, RECIPES_DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}
