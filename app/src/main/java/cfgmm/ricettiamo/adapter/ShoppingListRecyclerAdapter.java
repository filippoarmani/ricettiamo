package cfgmm.ricettiamo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Ingredient;

public class ShoppingListRecyclerAdapter extends
        RecyclerView.Adapter<ShoppingListRecyclerAdapter.ShoppingListViewHolder>{

    private final View view;
    private final List<Ingredient> shoppingList;

    public ShoppingListRecyclerAdapter (View view, List<Ingredient> shoppingList){
        this.view = view;
        this.shoppingList = shoppingList;
    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.template_item_shoppinglist, parent, false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListRecyclerAdapter.ShoppingListViewHolder holder,
                                 int position) {
        holder.bind(shoppingList.get(position));
    }

    @Override
    public int getItemCount() {
        if(shoppingList != null){
            return shoppingList.size();
        }
        return 0;
    }

    public ItemTouchHelper getItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull
            RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Ingredient deleteItem = shoppingList.get(viewHolder.getBindingAdapterPosition());

                int position = viewHolder.getBindingAdapterPosition();
                shoppingList.remove(position);
                notifyItemRemoved(position);

                Snackbar.make(view, "UNDO", Snackbar.LENGTH_SHORT).
                        setAction("UNDO", v -> {
                            shoppingList.add(position,deleteItem);
                            notifyItemInserted(position);
                        }).show();
            }
        });
    }

    public class ShoppingListViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        private final TextView textViewName;
        private final TextView textViewQta;
        private final  TextView textViewSize;

        public ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.text_shopList_name);
            this.textViewQta = itemView.findViewById(R.id.text_shopList_qta);
            this.textViewSize = itemView.findViewById(R.id.text_shopList_size);

            Button buttonLess = itemView.findViewById(R.id.button_shoppList_delete);

            itemView.setOnClickListener(this);
            buttonLess.setOnClickListener(this);
        }

        public void bind(Ingredient ingredient){
            textViewName.setText(ingredient.getName());
            textViewQta.setText(String.valueOf(ingredient.getQta()));
            textViewSize.setText(ingredient.getSize());
        }


        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.button_shoppList_delete) {
                notifyItemRemoved(getBindingAdapterPosition());
            }
        }

    }
}
