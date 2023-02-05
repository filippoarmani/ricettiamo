package cfgmm.ricettiamo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Recipe;

public class MyRecipesRecyclerAdapter extends RecyclerView.Adapter<MyRecipesRecyclerAdapter.MyRecipesViewHolder> {

    final List<Recipe> myRecipesList;
    final Context context;

    public MyRecipesRecyclerAdapter(Context ct, List<Recipe> myRecipesList) {
        this.context = ct;
        this.myRecipesList = myRecipesList;
    }

    @NonNull
    @Override
    public MyRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.template_my_recipes, parent,false);
        return new MyRecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipesViewHolder holder, int position) {
        holder.titleMyRecipe.setText(myRecipesList.get(position).getName());
        holder.starMyRecipe.setText(myRecipesList.get(position).getScore());
    }

    @Override
    public int getItemCount() {
        if(myRecipesList != null)
            return myRecipesList.size();

        return 0;
    }

    public static class MyRecipesViewHolder extends RecyclerView.ViewHolder {
        final TextView titleMyRecipe;
        final TextView starMyRecipe;

        public MyRecipesViewHolder(View view) {
            super(view);
            titleMyRecipe = view.findViewById(R.id.titleMyRecipe);
            starMyRecipe = view.findViewById(R.id.starMyRecipe);
        }
    }
}
