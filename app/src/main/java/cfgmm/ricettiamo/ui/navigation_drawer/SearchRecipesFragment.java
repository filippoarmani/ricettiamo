package cfgmm.ricettiamo.ui.navigation_drawer;

import static android.view.View.GONE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import cfgmm.ricettiamo.databinding.FragmentMSearchRecipesBinding;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.RecipeViewModel;
import cfgmm.ricettiamo.viewmodel.RecipeViewModelFactory;

public class SearchRecipesFragment extends Fragment {

    private FragmentMSearchRecipesBinding fragmentSearchRecipesBinding;
    private TextInputLayout inputRecipe;
    private List<Recipe> recipeList;
    private RecipesRecyclerAdapter recipesRecyclerAdapter;
    private RecipeViewModel recipeViewModel;
    private String search;

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

        IRecipesRepository iRecipesRepository = ServiceLocator.getInstance().getRecipesRepository(requireActivity().getApplication());
        recipeViewModel = new ViewModelProvider(requireActivity(), new RecipeViewModelFactory(iRecipesRepository)).get(RecipeViewModel.class);

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
        TextView noRecipesFound = view.findViewById(R.id.noRecipesFound);

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
                        recipeViewModel.updateRecipes(recipeList.get(position));
                    }
                });

        recyclerviewSearchRecipes.setLayoutManager(layoutManager);
        recyclerviewSearchRecipes.setAdapter(recipesRecyclerAdapter);

        btnSearch.setOnClickListener(v -> {
            fragmentSearchRecipesBinding.progress.setVisibility(View.VISIBLE);
            search = inputRecipe.getEditText().getText().toString().trim();
            if (search.length() != 0) {
                recipeViewModel.getSearchRecipes(search).observe(getViewLifecycleOwner(), result -> {
                    if(result != null && result.isSuccess()) {
                        recipeList.clear();
                        recipeList.addAll(((Result.ListRecipeResponseSuccess) result).getData());
                        if(recipeList == null || recipeList.size() == 0) {
                            recyclerviewSearchRecipes.setVisibility(GONE);
                            noRecipesFound.setVisibility(View.VISIBLE);
                        } else {
                            recyclerviewSearchRecipes.setVisibility(View.VISIBLE);
                            noRecipesFound.setVisibility(View.GONE);
                        }

                        requireActivity().runOnUiThread(() -> recipesRecyclerAdapter.notifyDataSetChanged());
                    } else {
                        recyclerviewSearchRecipes.setVisibility(View.VISIBLE);
                        noRecipesFound.setVisibility(View.GONE);
                    }
                });
            } else {
                recyclerviewSearchRecipes.setVisibility(GONE);
                noRecipesFound.setVisibility(View.VISIBLE);
            }
            fragmentSearchRecipesBinding.progress.setVisibility(GONE);
        });
    }

}