package cfgmm.ricettiamo.ui.navigation_drawer;

import static android.text.TextUtils.isEmpty;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.IngredientsRecyclerAdapter;
import cfgmm.ricettiamo.data.repository.ingredients.IIngredientsRepository;
import cfgmm.ricettiamo.data.repository.ingredients.IngredientsRepository;
import cfgmm.ricettiamo.data.repository.ingredients.IngredientsResponseCallback;
import cfgmm.ricettiamo.model.Ingredient;

public class FridgeFragment extends Fragment implements IngredientsResponseCallback {

    private final String TAG = FridgeFragment.class.getSimpleName();
    private List<Ingredient> ingredientList;
    private IngredientsRecyclerAdapter adapter;
    private IIngredientsRepository iIngredientsRepository;

    public FridgeFragment() {}


    public static FridgeFragment newInstance() {
        return new FridgeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iIngredientsRepository = new IngredientsRepository(requireActivity().getApplication(),
                this);
        ingredientList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_m_fridge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iIngredientsRepository.getFridgeListIngredients();

        Button buttonAdd = view.findViewById(R.id.Fridge_buttonAdd);
        TextInputLayout name_l = view.findViewById(R.id.Fridge_textName_layout);
        TextInputLayout qta_l = view.findViewById(R.id.Fridge_textQta_layout);
        TextInputLayout unit_l = view.findViewById(R.id.Fridge_textUnit_layout);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_list_ingredients_fridge);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

        adapter = new IngredientsRecyclerAdapter(requireView(), ingredientList,
                new IngredientsRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onDeleteButtonPressed(int position) {
                        iIngredientsRepository.deleteIngredient(ingredientList.get(position));
                    }
                });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.getItemTouchHelper().attachToRecyclerView(recyclerView);


        buttonAdd.setOnClickListener(v -> {
            String name = name_l.getEditText().getText().toString().trim();
            String qta = qta_l.getEditText().getText().toString().trim();
            String unit = unit_l.getEditText().getText().toString().trim();

            if (isEmpty(name)) {
                name_l.setError(getString(R.string.empty_fields));
            } else if (isEmpty(qta)) {
                qta_l.setError(getString(R.string.empty_fields));
            } else if (isEmpty(unit)) {
                unit_l.setError(getString(R.string.empty_fields));
            } else {
                float q = Float.parseFloat(qta);
                Ingredient newIngredient = new Ingredient(name, q, unit, false, true);
                iIngredientsRepository.insertIngredient(newIngredient);
                adapter.notifyItemInserted(ingredientList.size() - 1);
            }
        });

    }

    @Override
    public void onSuccess(List<Ingredient> ingredientList) {
        if (ingredientList != null) {
            this.ingredientList.clear();
            this.ingredientList.addAll(ingredientList);
            requireActivity().runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
            });
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                errorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onIngredientStatusChanged(Ingredient ingredient, boolean delete, boolean insert) {
        if (delete) {
            ingredientList.remove(ingredient);
            requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    getString(R.string.ingredient_removed_from_list_message),
                    Snackbar.LENGTH_SHORT).show();
        } else if (insert) {
            ingredientList.add(ingredient);
            requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
        }
    }
}
