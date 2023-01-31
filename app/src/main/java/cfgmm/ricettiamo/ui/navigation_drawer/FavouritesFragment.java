package cfgmm.ricettiamo.ui.navigation_drawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.RecipesRecyclerAdapter;
import cfgmm.ricettiamo.data.repository.recipe.IRecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesResponseCallback;
import cfgmm.ricettiamo.data.source.recipe.RecipesCallback;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.RecipeApiResponse;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouritesFragment extends Fragment implements RecipesResponseCallback {

    private static final String TAG = FavouritesFragment.class.getSimpleName();

    private ListView listViewFavNews;
    private Recipe[] recipesArray;
    private List<Recipe> recipesList;
    private IRecipesRepository iRecipesRepository;
    private RecipesRecyclerAdapter recipesListAdapter;
    private ProgressBar progressBar;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment FavoriteNewsFragment.
     */
    public static FavouritesFragment newInstance() {
        return new FavouritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iRecipesRepository =
                new RecipesRepository(requireActivity().getApplication(),
                        this);
        recipesList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                // It adds the menu item in the toolbar
                /*menuInflater.inflate(R.menu.top_app_bar, menu);*/
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                /*if (menuItem.getItemId() == R.id.delete) {
                    Log.d(TAG, "Delete menu item pressed");
                    iRecipesRepository.deleteFavoriteRecipes();
                }*/
                return false;
            }

        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        progressBar = view.findViewById(R.id.progress_bar);

        listViewFavNews = view.findViewById(R.id.listview_favrecipes);

        progressBar.setVisibility(View.VISIBLE);

        iRecipesRepository.getFavoriteRecipes();

        listViewFavNews.setOnItemClickListener((parent, view1, position, id) ->
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                        recipesList.get(position).getName(), Snackbar.LENGTH_SHORT).show());
    }

    private void useDefaultLisAdapter() {
        ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(requireContext(),
                android.R.layout.simple_list_item_1, recipesArray);
        listViewFavNews.setAdapter(adapter);
    }

    public void onSuccess(List<Recipe> recipesList) {
        if (recipesList != null) {
            this.recipesList.clear();
            this.recipesList.addAll(recipesList);
            requireActivity().runOnUiThread(() -> {
                recipesListAdapter.notifyDataSetChanged();
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
