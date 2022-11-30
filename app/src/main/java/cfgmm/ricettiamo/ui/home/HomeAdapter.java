package cfgmm.ricettiamo.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cfgmm.ricettiamo.R;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    String title[];
    int image[];
    Context context;

    public HomeAdapter(Context ct, String t[], int i[]) {
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
        holder.titleRecipe.setText(title[position]);
        holder.imageRecipe.setImageResource(image[position]);
    }

    @Override
    public int getItemCount() {
        return image.length;
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView titleRecipe;
        ImageButton imageRecipe;

        public HomeViewHolder(View view) {
            super(view);
            titleRecipe = view.findViewById(R.id.titleRecipe);
            imageRecipe = view.findViewById(R.id.imageRecipe);
        }
    }
}
