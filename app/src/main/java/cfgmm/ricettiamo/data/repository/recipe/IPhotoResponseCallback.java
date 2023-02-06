package cfgmm.ricettiamo.data.repository.recipe;

public interface IPhotoResponseCallback {
    void onSuccessUploadPhoto(String path);
    void onFailureUploadPhoto();
}
