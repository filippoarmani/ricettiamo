package cfgmm.ricettiamo.ui.navigation_drawer;

import static android.text.TextUtils.isEmpty;
import static cfgmm.ricettiamo.util.Constants.IMAGE;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.IngredientsRecyclerAdapter;
import cfgmm.ricettiamo.adapter.StepsRecyclerAdapter;
import cfgmm.ricettiamo.data.repository.recipe.IRecipesRepository;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.databinding.FragmentMAddNewRecipeBinding;
import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.Recipe;
import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.model.Step;
import cfgmm.ricettiamo.model.StepsAnalyze;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.RecipeViewModel;
import cfgmm.ricettiamo.viewmodel.RecipeViewModelFactory;
import cfgmm.ricettiamo.viewmodel.UserViewModel;
import cfgmm.ricettiamo.viewmodel.UserViewModelFactory;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AddNewRecipeFragment extends Fragment {

    public static final String dateFormat = "yyyy-MM-dd HH:mm:ss";

    private FragmentMAddNewRecipeBinding binding;
    private IngredientsRecyclerAdapter adapterIngredient;
    private StepsRecyclerAdapter stepsRecyclerAdapter;
    private UserViewModel userViewModel;
    private RecipeViewModel recipeViewModel;

    private Uri mainPicture;
    private List<Ingredient> ingredientList;
    private List<Step> stepList;
    private String title;
    private String difficulty;
    private String cost;
    private String prepTime;
    private String serving;
    private String category;
    private String author;
    private long idRecipe;
    private int stepNumber;

    public AddNewRecipeFragment() {}

    public static AddNewRecipeFragment newInstance() {
        return new AddNewRecipeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ingredientList = new ArrayList<>();
        stepList = new ArrayList<>();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());

                builder.setTitle(getString(R.string.delete_recipe));
                builder.setMessage(getString(R.string.lose_data));
                builder.setPositiveButton(getString(R.string.come_back), (dialog, id) -> NavHostFragment.findNavController(AddNewRecipeFragment.this).popBackStack());
                builder.setNegativeButton(getString(R.string.cancel),null);
                builder.show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository();
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory(userRepository)).get(UserViewModel.class);
        Result result = userViewModel.getCurrentUserLiveData().getValue();
        if(result != null && result.isSuccess()) {
            author = ((Result.UserResponseSuccess) result).getData().getId();
        }

        IRecipesRepository iRecipesRepository = ServiceLocator.getInstance().getRecipesRepository(requireActivity().getApplication());
        recipeViewModel = new ViewModelProvider(requireActivity(), new RecipeViewModelFactory(iRecipesRepository)).get(RecipeViewModel.class);

        mainPicture = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMAddNewRecipeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //ingredient adapter
        binding.ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,  false));
        adapterIngredient = new IngredientsRecyclerAdapter(requireView(), ingredientList, new IngredientsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onDeleteButtonPressed(int position) {
            }
        });
        binding.ingredientRecyclerView.setAdapter(adapterIngredient);
        adapterIngredient.getItemTouchHelper().attachToRecyclerView(binding.ingredientRecyclerView);

        //step adapter
        binding.stepRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,  false));
        stepsRecyclerAdapter = new StepsRecyclerAdapter(stepList, requireActivity().getApplication());
        binding.stepRecyclerView.setAdapter(stepsRecyclerAdapter);
        stepsRecyclerAdapter.getItemTouchHelper().attachToRecyclerView(binding.stepRecyclerView);

        recipeViewModel.getAllRecipes().observe(getViewLifecycleOwner(), result -> {
            if(result != null && result.isSuccess()) {
                List<Recipe> recipeList = ((Result.ListRecipeResponseSuccess) result).getData();
                if(recipeList.size() == 0) {
                    idRecipe = 0;
                } else {
                    idRecipe = recipeList.get(recipeList.size() - 1).getId() + 1;
                }
            }
        });

        binding.addMainPicture.setOnClickListener(v -> startPhoto());

        binding.addIngredientButton.setOnClickListener(v -> {
            String nameIngredient = binding.addIngredientLayout.getEditText().getText().toString().trim();
            String quantity = binding.addQuantityLayout.getEditText().getText().toString().trim();
            String unit = binding.addUnitLayout.getEditText().getText().toString().trim();

            if(!isEmpty(nameIngredient) && !isEmpty(quantity) && !isEmpty(unit)) {
                long idIngredient;
                if(ingredientList.size() > 0)
                    idIngredient = ingredientList.get(ingredientList.size() - 1).getId() +1;
                else
                    idIngredient = 0;

                Ingredient ingredient = new Ingredient(idIngredient, nameIngredient, Float.parseFloat(quantity), unit);
                ingredientList.add(ingredient);
                adapterIngredient.notifyItemInserted(ingredientList.size() - 1);
            } else {
                binding.addIngredientLayout.setError(getString(R.string.empty_fields));
            }
        });

        binding.addStepButton.setOnClickListener(v -> {
            String stepDescription = binding.addStepLayout.getEditText().getText().toString().trim();
            if(stepList.size() == 0) {
                stepNumber = 1;
            } else {
                stepNumber = stepList.get(stepList.size() - 1).getNumber() + 1;
            }
            Step step = new Step(stepNumber, stepDescription);
            if(!isEmpty(stepDescription)) {
                stepList.add(step);
                stepsRecyclerAdapter.notifyItemInserted(stepList.size()-1);
            } else {
                binding.addStepLayout.setError(getString(R.string.empty_fields));
            }
        });

        binding.saveRecipe.setOnClickListener(v -> {
            title = binding.addTitleLayout.getEditText().getText().toString().trim();
            difficulty = binding.difficoltyLayout.getEditText().getText().toString().trim();
            cost = binding.costLayout.getEditText().getText().toString().trim();
            category = binding.typeLayout.getEditText().getText().toString().trim();
            prepTime = binding.prepTimeLayout.getEditText().getText().toString().trim();
            serving = binding.servingLayout.getEditText().getText().toString().trim();

            if(checkData()) {
                    MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(requireActivity());

                    alert.setTitle(getString(R.string.save_recipe));
                    alert.setMessage(getString(R.string.save_recipe_alert));
                    alert.setPositiveButton(getString(R.string.save), (dialog, id) -> {
                        binding.progress.setVisibility(View.VISIBLE);
                        recipeViewModel.uploadPhoto(mainPicture).observe(getViewLifecycleOwner(), urlResult -> {
                            if (urlResult != null && urlResult.isSuccess()) {
                                String urlToImage = ((Result.PhotoResponseSuccess) urlResult).getData().toString();

                                List<String> dishTypes = new ArrayList<>();
                                dishTypes.add(category);

                                List<StepsAnalyze> stepsAnalyzes = new ArrayList<>();
                                stepsAnalyzes.add(new StepsAnalyze("", stepList));

                                Recipe recipe = new Recipe(
                                        this.idRecipe,
                                        author,
                                        title,
                                        0,
                                        Integer.parseInt(serving),
                                        Float.parseFloat(cost),
                                        Integer.parseInt(prepTime),
                                        ingredientList,
                                        getCurrentDate(),
                                        dishTypes,
                                        urlToImage,
                                        false,
                                        stepsAnalyzes
                                );
                                stepNumber = 0;
                                recipeViewModel.writeRecipe(recipe).observe(getViewLifecycleOwner(), writeResult -> {
                                    if (writeResult != null && writeResult.isSuccess()) {
                                        Snackbar.make(requireView(), R.string.saving_success, Snackbar.LENGTH_SHORT).show();
                                        Navigation.findNavController(requireView()).navigate(R.id.action_nav_add_new_recipe_to_nav_home);
                                    } else {
                                        Snackbar.make(requireView(), R.string.saving_failure, Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Snackbar.make(requireView(), R.string.error_photo, Snackbar.LENGTH_SHORT).show();
                            }
                            binding.progress.setVisibility(View.GONE);
                        });
                    });
                    alert.setNegativeButton(getString(R.string.cancel), null);
                    alert.show();

                }
        });
    }

    ActivityResultLauncher<String[]> mainPictureActivity = registerForActivityResult(new ActivityResultContracts.OpenDocument(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    try {
                        mainPicture = uri;
                        Glide.with(requireContext())
                                .load(uri)
                                .into(binding.addMainPicture);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public boolean checkData() {
        boolean ok = true;
        if(isEmpty(title)) {
            binding.addTitleLayout.setError(getString(R.string.empty_fields));
            ok = false;
        }

        if(isEmpty(difficulty)) {
            binding.difficoltyLayout.setError(getString(R.string.empty_fields));
            ok = false;
        }

        if(isEmpty(cost)) {
            binding.costLayout.setError(getString(R.string.empty_fields));
            ok = false;
        }

        if(isEmpty(prepTime)) {
            binding.prepTimeLayout.setError(getString(R.string.empty_fields));
            ok = false;
        }

        if(isEmpty(serving)) {
            binding.servingLayout.setError(getString(R.string.empty_fields));
            ok = false;
        }

        if(isEmpty(category)) {
            binding.typeLayout.setError(getString(R.string.empty_fields));
            ok = false;
        }

        if(ingredientList.size() == 0) {
            binding.addIngredientLayout.setError(getString(R.string.one_element));
            ok = false;
        }

        if(stepList.size() == 0) {
            binding.addStepLayout.setError(getString(R.string.one_element));
            ok = false;
        }

        if(mainPicture == null) {
            Snackbar.make(requireView(), R.string.noPhoto, Snackbar.LENGTH_SHORT).show();
            ok = false;
        }

        return ok;
    }

    public static String getCurrentDate() {
        Date currentDate = new Date();
        SimpleDateFormat df = new SimpleDateFormat(dateFormat, Locale.US);
        df.format(currentDate);
        currentDate.getTime();
        return currentDate.toString();
    }

    @NeedsPermission(Manifest.permission_group.STORAGE)
    public void startPhoto() {
        mainPictureActivity.launch(IMAGE);
    }
}