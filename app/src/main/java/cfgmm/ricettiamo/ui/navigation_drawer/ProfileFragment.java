package cfgmm.ricettiamo.ui.navigation_drawer;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.MyRecipesRecyclerAdapter;
import cfgmm.ricettiamo.data.repository.recipe.IRecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesRepository;
import cfgmm.ricettiamo.data.repository.recipe.RecipesResponseCallback;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.databinding.FragmentMProfileBinding;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.model.User;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.RecipeViewModel;
import cfgmm.ricettiamo.viewmodel.RecipeViewModelFactory;
import cfgmm.ricettiamo.viewmodel.UserViewModel;
import cfgmm.ricettiamo.viewmodel.UserViewModelFactory;

public class ProfileFragment extends Fragment implements RecipesResponseCallback {

    private UserViewModel userViewModel;
    private RecipeViewModel recipeViewModel;
    private FragmentMProfileBinding binding;

    private String id;

    public ProfileFragment() {
        // Required empty public constructor
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
        binding = FragmentMProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        binding.recipeCL.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_my_recipe));
        binding.positionCL.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_profile_to_nav_ranking));

        userViewModel.getCurrentPhotoLiveData().observe(getViewLifecycleOwner(), result -> {
            binding.pProgressCircular.setVisibility(VISIBLE);
            if (result.isSuccess()) {
                Uri photo = ((Result.PhotoResponseSuccess) result).getData();
                try {
                    Glide.with(this)
                            .load(photo)
                            .circleCrop()
                            .placeholder(R.drawable.user)
                            .into(binding.user);
                } catch (Exception e) {
                }
            }
            binding.pProgressCircular.setVisibility(GONE);
        });

        userViewModel.getCurrentUserLiveData().observe(getViewLifecycleOwner(), result -> {
            binding.pProgressCircular.setVisibility(VISIBLE);
            if (result.isSuccess()) {
                User user = ((Result.UserResponseSuccess) result).getData();
                id = user.getId();
                binding.displayName.setText(user.getDisplayName());

                binding.fullName.setText(user.getFullName());

                String userEmail = user.getEmail();
                if (isEmpty(userEmail)) {
                    binding.email.setVisibility(GONE);
                } else {
                    binding.email.setVisibility(VISIBLE);
                    binding.email.setText(userEmail);
                }

                String userDescription = user.getDescription();
                if (isEmpty(userDescription)) {
                    binding.descriptionCardView.setVisibility(INVISIBLE);
                    binding.description.setVisibility(INVISIBLE);
                } else {
                    binding.descriptionCardView.setVisibility(VISIBLE);
                    binding.description.setVisibility(VISIBLE);
                    binding.description.setText(userDescription);
                }

                String star = "" + user.getScore();
                binding.score.setText(star);

                recipeViewModel.getMyRecipes(id).observe(getViewLifecycleOwner(), resultRecipe -> {
                    if(resultRecipe != null && resultRecipe.isSuccess()) {
                        List<Recipe> recipeDate = ((Result.ListRecipeResponseSuccess) resultRecipe).getData();
                        if(recipeDate != null && recipeDate.size() > 0) {
                            binding.noRecipes.setVisibility(GONE);
                            binding.withRecipes.setVisibility(VISIBLE);

                            binding.firstRecipe.setText(recipeDate.get(0).getName());
                            binding.lastRecipe.setText(recipeDate.get(recipeDate.size() - 1).getName());
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
            binding.pProgressCircular.setVisibility(GONE);
        });

        userViewModel.getPosition().observe(getViewLifecycleOwner(), result -> {
            binding.pProgressCircular.setVisibility(VISIBLE);
            if (result.isSuccess()) {
                String position = "#" + ((Result.PositionResponseSuccess) result).getData();
                binding.position.setText(position);
            } else {
                Result.Error error = ((Result.Error) result);
                Snackbar.make(requireView(), error.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
            binding.pProgressCircular.setVisibility(GONE);
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