package cfgmm.ricettiamo.ui.frigorifero;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.List;

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
        return null;
    }
}
