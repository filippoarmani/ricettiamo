package cfgmm.ricettiamo.data.source.recipe;

import android.net.Uri;

import cfgmm.ricettiamo.data.repository.recipe.IPhotoResponseCallback;

public abstract class BasePhotoStorageDataSource {
    IPhotoResponseCallback photoResponseCallback;

    public void setCallBack(IPhotoResponseCallback photoResponseCallback) {
        this.photoResponseCallback = photoResponseCallback;
    }

    public abstract void uploadFile(Uri uri);
}
