package cfgmm.ricettiamo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.List;

import cfgmm.ricettiamo.R;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder>{

    private Context context;
    private List<Object> stepList;

    public StepAdapter(Context context, List<Object> stepList) {
        this.context = context;
        this.stepList = stepList;
    }

    @NonNull
    @Override
    public StepAdapter.StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.template_step, parent,false);
        return new StepAdapter.StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.StepViewHolder holder, int position) {
        if(stepList.get(position) instanceof Bitmap) {
            holder.text.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load((Bitmap) stepList.get(position))
                    .into(holder.image);
        } else {
            holder.text.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
            holder.text.setText((String) stepList.get(position));
        }
    }

    private boolean isURL(String s) {
        if(s == null)
            return false;

        return s.indexOf("android") == 0;
    }

    @Override
    public int getItemCount() {
        if(stepList != null) {
            return stepList.size();
        }
        return 0;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {
        final TextView text;
        final ImageView image;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textStep);
            image = itemView.findViewById(R.id.imageStep);
        }
    }
}
