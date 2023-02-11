package cfgmm.ricettiamo.data.repository.comment;

import androidx.lifecycle.MutableLiveData;

import cfgmm.ricettiamo.model.Comment;
import cfgmm.ricettiamo.model.Result;

public interface ICommentRepository {
    void writeNewComment(Comment comment);
    MutableLiveData<Result> readComment(String idRecipes);
}
