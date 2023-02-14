package cfgmm.ricettiamo.adapter;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Step;

public class StepsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private View view;
    private final List<Step> steps;
    private final Application application;

    public StepsRecyclerAdapter(List<Step> steps, Application application) {
        this.steps = steps;
        this.application = application;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.template_step, parent, false);
        return new RecipeDetailStepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StepsRecyclerAdapter.RecipeDetailStepsViewHolder) {
            ((StepsRecyclerAdapter.RecipeDetailStepsViewHolder) holder).bind(steps.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (steps != null) {
            return steps.size();
        }
        return 0;
    }

    public class RecipeDetailStepsViewHolder extends RecyclerView.ViewHolder {

        final TextView textViewNumber;
        final TextView textViewDescription;

        public RecipeDetailStepsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNumber = itemView.findViewById(R.id.numberStep);
            textViewDescription = itemView.findViewById(R.id.textStep);
        }

        public void bind(Step step) {
            textViewNumber.setText(String.valueOf(step.getNumber()));
            textViewDescription.setText(step.getDescription());
        }
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
                int position = viewHolder.getBindingAdapterPosition();
                for (int i = position + 1; i < steps.size(); i++) {
                    steps.get(i).setNumber(steps.get(i).getNumber() - 1);
                }
                steps.remove(position);
                notifyDataSetChanged();
            }
        });
    }
}
