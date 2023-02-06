package cfgmm.ricettiamo.ui.navigation_drawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.SearchRecipesAdapter;
import cfgmm.ricettiamo.data.repository.recipe.IRecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesResponseCallback;
import cfgmm.ricettiamo.databinding.FragmentMSearchRecipesBinding;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.viewmodel.RecipeViewModel;

public class SearchRecipes extends Fragment implements RecipesResponseCallback {

    private FragmentMSearchRecipesBinding fragmentSearchRecipesBinding;

    private TextInputLayout inputRecipe;
    private List<Recipe> recipeList;
    private SearchRecipesAdapter searchRecipesAdapter;
    private IRecipesRepository iRecipesRepository;
    private
    String search;
    private RecipeViewModel recipeViewModel;

    public SearchRecipes() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * @return A new instance of fragment SearchRecipe.
     */
    public static SearchRecipes newInstance() {  return new SearchRecipes(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iRecipesRepository = new RecipesRepository(requireActivity().getApplication(), this);
        recipeList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentSearchRecipesBinding = FragmentMSearchRecipesBinding.inflate(inflater, container, false);
        return fragmentSearchRecipesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        inputRecipe = view.findViewById(R.id.search_recipes_input);
        ImageButton btnSearch = view.findViewById(R.id.btn_search);

        btnSearch.setOnClickListener(v -> {
            search = inputRecipe.getEditText().getText().toString().trim();
            if (search.length() != 0) {
                iRecipesRepository.getRecipes(search);
            } else
                Snackbar.make(getView(), R.string.empty_fields, Snackbar.LENGTH_LONG).show();
        });

        RecyclerView recyclerviewSearchRecipes = view.findViewById(R.id.recyclerview_search_recipes);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

        searchRecipesAdapter = new SearchRecipesAdapter(recipeList, requireActivity().getApplication(),
                new SearchRecipesAdapter.OnItemClickListener() {
                    @Override
                    public void onRecipeItemClick(Recipe recipe) {
                        Snackbar.make(view, recipe.getName(), Snackbar.LENGTH_SHORT).show();

                        /*todo: query per aggiungere gli ingredienti. per la conversione ci dovrebbe
                           essere il codice funzionante commentato in recipeRepository (da spostare
                            nel metodo corretto)*/
                        cfgmm.ricettiamo.ui.navigation_drawer.SearchRecipesDirections.ActionSearchRecipesToRecipeDetailsFragment action =
                                SearchRecipesDirections.actionSearchRecipesToRecipeDetailsFragment(recipe);
                        Navigation.findNavController(view).navigate((NavDirections) action);
                    }

                    @Override
                    public void onFavoriteButtonPressed(int position) {
                        recipeList.get(position).setIsFavorite(!recipeList.get(position).isFavorite());
                        recipeViewModel.updateRecipes(recipeList.get(position));
                    }
                });
        recyclerviewSearchRecipes.setLayoutManager(layoutManager);
        recyclerviewSearchRecipes.setAdapter(searchRecipesAdapter);
    }

    /**
     * Checks if the device is connected to Internet.
     * See: https://developer.android.com/training/monitoring-device-state/connectivity-status-type#DetermineConnection
     * @return true if the device is connected to Internet; false otherwise.
     */
    /*private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }*/

    @Override
    public void onSuccess(List<Recipe> recipesList) {
        if (recipesList != null) {
            this.recipeList.clear();
            this.recipeList.addAll(recipesList);
        }

        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                searchRecipesAdapter.notifyDataSetChanged();
                //progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onFailure(String errorMessage) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onRecipesFavoriteStatusChanged(Recipe recipe) {
        if (recipe.isFavorite()) {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.recipes_removed_from_favorite_list_message),
                    Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.recipes_removed_from_favorite_list_message),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    public void UpdateRecipes(Recipe recipe) {

    }
}