package cfgmm.ricettiamo.util;

import android.app.Application;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.database.RecipesRoomDatabase;
import cfgmm.ricettiamo.data.repository.comment.CommentRepository;
import cfgmm.ricettiamo.data.repository.comment.ICommentRepository;
import cfgmm.ricettiamo.data.repository.ingredients.IIngredientsRepository;
import cfgmm.ricettiamo.data.repository.ingredients.IngredientsRepository;
import cfgmm.ricettiamo.data.repository.recipe.IRecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesRepository;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.data.repository.user.UserRepository;
import cfgmm.ricettiamo.data.service.RecipeApiService;
import cfgmm.ricettiamo.data.source.comment.BaseCommentDatabaseDataSource;
import cfgmm.ricettiamo.data.source.comment.CommentDatabaseDataSource;
import cfgmm.ricettiamo.data.source.ingredient.BaseIngredientLocalDataSource;
import cfgmm.ricettiamo.data.source.ingredient.BaseIngredientMockDataSource;
import cfgmm.ricettiamo.data.source.ingredient.IngredientLocalDataSource;
import cfgmm.ricettiamo.data.source.ingredient.IngredientMockDataSource;
import cfgmm.ricettiamo.data.source.recipe.BaseDatabaseRecipesDataSource;
import cfgmm.ricettiamo.data.source.recipe.BasePhotoStorageDataSource;
import cfgmm.ricettiamo.data.source.recipe.BaseRecipesLocalDataSource;
import cfgmm.ricettiamo.data.source.recipe.BaseRecipesRemoteDataSource;
import cfgmm.ricettiamo.data.source.recipe.DatabaseRecipesDataSource;
import cfgmm.ricettiamo.data.source.recipe.PhotoStorageDataSource;
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

    /**
     * Creates an instance of IRecipeRepository.
     * @return An instance of IRecipeRepository.
     */
    public IRecipesRepository getRecipesRepository(Application application) {
        BaseDatabaseRecipesDataSource databaseRecipesDataSource = new DatabaseRecipesDataSource();
        BasePhotoStorageDataSource photoStorageDataSource = new PhotoStorageDataSource();
        BaseRecipesLocalDataSource recipesLocalDataSource = new RecipesLocalDataSource(this.getRecipesDao(application));
        BaseRecipesRemoteDataSource recipesRemoteDataSource = new RecipesRemoteDataSource(application.getString(R.string.recipes_api_key));

        return new RecipesRepository(databaseRecipesDataSource, photoStorageDataSource, recipesLocalDataSource, recipesRemoteDataSource);
    }

    /**
     * Creates an instance of ICommentRepository.
     * @return An instance of ICommentRepository.
     */
    public ICommentRepository getCommentRepository() {
        BaseCommentDatabaseDataSource databaseDataSource =
                new CommentDatabaseDataSource();

        return new CommentRepository(databaseDataSource);
    }

    /**
     * Creates an instance of IIngredientRepository.
     * @return An instance of IIngredientRepository.
     */

    public IIngredientsRepository getIngredientRepository(Application application) {

        BaseIngredientLocalDataSource baseIngredientLocalDataSource = new IngredientLocalDataSource(this.getRecipesDao(application));

        JSONParserUtil jsonParserUtil = new JSONParserUtil(application);
        BaseIngredientMockDataSource baseIngredientMockDataSource = new IngredientMockDataSource(jsonParserUtil, JSONParserUtil.JsonParserType.GSON);

        return new IngredientsRepository(baseIngredientMockDataSource, baseIngredientLocalDataSource);
    }


}
