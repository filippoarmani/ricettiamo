package cfgmm.ricettiamo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import cfgmm.ricettiamo.data.repository.user.IUserRepository;
import cfgmm.ricettiamo.model.User;

public class UserViewModel extends ViewModel {

    IUserRepository userRepository;
    MutableLiveData<User> currentUserLiveData;

    public UserViewModel(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void signUp(User newUser, String email, String password) {
        userRepository.signUp(newUser, email, password);
    }

    public void signIn(String email, String password) {
        userRepository.signIn(email, password);
    }

    public MutableLiveData<User> getCurrentUserLiveData() {
        if(currentUserLiveData == null)
            currentUserLiveData = userRepository.getLoggedUser();

        return currentUserLiveData;
    }

    public boolean isLoggedUser() {
        return userRepository.isLoggedUser();
    }

    public void resetPassword(String email) {
        userRepository.resetPassword(email);
    }
}
