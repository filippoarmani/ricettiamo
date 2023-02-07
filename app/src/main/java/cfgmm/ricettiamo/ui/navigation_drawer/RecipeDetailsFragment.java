package cfgmm.ricettiamo.ui.navigation_drawer;

import static android.text.TextUtils.isEmpty;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.CommentAdapter;
import cfgmm.ricettiamo.adapter.IngredientDetailRecipesRecyclerAdapter;
import cfgmm.ricettiamo.adapter.StepsRecyclerAdapter;
import cfgmm.ricettiamo.data.repository.comment.ICommentRepository;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.databinding.FragmentMRecipeDetailsBinding;
import cfgmm.ricettiamo.model.Comment;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.CommentViewModel;
import cfgmm.ricettiamo.viewmodel.CommentViewModelFactory;
import cfgmm.ricettiamo.viewmodel.UserViewModel;
import cfgmm.ricettiamo.viewmodel.UserViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailsFragment extends Fragment {

    private static final String TAG = RecipeDetailsFragment.class.getSimpleName();

    private FragmentMRecipeDetailsBinding fragmentRecipeDetailsBinding;
    private CommentAdapter adapter;
    private UserViewModel userViewModel;
    private CommentViewModel commentViewModel;
    private List<Comment> comments;
    private IngredientDetailRecipesRecyclerAdapter ingredientDetailRecipesRecyclerAdapter;
    private StepsRecyclerAdapter stepsRecyclerAdapter;

    public RecipeDetailsFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * @return A new instance of fragment RecipeDetailFragment.
     */
    public static RecipeDetailsFragment newInstance() {  return new RecipeDetailsFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository();
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);

        ICommentRepository commentRepository = ServiceLocator.getInstance().getCommentRepository();
        commentViewModel = new ViewModelProvider(requireActivity(), new CommentViewModelFactory(commentRepository)).get(CommentViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentRecipeDetailsBinding = FragmentMRecipeDetailsBinding.inflate(inflater, container, false);
        return fragmentRecipeDetailsBinding.getRoot();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentRecipeDetailsBinding.reviewRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new CommentAdapter(requireContext(), comments);
        fragmentRecipeDetailsBinding.reviewRecyclerView.setAdapter(adapter);

        assert getArguments() != null;
        Recipe recipe = RecipeDetailsFragmentArgs.fromBundle(getArguments()).getRecipe();
        this.comments = new ArrayList<>();

        RecyclerView recyclerviewDetailRecipeIngredients = view.findViewById(R.id.recyclerview_ingredients_recipe_detail);
        LinearLayoutManager layoutManageringredients =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);
        ingredientDetailRecipesRecyclerAdapter = new IngredientDetailRecipesRecyclerAdapter(recipe.getIngredientsList(),
                requireActivity().getApplication());

        recyclerviewDetailRecipeIngredients.setLayoutManager(layoutManageringredients);
        recyclerviewDetailRecipeIngredients.setAdapter(ingredientDetailRecipesRecyclerAdapter);

        RecyclerView recyclerviewDetailRecipesteps = view.findViewById(R.id.recyclerview_steps_recipe_detail);
        LinearLayoutManager layoutManagersteps =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);
        int stepAnalyzeNumber = 0;
        for (int i = 0; i < recipe.getSteps().size(); i++) {
            if (recipe.getSteps().get(i).getName().equals("")) stepAnalyzeNumber = i;
        }
        stepsRecyclerAdapter =
                new StepsRecyclerAdapter(recipe.getSteps().get(stepAnalyzeNumber).getSteps(),
                requireActivity().getApplication());

        recyclerviewDetailRecipesteps.setLayoutManager(layoutManagersteps);
        recyclerviewDetailRecipesteps.setAdapter(stepsRecyclerAdapter);


        //commentViewModel.readComment("" + recipe.getId());

        Glide.with(fragmentRecipeDetailsBinding.imageRecipe.getContext()).
                load(recipe.getUrlToImage()).
                placeholder(R.drawable.ic_add).
                into(fragmentRecipeDetailsBinding.imageRecipe);

        fragmentRecipeDetailsBinding.nameRecipe.setText(recipe.getName());
        fragmentRecipeDetailsBinding.srcServingsValue.setText(String.valueOf(recipe.getServings()));
        fragmentRecipeDetailsBinding.srcCostValue.setText(String.valueOf(recipe.getCost() * recipe.getServings() / 100.0f));
        fragmentRecipeDetailsBinding.srcPrepTimeValue.setText(String.valueOf(recipe.getPrepTime()));

        fragmentRecipeDetailsBinding.addReviewButton.setOnClickListener(v -> {
            String text = fragmentRecipeDetailsBinding.addRewiewTextLayout.getEditText().getText().toString().trim();
            String score = fragmentRecipeDetailsBinding.addScoreLayout.getEditText().getText().toString().trim();

            boolean ok = true;
            if (isEmpty(text)) {
                fragmentRecipeDetailsBinding.addRewiewTextLayout.setError(getString(R.string.empty_fields));
                ok = false;
            }

            if (isEmpty(score)) {
                fragmentRecipeDetailsBinding.addScoreLayout.setError(getString(R.string.empty_fields));
                ok = false;
            }

            Result userResult = userViewModel.getCurrentUserLiveData().getValue();
            if (ok && userResult != null && userResult.isSuccess()) {
                String idRecipe = "" + recipe.getId();
                String idUser;

                if (userViewModel.isLoggedUser())
                    idUser = ((Result.UserResponseSuccess) userResult).getData().getDisplayName();
                else
                    idUser = getString(R.string.noLoggedUser);

                commentViewModel.writeNewComment(new Comment("" + (comments.size() + 1), idUser, idRecipe, text, Integer.parseInt(score)), recipe.getAuthor());
            } else {
                Snackbar.make(requireView(), R.string.unexpected_error, Snackbar.LENGTH_LONG).show();
            }
        });

        commentViewModel.getCurrentCommentListLiveData("" + recipe.getId()).observe(getViewLifecycleOwner(), commentResult -> {
            if (commentResult != null && commentResult.isSuccess()) {
                comments = ((Result.CommentResponseSuccess) commentResult).getData();

                if (comments.size() == 0) {
                    fragmentRecipeDetailsBinding.noReview.setVisibility(View.VISIBLE);
                    fragmentRecipeDetailsBinding.reviewRecyclerView.setVisibility(View.GONE);
                } else {
                    fragmentRecipeDetailsBinding.noReview.setVisibility(View.GONE);
                    fragmentRecipeDetailsBinding.reviewRecyclerView.setVisibility(View.VISIBLE);
                }

                fragmentRecipeDetailsBinding.reviewRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                adapter = new CommentAdapter(requireContext(), comments);
                fragmentRecipeDetailsBinding.reviewRecyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        comments = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentRecipeDetailsBinding = null;
    }
}