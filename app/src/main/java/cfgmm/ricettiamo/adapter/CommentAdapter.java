package cfgmm.ricettiamo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.model.Comment;
import cfgmm.ricettiamo.util.ServiceLocator;
import cfgmm.ricettiamo.viewmodel.UserViewModel;
import cfgmm.ricettiamo.viewmodel.UserViewModelFactory;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    final List<Comment> comments;
    final Context context;

    public CommentAdapter(Context ct, List<Comment> comments) {
        context = ct;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.template_comment, parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        holder.user.setText(comments.get(position).getIdUser());
        holder.description.setText(comments.get(position).getDescription());
        String score = "" + comments.get(position).getScore();
        holder.score.setText(score);
    }

    @Override
    public int getItemCount() {
        if(comments != null)
            return comments.size();

        return 0;
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
