package cfgmm.ricettiamo.data.repository.comment;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import cfgmm.ricettiamo.model.Comment;
import cfgmm.ricettiamo.model.Result;

public interface ICommentRepository {
    void writeNewComment(Comment comment, String authorId);
    MutableLiveData<Result> readComment(String idRecipes);
}
