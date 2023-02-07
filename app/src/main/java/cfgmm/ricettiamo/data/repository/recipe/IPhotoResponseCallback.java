package cfgmm.ricettiamo.data.repository.recipe;

import android.net.Uri;

public interface IPhotoResponseCallback {
    void onSuccessUploadPhoto(Uri uri);
    void onFailureUploadPhoto();
}
