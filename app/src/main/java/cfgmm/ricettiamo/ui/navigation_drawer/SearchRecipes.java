package cfgmm.ricettiamo.ui.navigation_drawer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.adapter.SearchRecipesAdapter;
import cfgmm.ricettiamo.model.Recipe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchRecipes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchRecipes extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private TextInputLayout inputRecipe;
    private String mParam1;
    private String mParam2;

    public SearchRecipes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment search_recipes.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchRecipes newInstance(String param1, String param2) {
        SearchRecipes fragment = new SearchRecipes();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_recipes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        inputRecipe = view.findViewById(R.id.search_recipes);
        ImageButton btnSearch = view.findViewById(R.id.btn_search);

        btnSearch.setOnClickListener(v -> {
            String search = inputRecipe.getEditText().getText().toString().trim();
            if (search.length() != 0)
                Snackbar.make(getView(), search, Snackbar.LENGTH_LONG).show();
            else
                Snackbar.make(getView(), R.string.empty_fields, Snackbar.LENGTH_LONG).show();
        });

        Recipe[] recipesArray = new Recipe[10];
        for (int i = 0; i < 10; i++){
            recipesArray[i] = new Recipe("author " + i, "title " + i, 0, null,
                    /*null,*/ null, null, null, false);
        }

        ListView listViewSearchRecipes = view.findViewById(R.id.search_list);

        /*ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(requireContext(),
                android.R.layout.simple_list_item_1, recipesArray);*/

        SearchRecipesAdapter adapter = new SearchRecipesAdapter(requireContext(),
                R.layout.recipe_item, recipesArray);

        listViewSearchRecipes.setAdapter(adapter);
    }

}