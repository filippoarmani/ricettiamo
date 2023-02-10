package cfgmm.ricettiamo.ui.navigation_drawer;

import static android.text.TextUtils.isEmpty;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.RecipesRecyclerAdapter;
import cfgmm.ricettiamo.adapter.ShoppingListRecyclerAdapter;
import cfgmm.ricettiamo.data.repository.ingredients.IIngredientsRepository;
import cfgmm.ricettiamo.data.repository.ingredients.IngredientsRepository;
import cfgmm.ricettiamo.data.repository.ingredients.IngredientsResponseCallback;
import cfgmm.ricettiamo.data.repository.recipe.RecipesRepository;
import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.IngredientApiResponse;
import cfgmm.ricettiamo.model.Recipe;

@SuppressLint("NotifyDataSetChanged")
public class ShoppingListFragment extends Fragment implements IngredientsResponseCallback {

    private final String TAG = ShoppingListFragment.class.getSimpleName();

    private List<Ingredient> shoppingList;
    private ShoppingListRecyclerAdapter adapter;
    private IIngredientsRepository iIngredientsRepository;
    public ShoppingListFragment() {}

    public static ShoppingListFragment newInstance() {
        return new ShoppingListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iIngredientsRepository = new IngredientsRepository(requireActivity().getApplication(), this);
        shoppingList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_m_shopping_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iIngredientsRepository.getShoppingListIngredients();

        Button buttonAdd = view.findViewById(R.id.shopList_buttonAdd);
        TextInputLayout name_l = view.findViewById(R.id.shoplist_textName_layout);
        TextInputLayout qta_l = view.findViewById(R.id.shopList_textQta_layout);
        TextInputLayout unit_l = view.findViewById(R.id.shopList_textUnit_layout);

        //adapter.getItemTouchHelper().attachToRecyclerView(recyclerView);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_list_ingredients_shopping);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.VERTICAL, false);

        adapter = new ShoppingListRecyclerAdapter(requireActivity().getApplication(), shoppingList,
                new ShoppingListRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onDeleteButtonPressed(int position) {
                        shoppingList.get(position).setShoppingList(!shoppingList.get(position).isShoppingList());
                        iIngredientsRepository.updateIngredient(shoppingList.get(position));
                    }
                });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.getItemTouchHelper().attachToRecyclerView(recyclerView);

        buttonAdd.setOnClickListener(v -> {
            String name = name_l.getEditText().getText().toString().trim();
            String qta = qta_l.getEditText().getText().toString().trim();
            String unit = unit_l.getEditText().getText().toString().trim();

            if(isEmpty(name)) {
                name_l.setError(getString(R.string.empty_fields));
            } else if(isEmpty(qta)) {
                qta_l.setError(getString(R.string.empty_fields));
            } else if(isEmpty(unit)) {
                unit_l.setError(getString(R.string.empty_fields));
            } else {
                float q = Float.parseFloat(qta);
                Ingredient newIngredient = new Ingredient(name, q, unit, true, false);
                shoppingList.add(newIngredient);
                adapter.notifyItemInserted(shoppingList.size() - 1);
                iIngredientsRepository.saveDataInDatabase(shoppingList);
            }

        });
    }

    private List<Ingredient> getIngredientListWithWithGSon() {
        InputStream inputStream = null;
        try {
            inputStream = requireActivity().getAssets().open("fridge-api-test.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        IngredientApiResponse ingredientApiResponse = new
                Gson().fromJson(bufferedReader, IngredientApiResponse.class);
        return ingredientApiResponse.getIngredients();
    }

    @Override
    public void onSuccess(List<Ingredient> ingredientList) {
        if (shoppingList != null) {
            this.shoppingList.clear();
            this.shoppingList.addAll(shoppingList);
            requireActivity().runOnUiThread(() -> {
                adapter.notifyDataSetChanged();
            });
        }
    }

    @Override
    public void onFailure(String errorMessage) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                errorMessage, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onIngredientStatusChanged(Ingredient ingredient) {
        shoppingList.remove(ingredient);
        requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                getString(R.string.ingredient_removed_from_list_message),
                Snackbar.LENGTH_LONG).show();
    }
}