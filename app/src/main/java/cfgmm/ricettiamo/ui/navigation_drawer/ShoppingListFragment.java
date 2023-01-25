package cfgmm.ricettiamo.ui.navigation_drawer;

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
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.ShoppingListRecyclerAdapter;
import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.IngredientApiResponse;



public class ShoppingListFragment extends Fragment {


    private final String TAG = ShoppingListFragment.class.getSimpleName();

    public ShoppingListFragment() {
        // Required empty public constructor
    }


    public static ShoppingListFragment newInstance() {
        return new ShoppingListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonAdd = view.findViewById(R.id.ShopList_buttonAdd);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_list_ingredients_shopping);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,  false);

        recyclerView.setLayoutManager(layoutManager);
        List<Ingredient> shoppingList = getIngredientListWithWithGSon();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            final EditText editText_name = view.findViewById(R.id.Shoplist_textName);
            final EditText editText_qta = view.findViewById(R.id.ShopList_textQta);

            @Override
            public void onClick(View v) {
                if(editText_name.getText().toString().trim().isEmpty()) {
                    editText_name.setError("error");
                }
                if(editText_qta.getText().toString().trim().isEmpty()) {
                    editText_qta.setError("error");
                }
                try{
                    float qta =  Float.valueOf(editText_qta.getText().toString());
                    shoppingList.add(new Ingredient(editText_name.getText().toString(),
                            qta,"l"));}
                catch (Exception e){
                    Log.v("error", editText_qta.getText().toString());
                }

                ShoppingListRecyclerAdapter adapter = createAdapater(v, shoppingList);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        });
        ShoppingListRecyclerAdapter adapter = new ShoppingListRecyclerAdapter(shoppingList,
                new ShoppingListRecyclerAdapter.OnItemClickListener() {

                    @Override
                    public void onIngredientItemClick(Ingredient ingredient) {
                        Snackbar.make(view, ingredient.getName(), Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
            public void onDeleteButtonPressed(int position) {
                Snackbar.make(view, getString(R.string.list_size_message) + shoppingList.size(),
                        Snackbar.LENGTH_SHORT).show();
            }
        });

    }




    private ShoppingListRecyclerAdapter createAdapater(View v, List<Ingredient> shoppingList) {
        ShoppingListRecyclerAdapter adapter = new ShoppingListRecyclerAdapter(shoppingList, new
                ShoppingListRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onIngredientItemClick(Ingredient ingredient) {

            }

            @Override
            public void onDeleteButtonPressed(int position) {

            }
        });
        return adapter;
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
        return ingredientApiResponse.getArticles();
    }
}