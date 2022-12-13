package adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Ingredient;

public class ShoppingListRecyclerAdapter extends
        RecyclerView.Adapter<ShoppingListRecyclerAdapter.ShoppingListViewHolder>{

    //interfaccia
    public interface OnItemClickListener {
        void onIngredientItemClick(Ingredient ingredient);
        void onDeleteButtonPressed(int position);
    }
    private final List<Ingredient> shoppingList;
    private final ShoppingListRecyclerAdapter.OnItemClickListener onItemClickListener;



    public ShoppingListRecyclerAdapter (List<Ingredient> shoppingList,
                                        OnItemClickListener onItemClickListener){
        this.shoppingList = shoppingList;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fragment_item_shopping_list, parent, false);
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


    public class ShoppingListViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{
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
