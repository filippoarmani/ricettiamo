package cfgmm.ricettiamo.data.repository.comment;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import cfgmm.ricettiamo.data.source.comment.BaseCommentDatabaseDataSource;
import cfgmm.ricettiamo.model.Comment;
import cfgmm.ricettiamo.model.Result;

public class CommentRepository implements ICommentRepository, ICommentResponseCallback{

    private BaseCommentDatabaseDataSource commentDatabaseDataSource;
    private MutableLiveData<Result> commentList;

    boolean exist;

    public CommentRepository(BaseCommentDatabaseDataSource commentDatabaseDataSource) {
        exist = false;
        this.commentDatabaseDataSource = commentDatabaseDataSource;
        this.commentDatabaseDataSource.setCallBack(this);

        this.commentList = new MutableLiveData<>();
    }

    @Override
    public void writeNewComment(Comment comment, String authorId) {
        commentDatabaseDataSource.writeComment(comment, authorId);
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
    public void onSuccessWriteComment(Comment comment, String authorId) {
        commentDatabaseDataSource.exists(authorId, true);
        if(exist)
            commentDatabaseDataSource.updateStars(comment.getIdUser(), comment.getScore(), true);

        commentDatabaseDataSource.exists(comment.getIdRecipe(), false);
        if(exist)
            commentDatabaseDataSource.updateStars(comment.getIdRecipe(), comment.getScore(), false);
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

    @Override
    public void setTrue() {
        exist = true;
    }

    @Override
    public void setFalse() {
        exist = false;
    }
}
