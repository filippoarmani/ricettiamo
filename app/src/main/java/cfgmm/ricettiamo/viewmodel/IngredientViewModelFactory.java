package cfgmm.ricettiamo.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import cfgmm.ricettiamo.data.repository.ingredients.IIngredientsRepository;

public class IngredientViewModelFactory implements ViewModelProvider.Factory {

    private final IIngredientsRepository ingredientsRepository;

    public IngredientViewModelFactory(IIngredientsRepository ingredientsRepository) {
        this.ingredientsRepository = ingredientsRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new IngredientViewModel(ingredientsRepository);
    }
}
