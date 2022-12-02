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

public class IngredientsRecyclerAdapter extends
        RecyclerView.Adapter<IngredientsRecyclerAdapter.IngredientViewHolder> {

    public interface OnItemClickListener {
        void onIngredientItemClick(Ingredient ingredient);
        void onDeleteButtonPressed(int position);
    }
    private final List<Ingredient> ingredientList;
    private final OnItemClickListener onItemClickListener;

    public IngredientsRecyclerAdapter(List<Ingredient> ingredientList, OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
        this.ingredientList = ingredientList;
    }


    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fragment_ingredient_list_item_list, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsRecyclerAdapter.IngredientViewHolder holder, int position) {
        holder.bind(ingredientList.get(position));
    }

    @Override
    public int getItemCount() {
        if (ingredientList != null) {
            return ingredientList.size();
        }
        return 0;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView textViewName;
        private final TextView textViewQta;
        private final  TextView textViewSize;

        public IngredientViewHolder(@NonNull View itemView ){
            super(itemView);
            this.textViewName = itemView.findViewById(R.id.text_ricette);
            this.textViewQta = itemView.findViewById(R.id.text_qta);
            this.textViewSize = itemView.findViewById(R.id.text_size);

            Button buttonAdd = itemView.findViewById(R.id.button_add);
            Button buttonLess = itemView.findViewById(R.id.button_less);

            itemView.setOnClickListener(this);
            buttonAdd.setOnClickListener(this);
            buttonLess.setOnClickListener(this);
        }


        public void bind(Ingredient ingredient) {
            textViewName.setText(ingredient.getName());
            textViewQta.setText(ingredient.getQta());
            textViewSize.setText(ingredient.getSize());
        }

        @Override
        public void onClick(View v) {
            if(v.getId()== R.id.button_less){
                int q= ingredientList.get(getAbsoluteAdapterPosition()).getQta();
                if(q <= 1)
                    ingredientList.get(getAbsoluteAdapterPosition()).setQta(0);
                else{
                ingredientList.get(getAbsoluteAdapterPosition()).setQta(q-1);
                }
            }
        }
    }
}
