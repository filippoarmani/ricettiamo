package cfgmm.ricettiamo.ui.navigation_drawer;

import static android.text.TextUtils.isEmpty;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.IngredientsRecyclerAdapter;
import cfgmm.ricettiamo.data.repository.ingredients.IIngredientsRepository;
import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.Result;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.IngredientViewModel;
import cfgmm.ricettiamo.viewmodel.IngredientViewModelFactory;

@SuppressLint("NotifyDataSetChanged")
public class FridgeFragment extends Fragment {

    private final String TAG = FridgeFragment.class.getSimpleName();
    private List<Ingredient> ingredientList;
    private IngredientsRecyclerAdapter adapter;
    private IIngredientsRepository ingredientsRepository;
    private IngredientViewModel ingredientViewModel;

    public FridgeFragment() {}


    public static FridgeFragment newInstance() {
        return new FridgeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ingredientsRepository = ServiceLocator.getInstance().getIngredientRepository(requireActivity().getApplication());
        ingredientViewModel = new ViewModelProvider(requireActivity(), new IngredientViewModelFactory(ingredientsRepository)).get(IngredientViewModel.class);
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

        Button buttonAdd = view.findViewById(R.id.Fridge_buttonAdd);
        TextInputLayout name_l = view.findViewById(R.id.Fridge_textName_layout);
        TextInputLayout qta_l = view.findViewById(R.id.Fridge_textQta_layout);
        TextInputLayout unit_l = view.findViewById(R.id.Fridge_textUnit_layout);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_list_ingredients_fridge);
        recyclerView.setItemAnimator(null);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

        adapter = new IngredientsRecyclerAdapter(requireView(), ingredientList,
                position -> {
            ingredientViewModel.deleteIngredient(ingredientList.get(position));
            adapter.notifyItemRemoved(ingredientList.indexOf(ingredientList.get(position)));
            if (ingredientList.size() < 2) { ingredientList.clear(); }
            adapter.notifyDataSetChanged();
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
                long id = 1;
                if (ingredientList.size() > 0) {
                    id = ingredientList.get(ingredientList.size() - 1).getId() + 1;
                }
                Ingredient newIngredient = new Ingredient(id, name, q, unit, false, true);
                ingredientViewModel.insertIngredient(newIngredient);
                Log.e("dimensione ", String.valueOf(ingredientList.size()));
                ingredientList.add(newIngredient);
                Log.e("aggiunto ", newIngredient.toString());
                Log.e("dimensione ", String.valueOf(ingredientList.size()));
                adapter.notifyItemInserted(ingredientList.size() - 1);
            }
        });

        ingredientViewModel.getFridgeListIngredients().observe(getViewLifecycleOwner(), result -> {
            if(result != null && result.isSuccess()) {
                List<Ingredient> allIngredientList = ((Result.ListIngredientResponseSuccess) result).getData();
                if(allIngredientList != null && allIngredientList.size() > 0) {
                    ingredientList.clear();
                    for(Ingredient ingrediet: allIngredientList) {
                        if(ingrediet.isFridgeList()) {
                            ingredientList.add(ingrediet);
                        }
                    }
                    requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());

                }
            } else {
                Snackbar.make(requireView(), R.string.unexpected_error, Snackbar.LENGTH_SHORT).show();
            }
        });

    }

}
