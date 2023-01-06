package cfgmm.ricettiamo.viewmodel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.snackbar.Snackbar;

import java.util.Map;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.model.User;

public class UserViewModel extends ViewModel {

    IUserRepository userRepository;
    MutableLiveData<User> currentUserLiveData;
    MutableLiveData<Uri> currentPhotoLiveData;

    public UserViewModel(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void signUp(User newUser, String email, String password) { userRepository.signUp(newUser, email, password); }

    public void signIn(String email, String password) {
        userRepository.signIn(email, password);
    }

    public MutableLiveData<User> getCurrentUserLiveData() {
        if(currentUserLiveData == null) {
            if(isLoggedUser()) {
                currentUserLiveData = userRepository.getLoggedUser();
            } else {
                currentUserLiveData = new MutableLiveData<>(null);
            }
        }

        return currentUserLiveData;
    }

    public MutableLiveData<Uri> getCurrentPhotoLiveData() {
        if(currentPhotoLiveData == null) {
            if(isLoggedUser()) {
                currentPhotoLiveData = userRepository.getCurrentPhoto();
            } else {
                currentPhotoLiveData = new MutableLiveData<>(Uri.parse(String.valueOf(R.drawable.user)));
            }
        }

        return currentPhotoLiveData;
    }

    public void signOut() {
        currentUserLiveData = userRepository.signOut();
    }
    public boolean isLoggedUser() {
        return userRepository.isLoggedUser();
    }

    public void resetPassword(String email) {
        userRepository.resetPassword(email);
    }

    public void updatePassword(String cPassword, String nPassword1) {
        userRepository.updatePassword(cPassword, nPassword1);
    }

    public void updateData(Map<String, Object> newInfo) {
        userRepository.updateData(newInfo);
        currentUserLiveData = userRepository.getLoggedUser();
    }

    public void updatePhoto(Uri uri) {
        userRepository.updatePhoto(uri);
        currentPhotoLiveData = userRepository.getCurrentPhoto();
    }

    public boolean strongPassword(String password) {
        if(password.length() < 8) {
            return false;
        }

        int i = 0;
        boolean noUpper = true;
        boolean noLower = true;
        boolean noNumber = true;
        boolean noSpecial = true;

        while(i < password.length() && (noUpper || noLower || noNumber || noSpecial)) {
            char c = password.charAt(i);

            if(c >= 'a' && c <= 'z')
                noLower = false;
            else if(c >= 'A' && c <= 'Z')
                noUpper = false;
            else if(c >= '0' && c <= '0')
                noNumber = false;
            else
                noSpecial = false;

            i++;
        }

        if(noUpper || noLower || noNumber || noSpecial) {
            return false;
        }

        return true;
    }
}
