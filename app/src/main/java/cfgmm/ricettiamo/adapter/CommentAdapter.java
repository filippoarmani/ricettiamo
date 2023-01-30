package cfgmm.ricettiamo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.Comment;
import cfgmm.ricettiamo.viewmodel.UserViewModel;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    final Comment[] comments;
    final Context context;

    private UserViewModel userViewModel;

    public CommentAdapter(Context ct, Comment[] comments) {
        context = ct;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.template_comment, parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        String id = comments[position].getIdUser();
        holder.user.setText(userViewModel.getDisplayNameUser(id));
        holder.description.setText(comments[position].getDescription());

        String score = "" + comments[position].getScore();
        holder.score.setText(score);
    }

    @Override
    public int getItemCount() {
        return comments.length;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        final TextView user;
        final TextView description;
        final TextView score;

        public CommentViewHolder(View view) {
            super(view);
            user = view.findViewById(R.id.comment_user);
            description = view.findViewById(R.id.comment);
            score = view.findViewById(R.id.score);
        }
    }
}
