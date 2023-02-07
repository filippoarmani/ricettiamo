package cfgmm.ricettiamo.util;

import android.app.Application;

import cfgmm.ricettiamo.data.database.RecipesRoomDatabase;
import cfgmm.ricettiamo.data.repository.comment.CommentRepository;
import cfgmm.ricettiamo.data.repository.comment.ICommentRepository;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.data.repository.user.UserRepository;
import cfgmm.ricettiamo.data.service.RecipeApiService;
import cfgmm.ricettiamo.data.source.comment.BaseCommentDatabaseDataSource;
import cfgmm.ricettiamo.data.source.comment.CommentDatabaseDataSource;
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

    public ICommentRepository getCommentRepository() {
        BaseCommentDatabaseDataSource databaseDataSource =
                new CommentDatabaseDataSource();

        return new CommentRepository(databaseDataSource);
    }
}
