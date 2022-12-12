package adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Ingredient;
import cfgmm.ricettiamo.model.Recipe;


public class RecipesRecyclerAdapter  extends
        RecyclerView.Adapter<RecipesRecyclerAdapter.RecipeViewHolder>{



    public interface OnItemClickListener {
        void onRecipeItemClick(Recipe recipe);
    }
    private final List<Recipe> recipeList;
    private final RecipesRecyclerAdapter.OnItemClickListener onItemClickListener;

    public RecipesRecyclerAdapter(List<Recipe> recipeList, OnItemClickListener onItemClickListener) {
        this.recipeList = recipeList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                      int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fragment_list_item_top_recipe, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesRecyclerAdapter.RecipeViewHolder holder,
                                 int position) {
        holder.bind(recipeList.get(position));
    }

    @Override
    public int getItemCount() {
        if (recipeList != null) {
            return recipeList.size();
        }
        return 0;
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{
        private final TextView textViewName;
        private final  TextView textViewScore;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.text_topRecipe_name);
            this.textViewScore = itemView.findViewById(R.id.text_topRecipe_numberVote);
            itemView.setOnClickListener(this);
        }

        public void bind(Recipe recipe) {
            textViewName.setText(recipe.getName());
            textViewScore.setText(String.valueOf(recipe.getScore()));

        }
        @Override
        public void onClick(View v) {
            //LINK pagina ricetta
        }
    }
}
