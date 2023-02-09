package cfgmm.ricettiamo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import cfgmm.ricettiamo.data.repository.comment.ICommentRepository;
import cfgmm.ricettiamo.model.Comment;
import cfgmm.ricettiamo.model.Result;

public class CommentViewModel extends ViewModel {

    private ICommentRepository commentRepository;
    private MutableLiveData<Result> currentCommentListLiveData;

    public CommentViewModel(ICommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public MutableLiveData<Result> getCurrentCommentListLiveData(String idRecipe) {
        this.readComment(idRecipe);
        return currentCommentListLiveData;
    }

    public void readComment(String idRecipe) {
        currentCommentListLiveData = commentRepository.readComment(idRecipe);
    }

    public void writeNewComment(Comment comment) {
        commentRepository.writeNewComment(comment);
    }
}
