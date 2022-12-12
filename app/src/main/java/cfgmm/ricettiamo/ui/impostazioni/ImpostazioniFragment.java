package cfgmm.ricettiamo.ui.impostazioni;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cfgmm.ricettiamo.LoginActivity;
import cfgmm.ricettiamo.R;
import cfgmm.ricettiamo.RegistrationActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImpostazioniFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImpostazioniFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextInputLayout change_display_name;
    private TextInputLayout change_description;
    private TextInputLayout change_password_current;
    private TextInputLayout change_password_new;
    private TextInputLayout change_password_new2;
    private TextInputLayout change_email_current;
    private TextInputLayout change_email_new;
    private TextInputLayout change_email_new2;

    private ImageButton change_photo;
    private Uri image_profile;
    private Bitmap bitmap;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public ImpostazioniFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImpostazioniFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImpostazioniFragment newInstance(String param1, String param2) {
        ImpostazioniFragment fragment = new ImpostazioniFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_impostazioni, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        change_photo = view.findViewById(R.id.change_user);
        image_profile = user.getPhotoUrl();
        change_photo.setImageURI(image_profile);

        change_display_name = view.findViewById(R.id.i_dName_layout);
        change_description = view.findViewById(R.id.modificaDescr);
        change_password_current = view.findViewById(R.id.i_cPassword_layout);
        change_password_new = view.findViewById(R.id.i_nPassword1_layout);
        change_password_new2 = view.findViewById(R.id.i_nPassword2_layout);
        change_email_current = view.findViewById(R.id.i_cEmail_layout);
        change_email_new = view.findViewById(R.id.i_nEmail1_layout);
        change_email_new2 = view.findViewById(R.id.i_nEmail2_layout);

        Button save = view.findViewById(R.id.salva);

        change_photo.setOnClickListener(v -> {
            if(v.getContext().checkPermission(Context.MEDIA_COMMUNICATION_SERVICE, 0, 0))
                mGetContent.launch("image/*");
        });

        save.setOnClickListener(v -> {
            String displayName = change_display_name.getEditText().getText().toString().trim();

            String description = change_description.getEditText().getText().toString().trim();

            String cPassword = change_password_current.getEditText().getText().toString().trim();
            String nPassword1 = change_password_new.getEditText().getText().toString().trim();
            String nPassword2 = change_password_new2.getEditText().getText().toString().trim();

            String cEmail = change_email_current.getEditText().getText().toString().trim();
            String nEmail1 = change_email_new.getEditText().getText().toString().trim();
            String nEmail2 = change_email_new2.getEditText().getText().toString().trim();

            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(getImageUri(view.getContext(), bitmap))
                    .build();

            user.updateProfile(profileUpdate).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(v.getContext(), "immagine cambiata",
                            Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(v.getContext(), "immagine non cambiata",
                            Toast.LENGTH_SHORT).show();
            });

            if(!(isEmpty(displayName))) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build();

                user.updateProfile(profileUpdates);
            }

            if(!isEmpty(description)) {
                //TODO
            }

            if(!(isEmpty(cPassword) || isEmpty(nPassword1) || isEmpty(nPassword2))) {
                AuthCredential credential = EmailAuthProvider
                        .getCredential(user.getEmail(), cPassword);

                user.reauthenticate(credential)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()) {
                                Log.d(TAG, "User re-authenticated.");
                                if(nPassword1.equals(nPassword2)) {
                                    user.updatePassword(nPassword1).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Log.d(TAG, "User password updated.");
                                        }
                                    });
                                } else {
                                    change_password_new.setError("Passwords not equal");
                                    change_password_new2.setError("Passwords not equal");
                                }

                            } else {
                                change_password_current.setError("Wrong Password");
                            }
                        });
            }

            if(!(isEmpty(cEmail) || isEmpty(nEmail1) || isEmpty(nEmail2))) {
                if(nEmail1.equals(nEmail2)) {
                    if(cEmail.equals(user.getEmail())) {
                        if(EmailValidator.getInstance().isValid(nEmail1))
                            user.updateEmail(nEmail1).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User email address updated.");
                                }
                            });
                        else
                            change_email_new.setError("Email not Valid");
                    } else {
                        change_email_current.setError("Wrong Email");
                    }
                } else {
                    change_email_new.setError("Emails not equal");
                    change_email_new2.setError("Emails not equal");
                }
            }

            /*FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            startActivity(intent);*/
        });
    }

    // GetContent creates an ActivityResultLauncher<String> to allow you to pass
    // in the mime type you'd like to allow the user to select
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                        change_photo.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, user.getUid()+"_photo", null);
        return Uri.parse(path);
    }

}