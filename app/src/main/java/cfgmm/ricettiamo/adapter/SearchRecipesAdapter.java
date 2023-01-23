package cfgmm.ricettiamo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Recipe;

public class SearchRecipesAdapter extends ArrayAdapter<Recipe> {

    private Recipe[] recipesArray;
    private int layout;


    public SearchRecipesAdapter(@NonNull Context context, int resource, @NonNull Recipe[] objects) {
        super(context, resource, objects);
        this.recipesArray = objects;
        this.layout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        }

        ImageView imageRecipe = convertView.findViewById(R.id.src_imageRecipe);
        TextView textViewName = convertView.findViewById(R.id.src_nameRecipe);
        TextView textViewInformations = convertView.findViewById(R.id.src_info);
        //TextView textViewIngredients = convertView.findViewById(R.id.src_ingredient_1);

        //imageRecipe.setImageUrl(recipesArray[position].getUrlToImage());
        textViewName.setText(recipesArray[position].getName());
        textViewInformations.setText(recipesArray[position].getDescription());
        //textViewIngredients.setText(recipesArray[position].getIngredients());


        return convertView;
    }
}
