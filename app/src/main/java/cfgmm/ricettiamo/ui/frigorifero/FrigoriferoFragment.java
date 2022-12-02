package cfgmm.ricettiamo.ui.frigorifero;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import adpter.IngredientsRecyclerAdapter;
import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Ingredient;


public class FrigoriferoFragment extends Fragment {

   private static String TAG = FrigoriferoFragment.class.getSimpleName();


    public FrigoriferoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FrigoriferoFragment.
     */

    public static FrigoriferoFragment newInstance() {
        FrigoriferoFragment fragment = new FrigoriferoFragment();
        return fragment;
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
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_list_ingredients);
        RecyclerView.LayoutManager layoutManager = new  LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,  false);

        recyclerView.setLayoutManager(layoutManager);

        List<Ingredient> ingredientList = new ArrayList<>();
        for (int i=0; i<10;i++){
            ingredientList.add(new Ingredient("nome" + i,3, "size"+ "kg"));
        }

        IngredientsRecyclerAdapter adapter = new IngredientsRecyclerAdapter(ingredientList,
                new IngredientsRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onIngredientItemClick(Ingredient ingredient) {
                        Snackbar.make(view, ingredient.getName(),Snackbar.LENGTH_SHORT).show();
                    }
    
                    @Override
                    public void onDeleteButtonPressed(int position) {

                    }
                });

                recyclerView.setAdapter(adapter);



    }
}