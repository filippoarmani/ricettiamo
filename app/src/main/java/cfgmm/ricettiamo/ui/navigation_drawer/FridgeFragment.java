package cfgmm.ricettiamo.ui.navigation_drawer;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import cfgmm.ricettiamo.adapter.IngredientsRecyclerAdapter;
import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.IngredientApiResponse;



public class FridgeFragment extends Fragment {

    private final String TAG = FridgeFragment.class.getSimpleName();
    Button button;
    EditText editText_name;
    EditText editText_qta;
    FloatingActionButton floatingActionButton;
    AlertDialog.Builder builder;


    public FridgeFragment() {
        // Required empty public constructor
    }


    public static FridgeFragment newInstance() {
        return new FridgeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fridge, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        builder = new AlertDialog.Builder(view.getContext());
        button = view.findViewById(R.id.button);
        floatingActionButton = view.findViewById(R.id.delete_floating_button);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_list_ingredients);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,  false);

        recyclerView.setLayoutManager(layoutManager);
        List<Ingredient> ingredientList = getIngredientListWithWithGSon();

        button.setOnClickListener(new View.OnClickListener() {
            EditText editText_name = view.findViewById(R.id.text_product);
            EditText editText_qta = view.findViewById(R.id.text_qtaAdd);

            @Override
            public void onClick(View v){
                if(editText_name.getText().toString().trim().isEmpty()) {
                    editText_name.setError("error");
                }
                if(editText_qta.getText().toString().trim().isEmpty()) {
                    editText_qta.setError("error");
                }
                try{
                    Float qta =  Float.valueOf(editText_qta.getText().toString());
                    ingredientList.add(new Ingredient(editText_name.getText().toString(),
                            qta,"l"));}
                catch (Exception e){
                    Log.v("error", editText_qta.getText().toString());
                }

                IngredientsRecyclerAdapter adapter = createAdapater(v, ingredientList);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        });


        IngredientsRecyclerAdapter adapter = new
                IngredientsRecyclerAdapter(ingredientList,
                new IngredientsRecyclerAdapter.OnItemClickListener()  {


                    @Override
                    public void onIngredientItemClick(Ingredient ingredient) {
                        Snackbar.make(view, ingredient.getName(), Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
            public void onDeleteButtonPressed(int position) {
                Snackbar.make(view, getString(R.string.list_size_message) + ingredientList.size(),
                        Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onAddButtonPressed(int position) {
                Snackbar.make(view, getString(R.string.list_size_message) + ingredientList.size(),
                        Snackbar.LENGTH_SHORT).show();
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
        return ingredientApiResponse.getArticles();
    }
    private  IngredientsRecyclerAdapter createAdapater(View view, List<Ingredient> ingredientList){
        IngredientsRecyclerAdapter adapter = new
                IngredientsRecyclerAdapter(ingredientList,
                new IngredientsRecyclerAdapter.OnItemClickListener() {

                    @Override
                    public void onIngredientItemClick(Ingredient ingredient) {
                    }

                    @Override
                    public void onDeleteButtonPressed(int position) {

                    }

                    @Override
                    public void onAddButtonPressed(int position) {

                    }
                });
        return adapter;}

}
