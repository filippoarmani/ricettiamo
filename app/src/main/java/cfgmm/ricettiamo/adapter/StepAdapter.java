package cfgmm.ricettiamo.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import cfgmm.ricettiamo.R;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder>{

    private View view;
    private List<Object> stepList;

    public StepAdapter(View view, List<Object> stepList) {
        this.view = view;
        this.stepList = stepList;
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
                Object deleteItem = stepList.get(viewHolder.getBindingAdapterPosition());

                int position = viewHolder.getBindingAdapterPosition();
                stepList.remove(position);
                notifyItemRemoved(position);

                Snackbar.make(view, "UNDO", Snackbar.LENGTH_SHORT).
                        setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                stepList.add(position,deleteItem);
                                notifyItemInserted(position);
                            }
                        }).show();
            }
        });
    }

    @NonNull
    @Override
    public StepAdapter.StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.template_step, parent,false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.StepViewHolder holder, int position) {
        if(stepList.get(position) instanceof Bitmap) {
            holder.text.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);

            Glide.with(view)
                    .load((Bitmap) stepList.get(position))
                    .into(holder.image);
        } else {
            holder.text.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
            holder.text.setText((String) stepList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if(stepList != null) {
            return stepList.size();
        }
        return 0;
    }

    public static class StepViewHolder extends RecyclerView.ViewHolder {
        final TextView text;
        final ImageView image;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textStep);
            image = itemView.findViewById(R.id.imageStep);
        }
    }
}
