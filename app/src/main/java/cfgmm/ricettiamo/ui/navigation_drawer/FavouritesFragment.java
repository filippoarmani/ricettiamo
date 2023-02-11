package cfgmm.ricettiamo.ui.navigation_drawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.RecipesRecyclerAdapter;
import cfgmm.ricettiamo.data.repository.recipe.IRecipesRepository;
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
public class FavouritesFragment extends Fragment {

    private static final String TAG = FavouritesFragment.class.getSimpleName();

    private List<Recipe> allFavoriteList;
    private List<Recipe> firebaseFavoriteList;
    private List<Recipe> memoryFavoriteList;

    private RecipesRecyclerAdapter recipesRecyclerAdapter;
    private CircularProgressIndicator progressBar;
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

        allFavoriteList = new ArrayList<>();
        firebaseFavoriteList = new ArrayList<>();
        memoryFavoriteList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository();
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        IRecipesRepository iRecipesRepository = ServiceLocator.getInstance().getRecipesRepository(requireActivity().getApplication());
        recipeViewModel = new ViewModelProvider(requireActivity(), new RecipeViewModelFactory(iRecipesRepository)).get(RecipeViewModel.class);

        return inflater.inflate(R.layout.fragment_m_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress_bar);

        recipeViewModel.getFavoriteRecipes().observe(getViewLifecycleOwner(), result -> {
            progressBar.setVisibility(View.VISIBLE);
            if(result != null && result.isSuccess()) {
                memoryFavoriteList.clear();
                memoryFavoriteList.addAll(((Result.ListRecipeResponseSuccess) result).getData());

                allFavoriteList.clear();
                allFavoriteList.addAll(memoryFavoriteList);
                allFavoriteList.addAll(firebaseFavoriteList);

                requireActivity().runOnUiThread(() -> recipesRecyclerAdapter.notifyDataSetChanged());
            }
            progressBar.setVisibility(View.GONE);
        });

        userViewModel.getCurrentUserLiveData().observe(getViewLifecycleOwner(), result -> {
            progressBar.setVisibility(View.VISIBLE);
            if (result != null && result.isSuccess()) {

                recipeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), resultRecipe -> {
                    if (resultRecipe != null && resultRecipe.isSuccess()) {
                        List<Recipe> allRecipeListFirebase = ((Result.ListRecipeResponseSuccess) resultRecipe).getData();

                        if (allRecipeListFirebase != null && allRecipeListFirebase.size() > 0) {
                            firebaseFavoriteList.clear();
                            for (Recipe recipe: allRecipeListFirebase) {
                                if (recipe.isFavorite())
                                    firebaseFavoriteList.add(recipe);
                            }

                            allRecipeListFirebase.clear();
                            allRecipeListFirebase.addAll(memoryFavoriteList);
                            allRecipeListFirebase.addAll(firebaseFavoriteList);

                            requireActivity().runOnUiThread(() -> recipesRecyclerAdapter.notifyDataSetChanged());
                        }
                    } else {
                        Snackbar.make(requireView(), getString(R.string.error_retrieving_recipe), Snackbar.LENGTH_SHORT).show();
                    }
                });
            } else {
                Result.Error error = ((Result.Error) result);
                Snackbar.make(requireView(), error.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });

        RecyclerView recyclerviewFavRecipes = view.findViewById(R.id.recyclerview_favourite_recipes);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

        recipesRecyclerAdapter = new RecipesRecyclerAdapter(allFavoriteList,
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
                        allFavoriteList.get(position).setIsFavorite(!allFavoriteList.get(position).isFavorite());
                        recipeViewModel.updateRecipes(allFavoriteList.get(position));
                    }
                });

        recyclerviewFavRecipes.setLayoutManager(layoutManager);
        recyclerviewFavRecipes.setAdapter(recipesRecyclerAdapter);
    }

}
