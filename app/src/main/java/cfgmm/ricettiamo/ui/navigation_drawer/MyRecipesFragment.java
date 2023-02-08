package cfgmm.ricettiamo.ui.navigation_drawer;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.RecipesRecyclerAdapter;
import cfgmm.ricettiamo.data.repository.recipe.IRecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesResponseCallback;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.databinding.FragmentMMyRecipesBinding;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.model.User;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.RecipeViewModel;
import cfgmm.ricettiamo.viewmodel.RecipeViewModelFactory;
import cfgmm.ricettiamo.viewmodel.UserViewModel;
import cfgmm.ricettiamo.viewmodel.UserViewModelFactory;


public class MyRecipesFragment extends Fragment implements RecipesResponseCallback {

    private static final String TAG = MyRecipesFragment.class.getSimpleName();

    private FragmentMMyRecipesBinding binding;
    private RecipeViewModel recipeViewModel;
    private UserViewModel userViewModel;

    private String id;
    private List<Recipe> recipeList;
    private RecipesRecyclerAdapter adapter;

    private IRecipesRepository iRecipesRepository;

    public MyRecipesFragment() {
        // Required empty public constructor
    }

    public static MyRecipesFragment newInstance() {
        return new MyRecipesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository();
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        iRecipesRepository = new RecipesRepository(requireActivity().getApplication(), this);
        recipeViewModel = new ViewModelProvider(requireActivity(), new RecipeViewModelFactory(iRecipesRepository)).get(RecipeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMMyRecipesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel.getCurrentUserLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                User user = ((Result.UserResponseSuccess) result).getData();
                id = user.getId();
                recipeList = new ArrayList<>();

                recipeViewModel.getMyRecipes(id).observe(getViewLifecycleOwner(), resultRecipe -> {
                    if(resultRecipe != null && resultRecipe.isSuccess()) {
                        recipeList = ((Result.ListRecipeResponseSuccess) resultRecipe).getData();

                        if(recipeList != null && recipeList.size() > 0) {
                            binding.noRecipes.setVisibility(GONE);
                            binding.withRecipes.setVisibility(VISIBLE);

                            LinearLayoutManager layoutManager =
                                new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
                            adapter = new RecipesRecyclerAdapter(recipeList, requireActivity().getApplication(),
                                    new RecipesRecyclerAdapter.OnItemClickListener() {
                                        @Override
                                        public void onRecipeItemClick(Recipe recipe) {
                                            MyRecipesFragmentDirections.ActionNavMyRecipeToRecipeDetailsFragment action =
                                                    MyRecipesFragmentDirections.actionNavMyRecipeToRecipeDetailsFragment(recipe);
                                            Navigation.findNavController(view).navigate(action);
                                        }

                                        @Override
                                        public void onFavoriteButtonPressed(int position) {
                                            recipeList.get(position).setIsFavorite(!recipeList.get(position).isFavorite());
                                            iRecipesRepository.updateRecipes(recipeList.get(position));
                                        }
                                    });


                            binding.withRecipes.setLayoutManager(layoutManager);
                            binding.withRecipes.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            binding.noRecipes.setVisibility(VISIBLE);
                            binding.withRecipes.setVisibility(GONE);
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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