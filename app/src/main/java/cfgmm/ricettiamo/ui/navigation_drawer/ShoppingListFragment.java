package cfgmm.ricettiamo.ui.navigation_drawer;

import static android.text.TextUtils.isEmpty;

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
import cfgmm.ricettiamo.adapter.ShoppingListRecyclerAdapter;
import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.IngredientApiResponse;

public class ShoppingListFragment extends Fragment {

    private final String TAG = ShoppingListFragment.class.getSimpleName();

    private List<Ingredient> shoppingList;
    private RecyclerView recyclerView;
    private ShoppingListRecyclerAdapter adapter;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    public static ShoppingListFragment newInstance() {
        return new ShoppingListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Button buttonAdd = view.findViewById(R.id.shopList_buttonAdd);
        TextInputLayout name_l = view.findViewById(R.id.shoplist_textName_layout);
        TextInputLayout qta_l = view.findViewById(R.id.shopList_textQta_layout);
        TextInputLayout unit_l = view.findViewById(R.id.shopList_textUnit_layout);

        recyclerView = view.findViewById(R.id.recyclerview_list_ingredients_shopping);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,  false));
        adapter = new ShoppingListRecyclerAdapter(requireView(), shoppingList);
        recyclerView.setAdapter(adapter);
        adapter.getItemTouchHelper().attachToRecyclerView(recyclerView);

        //Non ho capito perchè ritorna null
        //shoppingList = getIngredientListWithWithGSon();

        buttonAdd.setOnClickListener(v -> {
            String name = name_l.getEditText().getText().toString().trim();
            String qta = qta_l.getEditText().getText().toString().trim();
            String unit = unit_l.getEditText().getText().toString().trim();

            boolean ok = true;
            if(isEmpty(name)) {
                name_l.setError(getString(R.string.empty_fields));
                ok = false;
            }
            if(isEmpty(qta)) {
                qta_l.setError(getString(R.string.empty_fields));
                ok = false;
            }
            if(isEmpty(unit)) {
                unit_l.setError(getString(R.string.empty_fields));
                ok = false;
            }

            if(ok) {
                float q = Float.parseFloat(qta);
                Ingredient newIngredient = new Ingredient(name, q, unit);
                shoppingList.add(newIngredient);
                adapter.notifyItemInserted(shoppingList.size() - 1);
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

}