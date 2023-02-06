package cfgmm.ricettiamo.adapter;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Step;

public class StepsDetailRecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Step> steps;
    private final Application application;

    public StepsDetailRecipeRecyclerAdapter(List<Step> steps, Application application) {
        this.steps = steps;
        this.application = application;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.template_step, parent, false);
        return new RecipeDetailStepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StepsDetailRecipeRecyclerAdapter.RecipeDetailStepsViewHolder) {
            ((StepsDetailRecipeRecyclerAdapter.RecipeDetailStepsViewHolder) holder).bind(steps.get(position));
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
}
