package cfgmm.ricettiamo.data.source.recipe;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

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

        UploadTask uploadTask = ref.putFile(uri);
        uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return storageRef.getDownloadUrl();
        }).addOnSuccessListener(task -> {
            Log.d(TAG, "uploadFile: success");
            photoResponseCallback.onSuccessUploadPhoto(task.getPath());
        }).addOnFailureListener(error -> {
            Log.d(TAG, "uploadFile: success");
            photoResponseCallback.onFailureUploadPhoto();
        });
    }
}
