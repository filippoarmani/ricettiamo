package cfgmm.ricettiamo.ui.navigation_drawer;

import static android.text.TextUtils.isEmpty;

import static cfgmm.ricettiamo.util.Constants.IMAGE;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.HomeAdapter;
import cfgmm.ricettiamo.adapter.IngredientsRecyclerAdapter;
import cfgmm.ricettiamo.adapter.StepAdapter;
import cfgmm.ricettiamo.databinding.FragmentAddNewRecipeBinding;
import cfgmm.ricettiamo.databinding.FragmentSettingsBinding;
import cfgmm.ricettiamo.model.Ingredient;

public class AddNewRecipeFragment extends Fragment {

    private FragmentAddNewRecipeBinding binding;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddNewRecipeBinding.inflate(inflater, container, false);

        binding.ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,  false));
        binding.stepRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,  false));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

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

                IngredientsRecyclerAdapter adapter = new IngredientsRecyclerAdapter(ingredientList, new IngredientsRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onIngredientItemClick(Ingredient ingredient) { }

                    @Override
                    public void onDeleteButtonPressed(int position) { }
                });

                binding.ingredientRecyclerView.setAdapter(adapter);

            } else {
                binding.addIngredientLayout.setError(getString(R.string.empty_fields));
            }
        });

        binding.addStepButton.setOnClickListener(v -> {
            String step = binding.addStepLayout.getEditText().getText().toString().trim();

            if(!isEmpty(step)) {
                stepList.add(step);

                StepAdapter stepAdapter = new StepAdapter(requireContext(), stepList);
                binding.stepRecyclerView.setAdapter(stepAdapter);
            } else {
                binding.addStepLayout.setError(getString(R.string.empty_fields));
            }
        });

        binding.addPhotoButton.setOnClickListener(v -> {
            stepPictureActivity.launch(IMAGE);
        });

        binding.saveRecipe.setOnClickListener(v -> {

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
                        StepAdapter stepAdapter = new StepAdapter(requireContext(), stepList);
                        binding.stepRecyclerView.setAdapter(stepAdapter);
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