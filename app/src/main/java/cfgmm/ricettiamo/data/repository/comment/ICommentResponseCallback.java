package cfgmm.ricettiamo.data.repository.comment;

import java.util.List;

import cfgmm.ricettiamo.model.Comment;

public interface ICommentResponseCallback {
    void onSuccessReadComment(List<Comment> commentList);
    void onFailureReadComment(int idError);

    void onSuccessWriteComment(Comment comment, String authorId);
    void onFailureWriteComment(int idError);

    void onSuccessUpdateStars();
    void onFailureUpdateStars(int idError);

    void setTrue();

    void setFalse();
}
