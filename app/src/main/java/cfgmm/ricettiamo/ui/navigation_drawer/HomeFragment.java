package cfgmm.ricettiamo.ui.navigation_drawer;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.RecipesRecyclerAdapter;
import cfgmm.ricettiamo.data.repository.recipe.IRecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesResponseCallback;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.viewmodel.RecipeViewModel;
import cfgmm.ricettiamo.viewmodel.RecipeViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements RecipesResponseCallback {

    private RecipeViewModel recipeViewModel;

    RecipesRecyclerAdapter adapterStarter;
    RecipesRecyclerAdapter adapterFirst;
    RecipesRecyclerAdapter adapterSecond;
    RecipesRecyclerAdapter adapterDessert;

    List<Recipe> recipeStarterList;
    List<Recipe> recipeFirstList;
    List<Recipe> recipeSecondList;
    List<Recipe> recipeDessertList;

    RecyclerView rv_starters, rv_first, rv_second, rv_desserts;
    FloatingActionButton floatingActionButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IRecipesRepository iRecipesRepository = new RecipesRepository(requireActivity().getApplication(), this);
        recipeViewModel = new ViewModelProvider(requireActivity(), new RecipeViewModelFactory(iRecipesRepository)).get(RecipeViewModel.class);

        recipeStarterList = new ArrayList<>();
        recipeFirstList = new ArrayList<>();
        recipeSecondList = new ArrayList<>();
        recipeDessertList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_m_home, container, false);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        floatingActionButton = view.findViewById(R.id.search_floating_button);
        floatingActionButton.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.action_nav_home_to_search_recipes));

        rv_starters = view.findViewById(R.id.rv_starters);
        rv_first = view.findViewById(R.id.rv_first);
        rv_second = view.findViewById(R.id.rv_second);
        rv_desserts = view.findViewById(R.id.rv_dessert);

        rv_starters.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        rv_first.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        rv_second.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        rv_desserts.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));

        adapterStarter = new RecipesRecyclerAdapter(recipeStarterList, requireActivity().getApplication(), new RecipesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onRecipeItemClick(Recipe recipe) {
                HomeFragmentDirections.ActionNavHomeToRecipeDetailsFragment action =
                        HomeFragmentDirections.actionNavHomeToRecipeDetailsFragment(recipe);
                Navigation.findNavController(view).navigate((NavDirections) action);
            }

            @Override
            public void onFavoriteButtonPressed(int position) {
                recipeStarterList.get(position).setIsFavorite(!recipeStarterList.get(position).isFavorite());
                recipeViewModel.updateRecipes(recipeStarterList.get(position));
            }
        });

        adapterFirst = new RecipesRecyclerAdapter(recipeFirstList, requireActivity().getApplication(), new RecipesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onRecipeItemClick(Recipe recipe) {
                HomeFragmentDirections.ActionNavHomeToRecipeDetailsFragment action =
                        HomeFragmentDirections.actionNavHomeToRecipeDetailsFragment(recipe);
                Navigation.findNavController(view).navigate((NavDirections) action);
            }

            @Override
            public void onFavoriteButtonPressed(int position) {
                recipeFirstList.get(position).setIsFavorite(!recipeFirstList.get(position).isFavorite());
                recipeViewModel.updateRecipes(recipeFirstList.get(position));
            }
        });

        adapterSecond = new RecipesRecyclerAdapter(recipeSecondList, requireActivity().getApplication(), new RecipesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onRecipeItemClick(Recipe recipe) {
                HomeFragmentDirections.ActionNavHomeToRecipeDetailsFragment action =
                        HomeFragmentDirections.actionNavHomeToRecipeDetailsFragment(recipe);
                Navigation.findNavController(view).navigate((NavDirections) action);
            }

            @Override
            public void onFavoriteButtonPressed(int position) {
                recipeSecondList.get(position).setIsFavorite(!recipeSecondList.get(position).isFavorite());
                recipeViewModel.updateRecipes(recipeSecondList.get(position));
            }
        });

        adapterDessert = new RecipesRecyclerAdapter(recipeDessertList, requireActivity().getApplication(), new RecipesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onRecipeItemClick(Recipe recipe) {
                HomeFragmentDirections.ActionNavHomeToRecipeDetailsFragment action =
                        HomeFragmentDirections.actionNavHomeToRecipeDetailsFragment(recipe);
                Navigation.findNavController(view).navigate((NavDirections) action);
            }

            @Override
            public void onFavoriteButtonPressed(int position) {
                recipeDessertList.get(position).setIsFavorite(!recipeDessertList.get(position).isFavorite());
                recipeViewModel.updateRecipes(recipeDessertList.get(position));
            }
        });

        rv_starters.setAdapter(adapterStarter);
        rv_first.setAdapter(adapterFirst);
        rv_second.setAdapter(adapterSecond);
        rv_desserts.setAdapter(adapterDessert);

        recipeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), result -> {
            if(result != null && result.isSuccess()) {
                List<Recipe> recipeAllList = ((Result.ListRecipeResponseSuccess) result).getData();

                for(Recipe recipe: recipeAllList) {
                    if(recipe.getDishTypes().get(0).equals("Antipasti") || recipe.getDishTypes().get(0).equals("Starters")) {
                        recipeStarterList.add(recipe);
                        adapterStarter.notifyDataSetChanged();
                    }

                    if(recipe.getDishTypes().get(0).equals("Primi piatti") || recipe.getDishTypes().get(0).equals("First Dishes")) {
                        recipeFirstList.add(recipe);
                        adapterFirst.notifyDataSetChanged();
                    }

                    if(recipe.getDishTypes().get(0).equals("Secondi piatti") || recipe.getDishTypes().get(0).equals("Second Dishes")) {
                        recipeSecondList.add(recipe);
                        adapterSecond.notifyDataSetChanged();
                    }

                    if(recipe.getDishTypes().get(0).equals("Dolci") || recipe.getDishTypes().get(0).equals("Dessert")) {
                        recipeDessertList.add(recipe);
                        adapterDessert.notifyDataSetChanged();
                    }
                }

            }
        });

        rv_first.addOnScrollListener(new RecyclerView.OnScrollListener() {

           /* @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isConnected = isConnected();
                if (isConnected && totalItemCount != newsViewModel.getTotalResults()) {

                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();

                    // Condition to enable the loading of other news while the user is scrolling the list
                    if (totalItemCount == visibleItemCount ||
                                    (totalItemCount <= (lastVisibleItem + threshold) &&
                                    dy > 0 &&
                                    !newsViewModel.isLoading()
                            ) &&
                                    newsViewModel.getNewsResponseLiveData().getValue() != null &&
                                    newsViewModel.getCurrentResults() != newsViewModel.getTotalResults()
                    ) {
                        MutableLiveData<Result> newsListMutableLiveData = newsViewModel.getNewsResponseLiveData();

                        if (newsListMutableLiveData.getValue() != null &&
                                newsListMutableLiveData.getValue().isSuccess()) {

                            newsViewModel.setLoading(true);
                            newsList.add(null);
                            newsRecyclerViewAdapter.notifyItemRangeInserted(newsList.size(),
                                    newsList.size() + 1);

                            int page = newsViewModel.getPage() + 1;
                            newsViewModel.setPage(page);
                            newsViewModel.fetchNews(country);
                        }
                    }
                }
            }*/
        });

    }


    @Override
    public void onSuccess(List<Recipe> recipesList) {

    }

    @Override
    public void onFailure(String errorMessage) {

    }

    @Override
    public void onRecipesFavoriteStatusChanged(Recipe recipe) {

    }
}