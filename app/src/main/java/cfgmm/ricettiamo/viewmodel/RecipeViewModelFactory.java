package cfgmm.ricettiamo.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import cfgmm.ricettiamo.data.repository.recipe.IRecipesRepository;

public class RecipeViewModelFactory implements ViewModelProvider.Factory {

    private final IRecipesRepository recipesRepository;

    public RecipeViewModelFactory(IRecipesRepository recipesRepository) {
        this.recipesRepository = recipesRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeViewModel(recipesRepository);
    }
}
