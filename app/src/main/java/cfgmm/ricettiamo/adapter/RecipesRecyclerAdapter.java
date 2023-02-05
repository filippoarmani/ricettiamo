package cfgmm.ricettiamo.adapter;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Recipe;


public class RecipesRecyclerAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int RECIPES_VIEW_TYPE = 0;
    private static final int LOADING_VIEW_TYPE = 1;


    public interface OnItemClickListener {
        void onRecipeItemClick(Recipe recipe);
    }
    private final List<Recipe> recipeList;
    private final Application application;
    private final OnItemClickListener onItemClickListener;

    public RecipesRecyclerAdapter(List<Recipe> recipeList, Application application, OnItemClickListener onItemClickListener) {
        this.recipeList = recipeList;
        this.application = application;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (recipeList.get(position) == null) {
            return LOADING_VIEW_TYPE;
        } else {
            return RECIPES_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        if(viewType == RECIPES_VIEW_TYPE){
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.template_my_recipes, parent, false);
            return new RecipeViewHolder(view);
        }
        else{
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.template_loading_item, parent, false);
            return new LoadingRecipeViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecipeViewHolder){
            ((RecipeViewHolder) holder).bind(recipeList.get(position));
        }
        else if (holder instanceof LoadingRecipeViewHolder){
            ((LoadingRecipeViewHolder) holder).activate();
        }
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
        private final ImageView imageViewFavoriteRecipe;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.text_topRecipe_name);
            this.textViewScore = itemView.findViewById(R.id.text_topRecipe_numberVote);
            imageViewFavoriteRecipe = itemView.findViewById(R.id.imageview_favorite_recipes);
            itemView.setOnClickListener(this);
            imageViewFavoriteRecipe.setOnClickListener(this);
        }

        public void bind(Recipe recipe) {
            textViewName.setText(recipe.getName());
            textViewScore.setText(String.valueOf(recipe.getScore()));
            setImageViewFavoriteRecipe(recipeList.get(getAbsoluteAdapterPosition()).isFavorite());
        }
        @Override
        public void onClick(View v) {
            //LINK pagina ricetta
            if (v.getId() == R.id.imageview_favorite_recipes) {
                setImageViewFavoriteRecipe(!recipeList.get(getAbsoluteAdapterPosition()).isFavorite());
            } else {
                onItemClickListener.onRecipeItemClick(recipeList.get(getAbsoluteAdapterPosition()));
            }
        }

        private void setImageViewFavoriteRecipe(boolean isFavorite) {
            if (isFavorite) {
                imageViewFavoriteRecipe.setImageDrawable(
                        AppCompatResources.getDrawable(application,
                                R.drawable.ic_favourite_fill));
            } else {
                imageViewFavoriteRecipe.setImageDrawable(
                        AppCompatResources.getDrawable(application,
                                R.drawable.ic_favourite));
            }
        }
    }

    public static class LoadingRecipeViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar progressBar;

        LoadingRecipeViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.progressbar_loading_recipes);
        }

        public void activate() {
            progressBar.setIndeterminate(true);
        }
    }

}
