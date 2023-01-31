package cfgmm.ricettiamo.adapter;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Recipe;

public class SearchRecipesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int RECIPES_VIEW_TYPE = 0;
    /**
     * Interface to associate a click listener with
     * a RecyclerView item.
     */
    public interface OnItemClickListener {
        void onRecipeItemClick(Recipe recipe);
        void onFavoriteButtonPressed(int position);
    }
    private List<Recipe> recipeList;
    private final Application application;
    private final OnItemClickListener onItemClickListener;

    public SearchRecipesAdapter(List<Recipe> recipeList, Application application,
                                OnItemClickListener onItemClickListener) {
        this.application = application;
        this.recipeList = recipeList;
        this.onItemClickListener = onItemClickListener;
    }
    public int getItemViewType(int position) {
        return RECIPES_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Create a new view, which defines the UI of the list item
        View view = null;

        view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecipeViewHolder) {
            ((RecipeViewHolder) holder).bind(recipeList.get(position));
        } /*else if (holder instanceof LoadingNewsViewHolder) {
            ((LoadingNewsViewHolder) holder).activate();
        }*/
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
        private final TextView textViewInformations;
        private final ImageView imageViewRecipeImage;
        private final ImageView imageViewFavoriteRecipes;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.src_nameRecipe);
            textViewInformations = itemView.findViewById(R.id.src_info);
            imageViewRecipeImage = itemView.findViewById(R.id.src_imageRecipe);
            imageViewFavoriteRecipes = itemView.findViewById(R.id.imageview_favorite_recipes);
            itemView.setOnClickListener(this);
            imageViewFavoriteRecipes.setOnClickListener(this);
        }

        public void bind(Recipe recipe) {
            textViewName.setText(recipe.getName());
            textViewInformations.setText(recipe.getDescription());
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

    /*public static class LoadingRecipesViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar progressBar;

        LoadingRecipesViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressbar_loading_recipes);
        }

        public void activate() {
            progressBar.setIndeterminate(true);
        }
    }*/
}
