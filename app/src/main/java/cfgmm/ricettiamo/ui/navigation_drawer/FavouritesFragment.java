package cfgmm.ricettiamo.ui.navigation_drawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.RecipesRecyclerAdapter;
import cfgmm.ricettiamo.data.repository.recipe.IRecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesResponseCallback;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.RecipeViewModel;
import cfgmm.ricettiamo.viewmodel.RecipeViewModelFactory;
import cfgmm.ricettiamo.viewmodel.UserViewModel;
import cfgmm.ricettiamo.viewmodel.UserViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouritesFragment extends Fragment implements RecipesResponseCallback {

    private static final String TAG = FavouritesFragment.class.getSimpleName();

    private List<Recipe> recipesList;
    private IRecipesRepository iRecipesRepository;
    private RecipesRecyclerAdapter recipesRecyclerAdapter;
    private ProgressBar progressBar;
    private UserViewModel userViewModel;
    private RecipeViewModel recipeViewModel;

    public FavouritesFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * @return A new instance of fragment FavoriteRecipesFragment.
     */
    public static FavouritesFragment newInstance() {
        return new FavouritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iRecipesRepository = new RecipesRepository(requireActivity().getApplication(),
                        this);
        recipesList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository();
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        iRecipesRepository = new RecipesRepository(requireActivity().getApplication(), this);
        recipeViewModel = new ViewModelProvider(requireActivity(), new RecipeViewModelFactory(iRecipesRepository)).get(RecipeViewModel.class);

        return inflater.inflate(R.layout.fragment_m_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iRecipesRepository.getFavoriteRecipes();

        progressBar = view.findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.VISIBLE);
        userViewModel.getCurrentUserLiveData().observe(getViewLifecycleOwner(), result -> {
                    if (result.isSuccess()) {
                        recipesList = new ArrayList<>();

                        recipeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), resultRecipe -> {
                            if (resultRecipe != null && resultRecipe.isSuccess()) {
                                List<Recipe> allRecipeListFirebase =
                                        ((Result.ListRecipeResponseSuccess) resultRecipe).getData();
                                List<Recipe> favListFirebase = new ArrayList<>();
                                if (allRecipeListFirebase != null && allRecipeListFirebase.size() > 0) {
                                    for (Recipe recipe: allRecipeListFirebase) {
                                        if (recipe.isFavorite()) favListFirebase.add(recipe);
                                    }
                                    RecyclerView recyclerviewFavRecipes = view.findViewById(R.id.recyclerview_favourite_recipes);
                                    LinearLayoutManager layoutManager =
                                            new LinearLayoutManager(requireContext(),
                                                    LinearLayoutManager.VERTICAL, false);

                                    recipesRecyclerAdapter = new RecipesRecyclerAdapter(recipesList, favListFirebase,
                                            requireActivity().getApplication(),
                                            new RecipesRecyclerAdapter.OnItemClickListener() {
                                                @Override
                                                public void onRecipeItemClick(Recipe recipe) {
                                                    FavouritesFragmentDirections.ActionNavFavouritesToRecipeDetailsFragment action =
                                                            FavouritesFragmentDirections.actionNavFavouritesToRecipeDetailsFragment(recipe);
                                                    Navigation.findNavController(view).navigate(action);
                                                }

                                                @Override
                                                public void onFavoriteButtonPressed(int position) {
                                                    recipesList.get(position).setIsFavorite(!recipesList.get(position).isFavorite());
                                                    iRecipesRepository.updateRecipes(recipesList.get(position));
                                                }
                                            });
                                    recyclerviewFavRecipes.setLayoutManager(layoutManager);
                                    recyclerviewFavRecipes.setAdapter(recipesRecyclerAdapter);
                                    recipesRecyclerAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Snackbar.make(requireView(), getString(R.string.error_retrieving_recipe), Snackbar.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Result.Error error = ((Result.Error) result);
                        Snackbar.make(requireView(), error.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });



    }

    public void onSuccess(List<Recipe> recipesList) {
        if (recipesList != null) {
            this.recipesList.clear();
            this.recipesList.addAll(recipesList);
            requireActivity().runOnUiThread(() -> {
                recipesRecyclerAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            });
        }
    }

    public void onFailure(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                errorMessage, Snackbar.LENGTH_LONG).show();
    }

    public void onRecipesFavoriteStatusChanged(Recipe recipe) {
        recipesList.remove(recipe);
        requireActivity().runOnUiThread(() -> recipesRecyclerAdapter.notifyDataSetChanged());
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                getString(R.string.recipes_removed_from_favorite_list_message),
                Snackbar.LENGTH_LONG).show();
    }
}
