package cfgmm.ricettiamo.ui.navigation_drawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.RecipesRecyclerAdapter;
import cfgmm.ricettiamo.data.repository.recipe.IRecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesResponseCallback;
import cfgmm.ricettiamo.databinding.FragmentMSearchRecipesBinding;
import cfgmm.ricettiamo.model.Recipe;

public class SearchRecipesFragment extends Fragment implements RecipesResponseCallback {

    private FragmentMSearchRecipesBinding fragmentSearchRecipesBinding;
    private TextInputLayout inputRecipe;
    private List<Recipe> recipeList;
    private RecipesRecyclerAdapter recipesRecyclerAdapter;
    private IRecipesRepository iRecipesRepository;
    private
    String search;

    public SearchRecipesFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * @return A new instance of fragment SearchRecipe.
     */
    public static SearchRecipesFragment newInstance() {  return new SearchRecipesFragment(); }

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

        recipesRecyclerAdapter = new RecipesRecyclerAdapter(recipeList, requireActivity().getApplication(),
                new RecipesRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onRecipeItemClick(Recipe recipe) {
                        SearchRecipesFragmentDirections.ActionSearchRecipesToRecipeDetailsFragment action =
                                SearchRecipesFragmentDirections.actionSearchRecipesToRecipeDetailsFragment(recipe);
                        Navigation.findNavController(view).navigate(action);
                    }

                    @Override
                    public void onFavoriteButtonPressed(int position) {
                        recipeList.get(position).setIsFavorite(!recipeList.get(position).isFavorite());
                        iRecipesRepository.updateRecipes(recipeList.get(position));
                    }
                });
        recyclerviewSearchRecipes.setLayoutManager(layoutManager);
        recyclerviewSearchRecipes.setAdapter(recipesRecyclerAdapter);
    }

    @Override
    public void onSuccess(List<Recipe> recipesList) {
        if (recipesList != null) {
            this.recipeList.clear();
            this.recipeList.addAll(recipesList);
        }

        requireActivity().runOnUiThread(() -> recipesRecyclerAdapter.notifyDataSetChanged());
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
                    getString(R.string.recipes_added_to_favorite_list_message),
                    Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.recipes_removed_from_favorite_list_message),
                    Snackbar.LENGTH_LONG).show();
        }
    }
}