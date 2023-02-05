package cfgmm.ricettiamo.ui.navigation_drawer;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.MyRecipesRecyclerAdapter;
import cfgmm.ricettiamo.data.repository.recipe.IRecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesResponseCallback;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.databinding.FragmentMMyRecipesBinding;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.Result;
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
    private List<Recipe> myRecipesList;
    private MyRecipesRecyclerAdapter adapter;

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

        IRecipesRepository iRecipesRepository = new RecipesRepository(requireActivity().getApplication(), this);
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

        Result result = userViewModel.getCurrentUserLiveData().getValue();
        if(result != null && result.isSuccess()) {
            id = ((Result.UserResponseSuccess) result).getData().getId();

            recipeViewModel.getMyRecipes(id).observe(getViewLifecycleOwner(), resultRecipe -> {
                if (resultRecipe != null && resultRecipe.isSuccess()) {
                    myRecipesList = ((Result.ListRecipeResponseSuccess) resultRecipe).getData();

                    switch (myRecipesList.size()) {
                        case 0:
                            setVisibility(GONE, GONE, GONE, GONE, GONE, GONE, VISIBLE);
                            break;
                        case 1:
                            setVisibility(VISIBLE, VISIBLE, GONE, GONE, GONE, GONE, GONE);
                            setText(binding.nameRecipe1, binding.recipeStar1, myRecipesList.get(0));
                            break;
                        case 2:
                            setVisibility(VISIBLE, VISIBLE, VISIBLE, GONE, GONE, GONE, GONE);
                            setText(binding.nameRecipe1, binding.recipeStar1, myRecipesList.get(0));
                            setText(binding.nameRecipe1, binding.recipeStar1, myRecipesList.get(1));
                            break;
                        case 3:
                            setVisibility(VISIBLE, VISIBLE, VISIBLE, VISIBLE, GONE, GONE, GONE);
                            setText(binding.nameRecipe1, binding.recipeStar1, myRecipesList.get(0));
                            setText(binding.nameRecipe2, binding.recipeStar2, myRecipesList.get(1));
                            setText(binding.nameRecipe3, binding.recipeStar3, myRecipesList.get(2));
                            break;
                        default:
                            setVisibility(VISIBLE, VISIBLE, VISIBLE, VISIBLE, VISIBLE, VISIBLE, GONE);
                            setText(binding.nameRecipe1, binding.recipeStar1, myRecipesList.get(0));
                            setText(binding.nameRecipe2, binding.recipeStar2, myRecipesList.get(1));
                            setText(binding.nameRecipe3, binding.recipeStar3, myRecipesList.get(2));

                            binding.otherRecipes.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                            adapter = new MyRecipesRecyclerAdapter(requireContext(), myRecipesList.subList(3, myRecipesList.size()));
                            binding.otherRecipes.setAdapter(adapter);
                            break;
                    }

                } else {
                    Snackbar.make(requireView(), getString(R.string.error_retrieving_recipe), Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            Snackbar.make(requireView(), getString(R.string.unexpected_error), Snackbar.LENGTH_LONG).show();
        }
    }

    public void setVisibility(int v1, int v2, int v3, int v4, int v5, int v6, int v7) {
        binding.t1.setVisibility(v1);
        binding.card1.setVisibility(v2);
        binding.card2.setVisibility(v3);
        binding.card3.setVisibility(v4);
        binding.t2.setVisibility(v5);
        binding.otherRecipes.setVisibility(v6);
        binding.noRecipes.setVisibility(v7);
    }

    public void setText(TextView nameTV, TextView scoreTV, Recipe recipe) {
        nameTV.setText(recipe.getName());
        String score = "" + recipe.getScore();
        scoreTV.setText(score);
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