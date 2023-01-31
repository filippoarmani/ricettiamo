package cfgmm.ricettiamo.util;

import android.app.Application;

import java.io.IOException;
import java.security.GeneralSecurityException;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.database.RecipesRoomDatabase;
import cfgmm.ricettiamo.data.repository.recipe.IRecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesRepository;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.data.repository.user.UserRepository;
import cfgmm.ricettiamo.data.service.RecipeApiService;
import cfgmm.ricettiamo.data.source.recipe.BaseFavoriteRecipesDataSource;
import cfgmm.ricettiamo.data.source.recipe.BaseRecipesLocalDataSource;
import cfgmm.ricettiamo.data.source.recipe.BaseRecipesRemoteDataSource;
import cfgmm.ricettiamo.data.source.recipe.FavoriteRecipesDataSource;
import cfgmm.ricettiamo.data.source.recipe.RecipesLocalDataSource;
import cfgmm.ricettiamo.data.source.recipe.RecipesRemoteDataSource;
import cfgmm.ricettiamo.data.source.user.BaseDatabaseDataSource;
import cfgmm.ricettiamo.data.source.user.BaseFirebaseAuthDataSource;
import cfgmm.ricettiamo.data.source.user.DatabaseDataSource;
import cfgmm.ricettiamo.data.source.user.FirebaseAuthDataSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *  Registry to provide the dependencies for the classes
 *  used in the application.
 */
public class ServiceLocator {

    private static volatile ServiceLocator INSTANCE = null;

    private ServiceLocator() {}

    /**
     * Returns an instance of ServiceLocator class.
     * @return An instance of ServiceLocator.
     */
    public static ServiceLocator getInstance() {
        if (INSTANCE == null) {
            synchronized(ServiceLocator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ServiceLocator();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Returns an instance of NewsApiService class using Retrofit.
     * @return an instance of NewsApiService.
     */
    public RecipeApiService getRecipeApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.RECIPES_API_BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(RecipeApiService.class);
    }

    /**
     * Returns an instance of NewsRoomDatabase class to manage Room database.
     * @param application Param for accessing the global application state.
     * @return An instance of NewsRoomDatabase.
     */
    public RecipesRoomDatabase getRecipesDao(Application application) {
        return RecipesRoomDatabase.getDatabase(application);
    }

    /**
     * Returns an instance of INewsRepositoryWithLiveData.
     * @param application Param for accessing the global application state.
     * @param debugMode Param to establish if the application is run in debug mode.
     * @return An instance of INewsRepositoryWithLiveData.
     */
    /*public IRecipesRepository getRecipeRepository(Application application, boolean debugMode) {
        BaseRecipesRemoteDataSource recipesRemoteDataSource;
        BaseRecipesLocalDataSource recipesLocalDataSource;
        BaseFavoriteRecipesDataSource favoriteRecipesDataSource;
        //SharedPreferencesUtil sharedPreferencesUtil = new SharedPreferencesUtil(application);
        //DataEncryptionUtil dataEncryptionUtil = new DataEncryptionUtil(application);

        recipesRemoteDataSource = new RecipesRemoteDataSource(application.getString(R.string.recipes_api_key));

        recipesLocalDataSource = new RecipesLocalDataSource(getRecipesDao(application)/*,
                sharedPreferencesUtil, dataEncryptionUtil*//*);
        favoriteRecipesDataSource = new FavoriteRecipesDataSource(application.getString(R.string.toDo));*/
        /*try {
            favoriteRecipesDataSource = new FavoriteRecipesDataSource(application.getString(R.string.toDo)
                    dataEncryptionUtil.
                    readSecretDataWithEncryptedSharedPreferences(
                            ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ID_TOKEN)
            );
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }*/

        /*return new RecipesRepository(application, recipesRemoteDataSource, favoriteRecipesDataSource);
    }*/

    /**
     * Creates an instance of IUserRepository.
     * @return An instance of IUserRepository.
     */
    public IUserRepository getUserRepository() {
        BaseFirebaseAuthDataSource firebaseAuthDataSource =
                new FirebaseAuthDataSource();

        BaseDatabaseDataSource databaseDataSource =
                new DatabaseDataSource();

        return new UserRepository(firebaseAuthDataSource, databaseDataSource);
    }
}
