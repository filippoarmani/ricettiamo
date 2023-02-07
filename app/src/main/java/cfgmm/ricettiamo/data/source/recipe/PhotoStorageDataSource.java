package cfgmm.ricettiamo.data.source.recipe;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PhotoStorageDataSource extends BasePhotoStorageDataSource {

    private static final String TAG = PhotoStorageDataSource.class.getSimpleName();

    private final StorageReference storageRef;

    public PhotoStorageDataSource() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    @Override
    public void uploadFile(Uri uri) {
        StorageReference ref = storageRef.child("RecipeImages_" + uri.getLastPathSegment().substring(uri.getLastPathSegment().indexOf(":")+1) + ".jpeg");

        ref.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Download file From Firebase Storage
                    ref.getDownloadUrl()
                            .addOnSuccessListener(downloadPhotoUrl -> {
                                Log.d(TAG, "uploadFile: success");
                                photoResponseCallback.onSuccessUploadPhoto(downloadPhotoUrl);
                            })
                            .addOnFailureListener(e -> {
                                Log.d(TAG, "uploadFile: success");
                                photoResponseCallback.onFailureUploadPhoto();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "uploadFile: success");
                    photoResponseCallback.onFailureUploadPhoto();
                });
    }
}
