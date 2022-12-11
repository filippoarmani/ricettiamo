package adpter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShoppingListRecyclerAdapter extends
        RecyclerView.Adapter<IngredientsRecyclerAdapter.IngredientViewHolder>{
    @NonNull
    @Override
    public IngredientsRecyclerAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsRecyclerAdapter.IngredientViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
