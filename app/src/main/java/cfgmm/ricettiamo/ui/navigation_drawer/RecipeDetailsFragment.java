package cfgmm.ricettiamo.ui.navigation_drawer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.databinding.FragmentMRecipeDetailsBinding;
import cfgmm.ricettiamo.model.Recipe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailsFragment extends Fragment {

    private static final String TAG = RecipeDetailsFragment.class.getSimpleName();

    private FragmentMRecipeDetailsBinding fragmentRecipeDetailsBinding;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment NewsDetailFragment.
     */
    public static RecipeDetailsFragment newInstance() {
        return new RecipeDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentRecipeDetailsBinding = FragmentMRecipeDetailsBinding.inflate(inflater, container, false);
        return fragmentRecipeDetailsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Recipe recipe = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipe();

        Glide.with(fragmentRecipeDetailsBinding.imageRecipe.getContext()).
                load(recipe.getUrlToImage()).
                placeholder(R.drawable.ic_add).
                into(fragmentRecipeDetailsBinding.imageRecipe);

        fragmentRecipeDetailsBinding.nameRecipe.setText(recipe.getName());
        fragmentRecipeDetailsBinding.srcServingsValue.setText(String.valueOf(recipe.getServings()));
        fragmentRecipeDetailsBinding.srcCostValue.setText(String.valueOf(recipe.getCost()));
        fragmentRecipeDetailsBinding.srcPrepTimeValue.setText(String.valueOf(recipe.getPrepTime()));

    }
}