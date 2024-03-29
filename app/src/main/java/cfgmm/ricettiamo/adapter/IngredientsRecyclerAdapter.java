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

public class IngredientsRecyclerAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public interface OnItemClickListener {
        void onDeleteButtonPressed(int position);
    }
    private final List<Ingredient> ingredientList;
    private final View view;
    private final OnItemClickListener onItemClickListener;

    public IngredientsRecyclerAdapter(View view, List<Ingredient> ingredientList, OnItemClickListener onItemClickListener) {
        this.view = view;
        this.ingredientList = ingredientList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.template_ingredient_lists, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof IngredientsRecyclerAdapter.IngredientViewHolder) {
            ((IngredientsRecyclerAdapter.IngredientViewHolder) holder).bind(ingredientList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (ingredientList != null) {
            return ingredientList.size();
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
                Ingredient deleteItem = ingredientList.get(viewHolder.getBindingAdapterPosition());

                int position = viewHolder.getBindingAdapterPosition();
                ingredientList.remove(position);
                notifyItemRemoved(position);

                Snackbar.make(view, deleteItem.getName(), Snackbar.LENGTH_SHORT).
                        setAction("UNDO", v -> {
                            ingredientList.add(position,deleteItem);
                            notifyItemInserted(position);
                        }).show();
            }
        });
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView textViewName;
        private final TextView textViewQta;
        private final TextView textViewSize;

        public IngredientViewHolder(@NonNull View itemView ){
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.text_name);
            this.textViewQta = itemView.findViewById(R.id.text_qta);
            this.textViewSize = itemView.findViewById(R.id.text_size);

            Button buttonLess = itemView.findViewById(R.id.button_delete);

            itemView.setOnClickListener(this);
            buttonLess.setOnClickListener(this);
        }

        public void bind(Ingredient ingredient) {
            textViewName.setText(ingredient.getName());
            textViewQta.setText(String.valueOf(ingredient.getQta()));
            textViewSize.setText(ingredient.getSize());
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.button_delete) {
                onItemClickListener.onDeleteButtonPressed(getAbsoluteAdapterPosition());
            }

        }
    }
}
