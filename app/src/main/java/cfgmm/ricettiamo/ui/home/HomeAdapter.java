package cfgmm.ricettiamo.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cfgmm.ricettiamo.R;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    final String[] title;
    final int[] image;
    final Context context;

    public HomeAdapter(Context ct, String[] t, int[] i) {
        context = ct;
        title = t;
        image = i;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.template_home, parent,false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final int[] click = {0};
        holder.titleRecipe.setText(title[position]);
        holder.imageRecipe.setImageResource(image[position]);
        //click da sistemare con il database e non so perchè Android vuole che sia un array
        holder.favoriteRecipe.setOnClickListener(v -> {
            if(click[0] == 0) {
                holder.favoriteRecipe.setImageResource(R.drawable.ic_favourite_fill);
                click[0] = 1;
            } else {
                holder.favoriteRecipe.setImageResource(R.drawable.ic_favourite);
                click[0] = 0;
            }
        });
    }

    @Override
    public int getItemCount() {
        return image.length;
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        final TextView titleRecipe;
        final ImageView imageRecipe;
        final ImageButton favoriteRecipe;

        public HomeViewHolder(View view) {
            super(view);
            titleRecipe = view.findViewById(R.id.titleRecipe);
            imageRecipe = view.findViewById(R.id.imageRecipe);
            favoriteRecipe = view.findViewById(R.id.favouriteRecipe);
        }
    }
}
