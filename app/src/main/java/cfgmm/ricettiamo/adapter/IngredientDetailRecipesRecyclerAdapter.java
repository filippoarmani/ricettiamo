package cfgmm.ricettiamo.adapter;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Ingredient;

public class IngredientDetailRecipesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Ingredient> ingredientList;
    private final Application application;

    public IngredientDetailRecipesRecyclerAdapter(List<Ingredient> ingredientList, Application application) {
        this.ingredientList = ingredientList;
        this.application = application;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.template_ingredient_detail_recipe, parent, false);
        return new RecipeDetailIngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof IngredientDetailRecipesRecyclerAdapter.RecipeDetailIngredientsViewHolder) {
            ((IngredientDetailRecipesRecyclerAdapter.RecipeDetailIngredientsViewHolder) holder).bind(ingredientList.get(position));
        }
    }

    @Override
    public int getItemCount() {
            if (ingredientList != null) {
                return ingredientList.size();
            }
            return 0;
    }

    public class RecipeDetailIngredientsViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewName;
        private final TextView textViewQta;
        private final TextView textViewsize;

        public RecipeDetailIngredientsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.src_name_ingredient);
            textViewQta = itemView.findViewById(R.id.src_qta_ingredient);
            textViewsize = itemView.findViewById(R.id.src_size_ingredient);
        }

        public void bind(Ingredient ingredient) {
            textViewName.setText(ingredient.getName());
            textViewQta.setText(String.valueOf(ingredient.getQta()));
            textViewsize.setText(String.valueOf(ingredient.getSize()));
        }
    }
}
