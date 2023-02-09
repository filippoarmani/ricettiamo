package cfgmm.ricettiamo.data.repository.comment;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import cfgmm.ricettiamo.data.source.comment.BaseCommentDatabaseDataSource;
import cfgmm.ricettiamo.model.Comment;
import cfgmm.ricettiamo.model.Result;

public class CommentRepository implements ICommentRepository, ICommentResponseCallback{

    private BaseCommentDatabaseDataSource commentDatabaseDataSource;
    private MutableLiveData<Result> commentList;

    public CommentRepository(BaseCommentDatabaseDataSource commentDatabaseDataSource) {
        this.commentDatabaseDataSource = commentDatabaseDataSource;
        this.commentDatabaseDataSource.setCallBack(this);

        this.commentList = new MutableLiveData<>();
    }

    @Override
    public void writeNewComment(Comment comment) {
        commentDatabaseDataSource.writeComment(comment);
        readComment(comment.getIdRecipe());
    }

    @Override
    public MutableLiveData<Result> readComment(String idRecipes) {
        commentDatabaseDataSource.readComments(idRecipes);
        return commentList;
    }

    @Override
    public void onSuccessReadComment(List<Comment> commentList) {
        this.commentList.postValue(new Result.CommentResponseSuccess(commentList));
    }

    @Override
    public void onFailureReadComment(int idError) {
        this.commentList.postValue(new Result.Error(idError));
    }

    @Override
    public void onSuccessWriteComment(Comment comment) {
        commentDatabaseDataSource.updateScore(comment.getIdAuthorRecipe(), comment.getScore());
    }

    @Override
    public void onFailureWriteComment(int idError) {
        this.commentList.postValue(new Result.Error(idError));
    }

    @Override
    public void onSuccessUpdateStars() { }

    @Override
    public void onFailureUpdateStars(int idError) {
        this.commentList.postValue(new Result.Error(idError));
    }

}
