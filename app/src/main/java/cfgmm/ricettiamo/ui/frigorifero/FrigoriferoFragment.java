package cfgmm.ricettiamo.ui.frigorifero;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import adpter.IngredientsRecyclerAdapter;
import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Ingredient;


public class FrigoriferoFragment extends Fragment {

    private final String TAG = FrigoriferoFragment.class.getSimpleName();


    public FrigoriferoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     */

    public static FrigoriferoFragment newInstance(String param1, String param2) {
        return new FrigoriferoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frigorifero, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_list_ingredients);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,  false);

        recyclerView.setLayoutManager(layoutManager);

        List<Ingredient> ingredientList = new ArrayList<>();
        for (int i=0; i<10;i++){
            ingredientList.add(new Ingredient("nome" + i,"3", "size"+ "kg"));
        }

        IngredientsRecyclerAdapter adapter = new IngredientsRecyclerAdapter(ingredientList, new IngredientsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onIngredientItemClick(Ingredient ingredient) {
                Snackbar.make(view, ingredient.getName(), Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteButtonPressed(int position) {

            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

    }

}