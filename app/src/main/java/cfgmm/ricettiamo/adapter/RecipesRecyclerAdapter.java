package cfgmm.ricettiamo.adapter;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Recipe;

public class RecipesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Interface to associate a click listener with
     * a RecyclerView item.
     */
    public interface OnItemClickListener {
        void onRecipeItemClick(Recipe recipe);
        void onFavoriteButtonPressed(int position);
    }
    private final List<Recipe> recipeList;
    private final Application application;
    private final OnItemClickListener onItemClickListener;

    public RecipesRecyclerAdapter(List<Recipe> recipeList, Application application,
                                  OnItemClickListener onItemClickListener) {
        this.application = application;
        this.recipeList = recipeList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.template_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecipeViewHolder) {
            ((RecipeViewHolder) holder).bind(recipeList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (recipeList != null) {
            return recipeList.size();
        }
        return 0;
    }
    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView textViewName;
        private final TextView textViewServings;
        private final TextView textViewCost;
        private final TextView textViewPrepTime;
        private final ImageView imageViewRecipeImage;
        private final ImageView imageViewFavoriteRecipes;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.src_nameRecipe);
            textViewServings = itemView.findViewById(R.id.src_servings_value);
            textViewPrepTime = itemView.findViewById(R.id.src_prep_time_value);
            textViewCost = itemView.findViewById(R.id.src_cost_value);
            imageViewRecipeImage = itemView.findViewById(R.id.src_imageRecipe);
            imageViewFavoriteRecipes = itemView.findViewById(R.id.imageview_favorite_recipes);
            itemView.setOnClickListener(this);
            imageViewFavoriteRecipes.setOnClickListener(this);
        }

        public void bind(Recipe recipe) {
            textViewName.setText(recipe.getName());
            textViewServings.setText(String.valueOf(recipe.getServings()));
            textViewPrepTime.setText(String.valueOf(recipe.getPrepTime()));
            textViewCost.setText(String.valueOf((int) recipe.getCost() * recipe.getServings() / 100.0f));
            setImageViewFavoriteRecipes(recipeList.get(getAbsoluteAdapterPosition()).isFavorite());
            Glide.with(application)
                    .load(recipe.getUrlToImage())
                    .placeholder(R.drawable.ic_add)
                    .into(imageViewRecipeImage);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imageview_favorite_recipes) {
                setImageViewFavoriteRecipes(!recipeList.get(getAbsoluteAdapterPosition()).isFavorite());
                onItemClickListener.onFavoriteButtonPressed(getAbsoluteAdapterPosition());
            } else {
                onItemClickListener.onRecipeItemClick(recipeList.get(getAbsoluteAdapterPosition()));
            }
        }

        private void setImageViewFavoriteRecipes(boolean isFavorite) {
            if (isFavorite) {
                imageViewFavoriteRecipes.setImageDrawable(
                        AppCompatResources.getDrawable(application,
                                R.drawable.ic_favourite_fill));
            } else {
                imageViewFavoriteRecipes.setImageDrawable(
                        AppCompatResources.getDrawable(application,
                                R.drawable.ic_favourite));
            }
        }
    }
}
