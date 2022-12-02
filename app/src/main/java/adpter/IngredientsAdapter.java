package adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Ingredient;


public class IngredientsAdapter extends BaseAdapter  {

    private final List<Ingredient> ingredientList;


    public IngredientsAdapter(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @Override
    public int getCount() {
        if (ingredientList != null) {
            return ingredientList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return ingredientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.fragment_ingredient_list_item_list, parent, false);
        }

        TextView textName = convertView.findViewById(R.id.text_ricette);
        TextView textQta = convertView.findViewById(R.id.text_qta);
        TextView textSize = convertView.findViewById(R.id.text_size);

        Button buttonAdd = convertView.findViewById(R.id.button_add);
        Button buttonLess = convertView.findViewById(R.id.button_less);

        textName.setText(ingredientList.get(position).getName());
        textQta.setText(ingredientList.get(position).getQta());
        textSize.setText(ingredientList.get(position).getSize());

        buttonAdd.setOnClickListener(v -> {
            int q = ingredientList.get(position).getQta();
            ingredientList.get(position).setQta(q+1);
            // Call this method to refresh the UI and update the content of ListView
            notifyDataSetChanged();
        });

        buttonLess.setOnClickListener(v -> {
            ingredientList.remove(position);
            // Call this method to refresh the UI and update the content of ListView
            notifyDataSetChanged();
        });


        return convertView;
    }
}
