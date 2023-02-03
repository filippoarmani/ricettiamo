package cfgmm.ricettiamo.ui.navigation_drawer;

import static android.text.TextUtils.isEmpty;

import static cfgmm.ricettiamo.util.Constants.IMAGE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.HomeAdapter;
import cfgmm.ricettiamo.adapter.IngredientsRecyclerAdapter;
import cfgmm.ricettiamo.adapter.StepAdapter;
import cfgmm.ricettiamo.databinding.FragmentMAddNewRecipeBinding;
import cfgmm.ricettiamo.model.Ingredient;

public class AddNewRecipeFragment extends Fragment {

    private FragmentMAddNewRecipeBinding binding;
    private IngredientsRecyclerAdapter adapterIngredient;
    private StepAdapter stepAdapter;

    private String mainPicture;
    private List<Ingredient> ingredientList;
    private List<Object> stepList;

    public AddNewRecipeFragment() {
        // Required empty public constructor
    }

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
        adapterIngredient = new IngredientsRecyclerAdapter(requireView(), ingredientList);
        binding.ingredientRecyclerView.setAdapter(adapterIngredient);
        adapterIngredient.getItemTouchHelper().attachToRecyclerView(binding.ingredientRecyclerView);

        //step adapter
        binding.stepRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,  false));
        stepAdapter = new StepAdapter(requireView(), stepList);
        binding.stepRecyclerView.setAdapter(stepAdapter);
        stepAdapter.getItemTouchHelper().attachToRecyclerView(binding.stepRecyclerView);

        binding.addMainPicture.setOnClickListener(v -> {
            mainPictureActivity.launch(IMAGE);
        });

        binding.addIngredientButton.setOnClickListener(v -> {
            String nameIngredient = binding.addIngredientLayout.getEditText().getText().toString().trim();
            String quantity = binding.addQuantityLayout.getEditText().getText().toString().trim();
            String unit = binding.addUnitLayout.getEditText().getText().toString().trim();

            if(!isEmpty(nameIngredient) && !isEmpty(quantity) && !isEmpty(unit)) {
                Ingredient ingredient = new Ingredient(nameIngredient, Float.parseFloat(quantity), unit);
                ingredientList.add(ingredient);
                adapterIngredient.notifyItemInserted(ingredientList.size()-1);
            } else {
                binding.addIngredientLayout.setError(getString(R.string.empty_fields));
            }
        });

        binding.addStepButton.setOnClickListener(v -> {
            String step = binding.addStepLayout.getEditText().getText().toString().trim();

            if(!isEmpty(step)) {
                stepList.add(step);
                stepAdapter.notifyItemInserted(stepList.size()-1);
            } else {
                binding.addStepLayout.setError(getString(R.string.empty_fields));
            }
        });

        binding.addPhotoButton.setOnClickListener(v -> {
            stepPictureActivity.launch(IMAGE);
        });

        binding.saveRecipe.setOnClickListener(v -> {

            Navigation.findNavController(v).navigate(R.id.action_nav_add_new_recipe_to_nav_home);
        });
    }

    ActivityResultLauncher<String[]> mainPictureActivity = registerForActivityResult(new ActivityResultContracts.OpenDocument(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    try {
                        Glide.with(requireContext())
                                .load(uri)
                                .into(binding.addMainPicture);
                        mainPicture = uri.getPath();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

    ActivityResultLauncher<String[]> stepPictureActivity = registerForActivityResult(new ActivityResultContracts.OpenDocument(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                        stepList.add(bitmap);
                        stepAdapter.notifyItemInserted(stepList.size()-1);
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
}