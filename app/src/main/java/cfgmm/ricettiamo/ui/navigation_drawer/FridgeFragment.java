package cfgmm.ricettiamo.ui.navigation_drawer;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.IngredientsRecyclerAdapter;
import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.IngredientApiResponse;



public class FridgeFragment extends Fragment {

    private final String TAG = FridgeFragment.class.getSimpleName();
    Button button;
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
        button = view.findViewById(R.id.Fridge_buttonAdd);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_list_ingredients);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,  false);

        recyclerView.setLayoutManager(layoutManager);
        List<Ingredient> ingredientList = getIngredientListWithWithGSon();

        button.setOnClickListener(new View.OnClickListener() {
            final EditText editText_name = view.findViewById(R.id.Fridge_textName);
            final EditText editText_qta = view.findViewById(R.id.Fridge_textQta);
            final Spinner spinner = view.findViewById(R.id.Fridge_spinner);

            @Override
            public void onClick(View v){
                if(editText_name.getText().toString().trim().isEmpty()) {
                    editText_name.setError("error");
                }
                if(editText_qta.getText().toString().trim().isEmpty()) {
                    editText_qta.setError("error");
                }
                try{
                    float qta =  Float.valueOf(editText_qta.getText().toString());
                    ingredientList.add(new Ingredient(editText_name.getText().toString(),
                            qta,spinner.getSelectedItem().toString() ));}
                catch (Exception e){
                    Log.v("error", editText_qta.getText().toString());
                }

                IngredientsRecyclerAdapter adapter = createAdapater(v, ingredientList);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
        });

        IngredientsRecyclerAdapter adapter = createAdapater(view, ingredientList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull
            RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Ingredient deleteItem = ingredientList.get(viewHolder.getAdapterPosition());

                int position = viewHolder.getAdapterPosition();
                ingredientList.remove(position);
                adapter.notifyItemRemoved(position);

                Snackbar.make(recyclerView, deleteItem.getName(), Snackbar.LENGTH_SHORT).
                        setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ingredientList.add(position,deleteItem);
                                adapter.notifyItemInserted(position);
                            }
                        }).show();
            }
        }).attachToRecyclerView(recyclerView);

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
                });
        return adapter;}

}
