package cfgmm.ricettiamo.ui.navigation_drawer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.SearchRecipesAdapter;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.RecipeResponse;
import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.util.ErrorMessagesUtil;
import cfgmm.ricettiamo.viewmodel.RecipeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchRecipes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchRecipes extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private TextInputLayout inputRecipe;
    private List<Recipe> recipeList;
    private SearchRecipesAdapter searchRecipesAdapter;
    private
    String search;
    private RecipeViewModel recipeViewModel;
    private String mParam1;
    private String mParam2;

    public SearchRecipes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment search_recipes.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchRecipes newInstance(String param1, String param2) {
        SearchRecipes fragment = new SearchRecipes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        recipeList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        inputRecipe = view.findViewById(R.id.search_recipes);
        ImageButton btnSearch = view.findViewById(R.id.btn_search);

        btnSearch.setOnClickListener(v -> {
            search = inputRecipe.getEditText().getText().toString().trim();
            if (search.length() != 0)
                Snackbar.make(getView(), search, Snackbar.LENGTH_LONG).show();
            else
                Snackbar.make(getView(), R.string.empty_fields, Snackbar.LENGTH_LONG).show();
        });

        RecyclerView recyclerviewSearchRecipes = view.findViewById(R.id.recyclerview_search_recipes);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

        recipeList.add(new Recipe("mattia", "pollo", 5, "veloce", "banana",
                null, null, null, true));
        recipeList.add(new Recipe("gianni", "acqua", 5, "veloceissima", "banana",
                null, null, null, false));
        recipeList.add(new Recipe("elisa", "salame", 3, "lungo", "banana",
                null, null, null, true));

        searchRecipesAdapter = new SearchRecipesAdapter(recipeList, requireActivity().getApplication(),
                new SearchRecipesAdapter.OnItemClickListener() {
                    @Override
                    public void onRecipeItemClick(Recipe recipe) {
                        /*CountryNewsFragmentDirections.ActionCountryNewsFragmentToNewsDetailFragment action =
                                CountryNewsFragmentDirections.actionCountryNewsFragmentToNewsDetailFragment(recipe);
                        Navigation.findNavController(view).navigate(action);*/
                    }

                    @Override
                    public void onFavoriteButtonPressed(int position) {
                        recipeList.get(position).setIsFavorite(!recipeList.get(position).getIsFavorite());
                        recipeViewModel.updateRecipes(recipeList.get(position));
                    }
                });
        recyclerviewSearchRecipes.setLayoutManager(layoutManager);
        recyclerviewSearchRecipes.setAdapter(searchRecipesAdapter);

        /*RecipeResponse recipeResponse = Result.RecipeResponseSuccess.getData();
        List<Recipe> getRecipes = recipeResponse.getRecipesList();

        if (!recipeViewModel.isLoading()) {
            if (recipeViewModel.isFirstLoading()) {
                recipeViewModel.setFirstLoading(false);
                this.recipeList.addAll(getRecipes);
            } else {
                // Updates related to the favorite status of the news
                recipeList.clear();
                recipeList.addAll(getRecipes);
            }
        } else {
            recipeViewModel.setLoading(false);

            for (int i = 0; i < recipeList.size(); i++) {
                if (recipeList.get(i) == null) {
                    recipeList.remove(recipeList.get(i));
                }
            }
        }*/
        /*recyclerviewSearchRecipes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isConnected = isConnected();

                if (isConnected && totalItemCount != recipeViewModel.getTotalResults()) {

                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();

                    // Condition to enable the loading of other news while the user is scrolling the list
                    if (totalItemCount == visibleItemCount ||
                            (totalItemCount <= (lastVisibleItem + threshold) &&
                                    dy > 0 &&
                                    !recipeViewModel.isLoading()
                            ) &&
                                    recipeViewModel.getNewsResponseLiveData().getValue() != null &&
                                    recipeViewModel.getCurrentResults() != recipeViewModel.getTotalResults()
                    ) {
                        MutableLiveData<Result> newsListMutableLiveData = recipeViewModel.getNewsResponseLiveData();

                        if (newsListMutableLiveData.getValue() != null &&
                                newsListMutableLiveData.getValue().isSuccess()) {

                            recipeViewModel.setLoading(true);
                            recipeList.add(null);
                            recyclerviewSearchRecipes.notifyItemRangeInserted(recipeList.size(),
                                    recipeList.size() + 1);
                            recipeViewModel.getRecipes(search);
                        }
                    }
                }
            }
        });*/
    }

    /**
     * Checks if the device is connected to Internet.
     * See: https://developer.android.com/training/monitoring-device-state/connectivity-status-type#DetermineConnection
     * @return true if the device is connected to Internet; false otherwise.
     */
    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}