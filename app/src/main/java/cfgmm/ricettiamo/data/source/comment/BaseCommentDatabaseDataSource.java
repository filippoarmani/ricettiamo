package cfgmm.ricettiamo.data.source.comment;

import cfgmm.ricettiamo.data.repository.comment.ICommentResponseCallback;
import cfgmm.ricettiamo.model.Comment;

public abstract class BaseCommentDatabaseDataSource {

    ICommentResponseCallback commentResponseCallBack;

    public void setCallBack(ICommentResponseCallback commentResponseCallBack) {
        this.commentResponseCallBack = commentResponseCallBack;
    }

    public abstract void writeComment(Comment comment);
    public abstract void readComments(String idRecipe);

    public abstract void updateStars(String idUser, int score);
}
