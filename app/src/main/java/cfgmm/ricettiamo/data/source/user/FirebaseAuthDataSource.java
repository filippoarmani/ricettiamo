package cfgmm.ricettiamo.data.source.user;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.model.User;

public class FirebaseAuthDataSource extends BaseFirebaseAuthDataSource {

    public static final String TAG = FirebaseAuthDataSource.class.getSimpleName();

    FirebaseAuth firebaseAuth;

    public FirebaseAuthDataSource() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void signUp(User newUser, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(task -> {
                    if(isLoggedUser()) {
                        Log.d(TAG, "signUp: success");
                        newUser.setId(getCurrentId());
                        newUser.setSignInMethod("password");
                        updatePhoto(Uri.parse(String.valueOf(R.drawable.user)));
                        userResponseCallBack.onSuccessRegistration(newUser);
                    } else {
                        Log.d(TAG, "signUp: user null");
                        userResponseCallBack.onFailureRegistration(R.string.registration_error);
                    }
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "signUp: failure");
                    userResponseCallBack.onFailureRegistration(R.string.registration_error);
                });
    }

    @Override
    public void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(task -> {
                    FirebaseUser user = task.getUser();
                    if(isLoggedUser()) {
                        Log.d(TAG, "signIn: success");
                        userResponseCallBack.onSuccessLogin(user.getUid());
                    } else {
                        Log.d(TAG, "signIn: user null");
                        userResponseCallBack.onFailureLogin(R.string.login_error);
                    }
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "signIn: failure");
                    userResponseCallBack.onFailureLogin(R.string.login_error);
                });
    }

    @Override
    public void signOut() {
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    firebaseAuth.removeAuthStateListener(this);
                    Log.d(TAG, "User logged out");
                    userResponseCallBack.onSuccessLogout();
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
        firebaseAuth.signOut();
    }

    @Override
    public void resetPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(task -> {
                    Log.d(TAG, "resetPassword: success");
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "resetPassword: failure");
                });
    }

    @Override
    public void updateEmail(String email) {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(isLoggedUser()) {
            user.updateEmail(email)
                    .addOnSuccessListener(task -> {
                        Log.d(TAG, "updateEmail: success");
                        userResponseCallBack.onSuccessUpdateEmail();
                    })
                    .addOnFailureListener(error -> {
                        Log.d(TAG, "updateEmail: failure");
                        userResponseCallBack.onFailureUpdateEmail(R.string.updateEmail_error);
                    });
        }
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(isLoggedUser()) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
            user.reauthenticate(credential)
                    .addOnSuccessListener(task -> {
                        user.updatePassword(newPassword)
                                .addOnSuccessListener(task2 -> {
                                    Log.d(TAG, "updatePassword: success");
                                    userResponseCallBack.onSuccessUpdatePassword();
                                })
                                .addOnFailureListener(error -> {
                                    Log.d(TAG, "updatePassword: failure");
                                    userResponseCallBack.onFailureUpdatePassword(R.string.updatePassword_error);
                                });
                    })
                    .addOnFailureListener(error -> {
                        Log.d(TAG, "reauthenticate: failure");
                        userResponseCallBack.onFailureUpdatePassword(R.string.updatePassword_error);
                    });
        } else {
            Log.d(TAG, "updatePassword: user null");
            userResponseCallBack.onFailureUpdatePassword(R.string.updatePassword_error);
        }
    }

    @Override
    public void getCurrentPhoto() {
        Uri uri = firebaseAuth.getCurrentUser().getPhotoUrl();
        if(uri != null)
            userResponseCallBack.onSuccessGetPhoto(uri);
        else
            userResponseCallBack.onFailureGetPhoto(R.string.getPhoto_error);
    }

    @Override
    public void updatePhoto(Uri uri) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        Objects.requireNonNull(firebaseAuth.getCurrentUser()).updateProfile(profileUpdates)
                .addOnSuccessListener(task -> {
                    Log.d(TAG, "setCurrentPhoto: success");
                    userResponseCallBack.onSuccessSetPhoto(uri);
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "setCurrentPhoto: failure");
                    userResponseCallBack.onFailureSetPhoto(R.string.updatePhoto_error);
                });
    }

    @Override
    public boolean isLoggedUser() {
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override
    public String getCurrentId() {
        return Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
    }

    @Override
    public void delete() {
        firebaseAuth.getCurrentUser().delete()
                .addOnSuccessListener( task -> {
                    Log.d(TAG, "deleted: success");
                    userResponseCallBack.onSuccessDelete();
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "deleted: failure");
                    userResponseCallBack.onFailureDelete();
                });
    }

    @Override
    public void signInGoogle(String idToken) {
        if (idToken !=  null) {
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInGoogle: success");
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        userResponseCallBack.onSuccessLoginGoogle(new User(getCurrentId(), "google", firebaseUser.getDisplayName(), null));
                    } else {
                        userResponseCallBack.onFailureLogin(R.string.login_error);}
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInGoogle: failure", task.getException());
                    userResponseCallBack.onFailureLogin(R.string.login_error);
                }
            });
        }
    }
}
